package main.java.modules.MLModule.graph;

import main.java.modules.MLModule.MLModule;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;

/**
 * Created by Andrea on 21/11/2017.
 */
public class MLModulePrintResNode extends ConvNode{

    private MLModule module;

    public MLModulePrintResNode(String nodeId, MLModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList(module.getCurrentEval().printEvaluation(), "Do you want to run another model?");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        if(userInput.equals("yes"))
            return getSuccessor("root");

        return null;

    }
}
