package main.java.modules.ContextModule;

import main.java.core.DataScienceModuleHandler;
import main.java.modules.Module;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 03/11/2017.
 */
public class ContextModule extends Module {

    private enum STEPS {ASK_TAGS, VALID_TAGS};
    private int stepIndex;
    private List<String> context;


    public ContextModule(DataScienceModuleHandler handler) {
        super(handler, "ContextModule", null);
        context = new LinkedList<>();
    }

    @Override
    public List<String> reply(String userInput) {
        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep) {

            case ASK_TAGS: {
                stepIndex++;
                return Arrays.asList("Please think about your problem now",
                        "Once you have done that, make a summary of the problem using an arbitrary number of tags");
            }
            case VALID_TAGS: {
                List<String> tags = extractTags(userInput);
                if (tags != null) {
                    context.addAll(tags);
                    handler.switchToDefaultModule();
                    return Arrays.asList("Tags validated");
                }
                return Arrays.asList("I could not understand the tags, please write them separating with a space");
            }
            default:
                return Arrays.asList("Please can you repeat?");
        }
    }

    private List<String> extractTags(String userInput) {
        return Arrays.asList(userInput.split(" "));
    }

    public List<String> getContext() {
        return context;
    }
    @Override
    public String getModuleDescription() {
        return "You have to sum up the problem in a few tags";
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {

    }

    @Override
    public void saveModuleInstance() {
    }

    @Override
    public void resetModuleInstance() {

    }

    @Override
    public void resetConversation() {

    }
}
