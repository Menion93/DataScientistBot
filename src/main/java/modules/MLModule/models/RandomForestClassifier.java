package main.java.modules.MLModule.models;

import main.java.dataset.Dataset;
import main.java.modules.MLModule.Evaluation;
import main.java.modules.MLModule.Model;
import main.java.utils.Helper;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import java.util.Random;


/**
 * Created by Andrea on 29/10/2017.
 */
public class RandomForestClassifier extends Model {

    int seed = 667;

    @Override
    public Evaluation evaluateModel(String evaluation, Dataset dataset, String currentTarget) {

        if(!hasEvaluation(evaluation))
            return null;

        Evaluation eval = null;

        try{
            Helper helper = new Helper();
            Instances instances = helper.getCategoricalInstancesFromDataset(dataset, currentTarget);

            RandomForest clf = new RandomForest();

            //clf.buildClassifier(instances);
            clf.setNumTrees(100);

            weka.classifiers.Evaluation eTest = new weka.classifiers.Evaluation(instances);
            eTest.crossValidateModel(clf, instances, 5, new Random(seed), new Object[] { });
            eval = new Evaluation();
            eval.setModelName("random_forest");
            eval.setDatasetName(dataset.getDatasetName());
            eval.setEvaluationName("weka_eval_crossVal");
            eval.setEvaluationScore(1-eTest.errorRate());

            return eval;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return eval;
    }
}
