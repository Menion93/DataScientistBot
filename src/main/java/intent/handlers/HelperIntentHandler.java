package main.java.intent.handlers;

import main.java.core.DataScienceModuleHandler;
import main.java.intent.IntentHandler;

import java.util.List;

/**
 * Created by Andrea on 10/10/2017.
 */
public class HelperIntentHandler extends IntentHandler {

    private String[] KEYWORDS = {"description", "what can you do?", "exit bot", "help me"};

    public HelperIntentHandler(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleIntent() {
        return handler.getCurrentModule().getModuleDescription();
    }

    @Override
    public boolean isMyIntent(String userInput){
        return checkKeywordsInText(KEYWORDS, userInput);
    }
}
