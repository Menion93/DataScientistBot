package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

/**
 * Created by Andrea on 10/10/2017.
 */
public class HelpCommand extends Command {

    private String[] KEYWORDS = {"description", "what can you do?", "exit bot", "help me", "help", "describe"};

    public HelpCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleCommand() {
        return handler.getCurrentModule().getModuleDescription();
    }

    @Override
    public boolean commandIsRequested(String userInput){
        return checkKeywordsInText(KEYWORDS, userInput);
    }
}
