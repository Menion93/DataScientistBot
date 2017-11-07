package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class SaveCommand extends Command {

    private String[] KEYWORDS = {"save work", "save session"};

    public SaveCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        this.handler.getSession().saveCurrentInstance();
        return Arrays.asList("Current instance saved");
    }
}
