package main.java.intent;

import main.java.core.DataScienceModuleHandler;
import main.java.intent.handlers.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 09/10/2017.
 */
public class IntentTranslator {

    List<IntentHandler> handlerList;

    public IntentTranslator(DataScienceModuleHandler handler){

        handlerList = new LinkedList<>();

        handlerList.add(new BranchIntentHandler(handler));
        handlerList.add(new DeleteBranchIntentHandler(handler));
        handlerList.add(new ExitBotIntentHandler(handler));
        handlerList.add(new LoadBranchIntentHandler(handler));
        handlerList.add(new RecommendIntentHandler(handler));
        handlerList.add(new HelperIntentHandler(handler));
    }

    public String matchUserInputToIntent(String userInput){

        for(IntentHandler handler : handlerList){
            if(handler.isMyIntent(userInput)){
                return handler.handleIntent();
            }
        }

        return null;
    }
}
