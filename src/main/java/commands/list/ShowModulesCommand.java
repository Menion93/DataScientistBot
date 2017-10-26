package main.java.commands.list;

import main.java.commands.Command;
import main.java.core.DataScienceModuleHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 16/10/2017.
 */
public class ShowModulesCommand extends Command{

    private String[] KEYWORDS = {"show modules", "show module list"};

    public ShowModulesCommand(DataScienceModuleHandler handler) {
        super(handler);
    }

    @Override
    public boolean commandIsRequested(String userInput) {
        return checkKeywordsInText(KEYWORDS, userInput);
    }

    @Override
    public List<String> handleCommand() {
        List<String> moduleNames = handler.getModuleSubscription().getModuleNames();

        StringBuilder sb = new StringBuilder();
        sb.append("List of subscribed modules:");
        sb.append("\n");

        for(String mName : moduleNames){
            sb.append("\t");
            sb.append(mName);
            sb.append("\n");
        }

        return Arrays.asList(sb.toString());
    }
}
