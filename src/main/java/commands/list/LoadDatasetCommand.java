package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 24/10/2017.
 */
public class LoadDatasetCommand extends Command{

    private String[] KEYWORDS = {"!load dataset"};

    private String datasetName;

    public LoadDatasetCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        datasetName = null;
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if (!keyword_detected) return false;
        String[] tokens = userInput.split(" ");

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("dataset") && i < tokens.length - 1)
                datasetName = tokens[i + 1];
        }

        return true;
    }

    @Override
    public List<String> handleCommand() {
        if(datasetName == null)
            return Arrays.asList("You need to specify the dataset name to load");

        if(!existDataset(datasetName))
            return Arrays.asList("Dataset with name " + datasetName + " not found");

        if(isAlreadyInPool())
            return Arrays.asList("The selected dataset is already loaded");

        Dataset ds = new Dataset(handler.getRepository());
        ds.setDatasetName(datasetName);
        handler.getRepository().loadData(ds);
        handler.getSession().getDatasetPool().add(ds);
        return Arrays.asList("Added dataset " + datasetName + " to the pool");
    }

    private boolean existDataset(String newName) {
        return handler.getRepository().existDataset(newName);
    }

    public boolean isAlreadyInPool() {
        return handler.getSession().getDatasetByName(datasetName) != null;
    }
}
