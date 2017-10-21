package  main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering;

import  main.java.thirdParty.acsdb.model.Schema;

public class AcsdbPoint {

    protected Schema schema;
    protected DistanceFunction df;

    public void setSchema(Schema s){
        schema = s;
    }

    public void setDistanceFunction(DistanceFunction distanceFunction){
        df = distanceFunction;
    }

    public Schema getSchema(){
        return schema;
    }

    public double getNeighborSimilarity(AcsdbPoint datapoint){
        return df.getDistance(this, datapoint);
    }

}