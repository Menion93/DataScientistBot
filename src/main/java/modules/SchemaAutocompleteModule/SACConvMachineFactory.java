package main.java.modules.SchemaAutocompleteModule;

import main.java.modules.SchemaAutocompleteModule.graph.SACModuleMoreNode;
import main.java.modules.SchemaAutocompleteModule.graph.SACModuleSchemaInputNode;
import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConversationalFactory;

/**
 * Created by Andrea on 21/11/2017.
 */
public class SACConvMachineFactory implements ConversationalFactory {

    private SchemaAutocompleteModule module;

    public SACConvMachineFactory(SchemaAutocompleteModule module){
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {

        ConvMachine machine = new ConvMachine(module.getModuleHandler());
        SACModuleSchemaInputNode root = new SACModuleSchemaInputNode("root", module);
        SACModuleMoreNode endPoint = new SACModuleMoreNode("ask_more", module);

        root.addSuccessor("ask_more", endPoint);
        endPoint.addSuccessor("root", root);

        endPoint.setPrev(root);

        machine.setRoot(root);
        return machine;
    }
}
