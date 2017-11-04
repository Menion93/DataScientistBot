package main.java.modules.SchemaAutocompleteModule;

import main.java.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.modules.Module;
import main.java.utils.Helper;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

/**
 * Created by Andrea on 07/10/2017.
 */
public class SchemaAutocompleteModule extends Module {

    private enum STEPS {INSTRUCTIONS, SCHEMA_INPUT, ANOTHER_SCHEMA};
    private int stepIndex;
    private Map<String, Map<String,Double>> allAnalysis;
    private SACTMapper sactool;

    public SchemaAutocompleteModule(DataScienceModuleHandler handler,  ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "SchemaAutocomplete", step);
        allAnalysis = new HashMap<>();
        sactool = new SACTMapper();
    }

    @Override
    public List<String> reply(String userInput) {

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case INSTRUCTIONS: {
                stepIndex++;
                return Arrays.asList("Write the name of some attributes you are interested in, I will look the attributes");
            }
            case SCHEMA_INPUT: {
                List<String> schema = extractSchema(userInput);

                if(schema != null){
                    String orderedSchema = Helper.getLexicographicalOrder(schema);
                    Map<String,Double> singleAnalysis = allAnalysis.get(orderedSchema);
                    String result;
                    if(singleAnalysis == null){
                        Map<String,Double> anResult = sactool.getProbabileAttributes(schema);
                        allAnalysis.put(orderedSchema, anResult);
                        result = printResult(anResult);
                    }
                    else{
                        result = printResult(singleAnalysis);
                    }
                    List<String> replies = new LinkedList<>();
                    replies.add(result);
                    replies.add("Would you like to search for another schema?");
                    stepIndex++;
                    return replies;
                }
                return Arrays.asList("I could not read your schema, can you write it again?");

            }
            case ANOTHER_SCHEMA: {
                if(userInput.contains("yes")){
                    stepIndex -= 2;
                    return this.reply(null);
                }
                else{
                    stepIndex = 0;
                    handler.switchToDefaultModule();
                    return Arrays.asList("Module exited");
                }
            }
            default:
                return Arrays.asList("Can you repeat please?");
        }

    }

    private String printResult(Map<String, Double> probabileAttribute) {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry entry : probabileAttribute.entrySet()){
            sb.append(entry.getKey());
            sb.append("\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }

        return sb.toString();
    }

    private List<String> extractSchema(String userInput) {
        return Arrays.asList(userInput.split(" "));
    }

    @Override
    public String getModuleDescription() {
        return "I know how to suggest you a schema you might be interested in. Just give me a name of a few attributes" +
                "you have in mind, and I will give you the best matches";
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repo = handler.getRepository();
        allAnalysis = repo.getSchemaAutocompleteAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!allAnalysis.isEmpty()){
            DBRepository repo = handler.getRepository();
            repo.saveSchemaAutocompleteAnalysis(allAnalysis);
        }
    }

    @Override
    public void resetModuleInstance() {
        allAnalysis = new HashMap<>();
        stepIndex = 0;
    }

    @Override
    public void resetConversation() {
        stepIndex = 0;
    }
}
