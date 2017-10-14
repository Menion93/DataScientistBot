package main.java.intent.handlers;

import main.java.core.DataScienceModuleHandler;
import main.java.intent.IntentHandler;


/**
 * Created by Andrea on 09/10/2017.
 */
public class LoadBranchIntentHandler extends IntentHandler {

    public String[] KEYWORDS = {"loadbranch"};

    private String branchName;

    public LoadBranchIntentHandler(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleIntent() {

        if(branchName == null)
            return "You must give me the name of the branch you want to load";

        if(handler.getRepository().isAValidBranch(branchName)){
            loadBranch();
            return "Branch loaded";
        }

        return "The selected branch is not valid";
    }

    private void loadBranch() {
        handler.saveCurrentInstance();
        handler.loadBranch(branchName);
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

        //parseUserInput(userInput);

        /*
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
        }*/


    }

}
