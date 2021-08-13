package com.ibm.fsd.training.constant;

public class PixogramConstant {

	//External Configs -- Need to pull from outside
	public final static String IMG_PATH = "C:/training/Uploads/img/";
	public static final String VIDEO_PATH = "C:/training/Uploads/video/";
	public final static String VIDEO_THUMBNAIL_PATH = "C:/training/Uploads/thumbnail/";
	public static final String UNKNOWN_PATH = "C:/training/Uploads/unknown/";
	
	//CORS
	public static final String CORS_LIST = "http://localhost:4200";
		
	// Internal Config Properties
	public static final long CHUNK_SIZE = 1000000L;
	public static final String ALLOWED_IMAGE_TYPE = "image";
	public static final String ALLOWED_VIDEO_TYPE = "video";
	
	//URLS
	public static final String URL_FILE_UPLOAD = "/v1/api/pixo/upload/file";
	public static final String URL_VIDEO_DOWNLOAD = "/v1/api/pixo/view/video/{id}";
	public static final String URL_THUMBNAIL_IMAGE_DOWNLOAD = "/v1/api/pixo/view/image/thumbnail/{id}";
	public static final String URL_IMAGE_DOWNLOAD = "/v1/api/pixo/view/image/{id}";
	public static final String URL_FILE_INFO = "/v1/api/pixo/info/file";
	
	//Hide Constructor
	private PixogramConstant() {}
}
