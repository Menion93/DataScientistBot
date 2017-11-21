package main.java.modules.SchemaAutocompleteModule.graph;

import main.java.modules.SchemaAutocompleteModule.SACTMapper;
import main.java.modules.SchemaAutocompleteModule.SchemaAutocompleteModule;
import main.java.modules.conversational.ConvNode;
import main.java.utils.Helper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 21/11/2017.
 */
public class SACModuleSchemaInputNode extends ConvNode{

    private SchemaAutocompleteModule module;

    private String[] intro = {"Write the name of some attributes you are interested in, I will look the attributes",
            "Write a partial schema like <party president>, I will search for you the next attribute"};

    public SACModuleSchemaInputNode(String nodeId, SchemaAutocompleteModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList(Helper.selectRandomString(intro));
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        List<String> schema = extractSchema(userInput);
        SACTMapper sactool = new SACTMapper();

        if(schema == null){
            errorMessage = Arrays.asList("I could not read your schema, can you write it again?");
            return this;
        }

        Map<String, Map<String, Double>> context2Recommendation = module.getContext2Recommendation();
        String orderedSchema = Helper.getLexicographicalOrder(schema);
        Map<String,Double> singleAnalysis = context2Recommendation.get(orderedSchema);
        String result;
        if(singleAnalysis == null){
            Map<String,Double> anResult = sactool.getTopKProbabileAttributes(schema, 10);
            context2Recommendation.put(orderedSchema, anResult);
            result = printResult(anResult);
        }
        else{
            result = printResult(singleAnalysis);
        }

        module.setResultString(result);

        return getSuccessor("ask_more");
    }

    private List<String> extractSchema(String userInput) {
        return Arrays.asList(userInput.split(" "));
    }

    private String printResult(Map<String, Double> probabileAttribute) {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,Double> entry : probabileAttribute.entrySet()){
            sb.append(entry.getKey());
            sb.append("\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }

        return sb.toString();
    }

}
