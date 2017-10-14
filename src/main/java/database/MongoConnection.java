package main.java.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Andrea on 12/10/2017.
 */
public class MongoConnection {

    private MongoClient mongoClient;

    MongoDatabase db;

    public MongoConnection(){
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017/DataScienceBot");
        mongoClient = new MongoClient(connectionString);
        db = mongoClient.getDatabase(connectionString.getDatabase());
    }

    public MongoDatabase getDb(){
        return db;
    }
}
