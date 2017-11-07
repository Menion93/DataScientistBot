package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.List;

/**
 * Created by Andrea on 07/11/2017.
 */
public class RepeatCommand extends Command {

    private String[] KEYWORDS = {"repeat"};

    public RepeatCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        return handler.getLastMessage();
    }
}
