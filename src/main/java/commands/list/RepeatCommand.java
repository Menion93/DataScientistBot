package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 07/11/2017.
 */
public class RepeatCommand extends Command {

    private String[] KEYWORDS = {"!repeat"};

    public RepeatCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public String getBasicCommand() {
        return "!repeat";
    }

    @Override
    public List<String> handleCommand() {
        List<String> prevMessages =  handler.getCurrentModule().repeat();

        if(prevMessages == null)
            prevMessages = new LinkedList<>();

        return prevMessages;
    }
}
