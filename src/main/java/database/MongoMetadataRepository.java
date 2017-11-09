package main.java.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;

/**
 * Created by Andrea on 07/11/2017.
 */
public class MongoMetadataRepository extends MetadataRepository {

    private MongoDatabase db;

    public MongoMetadataRepository(){
        MongoConnection connection = new MongoConnection();
        db = connection.getDb();
    }

    @Override
    public void generateTag2Modules() {
        Map<String,List<String>> sessionName2tags = new HashMap<>();
        Map<String, Map<String, Double>> tag2modules = new HashMap<>();


        MongoCollection<Document> sessionsColl = db.getCollection("SessionColl");
        MongoCursor<Document> sessionCursor = sessionsColl.find().iterator();

        while(sessionCursor.hasNext()){
            Document doc = sessionCursor.next();
            sessionName2tags.put(doc.getString("sessionName"), (List<String>) doc.get("context"));
        }

        MongoCollection<Document> modulesColl = db.getCollection("ModuleColl");

        for(Map.Entry<String,List<String>> entry : sessionName2tags.entrySet()){
            Document query = new Document("sessionName", entry.getKey());
            MongoCursor<Document> moduleCursor = modulesColl.find(query).iterator();

            Set<String> modules = new HashSet<>();
            // For all
            while(moduleCursor.hasNext()){
                Document doc = moduleCursor.next();
                modules.addAll((List<String>) doc.get("moduleList"));
            }

            incrementModuleCounts(tag2modules, modules, entry.getValue());

        }

        MongoCollection<Document> moduleMetaColl = db.getCollection("ModuleMetaColl");
        moduleMetaColl.deleteMany(new Document());

        // Save the values
        for(Map.Entry entry : tag2modules.entrySet()){
            moduleMetaColl.insertOne(new Document("tag", entry.getKey())
                                            .append("module2count", entry.getValue()));
        }

    }

    private void incrementModuleCounts(Map<String, Map<String, Double>> tag2modules,
                                       Set<String> modules, List<String> tags) {
        for(String tag : tags){
            if(!tag2modules.containsKey(tag)){
                tag2modules.put(tag, new HashMap<>());
            }
            for(String module : modules){
                if(!tag2modules.get(tag).containsKey(module)){
                    tag2modules.get(tag).put(module, 0.d);
                }
                tag2modules.get(tag).put(module, tag2modules.get(tag).get(module) + 1);
            }
        }

    }


}
