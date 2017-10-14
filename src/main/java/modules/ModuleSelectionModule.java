package main.java.modules;

import main.java.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class ModuleSelectionModule extends Module {

    String firstTime = "For starter, select a module of the following";
    boolean isFirstTime = true;

    String[] REPLY_ONLOAD = {"Please select a new Module", "Nice Job, now please select a new module"};

    int currentStep;

    ModuleSubscription subscriptions;

    public ModuleSelectionModule(DataScienceModuleHandler handler) {
        super(handler, "ModuleSelection");
        this.subscriptions = handler.getModuleSubscription();
        this.currentStep = 0;
    }

    @Override
    public List<String> reply(String userInput) {
        return null;
    }

    @Override
    public String getModuleDescription() {
        return "I know all the people who are experts to do all kind of jobs. Ask me what you want to do.";
    }

    @Override
    public String makeRecommendation() {
        return "You must select a module first, or I will not know what to recommend you";
    }

    @Override
    public void loadModuleInstance() {

    }

    @Override
    public void saveModuleInstance() {

    }

    public String replyOnLoad() {

        if(isFirstTime){
            isFirstTime = false;
            return firstTime;
        }
        return selectRandomSentence(REPLY_ONLOAD);
    }

}
