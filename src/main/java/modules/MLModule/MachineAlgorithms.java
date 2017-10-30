package main.java.modules.MLModule;

import main.java.modules.MLModule.models.LinearRegressionClassifier;
import main.java.modules.MLModule.models.NaiveBayesClassifier;
import main.java.modules.MLModule.models.RandomForestClassifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andrea on 22/10/2017.
 */
public class MachineAlgorithms {

    private Map<String,Model> models;

    public MachineAlgorithms(){
        // Create and add the models
        models = new HashMap<>();

        // RandomForest
        Model randomForest = new RandomForestClassifier();
        randomForest.setModelName("RandomForest");
        randomForest.addEvaluation("weka_eval_crossVal");
        models.put("RandomForest", randomForest);

        // Linear Regression
        Model linearRegression = new LinearRegressionClassifier();
        linearRegression.setModelName("LinearRegression");
        linearRegression.addEvaluation("weka_eval_crossVal");
        models.put("LinearRegression", linearRegression);

        Model naiveBayes = new NaiveBayesClassifier();
        naiveBayes.setModelName("NaiveBayes");
        naiveBayes.addEvaluation("weka_eval_crossVak");
        models.put("NaiveBayes", naiveBayes);

    }

    public Model getModel(String modelName){
        return models.get(modelName);
    }

    public boolean modelExist(String modelName){
        return models.containsKey(modelName);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Set<String> clfNames = models.keySet();

        sb.append("Classifier:");

        for(String clf : clfNames){
            sb.append("\n\t");
            sb.append(clf);
        }
        return sb.toString();
    }
}
