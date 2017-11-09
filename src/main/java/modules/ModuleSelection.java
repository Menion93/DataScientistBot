package main.java.modules;

import main.java.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.database.MongoRecomRepository;
import main.java.recommending.ModuleRecommendation;

import java.util.*;

/**
 * Created by Andrea on 02/11/2017.
 */
public class ModuleSelection extends Module {

    private enum STEPS {ONLOAD, ASK_INTENTION, UNDERSTAND_INTENTION, STEP_SELECTION,MODULE_SELECTION, EVALUATION_SELECTION, RUN_MORE};
    private STEPS currentStep;
    private ModuleSubscription.PIPELINE_STEPS pipStep;
    private int pipIndex;
    private boolean stepAvailable;
    private ModuleRecommendation moduleRecom;
    private List<String> modules;
    private String prevUserInput;
    private STEPS prevStep;
    private int numOfModuleToRecomm = 5;

    public ModuleSelection(DataScienceModuleHandler handler) {
        super(handler, "ModuleSelection", null);
        currentStep = STEPS.ONLOAD;
        pipIndex = 0;
        stepAvailable = true;
        pipStep = ModuleSubscription.PIPELINE_STEPS.values()[pipIndex];
        modules = new LinkedList<>();
        moduleRecom = new ModuleRecommendation(new MongoRecomRepository(), "recom", handler);
    }

    @Override
    public List<String> reply(String userInput) {
        prevUserInput = userInput;
        prevStep = currentStep;

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
                        "You want to go to the next step or do you want to do more analysis at this phase level?",
                        "Or maybe do you want to know how many steps there are",
                        "In this case, please write \"show steps\"" );
            }
            case UNDERSTAND_INTENTION:{
                boolean understood = false;

                if(userInput.equals("show steps")){
                    currentStep = STEPS.STEP_SELECTION;
                    return Arrays.asList("PIPELINE STEPS:", printPipelineSteps(), "Please select the step you are " +
                            "interested in");
                }
                else
                    understood = understandIntention(userInput);

                if(understood){
                    currentStep = STEPS.MODULE_SELECTION;
                    List<Module> modules = suggestKModules(pipStep, numOfModuleToRecomm);
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
            case STEP_SELECTION:{
                 ModuleSubscription.PIPELINE_STEPS selectedStep = validateStep(userInput);

                 if(selectedStep != null){
                     pipStep = selectedStep;
                     currentStep = STEPS.MODULE_SELECTION;
                     List<Module> modules = suggestKModules(pipStep, numOfModuleToRecomm);
                     return Arrays.asList("I can recommend you those modules to work with then",
                             printModules(modules),
                             "Select the one you want to use writing its name");
                 }

                 return Arrays.asList("I could not understand the name of that step",
                         "PIPELINE STEPS:", printPipelineSteps(), "Please select one of those above");
            }

            case MODULE_SELECTION: {
                Module module = handler.getModuleSubscription().getModuleByName(userInput);
                if(module != null){
                    modules.add(module.getModuleName());
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

    private ModuleSubscription.PIPELINE_STEPS validateStep(String userInput) {
        for(ModuleSubscription.PIPELINE_STEPS step : ModuleSubscription.PIPELINE_STEPS.values()){
            if(step.toString().equals(userInput))
                return step;
        }

        return null;
    }

    private String printPipelineSteps() {
        StringBuilder sb = new StringBuilder();
        for(ModuleSubscription.PIPELINE_STEPS step : ModuleSubscription.PIPELINE_STEPS.values()){
            sb.append("\t");
            sb.append(step);
            sb.append("\n");
        }

        return sb.toString();
    }

    private List<Module> suggestKModules(ModuleSubscription.PIPELINE_STEPS pipStep, int k) {
        List<Module> recommendedModules = moduleRecom.getBestModulesByPhase(pipStep);

        int numOfSuggModules = recommendedModules.size();
        if(numOfSuggModules < k)
            addKRandomModulesByPipStep(k-numOfSuggModules, recommendedModules);

        return recommendedModules;
    }

    private void addKRandomModulesByPipStep(int k, List<Module> recommended) {
        List<Module> modules = handler.getModuleSubscription().getModulesByStep(pipStep);

        Collections.shuffle(modules);
        Iterator<Module> iterator = modules.iterator();
        int i = 0;

        while(iterator.hasNext() && i<k){
            Module module = iterator.next();

            if(!recommended.contains(module)){
                i++;
                recommended.add(module);
            }
        }
    }

    private String printModules(List<Module> modules) {
        StringBuilder sb = new StringBuilder();
        sb.append("Recommended Modules:\n");
        for(Module module : modules){
            sb.append("\t");
            sb.append(module.getModuleName());
            sb.append(": ");
            sb.append(module.getModuleDescription());
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
            return true;
        }

        if(userInput.contains("next")){

            if(pipIndex + 1 < steps.length){
                pipIndex++;
                pipStep = steps[pipIndex];
                return true;
            }

            stepAvailable = false;
        }

        if(userInput.contains("previous")){

            if(pipIndex - 1 >= 0){
                pipIndex--;
                pipStep = steps[pipIndex];
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
    public String getModuleUsage() {
        return null;
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repository = handler.getRepository();
        modules = repository.getModulesBySession();
    }

    @Override
    public void saveModuleInstance() {
        if(!modules.isEmpty()){
            DBRepository repository = handler.getRepository();
            repository.saveModulesBySession(modules);
        }
    }

    @Override
    public void resetModuleInstance() {

    }

    @Override
    public void resetConversation() {

    }

    @Override
    public List<String> repeat() {
        this.currentStep = prevStep;
        return reply(prevUserInput);
    }
}
