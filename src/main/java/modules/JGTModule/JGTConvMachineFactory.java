package main.java.modules.JGTModule;

import main.java.modules.JGTModule.graph.JGTModuleAskMoreNode;
import main.java.modules.JGTModule.graph.JGTModuleSchInputNode;
import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConversationalFactory;

/**
 * Created by Andrea on 21/11/2017.
 */
public class JGTConvMachineFactory implements ConversationalFactory {

    private JGTModule module;

    public JGTConvMachineFactory(JGTModule module){
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {
        ConvMachine machine = new ConvMachine(module.getModuleHandler());

        JGTModuleSchInputNode root = new JGTModuleSchInputNode("root",module);
        JGTModuleAskMoreNode askMore = new JGTModuleAskMoreNode("ask_more", module);

        root.addSuccessor("ask_more", askMore);
        askMore.addSuccessor("root", root);

        askMore.setPrev(root);

        machine.setRoot(root);

        return machine;
    }
}
