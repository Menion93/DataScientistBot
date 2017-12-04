package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 24/10/2017.
 */
public class ImportNewDatasetCommand extends Command {

    private String[] KEYWORDS = {"!import dataset", "!create dataset"};
    private boolean finishedTalking;
    private enum STEPS {PATH_VALIDATION, ADD_NAME}
    private int stepIndex;
    private Dataset newDataset;

    public ImportNewDatasetCommand(DataScienceModuleHandler handler) { super(handler); }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> reply(String userInput){
        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case PATH_VALIDATION: {
                // Extract and validate the name,
                String path = userInput;
                newDataset = new Dataset(handler.getRepository());
                try{
                    if(newDataset.loadFromFS(path)){
                        stepIndex++;
                        newDataset.setPath(path);
                        return Arrays.asList("Dataset loaded with success", "Now please choose a name for the new dataset");
                    }
                }catch(Exception e){

                }
                stepIndex = 0;
                finishedTalking = true;
                return Arrays.asList("I could not found the dataset at the path specified");
            }
            case ADD_NAME: {
                String name = userInput;

                if(!exists(name)){
                    newDataset.setDatasetName(name);
                    newDataset.setRoot(newDataset.getDatasetName());
                    newDataset.setFrom(null);
                    handler.getSession().getDatasetPool().add(newDataset);
                    stepIndex = 0;
                    finishedTalking = true;
                    return Arrays.asList("Dataset added to the pool");
                }
                return Arrays.asList("The choosen name is not valid");
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }
    }

    private boolean exists(String newName) {
        return handler.getRepository().existDataset(newName);
    }

    @Override
    public boolean finishedTalking(){
        return finishedTalking;
    }

    @Override
    public String getBasicCommand() {
        return "!import dataset";
    }

    @Override
    public List<String> handleCommand() {
        handler.continueHandlerDiscussion(this);
        finishedTalking = false;
        return Arrays.asList("Please add the path for the dataset you want to load");
    }
}
