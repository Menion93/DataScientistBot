package main.java.modules.MLModule;

/**
 * Created by Andrea on 22/10/2017.
 */
public abstract class Evaluation {

    protected String evaluationName;
    protected double evaluationScore;

    public Evaluation(){}

    public abstract void evaluateModel(Model model);
    public abstract String printEvaluation();

    public String getEvaluationName() {
        return evaluationName;
    }

    public double getEvaluationScore() {
        return evaluationScore;
    }
}
