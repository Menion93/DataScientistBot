package main.java.tests;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import main.java.Session.MessageEntry;
import main.java.database.MongoConnection;
import org.bson.Document;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 13/10/2017.
 */
public class Test4 {

    public static void main(String[] args){

        MongoConnection connection = new MongoConnection();
        MongoDatabase db = connection.getDb();

        String sessionName = "lol";
        String branchName = "lol";

        List<Document> documents = new LinkedList<>();
        List<MessageEntry> conversation = new LinkedList<>();
        conversation.add(new MessageEntry("CIAO", true));
        conversation.add(new MessageEntry("come stai", false));

        for(MessageEntry entry : conversation){
            documents.add(new Document("sessionName", sessionName)
                    .append("analysisName",branchName)
                    .append("message", entry.getMessage())
                    .append("date", entry.getTimestamp())
                    .append("userMessage", entry.isUserMessage()));
        }

        MongoCollection<Document> messagesColl = db.getCollection("MessagesColl");
        messagesColl.insertMany(documents);

        Document document = new Document("sessionName", sessionName).append("analysisName", branchName);
        MongoCursor<Document> cursor = messagesColl.find(document).iterator();

        List<MessageEntry> messages = new LinkedList<>();

        while(cursor.hasNext()){
            Document doc = cursor.next();
            messages.add(new MessageEntry((String) doc.get("message"),
                    (boolean) doc.get("userMessage"),
                    (long) doc.get("date")));
        }

        System.out.println(messages.toString());
    }
}
