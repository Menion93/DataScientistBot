package main.java.modules.preprocessing;

import main.java.core.DataScienceModuleHandler;
import main.java.modules.Module;

import java.util.List;

/**
 * Created by Andrea on 17/10/2017.
 */
public class PreprocessingModule extends Module {

    public PreprocessingModule(DataScienceModuleHandler handler, String moduleName) {
        super(handler, moduleName);
    }

    @Override
    public List<String> reply(String userInput) {
        return null;
    }

    @Override
    public String getModuleDescription() {
        return "With this module you can apply transformation to the datasets";
    }

    @Override
    public String makeRecommendation() {
        return null;
    }

    @Override
    public void loadModuleInstance() {

    }

    @Override
    public void saveModuleInstance() {

    }

    @Override
    public void resetModuleInstance() {

    }

    @Override
    public void resetConversation() {

    }
}
