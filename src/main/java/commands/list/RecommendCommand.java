package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;


/**
 * Created by Andrea on 09/10/2017.
 */
public class RecommendCommand extends Command {

    public String[] KEYWORDS = {"suggest", "recommend", "advice"};

    public RecommendCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleCommand() {

        return handler.getCurrentModule().makeRecommendation();
    }

    @Override
    public boolean commandIsRequested(String userInput){

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
