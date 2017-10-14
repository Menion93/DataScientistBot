package main.java.Dataset;

import java.util.List;
import java.util.*;

/**
 * Created by Andrea on 12/10/2017.
 */
public class Dataset {

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    private String datasetName;
    private List<String> schema;
    private Map<String,List<String>> categorical;
    private Map<String,List<Double>> numerical;

    public Dataset(String datasetName){
        this.datasetName = datasetName;
    }

    public void loadDataset(){
        // Load the dataset
        categorical = new HashMap<>();
        numerical = new HashMap<>();

        // Infer the dataset type per column

        // Add the column in the map
    }

    Double[][] getNumericalAttributes(){
        if(numerical == null)
            loadDataset();

        Double[][] nattr = new Double[numerical.size()][];

        int i = 0;

        for(Map.Entry entry : numerical.entrySet()){
            List<Double> col = (List<Double>) entry.getValue();
            nattr[i] = col.toArray(new Double[1]);
        }

        return nattr;
    }

    String[][] getCategoricalAttributes(){
        if(categorical == null)
            loadDataset();

        String[][] cattr = new String[categorical.size()][];

        int i = 0;

        for(Map.Entry entry : numerical.entrySet()){
            List<String> col = (List<String>) entry.getValue();
            cattr[i] = col.toArray(new String[1]);
        }

        return cattr;
    }
}
