package com.ibm.fsd.training.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails {

	@Id
	private String id;
	private String title;
	private String type;
	private String videoposter;
	private String description;
	private String effect;
	private String filename;
	private String filetype;
	private long filesize;
	private String uploaddate;
	private String uploadtime;
	private int defaultprofile;
	private int likes;
	private int unlike;
	private int shares;
	private int numberofcomments;
	private List<TagDetails> tags = new ArrayList<>();
}
