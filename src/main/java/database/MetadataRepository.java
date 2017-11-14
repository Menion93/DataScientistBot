package main.java.database;

/**
 * Created by Andrea on 07/11/2017.
 */
public abstract class MetadataRepository {

    public abstract void generateTag2Modules();
    public abstract void generateContext2Dataset();
    public abstract void generateContext2Model();

}
