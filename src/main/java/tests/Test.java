package main.java.tests;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.database.MongoConnection;
import main.java.modules.JoinGraphTraversalModule.JGTModule;
import org.bson.Document;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 13/10/2017.
 */
public class Test {

    public static void main(String[] args){

        MongoConnection connection = new MongoConnection();
        MongoDatabase db = connection.getDb();

        Map<String, List<List<String>>> mmap = new HashMap<>();

        List<List<String>> foo = new LinkedList<>();
        List<String> bar = new LinkedList<>();
        bar.add("ciao");
        bar.add("come");
        List<String> barz = new LinkedList<>();
        barz.add("stai");
        barz.add("??");
        foo.add(bar);
        foo.add(barz);

        List<List<String>> foo2 = new LinkedList<>();
        List<String> bar1 = new LinkedList<>();
        bar1.add("ciao");
        bar1.add("come");
        List<String> barz1 = new LinkedList<>();
        barz1.add("stai");
        barz1.add("??");
        foo2.add(bar1);
        foo2.add(barz1);

        mmap.put("foo", foo);
        mmap.put("foo2", foo2);

        String sessionName = "lool";
        String branchName = "try";


        MongoCollection<Document> JGTcoll = db.getCollection("JGTModuleColl");
        JGTcoll.deleteMany(new Document("sessionName", sessionName).append("branchName", branchName));
        JGTcoll.insertOne(new Document("sessionName", sessionName)
                .append("branchName",branchName)
                .append("map", mmap)
        );

    }
}
