package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class ExitModuleCommand extends Command {

    private String[] KEYWORDS = {"exit module"};

    public ExitModuleCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        handler.switchToDefaultModule();
        return handler.getCurrentModule().reply("");
    }
}
