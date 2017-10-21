package main.java.thirdParty.LFEnets.NeuralNets;

import main.java.thirdParty.LFEnets.preprocessing.QSAPreprocesser;
import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import java.util.*;
import java.io.IOException;

/**
 * Created by Andrea on 25/09/2017.
 */
public class FeatureRecom {

    Map<String, MultiLayerNetwork> networks;
    QSAPreprocesser preprocesser;

    public String base_path_nn = "F:/Documenti/git/DataScientistBot/src/main/java/thirdParty/LFEnets/neura_network_models";

    String[] transformations = {"sqrt", "freq", "log", "tanh", "normalize", "sigmoid", "square"};

    double threshold = 0.55;

    public FeatureRecom(){
        preprocesser = new QSAPreprocesser();
        networks = new HashMap<>();

        for(String transf : transformations){

            try {
                networks.put(transf,  KerasModelImport.importKerasSequentialModelAndWeights(
                        base_path_nn + "/" + transf + "-net_model",
                        base_path_nn + "/" + transf + "-weights"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKerasConfigurationException e) {
                e.printStackTrace();
            } catch (UnsupportedKerasConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<Integer, String> getRecommendation(double[][] dataset, double[] targets){
        Map<Integer, String> recommendations = new HashMap<>();

        INDArray preprocessedDataset = preprocesser.preprocessForFRNets(dataset, targets);
        double[][] predMat = new double[dataset[0].length][transformations.length];

        for(int i=0; i<transformations.length; i++){
            INDArray transfPred = networks.get(transformations[i]).output(preprocessedDataset);
            for(int j=0; j < predMat.length; j++){
                predMat[j][i] = transfPred.getDouble(j);
            }
        }

        for(int i=0; i<transformations.length; i++){
           double max = Arrays.stream(predMat[i]).max().getAsDouble();

           if(max > threshold){
               int transformationIndex = Arrays.asList(predMat).indexOf(max);
               recommendations.put(i, transformations[transformationIndex]);
           }
        }

        return recommendations;
    }

}
