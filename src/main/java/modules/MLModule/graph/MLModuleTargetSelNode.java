package main.java.modules.MLModule.graph;

import main.java.dataset.Dataset;
import main.java.modules.MLModule.MLModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 21/11/2017.
 */
public class MLModuleTargetSelNode extends ConvNode {

    private MLModule module;

    public MLModuleTargetSelNode(String nodeId, MLModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Now specify the target class");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        String currentTarget = extractAndValidateTarget(userInput);

        if(currentTarget == null){
            errorMessage = Arrays.asList("The target is not valid");
            return this;
        }
        module.setCurrentTarget(currentTarget);
        return getSuccessor("alg_selection");

    }

    private String extractAndValidateTarget(String userInput) {
        Dataset currentDataset = module.getCurrentDataset();
        Map<String, List<Double>> numericalAtt = currentDataset.getNumericalAttributes();
        Map<String, List<String>> categoricalAtt = currentDataset.getCategoricalAttributes();

        if(numericalAtt.containsKey(userInput) || categoricalAtt.containsKey(userInput))
            return userInput;
        else
            return null;
    }
}
