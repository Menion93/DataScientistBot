package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class DeleteAnalysisCommand extends Command {

    public String[] KEYWORDS = {"!delete analysis", "!cancel analysis", "!erase analysis"};

    public String analysisName;

    public DeleteAnalysisCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public List<String> handleCommand() {
        if(analysisName == null)
            return Arrays.asList("Please, you need to tell me the name of the analysis you want to delete");

        if(handler.getSession().getSessionName().equals(analysisName))
            return Arrays.asList("You cannot delete the analysis you are currently working on");

        if(handler.getRepository().isAValidAnalysis(analysisName)){
            handler.getSession().deleteAnalysis(analysisName);
            return Arrays.asList("Analysis with id: " + analysisName + " deleted");
        }
        else{
            handler.continueHandlerDiscussion(this);
            return Arrays.asList("The analysis name is not valid. Select another name");
        }
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        analysisName = null;
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if (!keyword_detected) return false;

        String[] tokens = userInput.split(" ");

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("analysis") && i < tokens.length - 1)
                analysisName = tokens[i + 1];
        }

        return true;
    }
}
