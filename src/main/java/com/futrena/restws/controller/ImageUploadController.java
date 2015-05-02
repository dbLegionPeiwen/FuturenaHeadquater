package com.futrena.restws.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futrena.restws.request.ImageBuffer;


@RestController
public class ImageUploadController{
	
//	public static String httpRequestDump(HttpServletRequest request) {
//		StringBuilder s=new StringBuilder();
//		s.append("HEAD:");
//		Enumeration headerNames = request.getHeaderNames();
//		while(headerNames.hasMoreElements()) 
//		{
//			String headerName = (String)headerNames.nextElement();
//			s.append(headerName + "=" + (request.getHeader(headerName) + ";"));
//			//System.out.println(request.getHeader(headerName));
//		}
//		
//		Enumeration params = request.getParameterNames(); 
//		s.append("BODY:");
//		while(params.hasMoreElements())
//		{
//			String paramName = (String)params.nextElement();
//			s.append(paramName + "=" + request.getParameter(paramName) + ";");
//			//System.out.println("Attribute Name - "+paramName+", Value - "+request.getParameter(paramName));
//		}		    
//		return s.toString();
//	}
    
    public static boolean writeImageFile(byte[] imageByteArray, String outFileName) {
    	boolean ret=false;
    	try{
    		// Write a image byte array into file system
    		FileOutputStream imageOutFile = new FileOutputStream(outFileName);
    		imageOutFile.write(imageByteArray);
    		imageOutFile.close();
    		ret=true;
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
        return ret;
    }
	
	@RequestMapping("/upload")
	public void Upload(@RequestBody ImageBuffer buffer,
			HttpServletResponse response, HttpServletRequest request) throws IOException{
		
		System.out.println(request.getServletContext().getRealPath(""));
		
		for(int i = 0; i<buffer.getBuffer().size(); i++) {
		
			String bufferString = buffer.getBuffer().get(i).split(",")[1];	
			

			
			writeImageFile(Base64.decodeBase64(bufferString), request.getServletContext().getRealPath("")+"/UploadImages/"+buffer.getProductID()+i+".jpeg");
		}
	}	

}
