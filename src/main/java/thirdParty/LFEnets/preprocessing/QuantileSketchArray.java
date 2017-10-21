package main.java.thirdParty.LFEnets.preprocessing;

import main.java.thirdParty.LFEnets.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;

/**
 * Created by Andrea on 25/09/2017.
 */
public class QuantileSketchArray {

    ArrayList<double[]> qSAs;

    int minScale;
    int maxScale;
    int bins;

    public QuantileSketchArray(int bins, int minScale, int maxScale, double[] column, double[] arrClass, double[] labels){
        this.qSAs = new ArrayList<>();

        for(int i=0; i< labels.length; i++){
            qSAs.add(new double[bins]);
        }

        this.bins = bins;
        this.minScale=minScale;
        this.maxScale = maxScale;
        generateQSAs(column, arrClass, labels);
    }

    private void generateQSAs(double[] column, double[] targets, double[] labels){

        MathHelper mh = new MathHelper();

        double max = mh.maxValue(column);
        double min = mh.minValue(column);
        double binWidth = (max-min) / bins;

        if(binWidth == 0)
            binWidth = 1;

        // Generate the QSAs for every class
        for(int i=0; i<column.length; i++){
            int binIndex = (int) (column[i]-min / binWidth);
            binIndex = (int) mh.clip(binIndex, 0, bins-1);
            int indexOfClass = ArrayUtils.indexOf(labels, targets[i]);
            qSAs.get(indexOfClass)[binIndex]++;
        }

        // Normalize the values across all qSAs
        for(double[] qsa : qSAs){
            double qsamax = mh.maxValue(qsa);
            double qsamin = mh.minValue(qsa);
            double qsarange = qsamax-qsamin;

            if(qsarange == 0)
                qsarange = 1;

            for(int i=0; i<qsa.length;i++){
                qsa[i] = ((qsa[i] - qsamin) / qsarange) * (maxScale-minScale) + minScale;
            }

        }

    }

    public double[] getQSA(int i){
        return qSAs.get(i);
    }

}
