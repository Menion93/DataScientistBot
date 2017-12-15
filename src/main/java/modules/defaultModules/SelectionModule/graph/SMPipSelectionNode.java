package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.modules.ModuleSubscription;
import main.java.modules.conversational.ConvNode;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;
import main.java.modules.Module;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMPipSelectionNode extends ConvNode{

    private SelectionModule module;

    public SMPipSelectionNode(String nodeId, SelectionModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("PIPELINE STEPS:", module.printPipelineSteps(), "Please select the step you are " +
                "interested in");
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {
        ModuleSubscription.PIPELINE_STEPS selectedStep = validateStep(userInput);

        if(selectedStep != null){
            module.setPipelineStep(selectedStep);
            List<Module> modules = module.suggestKModules(selectedStep, module.getNumberOfRecommendation());
            module.setModuleToRecommend(modules);
            return getSuccessor("module_selection");
        }

        errorMessage =  Arrays.asList("I could not understand the name of that step",
                "PIPELINE STEPS:", module.printPipelineSteps(), "Please select one of those above");

        return this;
    }

    private ModuleSubscription.PIPELINE_STEPS validateStep(String userInput) {
        for(ModuleSubscription.PIPELINE_STEPS step : ModuleSubscription.PIPELINE_STEPS.values()){
            if(step.toString().equals(userInput))
                return step;
        }

        return null;
    }
}
