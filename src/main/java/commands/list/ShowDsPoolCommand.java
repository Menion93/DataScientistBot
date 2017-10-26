package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;
import main.java.dataset.Dataset;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 24/10/2017.
 */
public class ShowDsPoolCommand extends Command {

    private String[] KEYWORDS = {"show datasets", "show pool"};

    public ShowDsPoolCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pool Datasets:\n");
        for(Dataset ds : handler.getSession().getDatasetPool()){
            sb.append("\t");
            sb.append(ds.getDatasetName());
            sb.append("\n");
        }

        return Arrays.asList(sb.toString());
    }
}
