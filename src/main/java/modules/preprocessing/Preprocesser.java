package main.java.modules.preprocessing;

import main.java.dataset.Dataset;
import main.java.thirdParty.LFEnets.transformation.Transformations;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andrea on 22/10/2017.
 */
public class Preprocesser {

    Transformations transformer = new Transformations();
    List<String> allTransformations;

    public Preprocesser(){
        transformer = new Transformations();
        allTransformations = new LinkedList<>();
        allTransformations.add("fillna");
        allTransformations.add("delete-column");
        allTransformations.add("sqrt");
        allTransformations.add("freq");
        allTransformations.add("log");
        allTransformations.add("tanh");
        allTransformations.add("normalize");
        allTransformations.add("sigmoid");
        allTransformations.add("square");
    }

    public boolean deleteColumn(Dataset ds, String datasetColumm){
        if(ds.getNumericalAttributes().containsKey(datasetColumm))
            ds.getNumericalAttributes().remove(datasetColumm);
        else if(ds.getCategoricalAttributes().containsKey(datasetColumm))
            ds.getCategoricalAttributes().remove(datasetColumm);
        else
            return false;
        return true;
    }

    public boolean fillNa(Dataset ds, String datasetColumn){
        return fillNumericNa(ds, datasetColumn) || fillCategoricalNa(ds, datasetColumn);
    }

    private boolean fillCategoricalNa(Dataset ds, String datasetColumn) {
        List<String> categorical = ds.getCategoricalAttributes().get(datasetColumn);

        if(categorical == null) return false;

        Map<String, Long> result = categorical.stream().collect(Collectors.groupingBy(d->d, Collectors.counting()));
        Optional<String> mostRelevantAttr = result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .filter(d -> !d.equals("MISSING") )
                .findFirst();

        String mra = mostRelevantAttr.get();

        if(mra == null) return false;

        Collections.replaceAll(categorical, "MISSING", mra);

        return true;
    }

    private boolean fillNumericNa(Dataset ds, String datasetColumn) {
        List<Double> numerical = ds.getNumericalAttributes().get(datasetColumn);

        if(numerical == null) return false;

        double[] numericalFiltered = numerical.stream().filter(d -> !Double.isInfinite(d)).mapToDouble(d->d).toArray();
        double mean = transformer.mean(numericalFiltered);

        Collections.replaceAll(numerical, Double.POSITIVE_INFINITY, mean);

        return true;
    }

    public boolean applyTransformationAndSave(Dataset ds, String attribute, String transformation) {
        // Apply the transformation
        if(transformation.equals("fillna"))
            return fillNa(ds, attribute);

        if(transformation.equals("drop-column"))
            return deleteColumn(ds, attribute);

        double[] column = transform(attribute, transformation, ds);
        ds.addNumericalAttribute(attribute+"_"+transformation, column);

        return true;
    }

    private double[] transform(String attr, String transformation, Dataset ds){

        List<Double> col = ds.getNumericalAttributes().get(attr);
        double[] colArr = col.stream().mapToDouble(d -> d).toArray();

        Transformations transformer = new Transformations();

        if(transformation.equals("sqrt"))
            return transformer.sqrt(colArr);
        if(transformation.equals("freq"))
            return transformer.freq(colArr);
        if(transformation.equals("log"))
            return transformer.log(colArr);
        if(transformation.equals("tanh"))
            return transformer.tanh(colArr);
        if(transformation.equals("normalize"))
            return transformer.normalize(colArr);
        if(transformation.equals("sigmoid"))
            return transformer.sigmoid(colArr);
        if(transformation.equals("square"))
            return transformer.square(colArr);

        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("List of possible operations:\n");
        sb.append("Column operation:\n");
        sb.append("\tsqrt/\n\tfreq\n\tlog\n\ttanh\n\tnormalize\n\tsigmoid\n\tsquare\n");
        sb.append("\tfillna\n\tdrop-column\n");
        return sb.toString();
    }

    public boolean hasTransformation(String transformation) {
        return allTransformations.contains(transformation);
    }
}
