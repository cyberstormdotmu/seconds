package fi.korri.epooq.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MediaUtils 
{
	
	public static boolean convertAudio(String source, String destination, String executable)
	{
		String ffmpegAudioMP3Command = executable + " -i " + source + " -vn " + destination;  
		System.out.println("Command: " + ffmpegAudioMP3Command);

		String[] ffmpegAudioMP3CommandArray = ffmpegAudioMP3Command.split(" ");
		Runtime runtime = Runtime.getRuntime();
		Process proc = null;
		int exitValue = 0;

		try 
		{
			System.out.println("Before process");
			proc = runtime.exec(ffmpegAudioMP3CommandArray);
			String convertLog = MediaUtils.checkStream(proc.getInputStream(), proc.getErrorStream());
			proc.waitFor();
//			System.out.println("---------- Log ------------");
//			System.out.println(convertLog);
			System.out.println("After process");
			exitValue = proc.exitValue();
		} 
		catch (Exception ex) 
		{
			if (proc != null) proc.destroy();  // safety
			exitValue = -1;
			System.out.println("Destroyed process");
			return false;
		}
		return true;		
	}

	public static String checkStream(InputStream in, InputStream err) 
	{
		StringBuilder consoleOutput = new StringBuilder();

		final BufferedReader stdout = new BufferedReader(new InputStreamReader(in));
		final BufferedReader stderr = new BufferedReader(new InputStreamReader(err));
		int l;
		String line;

		try 
		{
			stdout.close();

			for (l = 0; ((line = stderr.readLine()) != null);) 
			{
				if (line.length() > 0 && l < 200) 
				{
					consoleOutput.append(line);
					l++;
				}
			}

			stderr.close();

		} catch (IOException ex) 
		{
			System.out.println("Exception checking the stream: " + ex.getMessage());
		}	        
		return consoleOutput.toString();
	}
}
