package main.java.modules.defaultModules.SelectionModule;

import main.java.ModuleSubscription;
import main.java.commands.Command;
import main.java.commands.CommandHandler;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.database.MongoRecomRepository;
import main.java.modules.Module;
import main.java.modules.conversational.ConvMachine;
import main.java.recommending.ModuleRecommendation;

import java.util.*;

/**
 * Created by Andrea on 02/11/2017.
 */
public class SelectionModule extends Module {

    private ModuleSubscription.PIPELINE_STEPS pipStep;
    private int pipIndex;
    private boolean stepAvailable;
    private ModuleRecommendation moduleRecom;
    private List<String> sessionModules;
    private List<Module> moduleToRecommend;
    private int numOfModuleToRecomm = 5;
    private CommandHandler commandHandler;
    private ConvMachine stateMachine;
    private List<String> context;

    public SelectionModule(DataScienceModuleHandler handler, CommandHandler commandHanlder) {
        super(handler, "SelectionModule", null);
        pipIndex = 0;
        stepAvailable = true;
        pipStep = ModuleSubscription.PIPELINE_STEPS.values()[pipIndex];
        sessionModules = new LinkedList<>();
        moduleRecom = new ModuleRecommendation(new MongoRecomRepository(), "recom", handler);
        this.commandHandler = commandHanlder;
        context = new LinkedList<>();
    }

    @Override
    public List<String> reply(String userInput) {

        if(stateMachine == null){
            SMConvMachineFactory factory = new SMConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        return stateMachine.reply(userInput);
    }

    public String printPipelineSteps() {
        StringBuilder sb = new StringBuilder();
        for(ModuleSubscription.PIPELINE_STEPS step : ModuleSubscription.PIPELINE_STEPS.values()){
            sb.append("\t");
            sb.append(step);
            sb.append("\n");
        }

        return sb.toString();
    }

    public List<String> getSessionModules() {
        return sessionModules;
    }


    public List<Module> suggestKModules(ModuleSubscription.PIPELINE_STEPS pipStep, int k) {
        List<Module> recommendedModules = moduleRecom.getBestModulesByPhase(pipStep);

        int numOfSuggModules = recommendedModules.size();
        if(numOfSuggModules < k)
            addKRandomModulesByPipStep(k-numOfSuggModules, recommendedModules);

        return recommendedModules;
    }

    private void addKRandomModulesByPipStep(int k, List<Module> recommended) {
        List<Module> modules = handler.getModuleSubscription().getModulesByStep(getPipStep());

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

    public String printModules(List<Module> modules) {
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

    public boolean getStepAvailable() {
        return stepAvailable;
    }

    public void setModuleToRecommend(List<Module> moduleToRecommend) {
        this.moduleToRecommend = moduleToRecommend;
    }

    public List<Module> getModuleToRecommend(){
        return moduleToRecommend;
    }

    public void setPipelineStep(ModuleSubscription.PIPELINE_STEPS step){
        pipStep = step;
        pipIndex = Arrays.asList(ModuleSubscription.PIPELINE_STEPS.values()).indexOf(pipStep);
    }

    public int getNumberOfRecommendation() {
        return numOfModuleToRecomm;
    }

    public String getCurrentPipStep(){
        return pipStep.toString();
    }

    public ModuleSubscription.PIPELINE_STEPS getPipStep(){
        return pipStep;
    }

    public boolean understandIntention(String userInput) {

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
        StringBuilder sb = new StringBuilder("Useful commands:\n");

        for(Command command : commandHandler.getCommandList()){
            sb.append("\t");
            sb.append(command.getBasicCommand());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repository = handler.getRepository();
        sessionModules = repository.getModulesBySession();
    }

    @Override
    public void saveModuleInstance() {
        if(!sessionModules.isEmpty()){
            DBRepository repository = handler.getRepository();
            repository.saveModulesBySession(sessionModules);
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
        return stateMachine.repeat();
    }

    @Override
    public List<String> back() {
        return stateMachine.back();
    }

    @Override
    public List<String> onModuleLoad() {
        if(stateMachine == null){
            SMConvMachineFactory factory = new SMConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }
        return stateMachine.showCurrentStateText();
    }

    public void setContext(List<String> context) {
        this.context = context;
    }

    public List<String> getContext() {
        return context;
    }
}
