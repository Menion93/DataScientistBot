package main.java.modules.LFEModule;

import main.java.modules.LFEModule.graph.LFEModuleDSSelNode;
import main.java.modules.LFEModule.graph.LFEModuleMoreNode;
import main.java.modules.LFEModule.graph.LFEModuleTargetSelNode;
import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConversationalFactory;

/**
 * Created by Andrea on 21/11/2017.
 */
public class LFEConvMachineFactory implements ConversationalFactory {

    private LFEModule module;

    public LFEConvMachineFactory(LFEModule module){
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {

        ConvMachine machine = new ConvMachine(module.getModuleHandler());
        LFEModuleDSSelNode root = new LFEModuleDSSelNode("root", module);
        LFEModuleTargetSelNode tselection = new LFEModuleTargetSelNode("target_selection", module);
        LFEModuleMoreNode endPoint = new LFEModuleMoreNode("print_result", module);

        root.addSuccessor("target_selection", tselection);
        tselection.addSuccessor("print_result", endPoint);
        endPoint.addSuccessor("root", root);

        tselection.setPrev(root);
        endPoint.setPrev(tselection);

        machine.setRoot(root);

        return machine;
    }

}
