package main.java.modules.conversational;

import main.java.core.DataScienceModuleHandler;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 18/11/2017.
 */
public class ConvMachine {

    private ConvNode root;
    private ConvNode current;
    private DataScienceModuleHandler handler;

    public ConvMachine(DataScienceModuleHandler handler){
        this.handler = handler;
    }

    public List<String> reply(String userInput) {
        ConvNode successor = current.chooseSuccessor(userInput);

        if(successor == null){
            handler.switchToDefaultModule();
            return Arrays.asList("Module exited");
        }

        successor.onLoad();

        if (successor.equals(current)) {
            return successor.getErrorMessage();
        }

        current = successor;

        return current.getOnLoadMessages();
    }

    public void setRoot(ConvNode root) {
        this.root = root;
        this.root.onLoad();
        this.current = root;
    }

    public List<String> repeat() {
        current.onLoad();
        return current.getOnLoadMessages();
    }

    public List<String> back(){
        if(current.prev == null)
            return this.current.onLoadMessages;
        this.current = this.current.prev;
        this.current.onLoad();
        return current.onLoadMessages;
    }

    public List<String> showCurrentStateText(){
        this.current.onLoad();
        return this.current.onLoadMessages;
    }

    public void resetConversation() {
        this.current = root;
    }
}
