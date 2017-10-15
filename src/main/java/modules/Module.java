package main.java.modules;

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
    protected boolean firstTime;  // It checks if the module was used or not in this analysis
                                  // No need to load a module if it was not used

    public Module(DataScienceModuleHandler handler, String moduleName){
        this.handler = handler;
        this.moduleName = moduleName;
    }

    public String selectRandomSentence(String[] sentences){
        return Helper.selectRandomString(sentences);
    }

    public abstract List<String> reply(String userInput);
    public abstract String getModuleDescription();
    public void exitModule(){
        handler.changeModule(true);
    }
    public String getModuleName(){
        return this.moduleName;
    }
    public abstract String makeRecommendation();
    public abstract void loadModuleInstance();
    public abstract void saveModuleInstance();

}
