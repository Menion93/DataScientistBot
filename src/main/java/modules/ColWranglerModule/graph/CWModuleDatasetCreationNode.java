package main.java.modules.ColWranglerModule.graph;

import main.java.ModuleSubscription;
import main.java.dataset.Dataset;
import main.java.modules.ColWranglerModule.ColumnWranglerModule;
import main.java.modules.conversational.ConvNode;
import main.java.session.Session;

import java.util.*;

/**
 * Created by Andrea on 18/11/2017.
 */
public class CWModuleDatasetCreationNode extends ConvNode {

    private ColumnWranglerModule module;

    public CWModuleDatasetCreationNode(String nodeId, ColumnWranglerModule module) {
        super(nodeId);
        this.module = module;

    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Transformation done, please select a name for the new dataset");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        // Needed to check the datasets that are not saved permanently yet
        Session session =  module.getModuleHandler().getSession();

        if (alreadyExist(userInput) || session.getDatasetByName(userInput) != null) {
            errorMessage = Arrays.asList("Name is not valid, please try again");
            return this;
        }

        Dataset newDataset = module.getNewDataset();
        newDataset.setDatasetName(userInput);
        module.getModuleHandler().getSession().getDatasetPool().add(newDataset);
        saveTransformation(module.getCurrentDataset().getDatasetName(),
                           module.getCurrentAttribute(),
                           module.getCurrentTransformation());
        return getSuccessor("end_point");
    }

    private boolean alreadyExist(String newName) {
        return module.getModuleHandler().getRepository().existDataset(newName);
    }

    private void saveTransformation(String dsName, String column, String currentTransf) {
        Map<String, Map<String, List<String>>> ds2transf = module.getDs2Transf();
        Map<String,List<String>> column2transf = ds2transf.get(dsName);
        if(column2transf == null){
            ds2transf.put(dsName, new HashMap<>());
        }
        column2transf = ds2transf.get(dsName);
        List<String> transformations = column2transf.get(column);

        if(transformations == null){
            column2transf.put(column, new LinkedList<>());
        }

        column2transf.get(column).add(currentTransf);
    }
}
