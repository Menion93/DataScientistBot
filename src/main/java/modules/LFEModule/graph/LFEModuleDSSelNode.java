package main.java.modules.LFEModule.graph;

import main.java.ModuleSubscription;
import main.java.dataset.Dataset;
import main.java.modules.LFEModule.LFEModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 21/11/2017.
 */
public class LFEModuleDSSelNode extends ConvNode {

    private LFEModule module;

    private List<String> errorMessage1 = Arrays.asList("You must add a dataset to the pool first!",
            "Type \"!import dataset\" to add a new dataset first, or exit the module typing \"!exit module\"");

    public LFEModuleDSSelNode(String nodeId, LFEModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {

        String dsList = module.getModuleHandler().getSession().printDatasetList();

        if(dsList.equals(""))
            onLoadMessages = errorMessage1;
        else{
            List<String> replies = new LinkedList<>();
            replies.add("Choose the dataset you want to try the analysis");
            replies.add(module.getModuleHandler().getSession().printDatasetList());
            onLoadMessages = replies;
        }

        errorMessage = errorMessage1;
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        Dataset currentDataset = module.getModuleHandler().getSession().getDatasetByName(userInput);

        if(currentDataset == null){
            errorMessage = Arrays.asList("The dataset could not be recognized");
            return this;
        }

        module.setCurrentDataset(currentDataset);

        return getSuccessor("target_selection");

    }
}
