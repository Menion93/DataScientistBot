package main.java.modules.MLModule;

import main.java.dataset.Dataset;
import org.deeplearning4j.eval.meta.Prediction;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * Created by Andrea on 22/10/2017.
 */
public abstract class Model {

    protected Map<String, Evaluation> evaluationMap;

    public Set<String> getEvaluationList(){
        return evaluationMap.keySet();
    }

    public abstract boolean hasEvaluation(String evaluation);
    public abstract Evaluation evaluateModel(String evaluation, Dataset currentDataset, String currentTarget);
}
