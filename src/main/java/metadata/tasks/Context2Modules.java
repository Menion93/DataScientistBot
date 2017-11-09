package main.java.metadata.tasks;

import main.java.database.MetadataRepository;
import main.java.metadata.MetadataTask;

/**
 * Created by Andrea on 07/11/2017.
 */
public class Context2Modules extends MetadataTask {

    public Context2Modules(MetadataRepository repository) {
        super(repository, "Context2Modules");
    }

    @Override
    public void generateMetadata() {
        repository.generateTag2Modules();
    }
}
