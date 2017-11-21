package main.java.modules.SchemaAutocompleteModule.graph;

import main.java.modules.SchemaAutocompleteModule.SchemaAutocompleteModule;
import main.java.modules.conversational.ConvNode;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 21/11/2017.
 */
public class SACModuleMoreNode extends ConvNode {

    private SchemaAutocompleteModule module;

    public SACModuleMoreNode(String nodeId, SchemaAutocompleteModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        List<String> replies = new LinkedList<>();
        replies.add(module.getResultString());
        replies.add("Would you like to search for another schema?");
        onLoadMessages = replies;
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        if(userInput.equals("yes"))
            return getSuccessor("root");

        return null;
    }
}
