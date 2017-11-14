package main.java.metadata;

import main.java.database.MetadataRepository;
import main.java.database.MongoMetadataRepository;
import main.java.metadata.tasks.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by Andrea on 07/11/2017.
 */
public class TaskSubscription {

    public Map<String, MetadataTask> metadataTasks;

    public TaskSubscription(){
        MetadataRepository repository = new MongoMetadataRepository();
        metadataTasks = new HashMap<>();

        metadataTasks.put("Context2Dataset", new Context2Dataset(repository));
        metadataTasks.put("Context2Model", new Context2Model(repository));
        metadataTasks.put("Context2Modules", new Context2Modules(repository));

    }

    public void runAllTasks(){
        for(MetadataTask task : metadataTasks.values())
            task.generateMetadata();
    }

    public void runTaskByName(String name){
        MetadataTask task = metadataTasks.get(name);

        if(task != null)
            task.generateMetadata();
        else
            System.out.println("Task name not found");
    }


}
