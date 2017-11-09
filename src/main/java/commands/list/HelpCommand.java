package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 10/10/2017.
 */
public class HelpCommand extends Command {

    private String[] KEYWORDS = {"!help", "!what can you do?", };

    public HelpCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public List<String> handleCommand() {
        return Arrays.asList(handler.getCurrentModule().getModuleUsage());
    }

    @Override
    public boolean commandIsRequested(String userInput){
        return checkKeywordsInText(KEYWORDS, userInput);
    }
}
