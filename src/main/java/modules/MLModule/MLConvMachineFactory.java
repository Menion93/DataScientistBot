package main.java.modules.MLModule;

import main.java.modules.MLModule.graph.*;
import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConversationalFactory;

/**
 * Created by Andrea on 21/11/2017.
 */
public class MLConvMachineFactory implements ConversationalFactory {

    private MLModule module;

    public MLConvMachineFactory(MLModule module){
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {
        ConvMachine machine = new ConvMachine(module.getModuleHandler());
        MLModuleDSSelectionNode root = new MLModuleDSSelectionNode("root",module);
        MLModuleTargetSelNode tselection = new MLModuleTargetSelNode("target_selection", module);
        MLModuleAlgSelNode algSelection = new MLModuleAlgSelNode("alg_selection", module);
        MLModuleEvalSelNode evalSelection = new MLModuleEvalSelNode("eval_selection", module);
        MLModulePrintResNode endPoint = new MLModulePrintResNode("print_result", module);

        root.addSuccessor("target_selection", tselection);
        tselection.addSuccessor("alg_selection", algSelection);
        algSelection.addSuccessor("eval_selection", evalSelection);
        evalSelection.addSuccessor("print_result", endPoint);
        endPoint.addSuccessor("root", root);

        tselection.setPrev(root);
        algSelection.setPrev(tselection);
        evalSelection.setPrev(algSelection);
        endPoint.setPrev(evalSelection);

        machine.setRoot(root);
        return machine;
    }
}
