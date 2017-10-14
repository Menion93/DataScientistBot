package main.java.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import main.java.Dataset.Dataset;
import main.java.Session.MessageEntry;
import org.bson.Document;
import org.ejml.alg.dense.linsol.lu.LinearSolverLuKJI;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 12/10/2017.
 */
public class MongoRepository extends DBRepository {

    private MongoDatabase db;

    public MongoRepository(){
        MongoConnection connection = new MongoConnection();
        db = connection.getDb();
    }

    @Override
    public void saveMessage(List<MessageEntry> conversation) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        List<Document> documents = new LinkedList<>();

        for(MessageEntry entry : conversation){
            documents.add(new Document("sessionName", sessionName)
                    .append("branchName",branchName)
                    .append("message", entry.getMessage())
                    .append("date", entry.getTimestamp())
                    .append("userMessage", entry.isUserMessage()));
        }

        MongoCollection<Document> messages = db.getCollection("MessagesColl");
        messages.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        messages.insertMany(documents);
    }

    @Override
    public void saveJGTAnalysis(Map<String,Map<String, List<List<String>>>> goodAnalysis) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        MongoCollection<Document> JGTcoll = db.getCollection("JGTModuleColl");
        JGTcoll.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        JGTcoll.insertOne(new Document("sessionName", sessionName)
                                .append("branchName",branchName)
                                .append("map", goodAnalysis)
        );
    }

    @Override
    public void saveLFEAnalysis(Map<String, Map<Integer, String>> dataset2transf) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        MongoCollection<Document> LFEcoll = db.getCollection("LFEModuleColl");
        LFEcoll.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        LFEcoll.insertOne(new Document("sessionName", sessionName)
                .append("branchName",branchName)
                .append("map", dataset2transf)
        );
    }

    @Override
    public Map<String,Map<String, List<List<String>>>> getJGTAnalysis() {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> JGTcoll = db.getCollection("JGTModuleColl");
        Document result = JGTcoll.find(document).iterator().next();

        if(result == null){
            System.out.println("JGTData not found");
            return null;
        }

        return (Map<String,Map<String, List<List<String>>>>) result.get("map");
    }

    @Override
    public Map<String, Map<Integer, String>> getLFEAnalysis() {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> LFEcoll = db.getCollection("JFEModuleColl");
        Document result = LFEcoll.find(document).iterator().next();

        if(result == null){
            System.out.println("LFEData not found");
            return null;
        }

        return (Map<String, Map<Integer, String>>) result.get("map");
    }

    @Override
    public boolean isAValidBranch(String branchName) {
        String sessionName = session.getSessionName();
        Document query = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> sessionColl = db.getCollection("SessionColl");
        MongoCursor<Document> cursor = sessionColl.find(query).iterator();

        return cursor.hasNext();
    }

    @Override
    public List<MessageEntry> getConversation() {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> messagesColl = db.getCollection("MessagesColl");
        MongoCursor<Document> cursor = messagesColl.find(document).iterator();

        List<MessageEntry> messages = new LinkedList<>();

        while(cursor.hasNext()){
            Document doc = cursor.next();
            messages.add(new MessageEntry((String) doc.get("message"),
                        (boolean) doc.get("userMessage"),
                        (long) doc.get("date")));
        }

        return messages;
    }

    @Override
    public List<Dataset> getDatasetsPool() {

        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> messagesColl = db.getCollection("DatasetColl");
        List<String> datasetNames = (List<String>) messagesColl.find(document).iterator().next().get("names");
        List<Dataset> datasetList = new LinkedList<>();

        for(String name : datasetNames)
            datasetList.add(new Dataset(name));

        return datasetList;

    }

    @Override
    public void saveDatasetPool(List<Dataset> datasetsPool) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        List<String> datasetNames = new LinkedList<>();

        for(Dataset ds : datasetsPool)
            datasetNames.add(ds.getDatasetName());

        MongoCollection<Document> datasetColl = db.getCollection("DatasetColl");
        datasetColl.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        datasetColl.insertOne(new Document("sessionName", sessionName)
                .append("branchName", branchName)
                .append("names", datasetNames));
    }

    @Override
    public void registerSession() {
        Document query = new Document("sessionName", session.getSessionName())
                .append("branchName", session.getBranchName());
        MongoCollection<Document> sessionColl = db.getCollection("SessionColl");
        sessionColl.deleteMany(query);
        sessionColl.insertOne(query);

    }

    @Override
    public void saveSchemaAutocompleteAnalysis(Map<String, Map<String, Double>> allAnalysis) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        MongoCollection<Document> SACcoll = db.getCollection("SchemaAutocompleteModuleColl");
        SACcoll.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        SACcoll.insertOne(new Document("sessionName", sessionName)
                .append("branchName",branchName)
                .append("map", allAnalysis)
        );
    }

    @Override
    public Map<String, Map<String, Double>> getSchemaAutocompleteAnalysis() {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> JGTcoll = db.getCollection("SchemaAutocompleteModuleColl");
        Document result = JGTcoll.find(document).iterator().next();

        if(result == null){
            System.out.println("SACData not found");
            return null;
        }

        return (Map<String,Map<String, Double>>) result.get("map");
    }
}
