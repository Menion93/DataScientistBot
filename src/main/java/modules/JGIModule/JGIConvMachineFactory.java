package main.java.modules.JGIModule;

import main.java.modules.JGIModule.graph.JGIModuleAskMoreNode;
import main.java.modules.JGIModule.graph.JGIModuleSchInputNode;
import main.java.modules.conversational.ConvMachine;
import main.java.modules.conversational.ConversationalFactory;

/**
 * Created by Andrea on 21/11/2017.
 */
public class JGIConvMachineFactory implements ConversationalFactory {

    private JGIModule module;

    public JGIConvMachineFactory(JGIModule module){
        this.module = module;
    }

    @Override
    public ConvMachine getConversationalMachine() {
        ConvMachine machine = new ConvMachine(module.getModuleHandler());

        JGIModuleSchInputNode root = new JGIModuleSchInputNode("root",module);
        JGIModuleAskMoreNode askMore = new JGIModuleAskMoreNode("ask_more", module);

        root.addSuccessor("ask_more", askMore);
        askMore.addSuccessor("root", root);

        askMore.setPrev(root);

        machine.setRoot(root);

        return machine;
    }
}
