package main.java.commands;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import main.java.core.DataScienceModuleHandler;

import java.util.List;
import java.util.Properties;


/**
 * Created by Andrea on 09/10/2017.
 */
public abstract class Command {

    protected DataScienceModuleHandler handler;
    protected List<CoreMap> sentences;

    public Command(DataScienceModuleHandler handler){
        this.handler = handler;
    }

    public abstract boolean commandIsRequested(String userInput);

    public void parseUserInput(String userInput){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(userInput);

        // run all Annotators on this text
        pipeline.annotate(document);

        sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
    }

    public boolean finishedTalking(){
        return true;
    }

    public boolean checkKeywordsInText(String[] keywords, String text){

        String[] tokens = text.split(" ");

        boolean allContained = false;

        for(String keyword : keywords){
            String[] compoundKey = keyword.split(" ");
            int compoundKeyIndex = 0;

            // Check if a compound keyword is inside the text in the same order
            for(String token : tokens){
                if(compoundKeyIndex < compoundKey.length && compoundKey[compoundKeyIndex].equals(token)){
                    compoundKeyIndex++;
                    if(compoundKeyIndex == compoundKey.length)
                        allContained = true;
                }
            }
            // No need to check other options
            if(allContained)
                break;

        }

        return allContained;
    }

    public abstract String getBasicCommand();
    public List<String> reply(String userInput){
        return null;
    }
    public abstract List<String> handleCommand();
}
