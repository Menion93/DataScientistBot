package main.java.modules.MLModule;

import main.java.modules.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.dataset.Dataset;
import main.java.modules.Module;
import main.java.modules.conversational.ConvMachine;

import java.util.*;

/**
 * Created by Andrea on 17/10/2017.
 */
public class MLModule extends Module {

    private List<Evaluation> evaluations;
    private Dataset currentDataset;
    private String currentTarget;
    private Model currentModel;
    private Evaluation currentEval;
    private MachineAlgorithms models = new MachineAlgorithms();
    private ConvMachine stateMachine;


    public MLModule(DataScienceModuleHandler handler, ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "MLAlgorithms",step);
        evaluations = new LinkedList<>();
        models = new MachineAlgorithms();
    }

    @Override
    public List<String> reply(String userInput) {
      return stateMachine.reply(userInput);
    }

    public Evaluation getCurrentEval() {
        return currentEval;
    }

    public void setCurrentEvaluation(Evaluation currentEval) {
        this.currentEval = currentEval;
    }
    public void setCurrentDataset(Dataset currentDataset) {
        this.currentDataset = currentDataset;
    }

    public void setCurrentModel(Model currentModel) {
        this.currentModel = currentModel;
    }

    public Dataset getCurrentDataset(){
        return this.currentDataset;
    }

    public Model getCurrentModel() {
        return currentModel;
    }

    public String getCurrentTarget() {
        return currentTarget;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setCurrentTarget(String currentTarget) {
        this.currentTarget = currentTarget;
    }

    @Override
    public String getModuleDescription() {
        return "I know ho to apply various machine learning models";
    }

    @Override
    public String getModuleUsage() {
        return "You need to have a dataset to the pool first. I will need a dataset, a target class, " +
                "a model and an evaluation score. After that I will tell you the results of the training with " +
                " cross validation";
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repo = handler.getRepository();
        evaluations = repo.getMLModuleAnalysis();
    }

    public MachineAlgorithms getModels(){
        return models;
    }

    @Override
    public void saveModuleInstance() {
        if(!evaluations.isEmpty()){
            DBRepository repo = handler.getRepository();
            repo.saveMLModuleAnalysis(evaluations);
        }
    }

    @Override
    public void resetModuleInstance() {
        if(stateMachine == null){
            MLConvMachineFactory factory = new MLConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        evaluations = new LinkedList<>();
        resetConversation();
    }

    @Override
    public void resetConversation() {
        stateMachine.resetConversation();
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
    public List<String> onModuleLoad(){
        if(stateMachine == null){
            MLConvMachineFactory factory = new MLConvMachineFactory(this);
            stateMachine = factory.getConversationalMachine();
        }

        return stateMachine.showCurrentStateText();
    }
}
