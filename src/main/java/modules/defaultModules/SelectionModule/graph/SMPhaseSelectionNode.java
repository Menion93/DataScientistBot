package main.java.modules.defaultModules.SelectionModule.graph;

import main.java.database.MongoRecomRepository;
import main.java.modules.conversational.ConvNode;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;
import main.java.modules.Module;
import main.java.recommending.ModuleRecommendation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 19/11/2017.
 */
public class SMPhaseSelectionNode extends ConvNode {

    private SelectionModule module;
    private ModuleRecommendation moduleRecom;


    public SMPhaseSelectionNode(String nodeId, SelectionModule module) {
        super(nodeId);
        this.module = module;
        moduleRecom = new ModuleRecommendation(new MongoRecomRepository(), "recom", module.getModuleHandler());

    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("It seems you are in the " + module.getCurrentPipStep() + " phase",
                "You want to go to the next step or do you want to do more analysis at this phase level?",
                "Or maybe do you want to know how many steps there are",
                "In this case, please write \"show steps\"" );
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        if(userInput.equals("show steps")){
            return getSuccessor("pip_selection");

        }

        boolean understood = module.understandIntention(userInput);

        if(understood){
            List<Module> modules = module.suggestKModules(module.getPipStep(), module.getNumberOfRecommendation());
            module.setModuleToRecommend(modules);
            return getSuccessor("module_selection");
        }

        if(!module.getStepAvailable()){
            errorMessage =  Arrays.asList("There is no step beyond this point",
                    "Keep working at this phase or go in the opposite direction you previously had specified");

            return this;
        }

        errorMessage = Arrays.asList("Sorry I could not understand what you want to do",
                "I want to know if you want to go to the next or previous step, or you want " +
                        "to keep working at this level");

        return this;
    }






}
