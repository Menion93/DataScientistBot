package main.java.modules.ThirdParty;

import main.java.Dataset.Dataset;

import java.util.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 12/10/2017.
 */
public class ThirdPartyDummy {
    public Map<String,Double> getProbabileAttributes(List<String> context){

        Map<String,Double> attr2prob = new HashMap<>();

        attr2prob.put("Attr1", 0.54);
        attr2prob.put("Attri2", 0.4);

        return attr2prob;
    }

    public Map<String, List<List<String>>> getKRankings(List<String> schema){
        Map<String, List<List<String>>> join2AttrSchema = new HashMap<>();

        List<List<String>> cluster = new LinkedList<>();
        List<String> output = new LinkedList<>();
        output.add("bar");
        output.add("foo");
        cluster.add(output);

        join2AttrSchema.put("example", cluster);
        return join2AttrSchema;
    }

    public Map<Integer, String> getLFEAnalysis(Dataset ds){
        Map<Integer, String> result = new HashMap<>();
         result.put(0,"sqrt");
         result.put(3,"log");

         return result;
    }

    public String applyTransformationAndSave() {
        return "dummy";
    }
}
