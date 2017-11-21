package main.java.modules.ColWranglerModule.graph;

import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import main.java.modules.ColWranglerModule.ColumnWranglerModule;
import main.java.modules.conversational.ConvNode;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 18/11/2017.
 */
public class CWModuleDatasetSelectionNode extends ConvNode {

    private ColumnWranglerModule module;

    private List<String> errorMessage1 = Arrays.asList("You must add a dataset to the pool first!",
            "Type \"!import dataset\" to add a new dataset first, or exit the module typing \"!exit module\"");

    public CWModuleDatasetSelectionNode(String nodeId, ColumnWranglerModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        String dsList = module.getModuleHandler().getSession().printDatasetList();

        if(dsList.equals(""))
            onLoadMessages = errorMessage1;
        else
            onLoadMessages = Arrays.asList("Welcome to the ColumnWrangler module, here you can make transformations to " +
                    "the column of your dataset", "Select the dataset you want to work with", dsList);

    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        DataScienceModuleHandler handler = module.getModuleHandler();

        if(handler.getSession().getDatasetPool().size() == 0){
            errorMessage = Arrays.asList("You must add a dataset to the pool first!",
                    "Type \"!import dataset\" to add a new dataset first, or exit the module typing \"!exit module\"");
            return this;
        }

        Dataset currentDs = handler.getSession().getDatasetByName(userInput);

        if(currentDs == null){
            errorMessage = Arrays.asList("The selected dataset could not be found, please try again");
            return this;
        }

        module.setCurrentDataset(currentDs);

        return getSuccessor("attribute_selection");

    }

}
