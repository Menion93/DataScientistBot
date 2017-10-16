package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;
import main.java.utils.Helper;


/**
 * Created by Andrea on 09/10/2017.
 */
public class ExitBotCommand extends Command {

    private String[] KEYWORDS = {"see you next time", "exit the bot", "exit bot", "goodbye bot"};
    String[] EXIT_SENTENCES = {"See you next time!", "Have a nice day", "It was a pleasure to work with you"};

    public ExitBotCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleCommand() {
        handler.isSayingGoodbye(true);
        return Helper.selectRandomString(EXIT_SENTENCES);
    }

    @Override
    public boolean commandIsRequested(String userInput){
        return  checkKeywordsInText(KEYWORDS, userInput);
    }
}
