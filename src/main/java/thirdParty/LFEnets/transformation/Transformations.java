package main.java.thirdParty.LFEnets.transformation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 18/10/2017.
 */
public class Transformations {


    public double[] sqrt(double[] col){
        double[] result = new double[col.length];

        for(int i=0; i<col.length; i++)
            result[i] = Math.sqrt(col[i]);

        return result;
    }

    public double[] tanh(double[] col){
        double[] result = new double[col.length];

        for(int i=0; i<col.length; i++)
            result[i] = Math.tanh(col[i]);

        return result;
    }

    public double[] log(double[] col){
        double[] result = new double[col.length];

        for(int i=0; i<col.length; i++)
            result[i] = Math.log(col[i]);

        return result;
    }

    public double[] freq(double[] col){
        Map<Double, Double> num2Freq = new HashMap<>();

        for(double x : col){
            if(!num2Freq.containsKey(x)){
                num2Freq.put(x, 0.d);
            }
            num2Freq.put(x, num2Freq.get(x));
        }

        double[] result = new double[col.length];

        for(int i=0; i<col.length; i++)
            result[i] = num2Freq.get(col[i]);

        return result;
    }

    public double[] normalize(double[] col){
        double max = Arrays.stream(col).max().getAsDouble();
        double min = Arrays.stream(col).min().getAsDouble();
        double[] result = new double[col.length];

        for(int i = 0; i<col.length; i++){
            if(max != min)
                result[i] = (col[i]-min)/(max-min) + min;
            else
                result[i] = 0;
        }

        return result;
    }


    public double[] sigmoid(double[] col){
        double[] result = new double[col.length];

        for(int i=0; i<col.length; i++)
            result[i] = sigmoid(col[i]);

        return result;
    }

    private double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }

    public double[] square(double[] col){
        double[] result = new double[col.length];

        for(int i=0; i<col.length; i++)
            result[i] = Math.pow(col[i],2);

        return result;
    }

    public double mean(double[] arr) {
        long total = Arrays.stream(arr).count();
        double sum = Arrays.stream(arr).sum();
        return sum/total;
    }
}
