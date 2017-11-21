package main.java.modules.LFEModule.graph;

import main.java.modules.LFEModule.LFEModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;

/**
 * Created by Andrea on 21/11/2017.
 */
public class LFEModuleMoreNode extends ConvNode{

    private LFEModule module;

    public LFEModuleMoreNode(String nodeId, LFEModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList(module.getResultMessage(), "Do you want to use a new Dataset?");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        if(userInput.equals("yes"))
            return chooseSuccessor("root");

        return null;
    }
}
