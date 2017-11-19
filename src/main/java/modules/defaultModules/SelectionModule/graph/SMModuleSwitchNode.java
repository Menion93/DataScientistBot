package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.modules.conversational.ConvNode;

import java.util.Arrays;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMModuleSwitchNode extends ConvNode {

    public SMModuleSwitchNode(String nodeId) {
        super(nodeId);
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Module Switched");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        return getSuccessor("phase_selection");
    }
}
