package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.modules.conversational.ConvNode;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMContextNode extends ConvNode{

    private SelectionModule module;

    public SMContextNode(String nodeId, SelectionModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        this.onLoadMessages = Arrays.asList("Please think about your problem now",
                "Once you have done that, make a summary of the problem using an arbitrary number of tags");

        this.errorMessage = Arrays.asList("I could not understand the tags, please write them separating with a space");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        List<String> context = new LinkedList<>();
        List<String> tags = extractTags(userInput);

        if (tags == null) {
            return this;
        }

        context.addAll(tags);
        module.setContext(context);
        return getSuccessor("confirm_context");
    }

    private List<String> extractTags(String userInput) {
        return Arrays.asList(userInput.split(" "));
    }
}
