package main.java.dataset;

import main.java.database.DBRepository;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andrea on 12/10/2017.
 */
public class Dataset {

    private String datasetName; // This is the id of the dataset
    private String root; // This is the name of the first node in the ds versioning graph
    private String from; // This is name of the dataset that generated this one
    private boolean newDataset; // This checks if its a new dataset or not
    private List<String> schema;
    private Map<String,List<String>> categorical;
    private Map<String,List<Double>> numerical;

    private DBRepository repository;

    public Dataset(String datasetName, String root, String from, boolean isnew, DBRepository repo){
        this.datasetName = datasetName;
        this.root = root;
        this.from = from;
        this.newDataset = isnew;
        this.repository = repo;
        categorical = new HashMap<>();
        numerical = new HashMap<>();
    }

    public Dataset(DBRepository repo){
        this.repository = repo;
        newDataset = true;
        categorical = new HashMap<>();
        numerical = new HashMap<>();
    }


    public Double[][] getNumericalAsDouble(){
        if(numerical == null)
            repository.loadData(this);

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
            repository.loadData(this);

        String[][] cattr = new String[categorical.size()][];

        int i = 0;

        for(Map.Entry entry : numerical.entrySet()){
            List<String> col = (List<String>) entry.getValue();
            cattr[i] = col.toArray(new String[1]);
        }

        return cattr;
    }

    public Map<String, List<String>> getCategoricalAttributes(){
        if(categorical == null)
            repository.loadData(this);
        return categorical;
    }

    public List<String> getCategoricalAttribute(String attr){
        if(categorical == null)
            repository.loadData(this);
        List<String> values = categorical.get(attr);
        List<String> copy = new LinkedList<>();
        for(String sCopy : values)
            copy.add(sCopy);

        return copy;
    }

    public List<Double> getNumericalAttribute(String attr){
        if(numerical == null)
            repository.loadData(this);
        List<Double> values = numerical.get(attr);
        List<Double> copy = new LinkedList<>();
        for(Double sCopy : values)
            copy.add(sCopy);

        return copy;
    }

    public Map<String, List<Double>> getNumericalAttributes(){
        if(numerical == null)
            repository.loadData(this);
        return numerical;
    }

    public List<String> getSchema(){
        if(schema == null)
            repository.loadData(this);

        return schema;
    }

    @Override
    public String toString(){
        return "Name of the dataset:\t" + this.getDatasetName() + "\n" +
                "Schema:\t" + this.schema.toString();
    }

    public void addNumericalAttribute(String attr, double[] column) {
        if(numerical == null)
            repository.loadData(this);
        this.numerical.put(attr, Arrays.stream(column).mapToObj(d -> d).collect(Collectors.toList()));
        this.schema.add(attr);
    }

    public void addCategoticalAttribute(String attr, String[] column) {
        if(categorical == null)
            repository.loadData(this);
        this.categorical.put(attr, Arrays.asList(column));
        this.schema.add(attr);
    }

    public void addNumericalAttribute(String attr, List<Double> column) {
        if(numerical == null)
            repository.loadData(this);
        this.numerical.put(attr, column);
        this.schema.add(attr);
    }

    public void addCategoticalAttribute(String attr, List<String> column) {
        if(categorical == null)
            repository.loadData(this);
        this.categorical.put(attr, column);
        this.schema.add(attr);
    }

    public boolean hasColumn(String column) {
        if(categorical == null)
            repository.loadData(this);
        return categorical.containsKey(column) || numerical.containsKey(column);
    }

    public void copyDatasetData(Dataset ds) {
        categorical = new HashMap<>();
        for(String key : ds.getCategoricalAttributes().keySet())
            this.categorical.put(key, ds.getCategoricalAttribute(key));
        numerical = new HashMap<>();
        for(String key : ds.getNumericalAttributes().keySet())
            this.numerical.put(key, ds.getNumericalAttribute(key));

        this.schema = new LinkedList<>();

        for(String attr : ds.getSchema()){
            this.schema.add(attr);
        }
    }

    public boolean loadFromFS(String path) {
        DatasetLoader dsLoader = new DatasetLoader();
        return dsLoader.loadFromFS(path, this);
    }

    public String getDatasetName() {
        return datasetName;
    }
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
    public void save() {
        repository.saveDataset(this);
    }
    public String getRoot() {
        return root;
    }
    public Object getFrom() {
        return from;
    }
    public boolean isNew() {
        return newDataset;
    }
    public void isNew(boolean isNew){ this.newDataset = isNew; }
    public void setCategorical(Map<String, List<String>> categorical) {
        this.categorical = categorical;
    }
    public void setNumerical(Map<String, List<Double>> numerical) {
        this.numerical = numerical;
    }
    public void setRoot(String root) {
        this.root = root;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public void setSchema(List<String> schema) {
        this.schema = schema;
    }
}
