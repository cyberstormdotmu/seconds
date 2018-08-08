/**
 * Copyright 2009 Cubeia Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cubeia.firebase.bots.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.cubeia.firebase.api.util.TaskRepeater;

/**
 * @goal run
 * @requiresDependencyResolution runtime
 * @requiresProject
 */
public class FirebaseBotRunPlugin extends AbstractMojo {

	private static final String MY_GROUP_ID = "com.cubeia.firebase.bots";
	private static final String MY_ARTIFACT_ID = "firebase-bots-maven-plugin";

	private static final String DIST_GROUP_ID = "com.cubeia.firebase.bots";
	private static final String DIST_ARTIFACT_ID = "firebase-bots";

	private static final String SERVER_CLASS = "com.cubeia.firebase.bot.LocalBotServer";


	// --- MAGICAL MAVEN COMPONENTS --- //

	/**
	 * @parameter expression="${project.build.finalName}"
	 * @required
	 */
	private String finalName;

	/**
	 * @component
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * @component
	 */
	private ArtifactMetadataSource metadataSource;

	/**
	 * @parameter expression="${localRepository}"
	 */
	private ArtifactRepository localRepository;

	/**
	 * @parameter expression="${project.remoteArtifactRepositories}"
	 */
	private List<?> remoteRepositories;

	/**
	 * @readonly
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${project.build.directory}"
	 */
	private File outputDir;



	// --- CONFIGURATION --- ///

	/**
	 * @parameter default-value="true"
	 */
	private boolean deleteOnStart;

	/**
	 * @parameter default-value="8080"
	 */
	private int serverPort;

	/**
	 * @parameter default-value="0"
	 */
	private int botStartId;

	/**
	 * Overwrite the standard bot log4j configuration with a plugin
	 * specific which modified the log directory and logs non-firebase
	 * at DEBUG level to standard out.
	 *
	 * @parameter default-value="true"
	 */
	private boolean modifyLog4jConfiguration;


