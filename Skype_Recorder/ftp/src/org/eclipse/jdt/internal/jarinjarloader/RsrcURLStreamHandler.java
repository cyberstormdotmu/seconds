package org.eclipse.jdt.internal.jarinjarloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class RsrcURLStreamHandler extends URLStreamHandler {
	private ClassLoader classLoader;

	public RsrcURLStreamHandler(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	protected URLConnection openConnection(URL u) throws IOException {
		return new RsrcURLConnection(u, this.classLoader);
	}

	protected void parseURL(URL url, String spec, int start, int limit) {
		String file;
//		String file;
		if (spec.startsWith("rsrc:")) {
			file = spec.substring(5);
		} else {
//			String file;
			if (url.getFile().equals("./")) {
				file = spec;
			} else {
//				String file;
				if (url.getFile().endsWith("/"))
					file = url.getFile() + spec;
				else
					file = spec;
			}
		}
		super.setURL(url, "rsrc", "", -1, null, null, file, null, null);
	}
}