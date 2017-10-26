package main.java.core;

import main.java.ModuleSubscription;
import main.java.session.Session;
import main.java.database.DBRepository;
import main.java.commands.Command;
import main.java.commands.CommandHandler;
import main.java.modules.GreetingModule;
import main.java.modules.Module;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class DataScienceModuleHandler {

    private DBRepository repository;
    private Module defaultModule;
    private Module currentModule;
    private ModuleSubscription subscriptions;
    private Command currentIntent;
    private Session session;
    private boolean sayingGoodbye;
    private boolean changeModule;

    public DataScienceModuleHandler(DBRepository repository){
        defaultModule = new GreetingModule(this);
        currentModule = defaultModule;
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
            List<String> botReplies = currentIntent.reply(userInput);
            session.logMessages(botReplies, false);
            replies.addAll(botReplies);

            // If we finished talking with the handler, continue with the previous module
            if(currentIntent.finishedTalking())
                currentIntent = null;
            else return replies;

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
        List<String> moduleReplies = currentModule.reply(userInput);
        replies.addAll(moduleReplies);

        session.logMessages(replies, false);
        return replies;
    }

    public List<String> specialInputDetected(String userInput){
        CommandHandler translator = new CommandHandler(this);
        return translator.matchUserInputToCommand(userInput);
    }

    public void saveCurrentInstance(){
        session.saveSessionInfo();
        subscriptions.saveAllModulesInstances();
    }

    public void loadBranch(String branchName) {
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

    public boolean createNewAnalysis(String analysisName) {
        if(!repository.isAValidAnalysis(analysisName)){
            String prevAnalysis = session.getBranchName();
            String prevBranchName = session.getBranchName();
            session.setSessionInfo(analysisName, "main");

            session.saveSessionInfo();

            // Now switch to the previous session info
            session.setSessionInfo(prevAnalysis, prevBranchName);

            return true;
        }

        return false;
    }

    public void deleteAnalysis(String analysisName){
        repository.deleteAnalysis(analysisName);
    }

    public void loadAnalysis(String analysisName) {
        session.setSessionInfo(analysisName, "main");
        session.loadSession();
        subscriptions.resetAllModuleInstances();
    }

    public void continueHandlerDiscussion(Command intent){ this.currentIntent = intent;}
    public void changeModule(boolean changing){
        this.changeModule = changing;
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
    public void setCurrentModule(Module currentModule) { this.currentModule = currentModule; }
    public void switchToDefaultModule() { currentModule = defaultModule; }
}
