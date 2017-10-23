package main.java.modules.MLModule;

import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import main.java.modules.Module;

import java.util.*;

/**
 * Created by Andrea on 17/10/2017.
 */
public class MLModule extends Module {

    private enum STEPS {INSTRUCTION, DATASET_SELECTION, TARGET_SELECTION, MODEL_SELECTION, RUN_MORE};
    private int stepIndex;
    private Map<String, String> ds2model;
    private Map<String>
    private Dataset currentDataset;
    private String currentTarget;
    private MachineAlgorithms models = new MachineAlgorithms();

    public MLModule(DataScienceModuleHandler handler) {
        super(handler, "ColumnWrangler");
        ds2model = new HashMap<>();
        models = new MachineAlgorithms();
    }

    @Override
    public List<String> reply(String userInput) {

        if(handler.getSession().getDatasetPool().size() == 0)
            return Arrays.asList("You have to add a dataset in the pool first!");

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case INSTRUCTION: {
                stepIndex++;
                List<String> replies = new LinkedList<>();
                replies.add("Choose the dataset you want to try the analysis");
                replies.add(handler.getSession().printDatasetList());
                return replies;
            }
            case DATASET_SELECTION: {
                // Extract and validate the name,
                String currentDatasetName = extractAndValidateDatasetName(userInput);

                if(currentDatasetName != null){
                    return Arrays.asList("Now specify the target class");
                }
                return Arrays.asList("I could not found the dataset, can you rewrite it?");
            }
            case TARGET_SELECTION: {
                currentTarget = extractAndValidateTarget(userInput);

                if(currentTarget != null){
                    stepIndex++;
                    return Arrays.asList("Now select an algorithm to fit please", models.toString());
                }
                return Arrays.asList("The target is not valid");

            }
            case MODEL_SELECTION: {
                String modelSelected = userInput;
                if(models.validateModel(userInput)){
                    models.fitAndEvaluate(modelSelected);
                    stepIndex -= 2;
                    return this.reply(null);
                }
                stepIndex = 0;
                return this.reply(null);
            }

            case RUN_MORE: {
                if(userInput.contains("yes")){
                    lfetool.applyTransformationAndSave(currentDataset, anResult);
                    stepIndex -= 2;
                    return this.reply(null);
                }
                stepIndex = 0;
                return this.reply(null);
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }
    }

    private String extractAndValidateTarget(String userInput) {
        Map<String, List<Double>> numericalAtt = currentDataset.getNumericalAttributes();
        Map<String, List<String>> categoricalAtt = currentDataset.getCategoricalAttributes();

        if(numericalAtt.containsKey(userInput) || categoricalAtt.containsKey(userInput))
            return userInput;
        else
            return null;
    }

    private String extractAndValidateDatasetName(String userInput) {
        currentDataset = handler.getSession().getDatasetByName(userInput);

        if(currentDataset != null)
            return currentDataset.getDatasetName();

        return null;
    }

    @Override
    public String getModuleDescription() {
        return "You can train your dataset on the models contained in this module";
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
