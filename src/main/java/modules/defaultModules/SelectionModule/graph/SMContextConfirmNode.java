package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.modules.conversational.ConvNode;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;
import java.util.Arrays;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMContextConfirmNode extends ConvNode{

    private SelectionModule module;

    public SMContextConfirmNode(String nodeId, SelectionModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        this.onLoadMessages = Arrays.asList("Tags validated", "Are you sure about them?");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        if(userInput.equals("yes")){
            return getSuccessor("phase_selection");
        }
        return prev;
    }
}
