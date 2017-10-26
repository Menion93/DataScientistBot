package main.java.commands.list;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.Command;
import main.java.modules.Module;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class SwitchModuleCommand extends Command {

    private String[] KEYWORDS = {"change module", "switch module"};

    private String moduleName;

    public SwitchModuleCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        moduleName = null;
        boolean keyword_detected = checkKeywordsInText(KEYWORDS, userInput);

        // If we did not detect our keywords, discard this intent.
        if(!keyword_detected) return false;
        String[] tokens = userInput.split(" ");

        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("module") && i<tokens.length-1)
                moduleName = tokens[i+1];
        }

        return true;
    }

    @Override
    public List<String> handleCommand() {
        Module newModule = handler.getModuleSubscription().getModuleByName(moduleName);
        if(newModule != null){
            handler.getCurrentModule().resetConversation();
            handler.setCurrentModule(handler.getModuleSubscription().getModuleByName(moduleName));
            return Arrays.asList("Switched to module " + newModule.getModuleName() + "\n" + newModule.reply(""));
        }

        return Arrays.asList("Module name not recognized");
    }
}
