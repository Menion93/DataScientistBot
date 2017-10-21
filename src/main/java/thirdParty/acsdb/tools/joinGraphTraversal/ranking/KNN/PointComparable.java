package main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN;

import main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering.AcsdbPoint;

/**
 * Created by Andrea on 03/08/2017.
 */
public class PointComparable {

    private AcsdbPoint point;
    private float score;

    public PointComparable(AcsdbPoint point, float score){
        this.point = point;
        this.score = score;
    }

    public String printSchema(){
        return point.getSchema().toString();
    }

    public AcsdbPoint getPoint() {
        return point;
    }

    public float getScore() {
        return score;
    }
}
