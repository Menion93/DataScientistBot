package main.java.core;

import main.java.database.DBRepository;
import main.java.database.MongoRepository;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Andrea on 06/10/2017.
 */
public class DataScientistBot {

    // This will become the beginChat method in Kayak
    public static void main(String[] args){

        boolean goodbye = false;

        DBRepository repo = new MongoRepository();
        DataScienceModuleHandler handler = new DataScienceModuleHandler(repo);
        Scanner scanner = new Scanner(System.in);

        // This is the main loop, where we register user input and handle the bot replies
        while(!goodbye){
            String userInput = scanner.nextLine();
            List<String> replies = handler.reply(userInput);

            showReply(replies);

            goodbye = handler.isSayingGoodbye();
        }

    }

    public static void showReply(List<String> replies){
        for(String s : replies)
            System.out.println(s);
    }
}
