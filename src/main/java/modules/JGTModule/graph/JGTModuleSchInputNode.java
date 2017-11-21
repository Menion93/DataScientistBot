package main.java.modules.JGTModule.graph;

import main.java.modules.JGTModule.JGTMapper;
import main.java.modules.JGTModule.JGTModule;
import main.java.modules.conversational.ConvNode;
import main.java.utils.Helper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 21/11/2017.
 */
public class JGTModuleSchInputNode extends ConvNode {

    private JGTModule module;

    private String[] intro = {"Write the name of some attributes you are interested in, I will search for similar datasets",
            "Write relevant attributes separated by a space, I will look for interesting schemas for you"};

    public JGTModuleSchInputNode(String nodeId, JGTModule module) {
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
        Map<String, Map<String, List<List<String>>>> allAnalysis = module.getAllAnalysis();
        Map<String, List<List<String>>> analysis = allAnalysis.get(orderedSchema);
        String result;

        if(analysis == null){
            JGTMapper jgtool = new JGTMapper();
            Map<String, List<List<String>>> anResult = jgtool.getKRankings(schemas);

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
        return Arrays.asList(userInput.split(" "));
    }

    private String printResult(Map<String, List<List<String>>> kRankings) {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry entry : kRankings.entrySet()){
            sb.append(entry.getKey());
            sb.append("\n");
            for(List<String> table : (List<List<String>>) entry.getValue()){
                for(String attribute : table){
                    sb.append("\t");
                    sb.append(attribute);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
