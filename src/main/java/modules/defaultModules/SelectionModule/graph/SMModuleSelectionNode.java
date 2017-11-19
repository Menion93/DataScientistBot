package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.modules.Module;
import main.java.modules.conversational.ConvNode;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMModuleSelectionNode extends ConvNode {

    private SelectionModule module;

    public SMModuleSelectionNode(String nodeId, SelectionModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("I can recommend you those modules to work with then",
                module.printModules(module.getModuleToRecommend()),
                "Select the one you want to use writing its name");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        Module selectedModule = module.getModuleHandler().getModuleSubscription().getModuleByName(userInput);
        if(selectedModule == null){
            errorMessage =  Arrays.asList("I could not understand the module name, please write it again");
            return this;
        }

        List<String> sessionModules = module.getSessionModules();
        sessionModules.add(selectedModule.getModuleName());
        module.getModuleHandler().getCurrentModule().resetConversation();
        module.getModuleHandler().setCurrentModule(selectedModule);
        List<String> replies = new LinkedList<>();
        replies.addAll(Arrays.asList("Module switched"));

        return getSuccessor("end_point");

    }


}
