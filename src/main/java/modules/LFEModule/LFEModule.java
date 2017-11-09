package main.java.modules.LFEModule;

import main.java.ModuleSubscription;
import main.java.dataset.Dataset;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.modules.Module;

import java.util.*;
import java.util.List;

/**
 * Created by Andrea on 07/10/2017.
 */
public class LFEModule extends Module {

    private enum STEPS {PRINT_DATASET, SELECT_DATASET, SELECT_TARGET, CONFIRM_TRANSFORMATION};
    private Map<String, Map<Integer, String>> dataset2transfor;
    private int stepIndex;
    private String currentDatasetName;
    private Dataset currentDataset;
    private Map<Integer, String> anResult;
    private LFEMapper lfetool;
    private String prevUserInput;
    private int prevStep;


    public LFEModule(DataScienceModuleHandler handler,  ModuleSubscription.PIPELINE_STEPS step) {
        super(handler, "LFE", step);
        dataset2transfor = new HashMap<>();
        lfetool = new LFEMapper();
    }

    @Override
    public List<String> reply(String userInput) {
        prevUserInput = userInput;
        prevStep = stepIndex;

        if(handler.getSession().getDatasetPool().size() == 0)
            return Arrays.asList("You have to add a dataset in the pool first!");

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case PRINT_DATASET: {
                stepIndex++;
                List<String> replies = new LinkedList<>();
                replies.add("Choose the dataset you want to try the analysis");
                replies.add(handler.getSession().printDatasetList());
                return replies;
            }
            case SELECT_DATASET: {
                // Extract and validate the name,
                currentDatasetName = extractAndValidateDatasetName(userInput);

                if(currentDatasetName != null){
                    return Arrays.asList("Now specify the target class");
                }
                return Arrays.asList("I could not found the dataset, can you rewrite it?");
            }
            case SELECT_TARGET: {
                String target = extractAndValidateTarget(userInput);

                if(target != null){
                    Map<Integer, String> analysis = dataset2transfor.get(currentDatasetName);
                    String result;

                    if(analysis == null){
                        Dataset ds = handler.getSession().getDatasetByName(currentDatasetName);
                        anResult = lfetool.getLFEAnalysis(ds, target);
                        dataset2transfor.put(currentDatasetName, anResult);
                        result =  printResult(anResult);
                    }
                    else{
                        result = printResult(analysis);
                    }

                    this.resetConversation();
                    handler.switchToDefaultModule();
                    return Arrays.asList(result);
                }
                return Arrays.asList("The target is not valid");
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }
    }

    private String extractAndValidateTarget(String userInput) {
        Map<String, List<Double>> numericalAtt = currentDataset.getNumericalAttributes();

        if(numericalAtt.containsKey(userInput))
            return userInput;

        return null;
    }

    private String printResult(Map<Integer, String> lfeAnalysis) {

        StringBuilder sb = new StringBuilder();

        for(Map.Entry entry : lfeAnalysis.entrySet()){
            sb.append(entry.getKey());
            sb.append("\t");
            sb.append(entry.getValue());
        }

        return sb.toString();
    }

    private String extractAndValidateDatasetName(String userInput) {
        currentDataset = handler.getSession().getDatasetByName(userInput);

        if(currentDataset != null)
            return currentDataset.getDatasetName();

        return null;
    }


    @Override
    public String getModuleDescription() {
        return "I can help you building a new feature for your dataset, given the latter.";
    }

    @Override
    public String getModuleUsage() {
        return null;
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        DBRepository repository = handler.getRepository();
        dataset2transfor = repository.getLFEAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!dataset2transfor.isEmpty()){
            DBRepository repository = handler.getRepository();
            repository.saveLFEAnalysis(dataset2transfor);
        }
    }

    @Override
    public void resetModuleInstance() {
        Map<String, Map<Integer, String>> dataset2transfor = new HashMap<>();
        stepIndex = 0;
    }

    @Override
    public void resetConversation() {
        stepIndex = 0;
    }

    @Override
    public List<String> repeat() {
        this.stepIndex = prevStep;
        return reply(prevUserInput);
    }
}