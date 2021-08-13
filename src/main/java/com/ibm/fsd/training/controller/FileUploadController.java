package com.ibm.fsd.training.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.fsd.training.constant.PixogramConstant;
import com.ibm.fsd.training.model.FileDetails;
import com.ibm.fsd.training.service.FileUploadService;

@RestController
@CrossOrigin(PixogramConstant.CORS_LIST)
public class FileUploadController {

	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Uploading Image/Video (Single or Multiple)
	 * 
	 * @param data the file descriptor
	 * @param files the files
	 * @return the response
	 */
	@PostMapping(PixogramConstant.URL_FILE_UPLOAD)
	public ResponseEntity<String> uploadFiles(@RequestParam("data") final String data,
			@RequestParam("files") final MultipartFile[] files) {
		try {
			List<FileDetails> fileDetails = objectMapper.reader().forType(new TypeReference<List<FileDetails>>() {}).readValue(data);

			if (fileDetails.size() != files.length)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("File Details & File Counts are not matching.");
			IntStream.range(0, fileDetails.size()).forEach(index -> {
				FileDetails fDetails = fileDetails.get(index);
				fDetails.setId(UUID.randomUUID().toString());
				fDetails.setFilename(files[index].getOriginalFilename());
				fileUploadService.save(fileDetails.get(index));
				fileUploadService.saveFile(files[index], fDetails.getId(),fDetails.getType());
				fileUploadService.saveVideoThumbnailFile(files[index], fDetails.getId(), fDetails.getFilename(), "screenshot-1.jpeg",fDetails.getType());
				
			});

			return ResponseEntity.status(HttpStatus.OK).body("Files Uploaded Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Fail to upload files!");
		}
	}

}
