package com.kenure.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.kenure.constants.ApplicationConstants;

public class DownloadFilesUtills {
	

	public static void  downloadFiles(List<String> fileNames,HttpServletResponse response,ServletContext servletContext,String flag) throws IOException {
		String filePath = null;
		if(fileNames == null){
			// if no file exist then send blank data file.
			String fileName = "Data.csv";
			boolean noDataFileExist = false;
			String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
			if(flag.equals("DC")){
				String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
				filePath =  DcInsFilePath+File.separator+fileName;
			}
			if(flag.equals("EP")){
				String EpInsFilePath = resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
				filePath =  EpInsFilePath+File.separator+fileName;
			}
			noDataFileExist = new File(filePath).createNewFile();
			FileInputStream inputStream = new FileInputStream(filePath);
			response.setHeader("Content-disposition", "attachment;filename="+fileName);
			try {
				int c;
				while ((c = inputStream.read()) != -1) {
					response.getWriter().write(c);
				}
			} finally {
				if (inputStream != null) 
					inputStream.close();
				response.getWriter().close();
				if (noDataFileExist) {
					File tempFile = new File(filePath);
					tempFile.delete();
				}
			}
		}else{

			// Set the content type based to zip
			response.setContentType("Content-type: text/csv");
			response.setHeader("Content-Disposition","attachment; filename=EPInstallationFiles.zip");

			ServletOutputStream out = response.getOutputStream();
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

			for (String tempFileName : fileNames) {
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				if(flag.equals("DC")){
					String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
					filePath =  DcInsFilePath+File.separator+tempFileName;
				}
				if(flag.equals("EP")){
					String EpInsFilePath = resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
					filePath =  EpInsFilePath+File.separator+tempFileName;
				}
				
				File file = new File(filePath);
				zos.putNextEntry(new ZipEntry(file.getName()));

				// Get the file
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
				} catch (Exception fnfe) {
					// If the file does not exists, write an error entry instead of
					// file
					// contents
					zos.write(("ERROR: not find file " + file.getName())
							.getBytes());	
					zos.closeEntry();
					//logger.error("Couldfind file "+ file.getAbsolutePath());
					continue;
				}

				BufferedInputStream fif = new BufferedInputStream(fis);

				// Write the contents of the file
				int data = 0;
				while ((data = fif.read()) != -1) {
					zos.write(data);
				}
				fif.close();

				zos.closeEntry();
			}

			zos.close();

		}
	}
}
