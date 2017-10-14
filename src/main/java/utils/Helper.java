package main.java.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrea on 10/10/2017.
 */
public class Helper {

    public static String selectRandomString(String[] sentences){
        Random rnd = new Random();
        int index = rnd.nextInt(sentences.length);
        return sentences[index];
    }

    public static String getLexicographicalOrder(List<String> strings){
        strings.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();
        for(String s : strings){
            sb.append(s);
            sb.append("-");
        }
        return sb.toString().substring(0,sb.toString().length()-1);


    }
}
