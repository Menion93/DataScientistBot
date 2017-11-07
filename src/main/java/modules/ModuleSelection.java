package main.java.modules;

import main.java.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.MongoRecomRepository;
import main.java.recommending.ModuleRecommendation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 02/11/2017.
 */
public class ModuleSelection extends Module {

    private enum STEPS {ONLOAD, ASK_INTENTION, UNDERSTAND_INTENTION, MODULE_SELECTION, EVALUATION_SELECTION, RUN_MORE};

    private STEPS currentStep;
    private ModuleSubscription.PIPELINE_STEPS pipStep;
    private int pipIndex;
    private boolean stepAvailable;
    private ModuleRecommendation moduleRecom;


    public ModuleSelection(DataScienceModuleHandler handler) {
        super(handler, "ModuleSelection", null);
        currentStep = STEPS.ONLOAD;
        pipIndex = 0;
        stepAvailable = true;
        pipStep = ModuleSubscription.PIPELINE_STEPS.values()[pipIndex];

        moduleRecom = new ModuleRecommendation(new MongoRecomRepository(), "recom");
    }

    @Override
    public List<String> reply(String userInput) {

        switch (currentStep){

            case ONLOAD: {
                handler.switchContextModule();
                currentStep = STEPS.ASK_INTENTION;
                List<String> replies = new LinkedList<>();
                replies.addAll(Arrays.asList("Hello, I am DataScienceBot",
                        "I am here to help you thorough the building of your ml pipeline",
                        "Before you start building it I need to ask you a few questions"));
                replies.addAll(handler.reply(""));
                return replies;
            }
            case ASK_INTENTION:{
                currentStep = STEPS.UNDERSTAND_INTENTION;
                return Arrays.asList("It seems you are in the " + pipStep.toString() + " phase",
                        "You want to go to the next step or do you want to do more analysis at this phase level?");
            }
            case UNDERSTAND_INTENTION:{
                boolean understood = understandIntention(userInput);

                if(understood){
                    List<String> modules = suggestModules();
                    return Arrays.asList("I can recommend you those modules to work with then",
                            printModules(modules),
                            "Select the one you want to use writing its name");

                }
                if(!stepAvailable){
                    return Arrays.asList("There is no step beyond this point",
                            "Keep working at this phase or go in the opposite direction you previously had specified");
                }
                return Arrays.asList("Sorry I could not understand what you want to do",
                        "I want to know if you want to go to the next or previous step, or you want " +
                                "to keep working at this level");

            }

            case MODULE_SELECTION: {
                Module module = handler.getModuleSubscription().getModuleByName(userInput);
                if(module != null){
                    handler.getCurrentModule().resetConversation();
                    handler.setCurrentModule(module);
                    List<String> replies = new LinkedList<>();
                    replies.addAll(Arrays.asList("Module switched"));
                    replies.addAll(handler.reply(""));
                    currentStep = STEPS.ASK_INTENTION;
                    return replies;
                }
                return Arrays.asList("I could not understand the module name, please write it again");
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }
    }

    private List<String> suggestModules() {
        return moduleRecom.getBestModulesByPhase(pipStep);
    }

    private String printModules(List<String> modules) {
        StringBuilder sb = new StringBuilder();
        sb.append("Recommended Modules:\n");
        for(String module : modules){
            sb.append("\t");
            sb.append(module);
            sb.append("\n");
        }

        return sb.toString();
    }

    public void setPipelineStep(ModuleSubscription.PIPELINE_STEPS step){
        pipStep = step;
        pipIndex = Arrays.asList(ModuleSubscription.PIPELINE_STEPS.values()).indexOf(pipStep);
    }

    private boolean understandIntention(String userInput) {

        ModuleSubscription.PIPELINE_STEPS[] steps = ModuleSubscription.PIPELINE_STEPS.values();
        stepAvailable = true;

        if(userInput.contains("keep")){
            currentStep = STEPS.MODULE_SELECTION;
            return true;
        }

        if(userInput.contains("next")){

            if(pipIndex + 1 < steps.length){
                pipIndex++;
                pipStep = steps[pipIndex];
                currentStep = STEPS.MODULE_SELECTION;
                return true;
            }

            stepAvailable = false;
        }

        if(userInput.contains("previous")){

            if(pipIndex - 1 >= 0){
                pipIndex--;
                pipStep = steps[pipIndex];
                currentStep = STEPS.MODULE_SELECTION;
                return true;
            }
            stepAvailable = false;
        }

        return false;
    }

    @Override
    public String getModuleDescription() {
        return "I can help you choose what to do next";
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {

    }

    @Override
    public void saveModuleInstance() {

    }

    @Override
    public void resetModuleInstance() {

    }

    @Override
    public void resetConversation() {

    }
}
