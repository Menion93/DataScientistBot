package main.java.dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Andrea on 24/10/2017.
 */
public class DatasetLoader {

    private final String separator = ",";

    public boolean loadFromFS(String path, Dataset dataset) {

        Map<String, List<String>> categorical = new HashMap<>();
        Map<String, List<Double>> numerical = new HashMap<>();
        dataset.setCategorical(categorical);
        dataset.setNumerical(numerical);
        boolean secondLine = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = null;
            List<String> schema = null;
            List<String> types = null;

            while ((line = reader.readLine()) != null) {
                // Datasets always have header as the fist line
                if(schema != null && types == null){
                    types = new LinkedList<>();
                    inferTypes(line, types, schema, categorical, numerical);
                }
                // This is true from the second line onwards
                if(schema != null && types != null){
                    addRowToDataset(line, categorical, numerical, schema, types);
                }
                if(schema == null){
                    schema = new LinkedList<>();
                    parseSchema(line, schema);
                }
                
            }
            dataset.setSchema(schema);

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void inferTypes(String line, List<String> types,
                            List<String> schema,
                            Map<String, List<String>> categorical,
                            Map<String, List<Double>> numerical) {

        String[] values = line.split(separator);

        for(int i=0; i< schema.size(); i++){
            try{
                Double.parseDouble(values[i].trim());
                types.add("numerical");
                numerical.put(schema.get(i), new LinkedList<>());
            }
            catch(Exception e){
                types.add("categorical");
                categorical.put(schema.get(i), new LinkedList<>());
            }
        }
    }

    private void addRowToDataset(String line, Map<String, List<String>> categorical,
                                 Map<String, List<Double>> numerical,
                                 List<String> schema,
                                 List<String> types) {

        String[] values = line.split(separator);

        for(int i=0; i<schema.size(); i++){
            if(types.get(i).equals("numerical"))
                numerical.get(schema.get(i)).add(Double.parseDouble(values[i].trim()));
            else
                categorical.get(schema.get(i)).add(values[i].trim());
        }

    }

    private void parseSchema(String line, List<String> schema) {
        schema.addAll(Arrays.asList(line.split(separator)));
    }

}
