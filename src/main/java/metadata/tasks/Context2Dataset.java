package main.java.metadata.tasks;

import main.java.database.MetadataRepository;
import main.java.metadata.MetadataTask;

/**
 * Created by Andrea on 10/11/2017.
 */
public class Context2Dataset extends MetadataTask{

    public Context2Dataset(MetadataRepository repository) {
        super(repository, "Context2Dataset");
    }

    @Override
    public void generateMetadata() {
        repository.generateContext2Dataset();
    }
}
