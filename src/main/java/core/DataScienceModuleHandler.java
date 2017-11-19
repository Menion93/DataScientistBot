package main.java.core;

import main.java.ModuleSubscription;
import main.java.modules.defaultModules.SelectionModule.SelectionModule;
import main.java.recommending.RecomSubscription;
import main.java.session.Session;
import main.java.database.DBRepository;
import main.java.commands.Command;
import main.java.commands.CommandHandler;
import main.java.modules.Module;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class DataScienceModuleHandler {

    private DBRepository repository;
    private Module currentModule;
    private SelectionModule selectionModule;
    private ModuleSubscription subscriptions;
    private RecomSubscription recommendations;
    private Command currentIntent;
    private Session session;
    private boolean sayingGoodbye;
    private boolean justLoaded;
    private CommandHandler commandHandler;

    public DataScienceModuleHandler(DBRepository repository){
        commandHandler = new CommandHandler(this);
        selectionModule = new SelectionModule(this, commandHandler);
        currentModule = selectionModule;
        subscriptions = new ModuleSubscription(this);
        recommendations = new RecomSubscription();
        this.session = new Session(repository, this);
        this.repository = repository;
        this.justLoaded = true;
        repository.setSession(session);
    }

    public List<String> reply(String userInput){
        session.logMessage(userInput, true);

        List<String> replies = new LinkedList<>();

        // If we need to talk to an handler, talk to it first
        if(currentIntent != null){
            List<String> botReplies = currentIntent.reply(userInput);
            session.logMessages(botReplies, false);
            replies.addAll(botReplies);

            // If we finished talking with the handler, continue with the previous module
            if(currentIntent.finishedTalking())
                currentIntent = null;
            else{
                return replies;
            }

        }
        // If there is a special command, handle it
        else{

            List<String> specialReplies = specialInputDetected(userInput);

            if(specialReplies != null){
                replies.addAll(specialReplies);
                session.logMessages(specialReplies, false);
                return replies;
            }
        }

        // If no special command is given, continue with the normal workflow

        if(justLoaded){
            replies.addAll(currentModule.onModuleLoad());
            justLoaded = false;
        }
        else
            replies.addAll(currentModule.reply(userInput));

        session.logMessages(replies, false);
        return replies;
    }

    public List<String> specialInputDetected(String userInput){
        return commandHandler.matchUserInputToCommand(userInput);
    }

    public void setCurrentModule(Module currentModule) {
        justLoaded = true;
        this.currentModule = currentModule;
        this.selectionModule.setPipelineStep(currentModule.getModuleStep());
    }

    public void switchToDefaultModule() {
        currentModule.resetConversation();
        currentModule = selectionModule;
    }


    public List<String> getTags(){
        return selectionModule.getContext();
    }

    public void continueHandlerDiscussion(Command intent){ this.currentIntent = intent;}
    public boolean isSayingGoodbye() {
        return sayingGoodbye;
    }
    public void isSayingGoodbye(boolean goodbye){
        this.sayingGoodbye = goodbye;
    }
    public Module getCurrentModule(){
        return currentModule;
    }
    public DBRepository getRepository() { return repository; }
    public ModuleSubscription getModuleSubscription(){ return subscriptions; }
    public Session getSession(){return this.session;}
    public SelectionModule getSelectionModule() {
        return selectionModule;
    }
    public void setJustLoaded(boolean justLoaded) {
        this.justLoaded = justLoaded;
    }
}
