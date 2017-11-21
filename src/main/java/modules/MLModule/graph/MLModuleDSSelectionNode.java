package main.java.modules.MLModule.graph;

import main.java.dataset.Dataset;
import main.java.modules.MLModule.MLModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 21/11/2017.
 */
public class MLModuleDSSelectionNode extends ConvNode {

    private MLModule module;

    public MLModuleDSSelectionNode(String nodeId, MLModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        if(module.getModuleHandler().getSession().getDatasetPool().size() == 0)
            onLoadMessages =  Arrays.asList("You have to add a dataset in the pool first!",
                    "Type \"!import dataset\" to add a new dataset first, or exit the module typing \"!exit module\"");
        else{
            List<String> replies = new LinkedList<>();
            replies.add("Choose the dataset you want to try the analysis");
            replies.add(module.getModuleHandler().getSession().printDatasetList());
            onLoadMessages = replies;
        }
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        String currentDatasetName = extractAndValidateDatasetName(userInput);

        if(currentDatasetName == null){
            errorMessage = Arrays.asList("I could not found the dataset, can you rewrite it?");
            return this;
        }

        return getSuccessor("target_selection");
    }

    private String extractAndValidateDatasetName(String userInput) {
        Dataset currentDataset = module.getModuleHandler().getSession().getDatasetByName(userInput);
        module.setCurrentDataset(currentDataset);

        if(currentDataset != null)
            return currentDataset.getDatasetName();

        return null;
    }
}
