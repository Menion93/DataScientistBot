package main.java.intent.handlers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import main.java.core.DataScienceModuleHandler;
import main.java.intent.IntentHandler;


/**
 * Created by Andrea on 09/10/2017.
 */
public class BranchIntentHandler extends IntentHandler {

    private String[] KEYWORDS = {"branch", "make branch", "create branch"};
    private String branchName;
    private boolean finishedTalking;

    private enum STEPS {BRANCH_NAME, SWITCH_BRANCH};
    private int stepIndex;

    public BranchIntentHandler(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleIntent() {
        // Create a branch of this work
        handler.continueHandlerDiscussion(this);

        if(branchName == null) {
            return "Please specify a name for the branch to create";
        }

        // Bring it to the next level
        stepIndex++;

        if(handler.createNewBranch(branchName)){
            stepIndex++;
            return "Branch with name " + branchName + " created. Would you like to switch now?";
        }
        else return "Branch name is not valid, try with another name";

    }

    @Override
    public String reply(String userInput){
        parseUserInput(userInput);
        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case BRANCH_NAME: {
                // check if steps name is present, otherwise reask the same question
                branchName = parseBranchName();

                if(branchName != null){
                    if(handler.createNewBranch(branchName)){
                        stepIndex++;
                        return "Branch with name " + branchName + " created. Would you like to switch now?";
                    }
                    else return "Branch name is not valid, try with another name";
                }
                return "Please specify a name for the branch to create";
            }

            case SWITCH_BRANCH: {
                finishedTalking = true;

                if(userInput.equals("yes")){
                    handler.loadBranch(branchName);
                    return "Switched to the new branch";
                }
                return "As you whish";
            }

            default:
                return "Can you repeat?";
        }
    }


    private String parseBranchName() {
        CoreMap word = sentences.get(0);
        if(word != null)
            return word.get(CoreAnnotations.TextAnnotation.class);
        return null;
    }

    @Override
    public boolean finishedTalking(){
        return finishedTalking;
    }

    @Override
    public boolean isMyIntent(String userInput){
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if(!keyword_detected) return false;
        String[] tokens = userInput.split(" ");

        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("branch") && i<tokens.length-1)
                branchName = tokens[i+1];
        }

        return true;

        /*
        // Initialize NLP tools and parse user string
        parseUserInput(userInput);

        List<String> possibleIds = new LinkedList<>();
        Tree tree = null;
        SemanticGraph dependencies = null;

        // Iterate through all the labelled words
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

                // Check if it is a dobj or a noun, and add those option to the possible id set
                if(pos.toLowerCase().contains("nn") || pos.toLowerCase().contains("obj"))
                    possibleIds.add(pos);
            }

            // this is the parse tree of the current sentence
            tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            // this is the Stanford dependency graph of the current sentence
            dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

        }

        branchName = selectPossibleBranchNames(possibleIds, dependencies);*/

    }
/*
    public String selectPossibleBranchNames(List<String> possibleNames, SemanticGraph graph){
        return possibleNames.get(0);
    }
*/
}
