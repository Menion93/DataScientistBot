package main.java.modules.MLModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 22/10/2017.
 */
public class MachineAlgorithms {

    private Map<String,Model> models;

    public MachineAlgorithms(){
        // Create and add the models
        models = new HashMap<>();
    }

    public Model getModel(String modelName){
        return models.get(modelName);
    }

    public boolean modelExist(String modelName){
        return models.containsKey(modelName);
    }

    @Override
    public String toString(){
        return null;
    }
}
