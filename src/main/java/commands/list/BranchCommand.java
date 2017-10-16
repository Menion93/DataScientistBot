package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;


/**
 * Created by Andrea on 09/10/2017.
 */
public class BranchCommand extends Command {

    private String[] KEYWORDS = {"make branch", "create branch"};
    private String branchName;
    private boolean finishedTalking;

    private enum STEPS {BRANCH_NAME, SWITCH_BRANCH};
    private int stepIndex;

    public BranchCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public String handleCommand() {
        // Create a branch of this work
        stepIndex = 0;

        if(branchName == null) {
            handler.continueHandlerDiscussion(this);
            return "Please specify a name for the branch to create";
        }

        if(handler.createNewBranch(branchName)){
            stepIndex++;
            handler.continueHandlerDiscussion(this);
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
                branchName = parseBranchName(userInput);

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
                    handler.saveCurrentInstance();
                    handler.loadBranch(branchName);
                    return "Switched to the new branch";
                }
                return "As you whish";
            }

            default:
                return "Can you repeat?";
        }
    }


    private String parseBranchName(String userInput) {
        return userInput;
    }

    @Override
    public boolean finishedTalking(){
        return finishedTalking;
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if (!keyword_detected) return false;
        String[] tokens = userInput.split(" ");

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("branch") && i < tokens.length - 1)
                branchName = tokens[i + 1];
        }

        return true;
    }
}
