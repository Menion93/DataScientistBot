package main.java.modules.JGTModule.graph;

import main.java.modules.JGTModule.JGTModule;
import main.java.modules.conversational.ConvNode;

/**
 * Created by Andrea on 21/11/2017.
 */
public class JGTModuleAskMoreNode extends ConvNode {

    private JGTModule module;

    public JGTModuleAskMoreNode(String nodeId, JGTModule module) {
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
