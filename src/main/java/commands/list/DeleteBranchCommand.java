package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;
import java.util.List;

/**
 * Created by Andrea on 09/10/2017.
 */
public class DeleteBranchCommand extends Command {

    public String[] KEYWORDS = {"delete branch", "cancel branch", "erase branch"};

    public String branchName;

    public DeleteBranchCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleCommand() {

        if(branchName == null)
            return "Please, you need to tell me the name of the branch you want to delete";

        if(handler.getSession().getBranchName().equals(branchName))
            return "You cannot delete the branch you are currently working on";

        if(branchName.equals("main"))
            return "You cannot delete the main branch";

        if(handler.getRepository().isAValidBranch(branchName)){
            handler.getRepository().deleteBranch(branchName);
            return "Branch with id: " + branchName + " deleted";
        }
        else{
            handler.continueHandlerDiscussion(this);
            return "The branch name is not valid. Select another name";
        }
    }

    @Override
    public boolean commandIsRequested(String userInput){

        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if(!keyword_detected) return false;

        String[] tokens = userInput.split(" ");

        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("branch") && i<tokens.length-1)
                branchName = tokens[i+1];
        }

        return true;

        // Initialize NLP tools
        //parseUserInput(userInput);

        /*List<String> possibleIds = new LinkedList<>();

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
                if(pos.toLowerCase().contains("NN") || pos.toLowerCase().contains("obj"))
                    possibleIds.add(pos);
            }

        }

        analysisName = checkPossibleBranchNames(possibleIds);*/

        //return true;
    }

    /*public String checkPossibleBranchNames(List<String> ids){
        return "";
    }*/

}