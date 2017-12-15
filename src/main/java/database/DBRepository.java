package main.java.database;

import main.java.dataset.Dataset;
import main.java.modules.MLModule.Evaluation;
import main.java.session.MessageEntry;
import main.java.session.Session;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public abstract void saveJGIAnalysis(Map<String,Set<List<String>>> goodAnalysis);
    public abstract void saveLFEAnalysis(Map<String, Map<Integer, String>> dataset2transf);
    public abstract Map<String, Set<List<String>>> getJGIAnalysis();
    public abstract Map<String,Map<Integer,String>> getLFEAnalysis();
    public abstract boolean isAValidBranch(String branchName);
    public abstract List<MessageEntry> getConversation();
    public abstract List<Dataset> getDatasetsPool();
    public abstract void registerSession(List<String> context);
    public abstract void saveSchemaAutocompleteAnalysis(Map<String, Map<String, Double>> allAnalysis);
    public abstract Map<String,Map<String,Double>> getSchemaAutocompleteAnalysis();
    public abstract void deleteBranch(String branchName);
    public abstract boolean isAValidAnalysis(String analysisName);
    public abstract void deleteAnalysis(String analysisName);
    public abstract List<String> getBranches();
    public abstract List<String> getAnalysis();
    public abstract void saveDataset(Dataset dataset);
    public abstract void loadData(Dataset dataset);
    public abstract boolean existDataset(String newName);
    public abstract void saveMLModuleAnalysis(List<Evaluation> evaluations);
    public abstract List<Evaluation> getMLModuleAnalysis();
    public abstract Map<String,Map<String,List<String>>> getColumnWranglerAnalysis();
    public abstract void saveColumnWranglerAnalysis(Map<String, Map<String, List<String>>> ds2transf);
    public abstract List<String> getModulesBySession();
    public abstract void saveModulesBySession(List<String> modules);
}
