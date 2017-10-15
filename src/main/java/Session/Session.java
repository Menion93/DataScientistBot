package main.java.Session;

import main.java.Dataset.Dataset;
import main.java.database.DBRepository;
import main.java.modules.Module;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class Session {

    private String sessionName;
    private String branchName;

    private List<MessageEntry> conversation;
    private List<Module> modules;
    private DBRepository repository;
    private List<Dataset> datasetsPool;

    public Session(DBRepository repository){
        // Take a new id from the database and assign it to this analysis

        conversation = new LinkedList<>();
        this.repository = repository;
    }

    public void setSessionInfo(String analysis, String branch){
        this.sessionName = analysis;
        this.branchName = branch;
    }

    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    public void logMessage(String message, boolean isTheUser){
        conversation.add(new MessageEntry(message, isTheUser));
    }

    public void logMessage(List<String> messages, boolean isTheUser){
        for(String message : messages)
            logMessage(message, isTheUser);
    }

    public void saveSessionInfo(){
        repository.saveMessage(conversation);
        repository.saveDatasetPool(datasetsPool);
        repository.registerSession();
    }

    public String getBranchName(){return this.branchName; }
    public String getSessionName(){return this.sessionName;}

    // Will load the conversation with branchName and sessionName
    public void loadSession() {

        this.conversation = repository.getConversation();
        this.datasetsPool = repository.getDatasetsPool();
    }

    public List<Dataset> getDatasetPool() {
        return datasetsPool;
    }

    public Dataset getDatasetByName(String datasetName) {
        for(Dataset ds : datasetsPool){
            if(ds.getDatasetName().equals(datasetName))
                return ds;
        }
        return null;
    }

}
