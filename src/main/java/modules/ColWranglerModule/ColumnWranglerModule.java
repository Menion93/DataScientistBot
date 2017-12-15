package main.java.modules.ColWranglerModule;

import main.java.modules.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.dataset.Dataset;
import main.java.modules.Module;
import main.java.modules.conversational.ConvMachine;

import java.util.*;

/**
 * Created by Andrea on 17/10/2017.
 */
public class ColumnWranglerModule extends Module {

    private Map<String, Map<String, List<String>>> ds2transf;
    private Preprocesser preprocesser;
    private Dataset currentDataset;
    private Dataset newDataset;
    private String currentAttribute;
    private String currentTransf;
    private ConvMachine stateMachine;


    public ColumnWranglerModule(DataScienceModuleHandler handler,  ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "ColumnWrangler", step);
        ds2transf = new HashMap<>();
        preprocesser = new Preprocesser();
    }

    @Override
    public List<String> reply(String userInput) {
        return stateMachine.reply(userInput);
    }

    public void setCurrentDataset(Dataset currentDataset) {
        this.currentDataset = currentDataset;
    }

    public Dataset getCurrentDataset() {
        return currentDataset;
    }

    public void setCurrentAttribute(String currentAttribute) {
        this.currentAttribute = currentAttribute;
    }

    public Preprocesser getPreprocesser() {
        return preprocesser;
    }

    public String getCurrentAttribute() {
        return currentAttribute;
    }

    public Dataset getNewDataset() {
        return newDataset;
    }

    public String getCurrentTransformation() {
        return currentTransf;
    }

    public Map<String,Map<String,List<String>>> getDs2Transf() {
        return ds2transf;
    }

    @Override
    public String getModuleDescription() {
        return "It can apply basic transformation to the column of a dataset";
    }

    @Override
    public String getModuleUsage() {
        return "Requirements: a dataset is present in the pool.\nYou need to tell me which dataset, which attribute, " +
                "and which transformation you want to apply. A new dataset then will be created.";
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repo = handler.getRepository();
        ds2transf = repo.getColumnWranglerAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!ds2transf.isEmpty()){
            DBRepository repo = handler.getRepository();
            repo.saveColumnWranglerAnalysis(ds2transf);
        }
    }

    @Override
    public void resetModuleInstance() {
        if(stateMachine == null){
            CWConvMachineFactory factory = new CWConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        ds2transf = new HashMap<>();
        resetConversation();
    }

    @Override
    public void resetConversation() {
        this.stateMachine.resetConversation();
    }

    @Override
    public List<String> repeat() {
        return stateMachine.repeat();
    }

    @Override
    public List<String> back() {
        return stateMachine.back();
    }

    @Override
    public List<String> onModuleLoad() {
        if(stateMachine == null){
            CWConvMachineFactory factory = new CWConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        return stateMachine.showCurrentStateText();
    }


    public void setNewDataset(Dataset newDataset) {
        this.newDataset = newDataset;
    }

    public void setCurrentTransformation(String currentTransformation) {
        this.currentTransf = currentTransformation;
    }
}
