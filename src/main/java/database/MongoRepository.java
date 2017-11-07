package main.java.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import main.java.dataset.Dataset;
import main.java.modules.MLModule.Evaluation;
import main.java.session.MessageEntry;
import org.bson.Document;
import java.util.*;

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
        Document result = JGTcoll.find(document).iterator().tryNext();

        if(result == null){
            System.out.println("JGTData not found");
            return new HashMap<String,Map<String, List<List<String>>>>();
        }

        return (Map<String,Map<String, List<List<String>>>>) result.get("map");
    }

    @Override
    public Map<String, Map<Integer, String>> getLFEAnalysis() {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> LFEcoll = db.getCollection("LFEModuleColl");
        Document result = LFEcoll.find(document).iterator().tryNext();

        if(result == null){
            System.out.println("LFEData not found");
            return new HashMap<String, Map<Integer, String>>();
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
        MongoCollection<Document> datasetColl = db.getCollection("DatasetMetadataColl");
        List<Dataset> datasetList = new LinkedList<>();

        MongoCursor<Document> datasets = datasetColl.find(document).iterator();

        while(datasets.hasNext()){
            Document ds = datasets.next();
            Dataset dataset = new Dataset(ds.getString("name"),
                    ds.getString("root"),
                    ds.getString("from"),
                    false,
                    this);
            dataset.isNew(false);
            datasetList.add(dataset);
        }

        return datasetList;
    }


    @Override
    public void registerSession(List<String> context) {
        Document query = new Document("sessionName", session.getSessionName())
                .append("branchName", session.getBranchName());
        MongoCollection<Document> sessionColl = db.getCollection("SessionColl");
        sessionColl.deleteMany(query);
        sessionColl.insertOne(query.append("context", context));

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
        Document result = JGTcoll.find(document).iterator().tryNext();

        if(result == null){
            System.out.println("SACData not found");
            return new HashMap<>();
        }

        return (Map<String,Map<String, Double>>) result.get("map");
    }

    @Override
    public void deleteBranch(String branchName) {
        String sessionName = session.getSessionName();
        List<MongoCollection<Document>> collections = new LinkedList<>();

        Document query = new Document("sessionName", sessionName).append("branchName", branchName);
        collections.add(db.getCollection("SessionColl"));
        collections.add(db.getCollection("JGTModuleColl"));
        collections.add(db.getCollection("SchemaAutocompleteModuleColl"));
        collections.add(db.getCollection("DatasetMetadataColl"));
        collections.add(db.getCollection("MessagesColl"));
        collections.add(db.getCollection("LFEModuleColl"));

        for(MongoCollection coll : collections)
            coll.deleteMany(query);
    }

    @Override
    public boolean isAValidAnalysis(String analysisName) {
        Document query = new Document("sessionName", analysisName).append("branchName", "main");
        MongoCollection<Document> sessionColl = db.getCollection("SessionColl");
        MongoCursor<Document> cursor = sessionColl.find(query).iterator();

        return cursor.hasNext();
    }

    @Override
    public void deleteAnalysis(String analysisName) {
        List<MongoCollection<Document>> collections = new LinkedList<>();

        Document query = new Document("sessionName", analysisName);
        collections.add(db.getCollection("SessionColl"));
        collections.add(db.getCollection("SchemaAutocompleteModuleColl"));
        collections.add(db.getCollection("JGTModuleColl"));
        collections.add(db.getCollection("DatasetMetadataColl"));
        collections.add(db.getCollection("MessagesColl"));
        collections.add(db.getCollection("LFEModuleColl"));

        for(MongoCollection coll : collections)
            coll.deleteMany(query);
    }

    @Override
    public List<String> getBranches() {
        Document query = new Document("sessionName", session.getSessionName());
        MongoCollection coll = db.getCollection("SessionColl");
        MongoCursor<Document> cursor = coll.find(query).iterator();
        List<String> branches = new LinkedList<>();

        while(cursor.hasNext()){
            Document doc = cursor.next();
            branches.add((String) doc.get("branchName"));
        }

        return branches;
    }

    @Override
    public List<String> getAnalysis() {
        MongoCollection coll = db.getCollection("SessionColl");
        MongoCursor<Document> cursor = coll.find(new Document()).iterator();
        Set<String> analysis = new HashSet<>();

        while(cursor.hasNext()){
            Document doc = cursor.next();
            analysis.add((String) doc.get("sessionName"));
        }

        return new LinkedList<>(analysis);
    }

    @Override
    public void saveDataset(Dataset dataset) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        MongoCollection<Document> datasetMetadataColl = db.getCollection("DatasetMetadataColl");
        Document queryDS = new Document("sessionName", sessionName)
                .append("branchName", branchName)
                .append("name", dataset.getDatasetName());
        datasetMetadataColl.deleteOne(queryDS);
        Document insertQuery = new Document("sessionName", sessionName)
                .append("branchName", branchName)
                .append("name", dataset.getDatasetName())
                .append("root", dataset.getRoot())
                .append("from", dataset.getFrom());

        datasetMetadataColl.insertOne(insertQuery);

        if(dataset.isNew()){
            MongoCollection<Document> datasetColl = db.getCollection("DatasetColl");
            Document insertQuery2 = new Document("name", dataset.getDatasetName())
                    .append("data",
                            new Document("numerical",dataset.getNumericalAttributes())
                                    .append("categorical", dataset.getCategoricalAttributes()))
                    .append("schema", dataset.getSchema())
                    .append("types", dataset.getTypes());
            datasetColl.insertOne(insertQuery2);
            dataset.isNew(false);

        }

    }

    @Override
    public void loadData(Dataset dataset) {
        MongoCollection<Document> datasetColl = db.getCollection("DatasetColl");
        Document mongoDataset = null;
        try{
            Document query = new Document("name", dataset.getDatasetName());
            mongoDataset = datasetColl.find(query).iterator().next();
        }
        catch(Exception ex){}

        // Import the data
        if(mongoDataset != null){
            Document data = (Document) mongoDataset.get("data");
            dataset.setNumerical((Map<String, List<Double>>) data.get("numerical"));
            dataset.setCategorical((Map<String, List<String>>) data.get("categorical"));
            dataset.setSchema((List<String>) mongoDataset.get("schema"));
            dataset.setTypes((List<String>) mongoDataset.get("types"));
            dataset.isNew(false);
        }

        // Check for metadata
        MongoCollection<Document> datasetMetadataColl = db.getCollection("DatasetMetadataColl");
        Document metadata = null;
        try{
            Document query = new Document("name", dataset.getDatasetName());
            metadata = datasetColl.find(query).iterator().next();
        }
        catch(Exception ex){}

        if(metadata != null){
            dataset.setRoot((String) metadata.get("root"));
            dataset.setFrom((String) metadata.get("from"));
        }
        else{
            dataset.setRoot(dataset.getDatasetName());
            dataset.setFrom(null);
        }

    }

    @Override
    public boolean existDataset(String newName) {
        return db.getCollection("DatasetColl")
                .find(new Document("name", newName))
                .iterator()
                .hasNext();
    }

    @Override
    public void saveMLModuleAnalysis(List<Evaluation> evaluations) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        MongoCollection<Document> MLcoll = db.getCollection("MLModuleColl");
        MLcoll.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        for(Evaluation eval : evaluations)
            MLcoll.insertOne(new Document("sessionName", sessionName)
                    .append("branchName",branchName)
                    .append("datasetName", eval.getDatasetName())
                    .append("modelName", eval.getModelName())
                    .append("evaluationName", eval.getEvaluationName())
                    .append("evaluationScore", eval.getEvaluationScore()));
    }

    @Override
    public List<Evaluation> getMLModuleAnalysis() {

        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> MLcoll = db.getCollection("MLModuleColl");
        MongoCursor<Document> result = MLcoll.find(document).iterator();
        List<Evaluation> evaluations = new LinkedList<>();

        while(result.hasNext()){
            Document doc = result.next();
            Evaluation eval = new Evaluation();
            eval.setDatasetName(doc.getString("datasetName"));
            eval.setEvaluationName(doc.getString("evaluationName"));
            eval.setEvaluationScore(doc.getDouble("evaluationScore"));
            eval.setModelName(doc.getString("modelName"));

            evaluations.add(eval);
        }

        return evaluations;
    }

    @Override
    public Map<String, Map<String, List<String>>> getColumnWranglerAnalysis() {

        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();
        Document document = new Document("sessionName", sessionName).append("branchName", branchName);
        MongoCollection<Document> JGTcoll = db.getCollection("SchemaAutocompleteModuleColl");
        Document result = JGTcoll.find(document).iterator().tryNext();

        if(result == null){
            System.out.println("SACData not found");
            return new HashMap<>();
        }

        return (Map<String, Map<String, List<String>>>) result.get("map");
    }

    @Override
    public void saveColumnWranglerAnalysis(Map<String, Map<String, List<String>>> ds2transf) {
        String sessionName = session.getSessionName();
        String branchName = session.getBranchName();

        MongoCollection<Document> CWcoll = db.getCollection("ColumnWranglerModuleColl");
        CWcoll.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        CWcoll.insertOne(new Document("sessionName", sessionName)
                .append("branchName",branchName)
                .append("map", ds2transf)
        );
    }

}
