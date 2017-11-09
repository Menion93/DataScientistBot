package main.java.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 07/11/2017.
 */
public class MongoRecomRepository extends RecommendRepository{

    MongoDatabase db;

    public MongoRecomRepository(){
        MongoConnection connection = new MongoConnection();
        db = connection.getDb();
    }

    @Override
    public Map<String, Map<String, Double>> getModulesByTags(List<String> tags) {

        Map<String, Map<String, Double>> result = new HashMap<>();

        MongoCollection<Document> collection = db.getCollection("ModuleMetaColl");

        for(String tag : tags){
            Document query = new Document("tag", tag);
            MongoCursor<Document> cursor = collection.find(query).iterator();

            if(cursor.hasNext()){
                Document tagRes = cursor.next();
                result.put(tag, (Map<String, Double>) tagRes.get("module2count"));
            }
        }

        return result;
    }
}
