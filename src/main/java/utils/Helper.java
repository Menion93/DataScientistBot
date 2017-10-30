package main.java.utils;

import main.java.dataset.Dataset;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by Andrea on 10/10/2017.
 */
public class Helper {

    public static String selectRandomString(String[] sentences){
        Random rnd = new Random();
        int index = rnd.nextInt(sentences.length);
        return sentences[index];
    }

    public static String getLexicographicalOrder(List<String> strings){
        strings.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();
        for(String s : strings){
            sb.append(s);
            sb.append("-");
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

    public Instances getInstancesFromDataset(Dataset dataset, String target){
        List<String> schema = dataset.getSchema();
        List<String> types = dataset.getTypes();

        int numOfFeatures = schema.size();
        FastVector attributes = new FastVector(numOfFeatures);
        int indexOfClass = schema.indexOf(target);

        // Create the attributes
        for(int i=0; i<schema.size(); i++){
            if(types.get(i).equals("numerical")){
                attributes.addElement(new Attribute(schema.get(i), 1));
            }
            else{
                String[] uniqueVal = getUniqueVal(dataset.getCategoricalAttributes().get(schema.get(i)));
                FastVector fvNominalVal = new FastVector(uniqueVal.length);
                for(String val : uniqueVal)
                    fvNominalVal.addElement(val);
                attributes.addElement(new Attribute(schema.get(i), fvNominalVal));
            }
        }

        Instances instances = new Instances("Dataset", attributes, 10);
        instances.setClassIndex(indexOfClass);

        Map<String, List<Double>> numericalAttr = dataset.getNumericalAttributes();
        Map<String, List<String>> categoricalAttr = dataset.getCategoricalAttributes();

        int datasetSize = 0;

        // Find the number of instances
        if(numericalAttr.values().iterator().hasNext())
            datasetSize = numericalAttr.values().iterator().next().size();
        else
            datasetSize = categoricalAttr.values().iterator().next().size();

        // Now populate the dataset
        for(int i=0; i<datasetSize; i++){
            Instance instance = new Instance(attributes.size());
            for(int j=0; j<attributes.size(); j++){
                if(types.get(j).equals("numerical")){
                    instance.setValue((Attribute)attributes.elementAt(j),
                            numericalAttr.get(schema.get(j)).get(i));

                }
                else{
                    instance.setValue((Attribute)attributes.elementAt(j),
                            categoricalAttr.get(schema.get(j)).get(i));
                }

            }
            // add the instance
            instances.add(instance);
        }

        return instances;
    }

    public Instances getCategoricalInstancesFromDataset(Dataset dataset, String target){
        List<String> schema = dataset.getSchema();
        List<String> types = dataset.getTypes();

        Map<String, List<String>> categoricalAttr = dataset.getCategoricalAttributes();
        int numOfFeatures = categoricalAttr.size();

        FastVector attributes = new FastVector(numOfFeatures);
        int indexOfClass = schema.indexOf(target);

        // Create the attributes
        for(Map.Entry<String, List<String>> entry : categoricalAttr.entrySet()){
            String[] uniqueVal = getUniqueVal(entry.getValue());
            FastVector fvNominalVal = new FastVector(uniqueVal.length);
            for(String val : uniqueVal)
                fvNominalVal.addElement(val);
            attributes.addElement(new Attribute(entry.getKey(), fvNominalVal));

        }

        Instances instances = new Instances("Dataset", attributes, 10);
        instances.setClassIndex(indexOfClass);

        // Find the number of instances
        int datasetSize = categoricalAttr.values().iterator().next().size();

        // Now populate the dataset
        for(int i=0; i<datasetSize; i++){
            Instance instance = new Instance(attributes.size());

            int k = 0;
            for(int j=0; j<schema.size(); j++){
                if(types.get(j).equals("categorical")){
                    instance.setValue((Attribute)attributes.elementAt(k),
                        categoricalAttr.get(schema.get(j)).get(i));
                    k++;
                }
            }
            // add the instance
            instances.add(instance);
        }

        return instances;
    }

    public Instances getNumericalInstancesFromDataset(Dataset dataset, String target){
        List<String> schema = dataset.getSchema();
        List<String> types = dataset.getTypes();

        Map<String, List<Double>> numericalAttr = dataset.getNumericalAttributes();
        int numOfFeatures = numericalAttr.size();

        FastVector attributes = new FastVector(numOfFeatures);
        int indexOfClass = schema.indexOf(target);

        // Create the attributes
        for(Map.Entry<String, List<Double>> entry : numericalAttr.entrySet())
            attributes.addElement(new Attribute(entry.getKey()));

        Instances instances = new Instances("Dataset", attributes, 10);
        instances.setClassIndex(indexOfClass);

        // Find the number of instances
        int datasetSize = numericalAttr.values().iterator().next().size();

        // Now populate the dataset
        for(int i=0; i<datasetSize; i++){
            Instance instance = new Instance(attributes.size());

            int k = 0;
            for(int j=0; j<schema.size(); j++){
                if(types.get(j).equals("numerical")){
                    instance.setValue((Attribute)attributes.elementAt(k),
                         numericalAttr.get(schema.get(j)).get(i));
                    k++;
                }
            }
            // add the instance
            instances.add(instance);
        }

        return instances;
    }

    public String[] getUniqueVal(List<String> values){
        Set<String> mySet = new HashSet<>();
        mySet.addAll(values);
        return mySet.toArray(new String[0]);
    }



}
