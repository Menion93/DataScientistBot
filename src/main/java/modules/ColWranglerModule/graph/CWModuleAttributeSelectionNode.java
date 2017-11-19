package main.java.modules.ColWranglerModule.graph;

import main.java.modules.ColWranglerModule.ColumnWranglerModule;
import main.java.modules.conversational.ConvNode;
import java.util.Arrays;

/**
 * Created by Andrea on 18/11/2017.
 */
public class CWModuleAttributeSelectionNode extends ConvNode {

    private ColumnWranglerModule module;

    public CWModuleAttributeSelectionNode(String nodeId, ColumnWranglerModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        this.onLoadMessages = Arrays.asList("Now please select an attribute", module.getCurrentDataset().toString());

    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        if(!module.getCurrentDataset().hasColumn(userInput)){
            errorMessage = Arrays.asList("I could not found the attribute, please try again");
            return this;
        }

        module.setCurrentAttribute(userInput);

        return getSuccessor("transformation_selection");
    }


}
