package com.cubeia.firebase.bots.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

class LogFileUtil {

	private final Log log;
	private final BotDirectory dir;

	public LogFileUtil(Log log, BotDirectory dir) {
		this.log = log;
		this.dir = dir; 
	}
	
	public String getLog4jConfig() throws MojoExecutionException, IOException {
		InputStream in = getClass().getClassLoader().getResourceAsStream("log4j.templ");
		if(in == null) throw new MojoExecutionException("Could not find included log4j configuration file.");
		try {
			String xml = IOUtils.toString(in);
			info("Overwriting log4j configuration at: " + dir.confDirectory.getAbsolutePath());
			File logDirectory = new File(dir.botDirectory, "logs");
			String path = logDirectory.getAbsolutePath();
			/*
			 * Now for a blody hack: log4j can't handle native windows paths as
			 * a '\' gets read as an escape character. So, we'll replace them with
			 * forward slashes instead. Brilliant.
			 */
			path = path.replace('\\', '/');
			if(!path.endsWith("/")) {
				path += "/";
			}
			xml = xml.replace("@LOG_DIRECTORY@", path);
			// xml = xml.replace("@SLASH@", File.separator);
			return xml;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private void info(String msg) {
		if(log != null) {
			log.info(msg);
		}
	}
}
