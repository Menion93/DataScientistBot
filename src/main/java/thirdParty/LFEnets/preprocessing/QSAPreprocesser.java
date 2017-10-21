package main.java.thirdParty.LFEnets.preprocessing;

import main.java.thirdParty.LFEnets.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.*;

/**
 * Created by Andrea on 25/09/2017.
 */
public class QSAPreprocesser {

    int binsFR = 10;
    int minScaleFR = -10;
    int maxScaleFR = 10;


    public double[] getUniqueLabels(double[] targets){
        Set<Double> set_classes = new HashSet<>(Arrays.asList(ArrayUtils.toObject(targets)));
        return ArrayUtils.toPrimitive(set_classes.toArray(new Double[] {0.d}));
    }

    private List<QuantileSketchArray> getQSAfromDS(double[][] dataset, double[] targets, double[] labels,
                                                  int minScale, int maxScale, int bins){
        MathHelper mh = new MathHelper();

        double[][] transposedDS = mh.transpose(dataset);

        List<QuantileSketchArray> qSAsList = new ArrayList<>();

        for(int i=0; i<transposedDS.length;i++){
            qSAsList.add(new QuantileSketchArray(bins, minScale, maxScale, transposedDS[i], targets, labels));
        }

        return qSAsList;
    }

    // Create the QSA representation of the dataset
    public INDArray preprocessForFRNets(double[][] dataset, double[] targets){

        MathHelper mh = new MathHelper();

        // Get the QSA representation of the data
        double[] labels = getUniqueLabels(targets);
        List<QuantileSketchArray> qSAsList = getQSAfromDS(dataset, targets, labels, minScaleFR, maxScaleFR, binsFR);
        int numOfExamples = dataset[0].length;

        // Copy the QSAs in a big array
        double[] bigArr = new double[binsFR * labels.length * numOfExamples];
        int bigIndexArr = 0;

        for(QuantileSketchArray qsa : qSAsList){

            for(int labelIndex=0; labelIndex<labels.length;labelIndex++){
                bigIndexArr = mh.copyArrayFromTo(qsa.getQSA(labelIndex), bigArr, bigIndexArr);
            }
        }

        INDArray iNDds = Nd4j.create(bigArr,new int[]{numOfExamples,labels.length,binsFR},'c');

        return iNDds;
    }

}
