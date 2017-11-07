package main.java.recommending;

import main.java.database.MetadataRepository;
import main.java.database.MongoMetadataRepository;
import main.java.metadata.MetadataTask;
import main.java.metadata.tasks.Context2Modules;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 07/11/2017.
 */
public class RecomSubscription {


    public Map<String, Recommendation> recomTasks;

    public RecomSubscription(){
        MetadataRepository repository = new MongoMetadataRepository();
        recomTasks = new HashMap<>();

    }


    public void runTaskByName(String name){
        Recommendation recom = recomTasks.get(name);

        if(recom != null)
            recom.makeRecommendation();
        else
            System.out.println("Task name not found");
    }

}
