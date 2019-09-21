package main;

import java.awt.Image;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import image.ImageHelper;
import mongo.MongoDB;
import ocr.TesseractHelper;


public class Main {
	
	private static String DB_NAME = "off";
	private static String COLLECTION_NAME = "products";
	private static int LIMIT = 1;
	
	
	public static void main(String[] args) {
		
		MongoDB db = new MongoDB(DB_NAME, COLLECTION_NAME, LIMIT);
		Iterable<Document> result = db.getProducts();
		
		for (Document product : result) {
			System.out.println("#### " + product.getString("product_name"));
			
			Iterable<String> imagePaths = getImagePaths(product);
			for (String imagePath : imagePaths) {
				System.out.println(imagePath);
				
				File file = new File("images/" + product.getString("code") + ".jpg");

				
				if (!file.exists() || true) { // TODO
					Image image = ImageHelper.dowloadImage(imagePath);
					if (image != null) {
						ImageHelper.saveImage(file, image);
					}
					
					String ocr = TesseractHelper.doOCR(file);
					System.out.println(ocr);
				}
			}
		}
		
		db.close();
	}
	
	
	
	public static Iterable<String> getImagePaths(Document product) {
		List<String> results = new LinkedList<String>();
		
		String productCode = product.getString("code");
		
		Document images = product.get("images", Document.class);
		Document frontImage = images.get("front_de", Document.class);
		String imgRev = (frontImage.get("rev") instanceof Integer) ? frontImage.getInteger("rev").toString() : frontImage.getString("rev");
		
		// Two possible url formats:
		// When code length gte 10:	xxx/xxx/xxx/xxx...
		// Otherwise:				xxx...
		String dir = productCode;
		if (productCode.length() >= 10) {
			dir = String.format("%s/%s/%s/%s", productCode.substring(0,3), productCode.substring(3,6), productCode.substring(6,9), productCode.substring(9));
		}
		String path = String.format("https://static.openfoodfacts.org/images/products/%s/front_de.%s.full.jpg", dir, imgRev);
		
		results.add(path);
		return results;
	}
	
	
}
