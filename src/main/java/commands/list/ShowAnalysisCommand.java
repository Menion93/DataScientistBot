package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.List;

/**
 * Created by Andrea on 17/10/2017.
 */
public class ShowAnalysisCommand extends Command {

    private String[] KEYWORDS = {"show analysis"};

    public ShowAnalysisCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public String handleCommand() {
        StringBuilder sb = new StringBuilder();
        List<String> analysis = handler.getRepository().getAnalysis();

        for(String branch : analysis){
            sb.append("\t");
            sb.append(branch);
            sb.append("\n");
        }

        return sb.toString();
    }
}
