package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

/**
 * Created by Andrea on 16/10/2017.
 */
public class LoadAnalysisCommand extends Command {

    private String[] KEYWORDS = {"load analysis"};
    private String analysisName;

    public LoadAnalysisCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if(!keyword_detected) return false;

        String[] tokens = userInput.split(" ");

        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("analysis") && i<tokens.length-1)
                analysisName = tokens[i+1];
        }

        return true;
    }

    @Override
    public String handleCommand() {
        if(analysisName == null)
            return "You must give me the name of the branch you want to load";

        if(handler.getRepository().isAValidAnalysis(analysisName)){
            handler.saveCurrentInstance();
            handler.loadAnalysis(analysisName);
            return "Analysis loaded";
        }

        return "The selected analysis is not valid";
    }
}
