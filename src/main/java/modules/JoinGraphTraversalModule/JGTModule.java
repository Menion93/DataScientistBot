package main.java.modules.JoinGraphTraversalModule;

import main.java.Dataset.Dataset;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.modules.Module;
import main.java.modules.ThirdParty.ThirdPartyDummy;
import main.java.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.*;

/**
 * Created by Andrea on 07/10/2017.
 */
public class JGTModule extends Module {

    private enum STEPS {SCHEMA_INPUT, VALIDATE_SCHEMA, SEARCH_FOR_MORE};
    private int stepIndex;
    private Map<String,Map<String, List<List<String>>>> allAnalysis;

    public JGTModule(DataScienceModuleHandler handler) {
        super(handler, "JGT");
    }

    @Override
    public List<String> reply(String userInput) {

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case SCHEMA_INPUT: {
                stepIndex++;
                return Arrays.asList("Write the name of some attributes you are interested in, I will search for similar datasets");
            }
            case VALIDATE_SCHEMA: {
                List<String> schemas = extractSchema(userInput);

                if(schemas != null){
                    String orderedSchema = Helper.getLexicographicalOrder(schemas);
                    Map<String, List<List<String>>> analysis = allAnalysis.get(orderedSchema);
                    String result;

                    if(analysis == null){
                        ThirdPartyDummy tpd = new ThirdPartyDummy();
                        Map<String, List<List<String>>> anResult = tpd.getKRankings(schemas);
                        allAnalysis.put(orderedSchema, anResult);
                        result =  printResult(tpd.getKRankings(schemas));
                    }
                    else{
                        result = printResult(analysis);
                    }

                    List<String> replies = new LinkedList<>();
                    replies.add(result);
                    replies.add("Would you like to search for another schema?");
                    stepIndex++;
                    return replies;
                }
                return Arrays.asList("I could not understand the schema, can you rewrite it please?");
            }
            case SEARCH_FOR_MORE: {
                if(userInput.contains("yes")){
                    stepIndex -= 2;
                    return this.reply(null);
                }
                stepIndex = 0;
                return this.reply(null);
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }
    }

    private String printResult(Map<String, List<List<String>>> kRankings) {
        return null;
    }

    private List<String> extractSchema(String userInput) {
        return null;
    }

    @Override
    public String getModuleDescription() {
        return "I can show you similar datasets given a schema.";
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        if(!firstTime){
            DBRepository repo = handler.getRepository();
            allAnalysis = repo.getJGTAnalysis();
        }
    }

    @Override
    public void saveModuleInstance() {
        DBRepository repo = handler.getRepository();
        repo.saveJGTAnalysis(allAnalysis);
    }
}
