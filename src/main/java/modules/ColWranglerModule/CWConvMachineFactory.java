package main.java.modules.ColWranglerModule;

import main.java.modules.ColWranglerModule.graph.*;
import main.java.modules.conversational.ConversationalFactory;
import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConvNode;

/**
 * Created by Andrea on 18/11/2017.
 */
public class CWConvMachineFactory implements ConversationalFactory {

    ColumnWranglerModule module;

    public CWConvMachineFactory(ColumnWranglerModule module) {
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {
        ConvMachine machine = new ConvMachine(module.getModuleHandler());
        ConvNode root = new CWModuleDatasetSelectionNode("root", module);
        ConvNode attrSelection = new CWModuleAttributeSelectionNode("attribute_selection", module);
        ConvNode transformSelection = new CWModuleTransformSelectionNode("transformation_selection", module);
        ConvNode datasetCreation = new CWModuleDatasetCreationNode("dataset_creation", module);
        ConvNode endPoint = new CWModuleEndPoint("end_point", module);

        root.addSuccessor("attribute_selection", attrSelection);
        attrSelection.addSuccessor("transformation_selection", transformSelection);
        transformSelection.addSuccessor("dataset_creation", datasetCreation);
        datasetCreation.addSuccessor("end_point", endPoint);
        endPoint.addSuccessor("root", root);

        attrSelection.setPrev(root);
        transformSelection.setPrev(attrSelection);
        datasetCreation.setPrev(transformSelection);
        endPoint.setPrev(datasetCreation);

        machine.setRoot(root);

        return machine;
    }


}
