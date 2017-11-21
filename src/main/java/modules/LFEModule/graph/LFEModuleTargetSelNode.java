package main.java.modules.LFEModule.graph;

import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import main.java.modules.LFEModule.LFEMapper;
import main.java.modules.LFEModule.LFEModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 21/11/2017.
 */
public class LFEModuleTargetSelNode extends ConvNode {

    private LFEModule module;

    public LFEModuleTargetSelNode(String nodeId, LFEModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Now specify the target class");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        String target = extractAndValidateTarget(userInput);

        if(target == null){
            errorMessage = Arrays.asList("The target is not valid");
        }

        Map<String, Map<Integer, String>> dataset2transfor = module.getDataset2transfor();
        DataScienceModuleHandler handler = module.getModuleHandler();
        LFEMapper lfetool = new LFEMapper();
        String datasetName = module.getCurrentDataset().getDatasetName();
        Map<Integer, String> analysis = dataset2transfor.get(datasetName);
        String result;

        if(analysis == null){
            Dataset ds = handler.getSession().getDatasetByName(datasetName);
            Map<Integer, String> anResult = lfetool.getLFEAnalysis(ds, target);
            dataset2transfor.put(datasetName, anResult);
            result =  printResult(anResult);
        }
        else{
            result = printResult(analysis);
        }

        module.setResultMessage(result);

        return getSuccessor("print_result");

    }

    private String extractAndValidateTarget(String userInput) {
        Map<String, List<Double>> numericalAtt = module.getCurrentDataset().getNumericalAttributes();

        if(numericalAtt.containsKey(userInput))
            return userInput;

        return null;
    }

    private String printResult(Map<Integer, String> lfeAnalysis) {

        StringBuilder sb = new StringBuilder();

        for(Map.Entry entry : lfeAnalysis.entrySet()){
            sb.append(entry.getKey());
            sb.append("\t");
            sb.append(entry.getValue());
        }

        return sb.toString();
    }

}
