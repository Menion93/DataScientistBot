package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

/**
 * Created by Andrea on 16/10/2017.
 */
public class PrintCurrentSessionCommand extends Command{

    public String[] KEYWORDS = {"session info"};

    public PrintCurrentSessionCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public String handleCommand() {
        return handler.getSession().toString();
    }
}