	// --- PLUGIN METHOD --- //

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Artifact artifact = findDistributionArtifact();
		File zipFile = getArtifactFile(artifact);
		File tempDir = findCreateTmpBotDir();
		unzipDirsToTmpDir(zipFile, tempDir);
		File botDir = getFirebaseBotDir(tempDir, artifact);
		BotDirectory dir = new BotDirectory(artifact.getVersion(), botDir);
		getLog().info("Instance directory: " + dir.botDirectory.getAbsolutePath());
		copyProjectArtifactToDeploy(botDir);
		copyProjectDependenciesToDeploy(botDir);
		modifyMenuConfiguration(dir);
		modifyLogConfiguration(dir);
		doLaunch(dir);
		hang();
	}

	protected void hang() {
		/*
		 * Wait for shutdown. Is... this really the best 
		 * way to do it? /LJN
		 */
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// This is the kill, don't log exception
			}
		}
	}

	protected void doLaunch(BotDirectory dir) throws MojoExecutionException {
		ClassLoader loader = createClassLoader(dir);
		checkForBotControlFile(loader);
		Thread.currentThread().setContextClassLoader(loader);
		try {
			Class<?> serverClass = loader.loadClass(SERVER_CLASS);
			String[] cmds = createCommandLine(dir);
			getLog().info("Attempting to start Firebase Bot server on port: " + serverPort);
			Method method = serverClass.getMethod("main", cmds.getClass());
			method.invoke(null, (Object)cmds);
		} catch (ClassNotFoundException e) {
			throw new MojoExecutionException("Server class '" + SERVER_CLASS + "' not found", e);
		} catch (Exception e) {
			throw new MojoExecutionException("Reflection error", e);
		}
	}

	// --- PRIVATE METHODS --- //

	private void checkForBotControlFile(ClassLoader loader) throws MojoExecutionException {
		String[] botControlFiles = getBotControlFiles();
		for (String control : botControlFiles) {
			URL url = loader.getResource(control.trim());
			// getLog().info("URL: " + url);
			if(url == null) {
				throw new MojoExecutionException("Bot control file '" + control + "' could not be found by the class loader! Does it exist in src'/main/resources'?");
			}
		}
	}

	private void modifyLogConfiguration(BotDirectory dir) throws MojoExecutionException {
		if(modifyLog4jConfiguration) {
			try {
				File newFile = new File(dir.confDirectory, "log4j.xml");
				String xml = new LogFileUtil(getLog(), dir).getLog4jConfig();
				FileUtils.writeStringToFile(newFile, xml);
			} catch(IOException e) {
				throw new MojoExecutionException("Failed to overwrite log4j configuration", e);
			}
		} else {
			getLog().info("Using standard bot log4j configuration.");
		}
	}

	/*
	 * Modify the server config to a new bind address, if configured
	 */
	private void modifyMenuConfiguration(BotDirectory dir) throws MojoExecutionException {
		// if(botControlFile != null) {
			File conf = new File(dir.confDirectory, "menu.properties");
			try {
				InputStream in = new FileInputStream(conf);
				Properties p = new Properties();
				p.load(in);
				String[] htmlFiles = getBotControlFiles();
				getLog().info("Found the following bot resource files (.html): "+Arrays.toString(htmlFiles));
				
				for (String html : htmlFiles) {
					String trimmed = html.trim();
					p.setProperty(trimmed.split("\\.")[0], trimmed);
				}
				in.close();
				OutputStream out = new FileOutputStream(conf);
				p.store(out, "Properties modified by firebase-bots:run plugin");
				out.close();
			} catch(Exception e) {
				throw new MojoExecutionException("Failed to modify server configuration. Check your bot control file configuration: <botControlFile>...</botControlFile> ", e);
			}
		// } 
	}
	
	/*
	 * Return a list of *.html files names (bot control files)
	 */

	private String[] getBotControlFiles() {         
		String baseDir = project.getBasedir().toString();
		
		String resourcesDir = FilenameUtils.concat(baseDir, "src/main/resources");
            
		java.io.File resourcesFolder = new java.io.File(resourcesDir);

		getLog().info("Scanning for bot resource files (.html) in folder: "+resourcesFolder.getAbsolutePath());
		
		java.io.FilenameFilter htmlFilter = new java.io.FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".html");
			}
		};

		return resourcesFolder.list(htmlFilter); // list of file names
	}

	/*
	 * Create the arguments for the server process
	 */
	private String[] createCommandLine(BotDirectory dir) {
		return new String[] {
				"-p", String.valueOf(serverPort),
				"-i", String.valueOf(botStartId)
		};
	}

	/*
	 * Create a URL class loader for all libs + conf dir
	 */
	@SuppressWarnings("unchecked")
	private ClassLoader createClassLoader(BotDirectory dir) throws MojoExecutionException {
		List<File> list = new LinkedList<File>();
		list.add(dir.libDirectory);
		list.addAll(FileUtils.listFiles(dir.libDirectory, new String[] { "jar" }, false));
		list.add(dir.confDirectory);
		File[] arr = new File[list.size()];
		list.toArray(arr);
		try {
			URL[] urls = FileUtils.toURLs(arr);
			return new URLClassLoader(urls);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to create URL for class path", e);
		}
	}

	/*
	 * Find any relevant dependencies ('jar') and copy
	 * them to the lib directory
	 */
    @SuppressWarnings("unchecked")
	private void copyProjectDependenciesToDeploy(File botsDir) throws MojoFailureException, MojoExecutionException {
		// Check provided services
		for (Artifact artifact : (Collection<Artifact>) project.getArtifacts()) {
			if (artifactShouldBeIncluded(artifact)) {
				String type = artifact.getType();
				if(type != null && type.equalsIgnoreCase("jar")) {
					getLog().debug("Including dependency: " + artifact);
					copyArtifactToLib(botsDir, artifact);
				} else {
					getLog().debug("Ignoring dependency (no type): " + artifact);
				}
			}
		}
	}

	private boolean artifactShouldBeIncluded(Artifact artifact) {
		return artifact != null
			   &&  artifact.getDependencyTrail() != null
			   &&  !Artifact.SCOPE_TEST.equals(artifact.getScope())
			   &&  !Artifact.SCOPE_PROVIDED.equals(artifact.getScope());
	}

	/*
	 * Get hold of the output artifact file
	 */
	private File getProjectArtifactFile() throws MojoFailureException {
		String extension = ".jar"; // findExtension(a.getType());
		String name = finalName + extension;
		File file = new File(outputDir, name);
		if(!file.exists()) {
			throw new MojoFailureException("Project artifact '" + name + "' not found; Please run 'package' at least once");
		}
		return file;
	}

	/*
	 * Get hold of the artifact for this project, and copy it to the
	 * firebase deploy directory.
	 */
	private void copyProjectArtifactToDeploy(File firebaseDir) throws MojoFailureException, MojoExecutionException {
		File artifact = getProjectArtifactFile();
		File target = ensureLibDirectory(firebaseDir);
		try {
			FileUtils.copyFileToDirectory(artifact, target);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to copy project artifact to deploy directory", e);
		}
	}

	/*
	 * Get hold of the artifact for this project, and copy it to the
	 * firebase deploy directory.
	 */
	private void copyArtifactToLib(File botDir, Artifact artifact) throws MojoFailureException, MojoExecutionException {
		if(artifact.getFile() == null) throw new MojoFailureException("Artifact '" + artifact + "' is not resolved");
		File target = ensureLibDirectory(botDir);
		try {
			FileUtils.copyFileToDirectory(artifact.getFile(), target);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to copy project artifact to deploy directory", e);
		}
	}


	/*
	 * Get the deploy lib (and make sure it exists)
	 */
	private File ensureLibDirectory(File botDir) throws MojoExecutionException {
		File file = new File(botDir, "lib");
		if(!file.exists() && !file.mkdirs()) {
			throw new MojoExecutionException("Failed to create directory '" + file + "'");
		}
		return file;
	}


	/*
	 * Find the final root of the unzipped distribution using the 
	 * artifact version number...
	 */
	private File getFirebaseBotDir(File tempDir, Artifact artifact) throws MojoExecutionException {
		File file = new File(tempDir, "bots");
		if(!file.exists()) {
			throw new MojoExecutionException("Internal error, root directory '" + file + "' does not exist!");
		}
		return file;
	}

	/*
	 * Get file for artifact, and make sure we can read it
	 */
	private File getArtifactFile(Artifact artifact) throws MojoExecutionException {
		File file = artifact.getFile();
		if(!file.exists() || !file.canRead()) {
			throw new MojoExecutionException("Cannot read artifact file '" + file + "'");
		}
		return file;
	}


	/*
	 * Open the distribution in the work directory
	 */
	private void unzipDirsToTmpDir(File zipFile, File tempDir) throws MojoExecutionException {
		try {
			ZipFile zip = new ZipFile(zipFile);
			explode(zip, tempDir);
		} catch(IOException e) {
			throw new MojoExecutionException("Failed to unzip distribution", e);
		}
	}

	/*
	 * Explode zip file to destination directory
	 */
	private void explode(ZipFile file, File dir) throws IOException {
		for(Enumeration<? extends ZipEntry> en = file.entries(); en.hasMoreElements(); ) {
			ZipEntry entry = en.nextElement();
			File next = new File(dir, entry.getName());
			if(entry.isDirectory()) {
				next.mkdirs();
			} else {
				next.createNewFile();
				if (next.getParentFile() != null) {
					next.getParentFile().mkdirs();
				}
				InputStream in = file.getInputStream(entry);
				OutputStream out = new FileOutputStream(next);
				try {
					IOUtils.copy(in, out);
				} finally {
					IOUtils.closeQuietly(out);
					IOUtils.closeQuietly(in);
				}
			}
		}
	}

	/*
	 * Create a new directory inside the build target dir, the use for
	 * running firebase.
	 */
	private File findCreateTmpBotDir() throws MojoExecutionException {
		if(!outputDir.exists()) throw new MojoExecutionException("Output directory not found; Please run 'package' at least once");
		final File dir = new File(outputDir, "bots-run");
		TaskRepeater repeat = new Repeater("directory create/delete");
		if(dir.exists() && deleteOnStart) {
			doStartTmpDirCleanup(dir, repeat);
		}
		if(!dir.exists()) {
			doStartTmpDirCreation(dir, repeat);
		}
		getLog().info("Runtime directory '" + dir + "'; deleteOnStart: " + deleteOnStart);
		return dir;
	}

	private void doStartTmpDirCreation(final File dir, TaskRepeater repeat) throws MojoExecutionException {
		getLog().debug("Runtime directory '" + dir + "' does not exists, will be created");
		boolean b = repeat.safeExecute(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return dir.mkdir();
			}
		});
		if(!b) {
			throw new MojoExecutionException("Failed to create dir '" + dir + "'");
		}
	}

	private void doStartTmpDirCleanup(final File dir, TaskRepeater repeat) throws MojoExecutionException {
		getLog().warn("Runtime directory '" + dir + "' already exists, its content will be deleted");
		boolean b = repeat.safeExecute(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return FileUtils.deleteQuietly(dir);
			}
		});
		if(!b) {
			throw new MojoExecutionException("Failed to delete dir '" + dir + "'");
		}
	}

	/*
	 * This method attempts to find the Firebase Bot distribution ZIP file. This
	 * should be done like this:
	 *
	 *  1) Find the our own artifact
	 *  2) Resolve the transitive dependencies of said artifact
	 *  3) Return the one matching a distribution
	 *
	 * Easy, eh?
	 */
	private Artifact findDistributionArtifact() throws MojoExecutionException {
		Artifact mySelf = findOwnArtifact();
		try {
			return findDistributionArtifact(mySelf);
		} catch(AbstractArtifactResolutionException e) {
			throw new MojoExecutionException("Failed to resolve dependencies", e);
		}
	}


	/*
	 * Given the plugin artifact, get the transitive dependencies and locate the
	 * distribution zip among them.
	 */
	private Artifact findDistributionArtifact(Artifact mySelf) throws ArtifactResolutionException, ArtifactNotFoundException, MojoExecutionException {
		Collection<Artifact> set = getTransitive(mySelf);
		Artifact zip = null;
		for (Artifact a : set) {
			if(a.getGroupId().equals(DIST_GROUP_ID) && a.getArtifactId().equals(DIST_ARTIFACT_ID)) {
				zip = a;
				break;
			}
		}
		if(zip == null) {
			throw new MojoExecutionException("Failed to resolve transitive artifact matching " + DIST_GROUP_ID + ":" + DIST_ARTIFACT_ID);
		} else {
			return zip;
		}
	}


	/*
	 * Try to find the artifact matching my own group and artifact id.
	 */
	@SuppressWarnings("unchecked")
	private Artifact findOwnArtifact() throws MojoExecutionException {
		Collection<Artifact> set = project.getPluginArtifacts();
		Artifact ours = null;
		for (Artifact a : set) {
			if(a.getGroupId().equals(MY_GROUP_ID) && a.getArtifactId().equals(MY_ARTIFACT_ID)) {
				ours = a;
				break;
			}
		}
		if(ours == null) {
			throw new MojoExecutionException("Failed to resolve artifact matching " + MY_GROUP_ID + ":" + MY_ARTIFACT_ID);
		} else {
			return ours;
		}
	}

	/*
	 * This method magically resolves all dependencies for an artifact, which means
	 * that the returned artifacts all have a file location in the local repository.
	 */
	@SuppressWarnings("unchecked")
	private Collection<Artifact> getTransitive(Artifact ours) throws ArtifactResolutionException, ArtifactNotFoundException {
		ArtifactResolutionResult result =  artifactResolver.resolveTransitively(
												Collections.singleton(ours),
												project.getArtifact(),
												Collections.EMPTY_MAP,
												localRepository,
												remoteRepositories,
												metadataSource, null, Collections.EMPTY_LIST);
		return result.getArtifacts();
	}


	// --- PRIVATE CLASSES --- //

	private class Repeater extends TaskRepeater {

		public Repeater(String name) {
			super(name, 10, 500);
		}

		@Override
		protected void debug(String msg) {
			getLog().debug(msg);
		}

		@Override
		protected void error(String msg, Exception e) {
			getLog().error(msg, e);
		}

		@Override
		protected void warn(String msg) {
			getLog().warn(msg);
		}
	}
}
