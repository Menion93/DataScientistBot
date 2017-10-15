package main.java.modules.LFEModule;

import main.java.Dataset.Dataset;
import main.java.core.DataScienceModuleHandler;
import main.java.database.DBRepository;
import main.java.modules.JoinGraphTraversalModule.JGTModule;
import main.java.modules.Module;
import main.java.modules.ThirdParty.ThirdPartyDummy;
import main.java.utils.Helper;

import java.io.StringReader;
import java.util.*;
import java.util.List;

/**
 * Created by Andrea on 07/10/2017.
 */
public class LFEModule extends Module {

    private enum STEPS {PRINT_DATASET, SELECT_DATASET, CONFIRM_TRANSFORMATION};
    private Map<String, Map<Integer, String>> dataset2transfor;
    private int stepIndex;

    public LFEModule(DataScienceModuleHandler handler) {
        super(handler, "LFE");
    }

    @Override
    public List<String> reply(String userInput) {

        if(handler.getSession().getDatasetPool().size() == 0)
            return Arrays.asList("You have to add a dataset in the pool first!");

        STEPS currentStep = STEPS.values()[stepIndex];

        switch (currentStep){

            case PRINT_DATASET: {
                stepIndex++;
                List<String> replies = new LinkedList<>();
                replies.add("Choose the dataset you want to try the analysis");
                replies.add(printDatasetList());
                return replies;
            }
            case SELECT_DATASET: {
                // Extract and validate the name,
                String datasetName = extractAndValidateDatasetName(userInput);

                if(datasetName != null){
                    Map<Integer, String> analysis = dataset2transfor.get(datasetName);
                    String result;

                    if(analysis == null){
                        ThirdPartyDummy tpd = new ThirdPartyDummy();
                        Dataset ds = handler.getSession().getDatasetByName(datasetName);
                        Map<Integer, String> anResult = tpd.getLFEAnalysis(ds);
                        dataset2transfor.put(datasetName, anResult);
                        result =  printResult(anResult);
                    }
                    else{
                        result = printResult(analysis);
                    }

                    List<String> replies = new LinkedList<>();
                    replies.add(result);
                    replies.add("Would you like to apply those transformation?");
                    stepIndex++;
                    return replies;
                }
                return Arrays.asList("I could not find the dataset, can you rewrite it?");
            }
            case CONFIRM_TRANSFORMATION: {
                if(userInput.contains("yes")){
                    ThirdPartyDummy dummy = new ThirdPartyDummy();
                    String newName = dummy.applyTransformationAndSave();
                    handler.getSession().getDatasetPool().add(new Dataset(newName));
                    stepIndex -= 2;
                    return this.reply(null);
                }
                stepIndex = 0;
                return this.reply(null);
            }

            default:
                return Arrays.asList("Can you repeat please?");
        }
    }

    private String printDatasetList() {
        StringBuilder sb = new StringBuilder();

        for(Dataset ds : handler.getSession().getDatasetPool()){
            sb.append("\t");
            sb.append(ds.getDatasetName());
        }

        return sb.toString();
    }

    private String printResult(Map<Integer, String> lfeAnalysis) {

        StringBuilder sb = new StringBuilder();

        for(Map.Entry entry : lfeAnalysis.entrySet()){
            sb.append(entry.getKey());
            sb.append("\t");
            sb.append(entry.getValue());
        }

        return sb.toString();
    }

    private String extractAndValidateDatasetName(String userInput) {
        Dataset ds = handler.getSession().getDatasetByName(userInput);

        if(ds != null)
            return ds.getDatasetName();

        return null;
    }


    @Override
    public String getModuleDescription() {
        return "I can help you building a new feature for your dataset, given the latter.";
    }

    @Override
    public String makeRecommendation() {
        return "I've got nothing to recommend to you at the moment";
    }

    @Override
    public void loadModuleInstance() {
        if(!firstTime){
            DBRepository repository = handler.getRepository();
            dataset2transfor = repository.getLFEAnalysis();
        }
    }

    @Override
    public void saveModuleInstance() {
        DBRepository repository = handler.getRepository();
        repository.saveLFEAnalysis(dataset2transfor);
    }
}
