package main.java.metadata.tasks;

import main.java.database.MetadataRepository;
import main.java.metadata.MetadataTask;

/**
 * Created by Andrea on 10/11/2017.
 */
public class Context2Model extends MetadataTask {

    public Context2Model(MetadataRepository repository) {
        super(repository, "Context2Model");
    }

    @Override
    public void generateMetadata() {
        repository.generateContext2Model();
    }
}
