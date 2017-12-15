package main.java.modules.LFEModule;

import main.java.modules.ModuleSubscription;
import main.java.dataset.Dataset;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.modules.Module;
import main.java.modules.conversational.ConvMachine;

import java.util.*;
import java.util.List;

/**
 * Created by Andrea on 07/10/2017.
 */
public class LFEModule extends Module {

    private Map<String, Map<Integer, String>> dataset2transfor;
    private Dataset currentDataset;
    private String resultMessage;
    private ConvMachine stateMachine;


    public LFEModule(DataScienceModuleHandler handler,  ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "LFE", step);
        dataset2transfor = new HashMap<>();
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

    public Map<String, Map<Integer, String>> getDataset2transfor() {
        return dataset2transfor;
    }


    @Override
    public String getModuleDescription() {
        return "I can help you building a new feature for your dataset, given the latter.";
    }

    @Override
    public String getModuleUsage() {
        return null;
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repository = handler.getRepository();
        dataset2transfor = repository.getLFEAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!dataset2transfor.isEmpty()){
            DBRepository repository = handler.getRepository();
            repository.saveLFEAnalysis(dataset2transfor);
        }
    }

    public void setResultMessage(String result) {
        this.resultMessage = result;
    }

    public String getResultMessage() {
        return resultMessage;
    }


    @Override
    public void resetModuleInstance() {
        if(stateMachine == null){
            LFEConvMachineFactory factory = new LFEConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        Map<String, Map<Integer, String>> dataset2transfor = new HashMap<>();
        resetConversation();
    }

    @Override
    public void resetConversation() {
        stateMachine.resetConversation();
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
            LFEConvMachineFactory factory = new LFEConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        return stateMachine.showCurrentStateText();
    }

}