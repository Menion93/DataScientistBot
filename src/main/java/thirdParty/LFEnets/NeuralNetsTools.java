package main.java.thirdParty.LFEnets;

import main.java.thirdParty.LFEnets.NeuralNets.FeatureRecom;

/**
 * Created by Andrea on 02/10/2017.
 */
public class NeuralNetsTools {

	private static NeuralNetsTools instance;
	private FeatureRecom fr;
	
	protected NeuralNetsTools(){
		fr = new FeatureRecom();
	}
	
	public static NeuralNetsTools getInstance() {
		if(instance == null){
			instance = new  NeuralNetsTools();
		}
		return instance;
	}
	
	public FeatureRecom getFeatureRecomTool(){
		return fr;
	}
	
	
}
