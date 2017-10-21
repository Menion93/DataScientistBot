package main.java.thirdParty.LFEnets;

import java.util.Arrays;

/**
 * Created by Andrea on 26/09/2017.
 */
public class MathHelper {

    public static double maxValue(double array[]){
        double max = Arrays.stream(array).max().getAsDouble();
        return max;
    }

    public static double minValue(double array[]){
        double min = Arrays.stream(array).min().getAsDouble();
        return min;
    }

    public double clip(double value, double min, double max){
        if(value < min) return min;
        if(value > max) return max;
        else return value;
    }

    public static void inPlaceTranspose(double [][] matrix){

        int rows = matrix.length;
        int cols = matrix[0].length;

        for(int i=0;i<rows;i++){
            for(int j=i+1;j<cols;j++){
                matrix[i][j] = matrix[i][j] + matrix[j][i];
                matrix[j][i] = matrix[i][j] - matrix[j][i];
                matrix[i][j] = matrix[i][j] - matrix[j][i];
            }
        }
    }

    public double[][] transpose (double[][] array) {
        if (array == null || array.length == 0)//empty or unset array, nothing do to here
            return array;

        int width = array.length;
        int height = array[0].length;

        double[][] array_new = new double[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                array_new[y][x] = array[x][y];
            }
        }
        return array_new;
    }

    public static int copyArrayFromTo(double[] from, double[] to, int index){
        int i = 0;

        for(i=0; i < from.length && index+i < to.length; i++ ){
            to[index+i] = from[i];
        }

        return i+index;
    }
}
