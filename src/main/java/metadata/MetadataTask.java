package main.java.metadata;

import main.java.database.MetadataRepository;

/**
 * Created by Andrea on 04/11/2017.
 */
public abstract class MetadataTask {

    protected MetadataRepository repository;
    protected String metadataName;

    public MetadataTask(MetadataRepository repository, String name){
        this.repository = repository;
        this.metadataName = name;
    }

    public abstract void generateMetadata();
}
