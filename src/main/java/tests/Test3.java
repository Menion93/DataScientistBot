package main.java.tests;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.database.MongoConnection;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 13/10/2017.
 */
public class Test3 {

    public static void main(String[] args){

        MongoConnection connection = new MongoConnection();
        MongoDatabase db = connection.getDb();

        Document document = new Document("ciao","lol");
        MongoCollection<Document> JGTcoll = db.getCollection("JGTModuleColl");
        Document result = JGTcoll.find(document).iterator().next();

        if(result == null){
            System.out.println("Analysis not found");
        }

        System.out.println(result.toString());

        Map<String, List<List<String>>> analysis = new HashMap<>();

        analysis = (Map<String, List<List<String>>>) result.get("map");

        for(Map.Entry entry : analysis.entrySet()){
            System.out.println(entry.getKey() + "\t" + entry.getValue().toString());
        }
    }
}
