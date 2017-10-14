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

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Andrea on 09/10/2017.
 */
public class RecommendIntentHandler extends IntentHandler {

    public String[] KEYWORDS = {"suggest", "recommend", "advice"};

    public RecommendIntentHandler(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleIntent() {

        return handler.getCurrentModule().makeRecommendation();
    }

    @Override
    public boolean isMyIntent(String userInput){

        return checkKeywordsInText(KEYWORDS, userInput);
        /*parseUserInput(userInput);

        List<String> keywords = new LinkedList<>();

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                //System.out.println(word + " " + pos + " " + ne);
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
        }


        return false;*/
    }


}
