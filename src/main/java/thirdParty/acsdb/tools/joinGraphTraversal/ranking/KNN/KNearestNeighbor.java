package main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN;

import java.util.List;
import java.util.TreeSet;
import main.java.thirdParty.acsdb.model.Schema;
import main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering.AcsdbPoint;
import main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering.DistanceFunction;

/**
 * Created by Andrea on 02/08/2017.
 */
public class KNearestNeighbor {

    private int k;

    public KNearestNeighbor(int k){
        this.k = k;
    }


    public TreeSet<PointComparable> getNearestKNeighbor(List<AcsdbPoint> points, Schema selectedSchema, DistanceFunction df){

        System.out.println("Now evaluating " + points.size() + " points");

        AcsdbPoint selectedPoint = new AcsdbPoint();
        selectedPoint.setSchema(selectedSchema);

        TreeSet<PointComparable> topk = new TreeSet<>(new AcsdbComparator());

        for(AcsdbPoint point : points){
            if(topk.size() > k){
                float currentScore = df.getDistance(selectedPoint, point);
                PointComparable pc = topk.last();

                if(pc.getScore() < currentScore){
                    topk.remove(pc);
                    topk.add(new PointComparable(point, currentScore));
                }
            }
            else
                topk.add(new PointComparable(point, df.getDistance(selectedPoint, point)));
        }

        return topk;

    }
}
