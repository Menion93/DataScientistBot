package main.java.tests;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.dataset.Dataset;
import main.java.database.MongoConnection;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 14/10/2017.
 */
public class Test5 {

    public static void main(String[] args){

        MongoConnection connection = new MongoConnection();
        MongoDatabase db = connection.getDb();

        String sessionName = "try";
        String branchName = "try";

        List<String> datasetNames = new LinkedList<>();
        List<Dataset> dlist = new LinkedList<>();
        dlist.add(new Dataset("lol","root", "from", false));
        dlist.add(new Dataset("ciao", "root", "from", false));

        for(Dataset ds : dlist)
            datasetNames.add(ds.getDatasetName());

        MongoCollection<Document> datasetColl = db.getCollection("DatasetColl");
        datasetColl.deleteMany(new Document("sessionName", sessionName).append("analysisName", branchName));
        datasetColl.insertOne(new Document("sessionName", sessionName)
                .append("analysisName", branchName)
                .append("names", datasetNames));


        Document document = new Document("sessionName", sessionName).append("analysisName", branchName);
        MongoCollection<Document> messagesColl = db.getCollection("DatasetColl");
        List<String> retrieved = (List<String>) messagesColl.find(document).iterator().next().get("names");
        List<Dataset> datasetList = new LinkedList<>();

        for(String name : retrieved)
            datasetList.add(new Dataset(name, "root", "from", false));
    }
}
