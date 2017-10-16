package main.java.modules;

import main.java.core.DataScienceModuleHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class GreetingModule extends Module {

    private String[] GREETINGS = {"Nice to meet you", "Hello man, I'm DataScienceBot", "Hi, DataScienceBot at your service"};
    private String[] SMALLTALK = {"Lets do something useful", "Nice work, go on"};
    private boolean firstTime;


    public GreetingModule(DataScienceModuleHandler handler){
        super(handler, "Greeting");
        firstTime = true;
    }

    @Override
    public List<String> reply(String userInput) {

        if(firstTime){
            firstTime = false;
            return  Arrays.asList(selectRandomSentence(GREETINGS));
        }

         return Arrays.asList(selectRandomSentence(SMALLTALK));
    }

    @Override
    public String getModuleDescription() {
        return "Nice to meet you, I am DSBot, and I will help you building the pipeline for this analysis." +
                "For starter, I suggets you to answer some of my questions to help me understand your problem";
    }

    @Override
    public String makeRecommendation() {
        return "You need to select a module first to get a recommendation";
    }

    @Override
    public void loadModuleInstance() {

    }


    @Override
    public void saveModuleInstance() {
        // Do nothing
    }

    @Override
    public void resetModuleInstance() {
        firstTime = true;
    }

    @Override
    public void resetConversation() {
        // Do nothing
    }
}
