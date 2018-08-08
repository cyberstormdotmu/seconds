package com.tatva.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.tatva.model.FileUpload;

/*
 * 	File Upload Controller
 * 	Author: Tarkik Shah
 * 	Date: 08-01-2013
 * 
 */


@Controller
public class FileUploadController {
	
	
	private static final int BUFFER_SIZE=4096;
	
	private String filePath="E:/Sushant/Eclipse Workspace/FileUploadSpring/uploadedFiles/commons-net-2.0.jar";
	
	@RequestMapping(value="/displayForm.spring",method=RequestMethod.GET)
	public String displayForm(){
		 
		/*
		 *  Display The Page For Uploading Form..
		 */
		
		return "displayForm";
	}

	@RequestMapping(value="/upload.spring",method=RequestMethod.POST)
	public String uploadFiles(@ModelAttribute ("uploadForm") FileUpload fileUpload, Model model) throws IOException{
		
		/*
		 *  This Method Will Make The List Of Files User Wants To Upload
		 *  And Upload Them To Server...
		 *  All The Uploaded Files Will Be Checked From uploadedFiles Folder.... 
		 */

		List<MultipartFile> files=fileUpload.getFiles();
		
		List<String> fileNames=new ArrayList<>();
		
		InputStream inputStream=null;
		OutputStream outputStream=null;
		
		if(files!=null && files.size()>0) {
				
			for(MultipartFile file:files) {
					String fileName=file.getOriginalFilename();
					fileNames.add(fileName);
					System.out.println("File Name::--"+fileName);
					
					//Reading File
					inputStream=file.getInputStream();
					File newFile=new File("E:/Sushant/Eclipse Workspace/FileUploadSpring/uploadedFiles/"+fileName);
					if(!newFile.exists()){
						newFile.createNewFile();
					}
					
					//Writing File
					outputStream=new FileOutputStream(newFile);
					int read=0;
					byte[] bytes=new byte[1024];
					
					while((read=inputStream.read(bytes))!=-1){
						outputStream.write(bytes,0,read);
					}
			}
		}
		
		model.addAttribute("files", fileNames);
		return "successPage";
	}
	
	@RequestMapping(value="/downloadPage.spring")
	public String downloadPage(){
		return "downloadPage";
	}
	
	@RequestMapping(value="/download.spring")
	public void download(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		ServletContext context=request.getServletContext();
		File downloadFile=new File(filePath);
		FileInputStream fileInputStream=new FileInputStream(downloadFile);
		
		 String mimeType = context.getMimeType(filePath);
	        if (mimeType == null) {
	            // set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	       
	        response.setContentType(mimeType);
	        response.setContentLength((int) downloadFile.length());
	       
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                downloadFile.getName());
	        response.setHeader(headerKey, headerValue);
	        
	        OutputStream outStream = response.getOutputStream();
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = -1;
	        
	        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	 
	        fileInputStream.close();
	        outStream.close();
	        
	}
	
}
