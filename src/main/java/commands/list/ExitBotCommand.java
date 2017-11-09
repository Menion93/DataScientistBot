package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;
import main.java.utils.Helper;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Andrea on 09/10/2017.
 */
public class ExitBotCommand extends Command {

    private String[] KEYWORDS = {"!exit bot", "!goodbye bot"};
    String[] EXIT_SENTENCES = {"See you next time!", "Have a nice day", "It was a pleasure to work with you"};
    private boolean finishedTalking;

    public ExitBotCommand(DataScienceModuleHandler handler) {
        super(handler);
        finishedTalking = true;
    }

    @Override
    public List<String> handleCommand() {
        handler.continueHandlerDiscussion(this);
        finishedTalking = false;
        return Arrays.asList("Do you want to save before exit?");
    }

    @Override
    public List<String> reply(String userInput){

        if(userInput.equals("yes")){
            handler.getSession().saveCurrentInstance();
        }

        handler.isSayingGoodbye(true);
        return Arrays.asList(Helper.selectRandomString(EXIT_SENTENCES));

    }

    @Override
    public boolean finishedTalking(){
        return finishedTalking;
    }

    @Override
    public boolean commandIsRequested(String userInput){
        return  checkKeywordsInText(KEYWORDS, userInput);
    }
}
