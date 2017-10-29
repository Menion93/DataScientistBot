package main.java.modules.MLModule;

/**
 * Created by Andrea on 22/10/2017.
 */
public class Evaluation {

    protected String evaluationName;
    protected double evaluationScore;
    protected String datasetName;
    protected String modelName;

    public Evaluation(){}

    public String printEvaluation(){
        return "Name of the dataset\n\t" + datasetName +
                "Name of the algorithm\n\t" + modelName +
                "Name of the evaluation\n\t" + evaluationName +
                "Score Value\n\t" + evaluationScore;
    }

    public String getEvaluationName() {
        return evaluationName;
    }

    public void setEvaluationName(String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public double getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(double evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
