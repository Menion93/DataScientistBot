package main.java.modules.conversational;

import main.java.core.DataScienceModuleHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Andrea on 18/11/2017.
 */
public abstract class ConvNode {

    protected String nodeId;
    protected List<String> onLoadMessages;
    protected Map<String, ConvNode> id2successor;
    protected ConvNode prev;
    protected List<String> errorMessage;
    protected boolean alreadyLoaded;

    public ConvNode(String nodeId){
        this.nodeId = nodeId;
        this.id2successor = new HashMap<>();
    }

    public abstract void onLoad();
    public abstract ConvNode chooseSuccessor(String userInput);
    public void addSuccessor(String id, ConvNode succ){
        id2successor.put(id, succ);
    }
    public List<String> getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(List<String> errorMessage){
        this.errorMessage = errorMessage;
    }
    public List<String> getOnLoadMessages(){
        return onLoadMessages;
    }
    public ConvNode getSuccessor(String id){
        return id2successor.get(id);
    }
    public void setPrev(ConvNode prev){
        this.prev = prev;
    }

    @Override
    public boolean equals(Object node){
        ConvNode other = (ConvNode) node;
        return other.nodeId == nodeId;
    }
}
