import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;



public class MyProcess {
	private String ftp_host;
	private String ftp_user;
	private String ftp_pass;
	private String file;
	private int ftp_port;

	public MyProcess(String ftp_host, int ftp_port, String ftp_user,
			String ftp_pass, String file) {
		this.ftp_host = ftp_host;
		this.ftp_port = ftp_port;
		this.ftp_user = ftp_user;
		this.ftp_pass = ftp_pass;
		this.file = file;
	}

	public int run() {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		String SFTPWORKINGDIR = "/";
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(this.ftp_user, this.ftp_host,
					this.ftp_port);
			session.setPassword(this.ftp_pass);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(SFTPWORKINGDIR);
			File f = new File(this.file);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			System.out.println(dateFormat.format(cal.getTime()) + " uploading "
					+ this.file);
			channelSftp.put(new FileInputStream(f), f.getName());
			cal = Calendar.getInstance();
			System.out.println(dateFormat.format(cal.getTime()) + " "
					+ this.file + " finished!!!");
		} catch (Exception ex) {
			ex.printStackTrace();
			return 1;
		}
		return 0;
	}

	private static class URIEncoder {
		private static final String mark = "-_.!~*'()\"";
		private static final char[] hex = "0123456789ABCDEF".toCharArray();

		public static String encodeURI(String argString) {
			StringBuilder uri = new StringBuilder();

			char[] chars = argString.toCharArray();
			for (int i = 0; i < chars.length; ++i) {
				char c = chars[i];
				if (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z'))
						|| ((c >= 'A') && (c <= 'Z'))
						|| ("-_.!~*'()\"".indexOf(c) != -1))
					uri.append(c);
				else {
					appendEscaped(uri, c);
				}
			}
			return uri.toString();
		}

		private static void appendEscaped(StringBuilder uri, char c) {
			if (c <= '\15') {
				uri.append("%");
				uri.append('0');
				uri.append(hex[c]);
			} else if (c <= 255) {
				uri.append("%");
				uri.append(hex[(c >> '\4')]);
				uri.append(hex[(c & 0xF)]);
			} else {
				uri.append('\\');
				uri.append('u');
				uri.append(hex[(c >> '\f')]);
				uri.append(hex[(c >> '\b' & 0xFFF)]);
				uri.append(hex[(c >> '\4' & 0xFF)]);
				uri.append(hex[(c & 0xF)]);
			}
		}
	}
}
