package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 17/10/2017.
 */
public class ShowBranchesCommand extends Command {

    private String[] KEYWORDS = {"show branches"};

    public ShowBranchesCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        StringBuilder sb = new StringBuilder();
        List<String> branches = handler.getRepository().getBranches();

        for(String branch : branches){
            sb.append("\t");
            sb.append(branch);
            sb.append("\n");
        }

        return Arrays.asList(sb.toString());
    }
}
