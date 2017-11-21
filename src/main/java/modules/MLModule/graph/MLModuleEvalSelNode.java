package main.java.modules.MLModule.graph;

import main.java.modules.MLModule.Evaluation;
import main.java.modules.MLModule.MLModule;
import main.java.modules.MLModule.Model;
import main.java.modules.conversational.ConvNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrea on 21/11/2017.
 */
public class MLModuleEvalSelNode extends ConvNode {

    private MLModule module;

    public MLModuleEvalSelNode(String nodeId, MLModule module) {
        super(nodeId);
        this.module = module;
    }

    @Override
    public void onLoad() {
        onLoadMessages = Arrays.asList("Now please select an evaluation method",
                printEvaluationOptions(module.getCurrentModel().getEvaluationList()));
    }

    @Override
    public ConvNode chooseSuccessor(String userInput) {

        String evaluation = userInput;
        Model currentModel = module.getCurrentModel();

        if(!currentModel.hasEvaluation(evaluation)){
            errorMessage =  Arrays.asList("Evaluation with name" + evaluation + " is not valid",
                    "Please select another name");
            return this;
        }

        Evaluation eval = currentModel.evaluateModel(evaluation, module.getCurrentDataset(), module.getCurrentTarget());
        module.setCurrentEvaluation(eval);
        module.getEvaluations().add(eval);
        return getSuccessor("print_result");

    }


    private String printEvaluationOptions(List<String> evaluationList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evaluations:");

        for(String clf : evaluationList){
            sb.append("\n\t");
            sb.append(clf);
        }
        return sb.toString();
    }
}
