package main.java;

import main.java.core.DataScienceModuleHandler;
import main.java.modules.JGTModule.JGTModule;
import main.java.modules.MLModule.MLModule;
import main.java.modules.Module;
import main.java.modules.SchemaAutocompleteModule.SchemaAutocompleteModule;
import main.java.modules.ColWranglerModule.ColumnWranglerModule;

import java.util.*;

/**
 * Created by Andrea on 06/10/2017.
 */
public class ModuleSubscription {

    private DataScienceModuleHandler handler;


    public enum PIPELINE_STEPS  {DATASET_SEARCH, DATA_CLEANING , DATA_INTEGRATION, FEATURE_ENGINEERING, LEARNING};

    HashMap<PIPELINE_STEPS, List<Module>> subscriptions;

    public ModuleSubscription(DataScienceModuleHandler handler){
        this.handler = handler;
        subscriptions = new HashMap<>();

        for(PIPELINE_STEPS step : PIPELINE_STEPS.values())
            subscriptions.put(step, new ArrayList<>());

        // Add dataset search modules here
        List<Module> datasetResearchModules = subscriptions.get(PIPELINE_STEPS.DATASET_SEARCH);
        datasetResearchModules.add(new SchemaAutocompleteModule(handler, PIPELINE_STEPS.DATASET_SEARCH));
        datasetResearchModules.add(new JGTModule(handler, PIPELINE_STEPS.DATASET_SEARCH));

        List<Module> dataCleaningModules = subscriptions.get(PIPELINE_STEPS.DATA_INTEGRATION);
        dataCleaningModules.add(new ColumnWranglerModule(handler, PIPELINE_STEPS.DATA_INTEGRATION));

        //List<Module> dataIntegrationModules = subscriptions.get(PIPELINE_STEPS.DATA_INTEGRATION);

        //List<Module> featureEngineeringModules = subscriptions.get(PIPELINE_STEPS.FEATURE_ENGINEERING);
        //featureEngineeringModules.add(new LFEModule(handler, PIPELINE_STEPS.FEATURE_ENGINEERING));


        List<Module> trainingEvalModules = subscriptions.get(PIPELINE_STEPS.LEARNING);
        trainingEvalModules.add(new MLModule(handler, PIPELINE_STEPS.LEARNING));

    }

    public List<String> listAllSubscribedModules(){
        List<String> modules = new LinkedList<>();

        for(List<Module> moduleL : subscriptions.values())
            for(Module module : moduleL )
                modules.add(module.getModuleName());

        return modules;
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

    public List<Module> getModulesByStep(PIPELINE_STEPS step){
        return subscriptions.get(step);
    }

    public List<Module> getModuleListByNames(LinkedList<String> moduleNames) {
        List<Module> result = new LinkedList<>();

        for(String name : moduleNames)
            result.add(getModuleByName(name));

        return result;
    }


    public List<String> getModuleByStep(PIPELINE_STEPS step) {
        List<Module> modules = subscriptions.get(step);
        List<String> moduleNames = new LinkedList<>();

        for(Module moduleName : modules)
            moduleNames.add(moduleName.getModuleName());

        return moduleNames;
    }

 }
