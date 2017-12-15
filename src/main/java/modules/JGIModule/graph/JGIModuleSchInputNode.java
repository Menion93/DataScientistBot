package main.java.modules.JGIModule.graph;

import main.java.modules.JGIModule.JGIMapper;
import main.java.modules.JGIModule.JGIModule;
import main.java.modules.conversational.ConvNode;
import main.java.utils.Helper;

import java.util.*;

/**
 * Created by Andrea on 21/11/2017.
 */
public class JGIModuleSchInputNode extends ConvNode {

    private JGIModule module;

    private String[] intro = {"Write the name of some attributes you are interested in separated by \"-\", I will search for similar datasets",
            "Write relevant attributes separated by \"-\", I will look for interesting schemas for you"};

    public JGIModuleSchInputNode(String nodeId, JGIModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList(Helper.selectRandomString(intro));
    }


    @Override
    public ConvNode chooseSuccessor(String userInput) {
        List<String> schemas = extractSchema(userInput);

        if(schemas == null){
            errorMessage =  Arrays.asList("I could not understand the schema, can you rewrite it please?");
            return this;
        }

        String orderedSchema = Helper.getLexicographicalOrder(schemas);
        Map<String, Set<List<String>>> allAnalysis = module.getAllAnalysis();
        Set<List<String>> analysis = allAnalysis.get(orderedSchema);
        String result;

        if(analysis == null){
            JGIMapper jgtool = new JGIMapper();
            Set<List<String>> anResult = jgtool.getKRankings(schemas);

            if(anResult == null){
                errorMessage = Arrays.asList("The system failed to get a result");
                return this;
            }
            allAnalysis.put(orderedSchema, anResult);
            result =  printResult(anResult);
        }
        else{
            result = printResult(analysis);
        }

        List<String> replies = new LinkedList<>();
        replies.add(result);
        replies.add("Would you like to search for another schema?");

        module.saveJGTMessage(replies);

        return getSuccessor("ask_more");

    }

    private List<String> extractSchema(String userInput) {
        return Arrays.asList(userInput.split("-"));
    }

    private String printResult(Set<List<String>> kRankings) {
        StringBuilder sb = new StringBuilder();

        for(List<String> schema : kRankings){

            for(String attr : schema){
                sb.append(attr);
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
