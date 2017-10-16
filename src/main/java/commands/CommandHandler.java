package main.java.commands;

import main.java.core.DataScienceModuleHandler;
import main.java.commands.list.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 09/10/2017.
 */
public class CommandHandler {

    List<Command> handlerList;

    public CommandHandler(DataScienceModuleHandler handler){

        handlerList = new LinkedList<>();

        handlerList.add(new BranchCommand(handler));
        handlerList.add(new DeleteBranchCommand(handler));
        handlerList.add(new ExitBotCommand(handler));
        handlerList.add(new LoadBranchCommand(handler));
        handlerList.add(new RecommendCommand(handler));
        handlerList.add(new HelpCommand(handler));
        handlerList.add(new MakeAnalysisCommand(handler));
        handlerList.add(new LoadAnalysisCommand(handler));
        handlerList.add(new DeleteAnalysisCommand(handler));
        handlerList.add(new SwitchModuleCommand(handler));
        handlerList.add(new ShowModulesCommand(handler));
        handlerList.add(new ResetModuleCommand(handler));
        handlerList.add(new ExitModuleCommand(handler));
        handlerList.add(new SaveCommand(handler));
        handlerList.add(new PrintCurrentSessionCommand(handler));
        handlerList.add(new ShowBranchesCommand(handler));
        handlerList.add(new ShowAnalysisCommand(handler));
    }

    public String matchUserInputToCommand(String userInput){

        for(Command handler : handlerList){
            if(handler.commandIsRequested(userInput)){
                return handler.handleCommand();
            }
        }

        return null;
    }
}
