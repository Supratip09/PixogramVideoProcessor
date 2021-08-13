package com.ibm.fsd.training.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.fsd.training.constant.PixogramConstant;
import com.ibm.fsd.training.model.FileDetails;
import com.ibm.fsd.training.service.FileUploadService;
import com.ibm.fsd.training.service.VideoStreamingService;

@RestController
@CrossOrigin(PixogramConstant.CORS_LIST)
public class FileStreamController {

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private VideoStreamingService service;

	/**
	 * Download/View Image
	 * 
	 * @param id the image id
	 * @return the response
	 * @throws IOException the exception may be thrown
	 */
	@GetMapping(value = PixogramConstant.URL_FILE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDetails>> getFileInfo(@RequestParam("type") final String fileType) throws IOException {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fileUploadService.getAll(fileType));
	}
	
	/**
	 * Download/View Video
	 * 
	 * @param rangeHeader
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = PixogramConstant.URL_VIDEO_DOWNLOAD, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<ResourceRegion> streamVideo(
			@RequestHeader(value = "Range", required = false) String rangeHeader, @PathVariable("id") final String id)
			throws IOException {
		final String videoPath = new StringBuilder().append(PixogramConstant.VIDEO_PATH).append(id).append(File.separator).toString();

		if (!new File(videoPath).exists()) {
			new File(videoPath).mkdirs();
		}
		final FileDetails fileDetails = fileUploadService.get(id);
		return service.getVideoRegion(rangeHeader, videoPath, fileDetails.getFilename());

	}
	
	/**
	 * Download/View Image
	 * 
	 * @param id the image id
	 * @return the response
	 * @throws IOException the exception may be thrown
	 */
	@GetMapping(value = PixogramConstant.URL_IMAGE_DOWNLOAD, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> streamImage(@PathVariable("id") final String id) throws IOException {
		Path root = Paths.get(PixogramConstant.IMG_PATH+id);
		final String fileName = fileUploadService.get(id).getFilename();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(Files.readAllBytes(root.resolve(fileName)));
	}
	
	/*
	 * Download/View Thumbnail Images of Video
	 * 
	 * @param id the image id
	 * @return the response
	 * @throws IOException the exception may be thrown
	 */
	@GetMapping(value = PixogramConstant.URL_THUMBNAIL_IMAGE_DOWNLOAD, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> streamThumbnailImage(@PathVariable("id") final String id) throws IOException {
		Path root = Paths.get(PixogramConstant.VIDEO_THUMBNAIL_PATH+id);
	    String fileName = fileUploadService.get(id).getVideoposter();
		if(StringUtils.isEmpty(fileName)) {
			final FileDetails fileDetails = fileUploadService.get(id);
			fileName = new File(PixogramConstant.VIDEO_THUMBNAIL_PATH+id).list()[0];
			fileDetails.setVideoposter(fileName);
			fileUploadService.save(fileDetails);
		}
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(Files.readAllBytes(root.resolve(fileName)));
	}

}
