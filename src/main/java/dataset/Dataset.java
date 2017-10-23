package main.java.dataset;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

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

    public Double[][] getNumericalAsDouble(){
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

    public String[][] getCategoricalAsString(){
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

    public Map<String, List<String>> getCategoricalAttributes(){
        return categorical;
    }

    public Map<String, List<Double>> getNumericalAttributes(){
        return numerical;
    }

    public List<String> getSchema(){
        return schema;
    }

    @Override
    public String toString(){
        return "Name of the dataset:\t" + this.getDatasetName() + "\n" +
                "Schema:\t" + this.schema.toString();
    }

    public void addNumericalAttribute(String attr, double[] column) {
        this.numerical.put(attr, Arrays.stream(column).mapToObj(d -> d).collect(Collectors.toList()));
    }

    public void addCategoticalAttribute(String attr, String[] column) {
        this.categorical.put(attr, Arrays.asList(column));
    }

    public boolean hasColumn(String column) {
        return categorical.containsKey(column) || numerical.containsKey(column);
    }

}
