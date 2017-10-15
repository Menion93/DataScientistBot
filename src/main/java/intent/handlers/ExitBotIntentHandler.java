package main.java.intent.handlers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import main.java.core.DataScienceModuleHandler;
import main.java.intent.IntentHandler;
import main.java.utils.Helper;


/**
 * Created by Andrea on 09/10/2017.
 */
public class ExitBotIntentHandler extends IntentHandler {

    private String[] KEYWORDS = {"see you next time", "exit the bot", "exit bot", "goodbye bot"};
    String[] EXIT_SENTENCES = {"See you next time!", "Have a nice day", "It was a pleasure to work with you"};

    public ExitBotIntentHandler(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleIntent() {
        handler.isSayingGoodbye(true);
        return Helper.selectRandomString(EXIT_SENTENCES);
    }

    @Override
    public boolean isMyIntent(String userInput){
        return  checkKeywordsInText(KEYWORDS, userInput);
    }
}
