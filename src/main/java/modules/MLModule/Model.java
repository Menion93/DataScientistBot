package main.java.modules.MLModule;

import main.java.dataset.Dataset;
import org.deeplearning4j.eval.meta.Prediction;

import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * Created by Andrea on 22/10/2017.
 */
public abstract class Model {

    protected List<String> evaluationsList;

    public Model(){
        evaluationsList = new LinkedList<>();
    }

    public List<String> getEvaluationList(){
        return evaluationsList;
    }

    public boolean hasEvaluation(String evaluation){
        return evaluationsList.contains(evaluation);
    }

    public void addEvaluation(String name){
        evaluationsList.add(name);
    }
    public abstract Evaluation evaluateModel(String evaluation, Dataset currentDataset, String currentTarget);
}
