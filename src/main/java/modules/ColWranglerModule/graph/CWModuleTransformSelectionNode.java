package main.java.modules.ColWranglerModule.graph;

import main.java.dataset.Dataset;
import main.java.modules.ColWranglerModule.ColumnWranglerModule;
import main.java.modules.ColWranglerModule.Preprocesser;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;

/**
 * Created by Andrea on 18/11/2017.
 */
public class CWModuleTransformSelectionNode extends ConvNode {

    private ColumnWranglerModule module;

    public CWModuleTransformSelectionNode(String nodeId, ColumnWranglerModule module) {
        super(nodeId);
        this.module = module;

    }

    @Override
    public void onLoad() {
        this.onLoadMessages = Arrays.asList("Now please select a transformation to apply", module.getPreprocesser().toString());
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        Preprocesser preprocesser = module.getPreprocesser();
        if(!preprocesser.hasTransformation(userInput)){
            errorMessage = Arrays.asList("I could not recognize the transformation, try again");
            return this;
        }
        Dataset newDataset = new Dataset(module.getModuleHandler().getRepository());
        Dataset currentDataset = module.getCurrentDataset();
        newDataset.setRoot(currentDataset.getRoot());
        newDataset.setFrom(currentDataset.getDatasetName());
        newDataset.copyDatasetData(currentDataset);
        preprocesser.applyTransformationAndSave(newDataset, module.getCurrentAttribute(), userInput);

        module.setNewDataset(newDataset);
        module.setCurrentTransformation(userInput);

        return getSuccessor("dataset_creation");

    }

}
