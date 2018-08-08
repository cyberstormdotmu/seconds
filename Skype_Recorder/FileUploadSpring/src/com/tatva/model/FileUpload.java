package com.tatva.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;



/*
 * 	File Upload Model
 * 	Author: Tarkik Shah
 * 	Date: 08-01-2013
 * 
 */


public class FileUpload {

	private List<MultipartFile> files;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
}
