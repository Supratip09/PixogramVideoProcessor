package com.ibm.fsd.training.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.fsd.training.model.FileDetails;

@Repository
public interface FileUploadRepository extends MongoRepository<FileDetails, String>{
	
	public List<FileDetails> findByType(final String type);

}
