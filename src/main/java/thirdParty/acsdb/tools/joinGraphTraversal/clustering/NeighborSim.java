package  main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.TreeSet;
import main.java.thirdParty.acsdb.AcsdbRepo;
import main.java.thirdParty.acsdb.model.AcsdbValue;
import java.util.Map;

/**
 * Created by Andrea on 02/08/2017.
 */
public class NeighborSim implements DistanceFunction{

    private Map<String, AcsdbValue> acsdb3Values;
    private Map<String, AcsdbValue> acsdb2Values;

    private String sharedAttr;
    private int dataSize;

    public NeighborSim(AcsdbRepo repo) throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        acsdb2Values = repo.getAcsdb2Values();
        acsdb3Values = repo.getAcsdb3Values();
    }

    public void setAttrib(String attr){
        sharedAttr = attr;
    }

    public void setDataSize(int size){
        dataSize = size;
    }

    public float getDistance(AcsdbPoint a, AcsdbPoint b){

        float normalizationFactor = a.getSchema().toList().size();

        float probNotNormalized = 0;

        for(String x : a.getSchema().toList())
            for(String y : b.getSchema().toList())
                probNotNormalized += getNNProb(x,y);

        return probNotNormalized/normalizationFactor;
    }

    private float getNNProb(String x, String y){

        if(x.equals(y))
            return 1;

        TreeSet<String> orderedList = new TreeSet<>();
        orderedList.add(x);
        orderedList.add(y);
        orderedList.add(sharedAttr);

        StringBuilder sb = new StringBuilder();
        for(String sorted : orderedList)
            sb.append(sorted + "-");

        String sortedHeader = sb.toString();
        sortedHeader = sortedHeader.substring(0, sortedHeader.length()-1);

        float distance = 0;

        try{
            distance = dataSize * acsdb3Values.get(sortedHeader).getSubHeader() /
                    (acsdb2Values.get(x).getSubHeader() * acsdb2Values.get(y).getSubHeader());
        }
        catch(Exception e){

        }

        return distance;
    }
}
