package com.ibm.fsd.training.service;

import java.awt.image.BufferedImage;

import java.io.File;
 
import java.io.IOException;
 
import java.util.ArrayList;
 
import java.util.Collections;
 
import java.util.List;
 
import javax.imageio.ImageIO;
 
import org.bytedeco.javacpp.opencv_core;
 
import org.bytedeco.javacpp.opencv_core.IplImage;
 
import org.bytedeco.javacv.FFmpegFrameGrabber;
 
import org.bytedeco.javacv.Frame;
 
import org.bytedeco.javacv.FrameGrabber.Exception;
 
import org.bytedeco.javacv.Java2DFrameConverter;
 
import org.bytedeco.javacv.OpenCVFrameConverter;
 

public class VideoImageFrame {
	public static void main(String[] args) throws Exception {
		randomGrabberFFmpegImage("C:\\training\\Uploads\\video\\43d6b25d-ad4d-49dc-9e33-1feb6f837205\\greenish_blue_butterfly_butterfly_523.mp4", "C:\\training\\Uploads\\thumbnail\\43d6b25d-ad4d-49dc-9e33-1feb6f837205", "screenshot", 2);
	}
 
	/**
	  * Generate image thumbnails
	  * @param filePath: video full path
	  * @param targerFilePath: thumbnail storage directory
	  * @param targetFileName: thumbnail file name
	  * @param randomSize: the number of random numbers generated
	 * @throws Exception
	 */
	public static void randomGrabberFFmpegImage(String filePath, String targerFilePath, String targetFileName, int randomSize) throws Exception {
		FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
		ff.start();
		String rotate = ff.getVideoMetadata("rotate");
		int ffLength = ff.getLengthInFrames();
		List<Integer> randomGrab = random(ffLength, randomSize);
		int maxRandomGrab = randomGrab.get(randomGrab.size() - 1);
		Frame f;
		int i = 0;
		while (i < ffLength) {
			f = ff.grabImage();
			if (randomGrab.contains(i)) {
				if (null != rotate && rotate.length() > 1) {
					OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
					IplImage src = converter.convert(f);
					f = converter.convert(rotate(src, Integer.valueOf(rotate)));
				}
				doExecuteFrame(f, targerFilePath, targetFileName, i);
			}
			if (i >= maxRandomGrab) {
				break;
			}
			i++;
		}
		ff.stop();
	}
 
	/**
	  * Rotate the picture
	  * @param src: image
	  * @param angle: rotation angle
	 * @return
	 */
	public static IplImage rotate(IplImage src, int angle) {
		IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
		opencv_core.cvTranspose(src, img);
		opencv_core.cvFlip(img, img, angle);
		return img;
	}
 
	/**
	  * Generate thumbnails
	  * @param f Frame object
	 * @param targerFilePath 
	 * @param targetFileName
	 * @param index
	 */
	public static void doExecuteFrame(Frame f, String targerFilePath, String targetFileName, int index) {
		if (null == f || null == f.image) {
			return;
		}
		Java2DFrameConverter converter = new Java2DFrameConverter();
		String imageMat = "png";
		String FileName = targerFilePath + File.separator + targetFileName + "_" + index + "." + imageMat;
		BufferedImage bi = converter.getBufferedImage(f);
		File output = new File(FileName);
		try {
			ImageIO.write(bi, imageMat, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/**
	  * Randomly generate random number sets
	  * @param baseNum: random seed
	  * @param length: random number set length
	  * @return: a collection of random numbers
	 */
	public static List<Integer> random(int baseNum, int length) {
		List<Integer> list = new ArrayList<>(length);
		while (list.size() < length) {
			Integer next = (int) (Math.random() * baseNum);
			if (list.contains(next)) {
				continue;
			}
			list.add(next);
		}
		Collections.sort(list);
		return list;
	}
 

}
