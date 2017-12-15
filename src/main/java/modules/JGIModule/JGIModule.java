package main.java.modules.JGIModule;

import main.java.modules.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.modules.Module;
import main.java.modules.conversational.ConvMachine;

import java.util.List;
import java.util.*;

/**
 * Created by Andrea on 07/10/2017.
 */
public class JGIModule extends Module {

    private Map<String, Set<List<String>>> allAnalysis;
    private List<String> resultString;
    private ConvMachine stateMachine;

    public JGIModule(DataScienceModuleHandler handler, ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "JoinGraphIntegrator", step);
        allAnalysis = new HashMap<>();
    }

    @Override
    public List<String> reply(String userInput) {
        return stateMachine.reply(userInput);
    }

    @Override
    public String getModuleDescription() {
        return "I can recommend you similar dataset to join given a schema.";
    }

    @Override
    public String getModuleUsage() {
        return "Write the schema writing its attributes separated by a space. I will " +
                "give you the result in a few seconds";
    }

    public void saveJGTMessage(List<String> replies) {
        this.resultString = replies;
    }

    public List<String> getResultString() {
        return resultString;
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repo = handler.getRepository();
        allAnalysis = repo.getJGIAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!allAnalysis.isEmpty()){
            DBRepository repo = handler.getRepository();
            repo.saveJGIAnalysis(allAnalysis);
        }
    }

    public Map<String, Set<List<String>>> getAllAnalysis() {
        return allAnalysis;
    }

    @Override
    public void resetModuleInstance() {
        if(stateMachine == null){
            JGIConvMachineFactory factory = new JGIConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        allAnalysis = new HashMap<>();
        stateMachine.resetConversation();
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
            JGIConvMachineFactory factory = new JGIConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        return stateMachine.showCurrentStateText();
    }
}