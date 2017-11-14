package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class PrintCurrentSessionCommand extends Command{

    public String[] KEYWORDS = {"!session info"};

    public PrintCurrentSessionCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public String getBasicCommand() {
        return "!session info";
    }

    @Override
    public List<String> handleCommand() {
        return Arrays.asList(handler.getSession().toString());
    }
}
