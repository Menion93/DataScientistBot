package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.modules.conversational.ConvNode;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;
import java.util.Arrays;


/**
 * Created by Andrea on 19/11/2017.
 */
public class SMOnloadNode extends ConvNode {

    private SelectionModule module;

    public SMOnloadNode(String nodeId, SelectionModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        this.onLoadMessages = Arrays.asList("Hello, I am DataScienceBot",
                "I am here to help you thorough the building of your ml pipeline",
                "Before you start building it I need to ask you a few questions");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        return getSuccessor("context");
    }


}
