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

    public Map<String, Evaluation> evaluationMap;

    public Set<String> getEvaluationList(){
        return evaluationMap.keySet();
    }

    public abstract void fit(Dataset dataset);
    public abstract List<Prediction> predict(Dataset dataset);
    public abstract String printEvaluation();
}
