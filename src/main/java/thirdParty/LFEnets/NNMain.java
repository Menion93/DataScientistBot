package main.java.thirdParty.LFEnets;


import main.java.thirdParty.LFEnets.preprocessing.QSAPreprocesser;
import org.nd4j.linalg.api.ndarray.INDArray;


/**
 * Created by Andrea on 25/09/2017.
 */
public class NNMain {

    public static void main(String[] args) {

        //FeatureRecom fr = new FeatureRecom();

        QSAPreprocesser preprocesser = new QSAPreprocesser();

        INDArray ds = preprocesser.preprocessForFRNets(new double[][] {{1,2,4,5,4},{3,4,4,5,4},{5,6,4,5,4},{7,8,4,5,4},{9,10,4,5,4},{11,12,4,5,4}}, new double[]{1,0,0,0,0,1});
        System.out.println(ds.toString());

    }
}
