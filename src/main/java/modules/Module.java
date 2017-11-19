package main.java.modules;

import main.java.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.utils.Helper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrea on 06/10/2017.
 */
public abstract class Module {

    protected DataScienceModuleHandler handler;
    protected String moduleName;
    protected ModuleSubscription.PIPELINE_STEPS step;

    public Module(DataScienceModuleHandler handler, String moduleName, ModuleSubscription.PIPELINE_STEPS step){
        this.handler = handler;
        this.moduleName = moduleName;
        this.step = step;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Module module = (Module) o;
        return getModuleName() != null ? getModuleName().equals(module.getModuleName()) : module.getModuleName() == null;
    }

    @Override
    public int hashCode() {
        return getModuleName() != null ? getModuleName().hashCode() : 0;
    }

    public DataScienceModuleHandler getModuleHandler(){ return handler; }
    public String selectRandomSentence(String[] sentences){
        return Helper.selectRandomString(sentences);
    }
    public String getModuleName(){
        return this.moduleName;
    }
    public ModuleSubscription.PIPELINE_STEPS getModuleStep(){ return step; }
    public abstract List<String> reply(String userInput);
    public abstract String getModuleDescription();
    public abstract String getModuleUsage();
    public abstract String makeRecommendation();
    public abstract void loadModuleInstance();
    public abstract void saveModuleInstance();
    public abstract void resetModuleInstance();
    public abstract void resetConversation();
    public abstract List<String> repeat();
    public abstract List<String> back();
    public abstract List<String> onModuleLoad();
}
