package main.java;

import main.java.core.DataScienceModuleHandler;
import main.java.modules.JGTModule.JGTModule;
import main.java.modules.Module;
import main.java.modules.SchemaAutocompleteModule.SchemaAutocompleteModule;
import main.java.modules.preprocessing.ColumnWranglerModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 06/10/2017.
 */
public class ModuleSubscription {

    private DataScienceModuleHandler handler;

    public enum PIPELINE_STEPS  {DATASET_SEARCH, DATA_INTEGRATION, FEATURE_ENGINEERING, MODEL_SELECTION, EVALUATION};

    HashMap<PIPELINE_STEPS, List<Module>> subscriptions;

    public ModuleSubscription(DataScienceModuleHandler handler){
        this.handler = handler;
        subscriptions = new HashMap<>();

        for(PIPELINE_STEPS step : PIPELINE_STEPS.values())
            subscriptions.put(step, new ArrayList<>());

        // Add dataset search modules here
        List<Module> datasetResearchModules = subscriptions.get(PIPELINE_STEPS.DATASET_SEARCH);
        datasetResearchModules.add(new SchemaAutocompleteModule(handler));
        datasetResearchModules.add(new JGTModule(handler));

        List<Module> dataIntegrationModules = subscriptions.get(PIPELINE_STEPS.DATA_INTEGRATION);
        dataIntegrationModules.add(new ColumnWranglerModule(handler));

        //List<Module> featureEngineeringModules = subscriptions.get(PIPELINE_STEPS.FEATURE_ENGINEERING);
        //featureEngineeringModules.add(new LFEModule(handler));

        List<Module> modelSelectionModules = subscriptions.get(PIPELINE_STEPS.MODEL_SELECTION);
        List<Module> evaluationModules = subscriptions.get(PIPELINE_STEPS.EVALUATION);

    }

    public List<String> listAllSubscribedModules(){
        List<String> modules = new LinkedList<>();

        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                modules.add(module.getModuleName());

        return modules;
    }

    public String getModuleDescriptionByName(String name){
        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                if(module.getModuleName().equals(name))
                    return module.getModuleDescription();
        return null;
    }

    public void saveAllModulesInstances(){
        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                module.saveModuleInstance();
    }

    public void loadAllModuleInstances(){
        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                module.loadModuleInstance();
    }

    public void resetAllModuleInstances(){
        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                module.resetModuleInstance();
    }

    public Module getModuleByName(String moduleName) {
        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                if(module.getModuleName().toLowerCase().equals(moduleName.toLowerCase()))
                    return module;
        return null;
    }

    public List<String> getModuleNames() {
        List<String> moduleNames = new LinkedList<>();

        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                moduleNames.add(module.getModuleName());

        return moduleNames;
    }

}
