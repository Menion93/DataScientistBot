package main.java.modules.preprocessing;

import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.dataset.Dataset;
import main.java.modules.Module;
import java.util.*;

/**
 * Created by Andrea on 17/10/2017.
 */
public class ColumnWranglerModule extends Module {

    private enum STEPS {INSTRUCTION, DATASET_SELECCTION, COLUMN_SELECTION, TRANSFORMATION_SELECTION, DATASET_CONSTRUCTION};
    private int stepIndex;
    private Map<String, Map<String, List<String>>> ds2transf;
    private Preprocesser preprocesser;
    private Dataset currentDataset;
    private Dataset newDataset;
    private String column;
    private String currentTransf;


    public ColumnWranglerModule(DataScienceModuleHandler handler) {
        super(handler, "ColumnWrangler");
        ds2transf = new HashMap<>();
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
                currentTransf = userInput;
                if(preprocesser.hasTransformation(currentTransf)){
                    newDataset = new Dataset(handler.getRepository());
                    newDataset.setRoot(currentDataset.getRoot());
                    newDataset.setFrom(currentDataset.getDatasetName());
                    newDataset.copyDatasetData(currentDataset);
                    preprocesser.applyTransformationAndSave(newDataset, column, currentTransf);
                    stepIndex++;
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
                    saveTransformation(currentDataset.getDatasetName(), column, currentTransf);
                    return Arrays.asList("Dataset added in the pool");
                }
                return Arrays.asList("Name is not valid, please try again");
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }

    }

    private void saveTransformation(String dsName, String column, String currentTransf) {
        Map<String,List<String>> column2transf = ds2transf.get(dsName);
        if(column2transf == null){
            ds2transf.put(dsName, new HashMap<>());
        }
        column2transf = ds2transf.get(dsName);
        List<String> transformations = column2transf.get(column);

        if(transformations == null){
            column2transf.put(column, new LinkedList<>());
        }

        column2transf.get(column).add(currentTransf);
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
        DBRepository repo = handler.getRepository();
        ds2transf = repo.getColumnWranglerAnalysis();
    }

    @Override
    public void saveModuleInstance() {
        if(!ds2transf.isEmpty()){
            DBRepository repo = handler.getRepository();
            repo.saveColumnWranglerAnalysis(ds2transf);
        }
    }

    @Override
    public void resetModuleInstance() {

    }

    @Override
    public void resetConversation() {

    }
}
