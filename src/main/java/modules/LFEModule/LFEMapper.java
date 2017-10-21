package main.java.modules.LFEModule;

import main.java.dataset.Dataset;
import main.java.thirdParty.LFEnets.NeuralNets.FeatureRecom;
import main.java.thirdParty.LFEnets.NeuralNetsTools;
import main.java.thirdParty.LFEnets.transformation.Transformations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LFEMapper{
	
	private FeatureRecom fr;

	
	public LFEMapper(){
		fr = NeuralNetsTools.getInstance().getFeatureRecomTool();
	}
	
    public Map<Integer, String> getLFEAnalysis(Dataset ds, String tLabel){
    	
    	Map<String, List<Double>> data = ds.getNumericalAttributes();
    	
    	int dataSize = data.get(tLabel).size();
    	int numberOfColumn = data.size();
    	
    	double[][] dataWOLabel = new double[dataSize][numberOfColumn-1];
    	double[] targets = new double[dataSize];
    	
    	int i = 0;
    	int offset = 0;
    	
    	for(Map.Entry<String, List<Double>> entry : data.entrySet()){
    		if(!entry.getKey().equals(tLabel)){
    			List<Double> column = entry.getValue();
    			dataWOLabel[i] = column.stream().mapToDouble(d -> d).toArray();
    			i++;
    		}
    		else{
    			List<Double> column = entry.getValue();
    			offset = i;
  				targets = column.stream().mapToDouble(d -> d).toArray();
    		}
    	}
    	
    	Map<Integer, String> result = fr.getRecommendation(dataWOLabel, targets);
    	Map<Integer, String> copyResult = new HashMap<>();
    	
    	// Readjust the offset
    	for(Map.Entry<Integer, String> entry : result.entrySet())
    		if(entry.getKey() >= offset)
    			copyResult.put(entry.getKey()+1, entry.getValue());
    		else
    			copyResult.put(entry.getKey(), entry.getValue());
    	

        return copyResult;
    }

    public void applyTransformationAndSave(Dataset ds, Map<Integer, String> anResult, String datasetName) {
    	// Apply the transformation
    	Dataset newDataset = new Dataset(datasetName);
    	newDataset.copy(ds);
    	
    	for(Map.Entry<Integer, String> entry : anResult.entrySet()){
    		double[] column = transform(entry.getKey(), entry.getValue(), ds);
    		String attrName = ds.getSchema().get(entry.getKey());
    		newDataset.addNumericalAttribute(attrName + "-" + entry.getValue(), column);
    		
    	}
    }
    
    private double[] transform(int attrIndex, String transformation, Dataset ds){
    	
    	List<Double> col = ds.getNumericalAttributes().get(ds.getSchema().get(attrIndex));
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
	
}