package main.java.modules.defaultModules.SelectionModule;

import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConversationalFactory;
import main.java.modules.defaultModules.SelectionModule.graph.*;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMConvMachineFactory implements ConversationalFactory {

    private SelectionModule module;

    public SMConvMachineFactory(SelectionModule module){
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {

        ConvMachine machine = new ConvMachine(module.getModuleHandler());
        SMOnloadNode root = new SMOnloadNode("root", module);
        SMModuleSwitchNode endPoint = new SMModuleSwitchNode("end_point");
        SMContextNode contextNode = new SMContextNode("context", module);
        SMContextConfirmNode confirmContext = new SMContextConfirmNode("confirm_context", module);
        SMModuleSelectionNode moduleSelection = new SMModuleSelectionNode("module_selection", module);
        SMPhaseSelectionNode phaseSelection = new SMPhaseSelectionNode("phase_selection", module);
        SMPipSelectionNode pipSelectionNode = new SMPipSelectionNode("pip_selection", module);

        root.addSuccessor("context", contextNode);
        contextNode.addSuccessor("confirm_context", confirmContext);
        confirmContext.addSuccessor("phase_selection", phaseSelection);
        phaseSelection.addSuccessor("pip_selection", pipSelectionNode);
        phaseSelection.addSuccessor("module_selection", moduleSelection);
        pipSelectionNode.addSuccessor("module_selection", moduleSelection);
        moduleSelection.addSuccessor("end_point", endPoint);
        endPoint.addSuccessor("phase_selection", phaseSelection);

        endPoint.setPrev(moduleSelection);
        moduleSelection.setPrev(phaseSelection);
        pipSelectionNode.setPrev(phaseSelection);
        confirmContext.setPrev(contextNode);

        machine.setRoot(root);

        return machine;
    }
}
