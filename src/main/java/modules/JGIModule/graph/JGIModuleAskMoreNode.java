package main.java.modules.JGIModule.graph;

import main.java.modules.JGIModule.JGIModule;
import main.java.modules.conversational.ConvNode;

/**
 * Created by Andrea on 21/11/2017.
 */
public class JGIModuleAskMoreNode extends ConvNode {

    private JGIModule module;

    public JGIModuleAskMoreNode(String nodeId, JGIModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = module.getResultString();
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        if(userInput.equals("yes")){
            return prev;
        }

        return null;
    }
}
