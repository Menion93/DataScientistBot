package main.java.modules.ColWranglerModule.graph;

import main.java.modules.ColWranglerModule.ColumnWranglerModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;

/**
 * Created by Andrea on 18/11/2017.
 */
public class CWModuleEndPoint extends ConvNode {

    ColumnWranglerModule module;

    public CWModuleEndPoint(String nodeId, ColumnWranglerModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Dataset added in the pool", "Do you want to \"exit\" or \"return\" at the start?");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        if(userInput.equals("return"))
            return getSuccessor("root");

        return null;
    }

}
