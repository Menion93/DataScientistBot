package main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN;

import java.util.Comparator;

/**
 * Created by Andrea on 03/08/2017.
 */
public class AcsdbComparator implements Comparator<PointComparable>{
    @Override
    public int compare(PointComparable o1, PointComparable o2) {
        if(o1.getScore() < o2.getScore())
            return 1;
        else if (o1.getScore() > o2.getScore())
            return -1;
        else if(o1.getPoint().equals(o2.getPoint()))
            return 0;

        return 1;
    }
}
