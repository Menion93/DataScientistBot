package main.java.tests;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.session.MessageEntry;
import main.java.database.MongoConnection;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 13/10/2017.
 */
public class Test2 {

    public static void main(String[] args){


        // We cannot use custom objects!! this wont work
        MongoConnection connection = new MongoConnection();
        MongoDatabase db = connection.getDb();

        List<MessageEntry> entr = new LinkedList<>();
        entr.add(new MessageEntry("lol", false));
        entr.add(new MessageEntry("kek", true));


        MongoCollection coll = db.getCollection("messages");
        coll.insertOne(new Document("conversation",entr ));

    }

}




