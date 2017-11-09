package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 24/10/2017.
 */
public class ShowDatasetCommand extends Command {

    private String[] KEYWORDS = {"!show dataset"};

    String datasetName;

    public ShowDatasetCommand(DataScienceModuleHandler handler) {
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
            return Arrays.asList("Please you have to specify a name for a dataset");

        Dataset ds = handler.getSession().getDatasetByName(datasetName);

        if(ds == null)
            return Arrays.asList("Dataset not found in the pool");

        return Arrays.asList(ds.toString());
    }
}
