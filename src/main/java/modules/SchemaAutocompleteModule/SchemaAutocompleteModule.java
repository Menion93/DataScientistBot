package main.java.modules.SchemaAutocompleteModule;

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
public class SchemaAutocompleteModule extends Module {

    private Map<String, Map<String,Double>> context2Recommendation;
    private String resultString;
    private ConvMachine stateMachine;

    public SchemaAutocompleteModule(DataScienceModuleHandler handler,  ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "SchemaAutocomplete", step);
        context2Recommendation = new HashMap<>();
    }

    @Override
    public List<String> reply(String userInput) {
        return stateMachine.reply(userInput);
    }

    public Map<String, Map<String, Double>> getContext2Recommendation() {
        return context2Recommendation;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    @Override
    public String getModuleDescription() {
        return "I know how to suggest you a schema you might be interested in. Just give me a name of a few attributes" +
                "you have in mind, and I will give you the best matches";
    }

    @Override
    public String getModuleUsage() {
        return "Write a set of attribute separated by a schema";
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repo = handler.getRepository();
        context2Recommendation = repo.getSchemaAutocompleteAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!context2Recommendation.isEmpty()){
            DBRepository repo = handler.getRepository();
            repo.saveSchemaAutocompleteAnalysis(context2Recommendation);
        }
    }

    @Override
    public void resetModuleInstance() {
        if(stateMachine == null){
            SACConvMachineFactory factory = new SACConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        context2Recommendation = new HashMap<>();
        this.resetConversation();
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
        return stateMachine.repeat();
    }

    @Override
    public List<String> onModuleLoad() {
        if(stateMachine == null){
            SACConvMachineFactory factory = new SACConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        return stateMachine.showCurrentStateText();
    }
}
