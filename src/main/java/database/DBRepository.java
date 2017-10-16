package main.java.database;

import main.java.Dataset.Dataset;
import main.java.Session.MessageEntry;
import main.java.Session.Session;

import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 12/10/2017.
 */
public abstract class DBRepository {

    protected Session session;

    public DBRepository(){}

    public void setSession(Session session){
        this.session = session;
    }
    public abstract void saveMessage(List<MessageEntry> conversation);
    public abstract void saveJGTAnalysis(Map<String,Map<String, List<List<String>>>> goodAnalysis);
    public abstract void saveLFEAnalysis(Map<String, Map<Integer, String>> dataset2transf);
    public abstract Map<String, Map<String,List<List<String>>>> getJGTAnalysis();
    public abstract Map<String,Map<Integer,String>> getLFEAnalysis();
    public abstract boolean isAValidBranch(String branchName);
    public abstract List<MessageEntry> getConversation();
    public abstract List<Dataset> getDatasetsPool();
    public abstract void saveDatasetPool(List<Dataset> datasetsPool);
    public abstract void registerSession();
    public abstract void saveSchemaAutocompleteAnalysis(Map<String, Map<String, Double>> allAnalysis);
    public abstract Map<String,Map<String,Double>> getSchemaAutocompleteAnalysis();
    public abstract void deleteBranch(String branchName);
    public abstract boolean isAValidAnalysis(String analysisName);
    public abstract void deleteAnalysis(String analysisName);
    public abstract List<String> getBranches();
    public abstract List<String> getAnalysis();
}
