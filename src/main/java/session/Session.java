package main.java.session;

import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import main.java.database.DBRepository;

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
    private DataScienceModuleHandler handler;

    public Session(DBRepository repository, DataScienceModuleHandler handler){
        conversation = new LinkedList<>();
        datasetsPool = new LinkedList<>();
        this.repository = repository;
        this.handler = handler;
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
        repository.registerSession(handler.getSelectionModule().getContext());
    }

    public void saveCurrentInstance(){
        saveSessionInfo();
        handler.getModuleSubscription().saveAllModulesInstances();
        handler.getSelectionModule().saveModuleInstance();
    }

    public void loadBranch(String branchName) {
        setBranchName(branchName);
        loadSession();
        handler.getModuleSubscription().loadAllModuleInstances();
        handler.getSelectionModule().loadModuleInstance();
    }

    public boolean createNewBranch(String branchName) {
        if(!repository.isAValidBranch(branchName)){
            String prevBranchName = getBranchName();
            setBranchName(branchName);

            // This saves the current instance with the new branch name, making a copy of it
            saveCurrentInstance();
            saveSessionInfo();

            // Now switch to the previous name branch
            setBranchName(prevBranchName);

            return true;
        }

        return false;
    }

    public boolean createNewAnalysis(String analysisName) {
        if(!repository.isAValidAnalysis(analysisName)){
            String prevAnalysis = getBranchName();
            String prevBranchName = getBranchName();
            setSessionInfo(analysisName, "main");

            saveSessionWithoutTags();

            // Now switch to the previous session info
            setSessionInfo(prevAnalysis, prevBranchName);

            return true;
        }

        return false;
    }

    public void deleteAnalysis(String analysisName){
        repository.deleteAnalysis(analysisName);
    }

    public void loadAnalysis(String analysisName) {
        setSessionInfo(analysisName, "main");
        loadSession();
        handler.getModuleSubscription().resetAllModuleInstances();
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
