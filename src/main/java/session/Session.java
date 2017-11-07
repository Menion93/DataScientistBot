package main.java.session;

import main.java.dataset.Dataset;
import main.java.database.DBRepository;
import main.java.modules.ContextModule.ContextModule;
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
    private DBRepository repository;
    private List<Dataset> datasetsPool;
    private ContextModule context;

    public Session(DBRepository repository, ContextModule context){
        // Take a new id from the database and assign it to this analysis

        conversation = new LinkedList<>();
        datasetsPool = new LinkedList<>();
        this.repository = repository;
        this.context = context;
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

    public void logMessages(List<String> messages, boolean isTheUser){
        for(String message : messages)
            logMessage(message, isTheUser);
    }

    public void saveSessionWithoutTags(){
        if(conversation.size() != 0)
            repository.saveMessage(conversation);

        for(Dataset ds : datasetsPool)
            ds.save();
    }

    public void saveSessionInfo(){
        saveSessionWithoutTags();
        repository.registerSession(context.getContext());
    }

    public String getBranchName(){return this.branchName; }
    public String getSessionName(){return this.sessionName;}

    // Will load the conversation with analysisName and sessionName
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

    @Override
    public String toString(){
        return "Analysis name:\t" + this.getSessionName() +
                "\n" + "Branch name:\t" + this.getBranchName();
    }

    public String printDatasetList() {
        StringBuilder sb = new StringBuilder();

        for(Dataset ds : datasetsPool){
            sb.append("\t");
            sb.append(ds.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

}
