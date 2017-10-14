package main.java.core;

import main.java.ModuleSubscription;
import main.java.Session.Session;
import main.java.database.DBRepository;
import main.java.intent.IntentHandler;
import main.java.intent.IntentTranslator;
import main.java.modules.GreetingModule;
import main.java.modules.Module;
import main.java.modules.ModuleSelectionModule;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class DataScienceModuleHandler {

    private DBRepository repository;

    private Module currentModule;
    private ModuleSelectionModule selectionModule;
    private ModuleSubscription subscriptions;
    private IntentHandler currentIntent;
    private Session session;

    private boolean sayingGoodbye;
    private boolean changeModule;

    public DataScienceModuleHandler(DBRepository repository){
        currentModule = new GreetingModule(this);
        selectionModule = new ModuleSelectionModule(this);
        subscriptions = new ModuleSubscription(this);
        this.session = new Session(repository);
        this.repository = repository;

        repository.setSession(session);
    }

    public List<String> reply(String userInput){
        session.logMessage(userInput, true);

        List<String> replies = new LinkedList<>();

        // If we need to talk to an handler, talk to it first
        if(currentIntent != null){
            String reply = currentIntent.reply(userInput);
            session.logMessage(reply, false);
            replies.add(reply);

            // If we finished talking with the handler, continue with the previous module
            if(currentIntent.finishedTalking())
                currentIntent = null;
            else return replies;

        }
        // If there is a special command, handle it
        else{

            String specialReply = specialInputDetected(userInput);

            if(specialReply != null){
                replies.add(specialReply);
                session.logMessage(specialReply, false);
                return replies;
            }
        }


        // If no special command is given, continue with the normal workflow
        List<String> moduleReplies = currentModule.reply(userInput);
        replies.addAll(moduleReplies);

        if(isChangingModule()){
            currentModule = selectionModule;
            replies.add(selectionModule.replyOnLoad());
        }

        session.logMessage(replies, false);
        return replies;
    }

    public String specialInputDetected(String userInput){
        IntentTranslator translator = new IntentTranslator(this);
        return translator.matchUserInputToIntent(userInput);
    }

    public void saveCurrentInstance(){
        session.saveSessionInfo();
        subscriptions.saveAllModulesInstances();
    }

    public void loadBranch(String branchName) {
        saveCurrentInstance();
        session.setBranchName(branchName);
        session.loadSession();
        subscriptions.loadAllModuleInstances();
    }

    public boolean createNewBranch(String branchName) {
        if(!repository.isAValidBranch(branchName)){
            String prevBranchName = session.getBranchName();
            session.setBranchName(branchName);

            // This saves the current instance with the new branch name, making a copy of it
            saveCurrentInstance();
            session.saveSessionInfo();

            // Now switch to the previous name branch
            session.setBranchName(prevBranchName);

            return true;
        }

        return false;
    }

    public void continueHandlerDiscussion(IntentHandler intent){ this.currentIntent = intent;}
    public void changeModule(boolean changing){
        this.changeModule = changing;
    }
    public boolean isChangingModule(){
        return this.changeModule;
    }
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



}
