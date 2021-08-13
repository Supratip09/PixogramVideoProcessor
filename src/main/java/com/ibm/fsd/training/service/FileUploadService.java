package com.ibm.fsd.training.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.fsd.training.constant.PixogramConstant;
import com.ibm.fsd.training.model.FileDetails;
import com.ibm.fsd.training.repository.FileUploadRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class FileUploadService {
	
	@Autowired
	private FileUploadRepository fileUploadRepository;
	
	public List<FileDetails> getAll(final String fileType) {
		if(StringUtils.isEmpty(fileType)) {
			return fileUploadRepository.findAll();
		}
		if(PixogramConstant.ALLOWED_IMAGE_TYPE.equals(fileType))
			return fileUploadRepository.findByType(PixogramConstant.ALLOWED_IMAGE_TYPE);
		else if(PixogramConstant.ALLOWED_VIDEO_TYPE.equals(fileType))
			return fileUploadRepository.findByType(PixogramConstant.ALLOWED_VIDEO_TYPE);
		else
			throw new RuntimeException("Unknown File Types.Please provide a valid file type to process further."); 
		
	}
	
	public FileDetails get(final String id) {
		return fileUploadRepository.findById(id).orElse(null);
	}
	
	public void save(final FileDetails fileDetails) {
		fileUploadRepository.save(fileDetails);
	}
	
	public void saveFile(MultipartFile file,String dirPath,String type) {
		try {
			Path root = null;
			if(PixogramConstant.ALLOWED_IMAGE_TYPE.equals(type))
				root = Paths.get(PixogramConstant.IMG_PATH+dirPath);
			else if(PixogramConstant.ALLOWED_VIDEO_TYPE.equals(type)) 
				root = Paths.get(PixogramConstant.VIDEO_PATH+dirPath);
			else
				root = Paths.get(PixogramConstant.UNKNOWN_PATH+dirPath);
			Files.createDirectory(root);
		    Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
		    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}
	
	public void saveVideoThumbnailFile(MultipartFile file,String dirPath,String videoFileName,String thumbnailFileName,String type) {
	try {
		Path thumbnail = Paths.get(PixogramConstant.VIDEO_THUMBNAIL_PATH+dirPath);
		Files.createDirectory(thumbnail);
		if(PixogramConstant.ALLOWED_IMAGE_TYPE.equals(type)) {
			Thumbnails.of(PixogramConstant.IMG_PATH+dirPath+File.separator+videoFileName)
	        .size(640, 480)
	        .outputFormat("JPEG")
	        .outputQuality(1)
	        .toFile(new File(PixogramConstant.VIDEO_THUMBNAIL_PATH+dirPath+File.separator+thumbnailFileName));

		}
		else if(PixogramConstant.ALLOWED_VIDEO_TYPE.equals(type)) {
		  VideoImageFrame.randomGrabberFFmpegImage(
		  PixogramConstant.VIDEO_PATH+dirPath+File.separator+videoFileName,
		  PixogramConstant.VIDEO_THUMBNAIL_PATH+dirPath, "screenshot", 1);
		}
	} catch (IOException e) {
		throw new RuntimeException("Could not store the thumbnail file. Error: " + e.getMessage());
	}
	}

}
