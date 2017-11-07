package main.java.commands.list;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class MakeAnalysisCommand extends Command {

    private String[] KEYWORDS = {"make analysis", "create analysis"};
    private enum STEPS {ANALYSIS_NAME, SWITCH_ANALYSIS};
    private int stepIndex = 0;
    private String analysisName;
    boolean finishedTalking;


    public MakeAnalysisCommand(DataScienceModuleHandler handler) {
        super(handler);

    }

    @Override
    public List<String> reply(String userInput){
        parseUserInput(userInput);
        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case ANALYSIS_NAME: {
                // check if steps name is present, otherwise reask the same question
                analysisName = parseAnalysisName();

                if(analysisName != null){
                    if(handler.createNewBranch(analysisName)){
                        stepIndex++;
                        return Arrays.asList("Analysis with name " + analysisName + " created", "Would you like to switch now?");
                    }
                    else return Arrays.asList("Analysis name is not valid, try with another name");
                }
                return Arrays.asList("Please specify a name for the Analysis to create");
            }

            case SWITCH_ANALYSIS: {
                finishedTalking = true;

                if(userInput.equals("yes")){
                    handler.loadAnalysis(analysisName);
                    return Arrays.asList("Switched to the new project");
                }
                return Arrays.asList("As you whish");
            }

            default:
                return Arrays.asList("Can you repeat?");
        }
    }

    @Override
    public boolean finishedTalking(){
        return finishedTalking;
    }

    private String parseAnalysisName() {
        CoreMap word = sentences.get(0);
        if(word != null)
            return word.get(CoreAnnotations.TextAnnotation.class);
        return null;
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        analysisName = null;
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if(!keyword_detected) return false;
        String[] tokens = userInput.split(" ");

        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("analysis") && i<tokens.length-1)
                analysisName = tokens[i+1];
        }

        return true;
    }

    @Override
    public List<String> handleCommand() {
        // Create a branch of this work

        if(analysisName == null) {
            return Arrays.asList("Please specify a name for the analysis to create");
        }

        if(handler.createNewAnalysis(analysisName)){
            stepIndex++;
            handler.continueHandlerDiscussion(this);
            finishedTalking = false;
            return Arrays.asList("Analysis with name " + analysisName + " created", "Would you like to switch now?");
        }
        else return Arrays.asList("Analysis name is not valid, try with another name");
    }
}
