package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class ResetModuleCommand extends Command {

    private String[] KEYWORDS = {"!reset module"};

    public ResetModuleCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        handler.getCurrentModule().resetModuleInstance();
        return Arrays.asList("Module resetted");
    }
}
