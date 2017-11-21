package main.java.modules.MLModule.graph;

import main.java.modules.MLModule.MLModule;
import main.java.modules.MLModule.MachineAlgorithms;
import main.java.modules.MLModule.Model;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 21/11/2017.
 */
public class MLModuleAlgSelNode extends ConvNode {

    public MLModule module;

    public MLModuleAlgSelNode(String nodeId, MLModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Now select an algorithm to fit please", module.getModels().toString());

    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        String modelSelected = userInput;
        MachineAlgorithms models = module.getModels();
        if(!models.modelExist(modelSelected)){
            errorMessage = Arrays.asList("Model selected not found");
            return this;
        }

        Model currentModel = models.getModel(modelSelected);
        module.setCurrentModel(currentModel);
        return getSuccessor("eval_selection");

    }

}
