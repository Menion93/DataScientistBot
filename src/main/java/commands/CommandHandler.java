package main.java.commands;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.list.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 09/10/2017.
 */
public class CommandHandler {

    private List<Command> commandList;

    public CommandHandler(DataScienceModuleHandler handler){

        commandList = new LinkedList<>();

        commandList.add(new BranchCommand(handler));
        commandList.add(new DeleteBranchCommand(handler));
        commandList.add(new ExitBotCommand(handler));
        commandList.add(new LoadBranchCommand(handler));
        commandList.add(new RecommendCommand(handler));
        commandList.add(new HelpCommand(handler));
        commandList.add(new MakeAnalysisCommand(handler));
        commandList.add(new LoadAnalysisCommand(handler));
        commandList.add(new DeleteAnalysisCommand(handler));
        commandList.add(new SwitchModuleCommand(handler));
        commandList.add(new ShowModulesCommand(handler));
        commandList.add(new ResetModuleCommand(handler));
        commandList.add(new ExitModuleCommand(handler));
        commandList.add(new SaveCommand(handler));
        commandList.add(new PrintCurrentSessionCommand(handler));
        commandList.add(new ShowBranchesCommand(handler));
        commandList.add(new ShowAnalysisCommand(handler));
        commandList.add(new ShowDatasetCommand(handler));
        commandList.add(new ShowDsPoolCommand(handler));
        commandList.add(new ImportNewDatasetCommand(handler));
        commandList.add(new LoadDatasetCommand(handler));
        commandList.add(new RepeatCommand(handler));
        commandList.add(new BackCommand(handler));
    }

    public List<Command> getCommandList(){
        return commandList;
    }

    public List<String> matchUserInputToCommand(String userInput){

        for(Command handler : commandList){
            if(handler.commandIsRequested(userInput)){
                return handler.handleCommand();
            }
        }

        return null;
    }

}
