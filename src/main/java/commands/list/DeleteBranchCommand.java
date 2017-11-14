package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 09/10/2017.
 */
public class DeleteBranchCommand extends Command {

    public String[] KEYWORDS = {"!delete branch", "!cancel branch", "!erase branch"};

    public String branchName;

    public DeleteBranchCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public List<String> handleCommand() {

        if(branchName == null)
            return Arrays.asList("Please, you need to tell me the name of the branch you want to delete");

        if(handler.getSession().getBranchName().equals(branchName))
            return Arrays.asList("You cannot delete the branch you are currently working on");

        if(branchName.equals("main"))
            return Arrays.asList("You cannot delete the main branch");

        if(handler.getRepository().isAValidBranch(branchName)){
            handler.getRepository().deleteBranch(branchName);
            return Arrays.asList("Branch with id: " + branchName + " deleted");
        }
        else{
            handler.continueHandlerDiscussion(this);
            return Arrays.asList("The branch name is not valid. Select another name");
        }
    }

    @Override
    public boolean commandIsRequested(String userInput){
        branchName = null;
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if(!keyword_detected) return false;

        String[] tokens = userInput.split(" ");

        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("branch") && i<tokens.length-1)
                branchName = tokens[i+1];
        }

        return true;

    }

    @Override
    public String getBasicCommand() {
        return "!delete branch";
    }

    /*public String checkPossibleBranchNames(List<String> ids){
        return "";
    }*/

}
