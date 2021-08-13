package com.ibm.fsd.training.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
	public static void main(String[] args) throws IOException {
        BufferedImage crop = new Test().crop(0.8509);
        System.out.println(crop.getWidth() + "x" + crop.getHeight());
        ImageIO.write(crop, "png", new File("C:\\training\\Uploads\\img\\7944ec3b-d768-4bca-8642-acd058241df7\\Square.png"));
    }
 
    public BufferedImage crop(double amount) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File("C:\\training\\Uploads\\img\\7944ec3b-d768-4bca-8642-acd058241df7\\image (4).png"));
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        
        System.out.println("Height :: "+height+" Weith :: "+width);

       // int targetWidth = (int)(width * amount);
        //int targetHeight = (int)(height * amount);
        int targetWidth =Math.min(height, width);
        //int targetWidth = 700;
        int targetHeight = Math.min(height, width);
        // Coordinates of the image's middle
        int xc = (width - targetWidth) / 2;
        int yc = (height - targetHeight) / 2;

        // Crop
		
		  BufferedImage croppedImage = originalImage.getSubimage( 
				  xc, 
				  yc,
				  targetWidth,  // widht
				  targetHeight // height 
				  );
		 
        return croppedImage;
    }
}
