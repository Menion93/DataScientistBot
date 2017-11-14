package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Andrea on 09/10/2017.
 */
public class RecommendCommand extends Command {

    public String[] KEYWORDS = {"!suggest", "!recommend", "!advice"};

    public RecommendCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public List<String> handleCommand() {

        return Arrays.asList(handler.getCurrentModule().makeRecommendation());
    }

    @Override
    public boolean commandIsRequested(String userInput){
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public String getBasicCommand() {
        return "!recommend";
    }


}
