package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.List;

/**
 * Created by Andrea on 16/11/2017.
 */
public class BackCommand extends Command {

    private String[] KEYWORDS = {"!back"};

    public BackCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public String getBasicCommand() {
        return "!back";
    }

    @Override
    public List<String> handleCommand() {
        return handler.getCurrentModule().back();
    }
}
