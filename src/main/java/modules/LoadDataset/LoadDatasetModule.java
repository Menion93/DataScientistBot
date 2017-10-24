package main.java.modules.LoadDataset;

import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import main.java.modules.Module;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 24/10/2017.
 */
public class LoadDatasetModule extends Module{

    private enum STEPS {PATH_SELECTION, PATH_VALIDATION, ADD_NAME};
    private int stepIndex;
    private Dataset newDataset;

    public LoadDatasetModule(DataScienceModuleHandler handler) {
        super(handler, "LoadDataset");
    }

    @Override
    public List<String> reply(String userInput) {

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case PATH_SELECTION: {
                stepIndex++;
                return Arrays.asList("Please add the path for the dataset you want to load");
            }
            case PATH_VALIDATION: {
                // Extract and validate the name,
                String path = userInput;
                newDataset = new Dataset(handler.getRepository());
                if(newDataset.loadFromFS(path)){
                    stepIndex++;
                    return Arrays.asList("Dataset loaded with success", "Now please choose a name for the new dataset");
                }
                return Arrays.asList("I could not found the dataset at the path specified, can you write it again?");
            }
            case ADD_NAME: {
                String name = userInput;

                if(isAValidDataset(name)){
                    newDataset.setDatasetName(name);
                    newDataset.setRoot(newDataset.getDatasetName());
                    newDataset.setFrom(null);
                    handler.getSession().getDatasetPool().add(newDataset);
                    return Arrays.asList("Dataset added to the pool");
                }
                return Arrays.asList("The choosen name is not valid");
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
        return null;
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
