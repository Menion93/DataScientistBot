package main.java.modules.preprocessing;

import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import main.java.modules.Module;
import java.util.*;

/**
 * Created by Andrea on 17/10/2017.
 */
public class ColumnWranglerModule extends Module {

    private enum STEPS {INSTRUCTION, DATASET_SELECCTION, COLUMN_SELECTION, TRANSFORMATION_SELECTION, DATASET_CONSTRUCTION};
    private int stepIndex;
    private Map<String, Map<String,Map<String,Double>>> ds2transf2params;
    private Preprocesser preprocesser;
    private Dataset currentDataset;
    private Dataset newDataset;
    private String column;


    public ColumnWranglerModule(DataScienceModuleHandler handler) {
        super(handler, "ColumnWrangler");
        ds2transf2params = new HashMap<>();
        preprocesser = new Preprocesser();
    }


    @Override
    public List<String> reply(String userInput) {

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case INSTRUCTION: {
                if(handler.getSession().getDatasetPool().size() == 0)
                    return Arrays.asList("You must add a dataset to the pool first!");
                stepIndex++;
                String dsList = handler.getSession().printDatasetList();
                return Arrays.asList("Select the dataset you want to work with", dsList);
            }
            case DATASET_SELECCTION: {
                currentDataset = handler.getSession().getDatasetByName(userInput);
                if(currentDataset != null){
                    stepIndex++;
                    return Arrays.asList("Now please select an attribute", currentDataset.toString());
                }
                else return Arrays.asList("The selected dataset could not be found, please try again");

            }
            case COLUMN_SELECTION: {
                column = userInput;
                if(currentDataset.hasColumn(column)){
                    stepIndex++;
                    return Arrays.asList("Now please select a transformation to apply", preprocesser.toString());
                }
                else
                    return Arrays.asList("I could not found the attribute, please try again");
            }
            case TRANSFORMATION_SELECTION: {
                String transformation = userInput;
                if(preprocesser.hasTransformation(transformation)){
                    newDataset = new Dataset(handler.getRepository());
                    newDataset.setRoot(currentDataset.getRoot());
                    newDataset.setFrom(currentDataset.getDatasetName());
                    newDataset.copyData(currentDataset);
                    preprocesser.applyTransformationAndSave(newDataset, transformation, column);
                    stepIndex = 0;
                    return Arrays.asList("Transformation done, please select a name for the new dataset");
                }
                return Arrays.asList("I could not recognize the transformation, try again");
            }

            case DATASET_CONSTRUCTION: {
                String newName = userInput;
                if(isAValidDataset(newName)){
                    newDataset.setDatasetName(newName);
                    handler.getSession().getDatasetPool().add(newDataset);
                    stepIndex = 0;
                    return Arrays.asList("Dataset added in the pool");
                }
                return Arrays.asList("Name is not valid, please try again");
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }

    }

    private boolean isAValidDataset(String newName) {
        return !handler.getRepository().existDataset(newName);
    }

    @Override
    public String getModuleDescription() {
        return "With this module you can apply transformation to the datasets";
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
