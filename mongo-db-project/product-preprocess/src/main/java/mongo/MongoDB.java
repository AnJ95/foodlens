package mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.in;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	
	private int limit;
	private MongoCollection<Document> collection;
	private MongoClient mongoClient;
	
	public MongoDB(String dbName, String collectionName, int limit) {
		this.limit = limit;
		this.mongoClient = new MongoClient("localhost", 27017);
		
		MongoDatabase db = mongoClient.getDatabase(dbName);
		System.out.println("Connected to database '" + db.getName() + "'");
		
		this.collection = db.getCollection(collectionName);
		System.out.println("Selecting collection '" + collectionName + "' with " + collection.countDocuments() + " documents");	 
	}
	
	public FindIterable<Document> getProducts() {
		return collection.find(getFilter()).limit(limit);
	}


	public Bson getFilter() {
		//return eq("product_name", "Soft Cake Orange");
		return and(
				in("countries", "Deutschland"),
				exists("images.front_de")
				);
	}

	public void close() {
		mongoClient.close();
	}

}
