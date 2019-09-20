
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.api.services.vision.v1.Vision.Images;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

/*
import com.google.api.services.vision.v1.Vision.Builder;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpRequestInitializer;
*/



public class Main {
	
	
	private static String DB_NAME = "off";
	private static String COLLECTION_NAME = "products";
	private static int LIMIT = 10000000;
	
	public static void main(String[] args) {
		
		/*
		HttpTransport transport;
		JsonFactory jsonFactory;
		HttpRequestInitializer httpRequestInitializer;
		Builder b = new Builder(transport, jsonFactory, httpRequestInitializer);
		*/
		
		
		
		MongoCollection<Document> collection = getProducts();
		
		FindIterable<Document> result = collection.find(getFilter());
		result = result.limit(LIMIT);
		
		
		for (Document product : result) {
			System.out.println("#### " + product.getString("product_name"));
			
			Iterable<String> imagePaths = getImagePaths(product);
			for (String imagePath : imagePaths) {
				System.out.println(imagePath);
				
				File file = new File("images/" + product.getString("code") + ".jpg");
				if (!file.exists()) {
					Image image = dowloadImage(imagePath);
					if (image != null) {
						saveImage(file, image);
					}
				}
			}
		}
	}
	
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
	
	public static Iterable<String> getImagePaths(Document product) {
		List<String> results = new LinkedList<String>();
		
		String productCode = product.getString("code");
		//results.add(id);
		
		Document images = product.get("images", Document.class);
		Document frontImage = images.get("front_de", Document.class);
		String imgRev = (frontImage.get("rev") instanceof Integer) ? frontImage.getInteger("rev").toString() : frontImage.getString("rev");
		
		
		String dir = productCode;
		if (productCode.length() >= 9) {
			dir = String.format("%s/%s/%s/%s", productCode.substring(0,3), productCode.substring(3,6), productCode.substring(6,9), productCode.substring(9));
		}
		String path = String.format("https://static.openfoodfacts.org/images/products/%s/front_de.%s.full.jpg", dir, imgRev);
		
		results.add(path);
		//Document images = product.get("images", Document.class);
		//images.forEach( (str, obj) -> {results.add(str);} );
		return results;
	}
	
	public static Bson getFilter() {
		//return eq("product_name", "Soft Cake Orange");
		return and(
				in("countries", "Deutschland"),
				exists("images.front_de")
				);
	}
	
	
	public static MongoCollection<Document> getProducts() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		System.out.println("Connected to database '" + db.getName() + "'");
		
		MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
		System.out.println("Selecting collection '" + COLLECTION_NAME + "' with " + collection.countDocuments() + " documents");
		//System.out.println("Selecting collection '" + COLLECTION_NAME + "'");
		
		return collection;
	}
}
