package image;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageHelper {
	public static Image dowloadImage(String imagePath) {
		Image image = null;
		try {
		    URL url = new URL(imagePath);
		    image = ImageIO.read(url);
		} catch (IOException e) {
			System.out.println("Could not download image at path " + imagePath);
		}
		return image;
	}
	
	public static void saveImage(File file, Image image) {
		try {
		    if (!ImageIO.write((RenderedImage) image, "jpg", file)) {
		    	throw new IOException();
		    }
		} catch (IOException e) {
			System.out.println("Could not save image at path " + file.getAbsolutePath());
		}
	}
}
