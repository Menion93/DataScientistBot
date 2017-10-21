package  main.java.thirdParty.acsdb.tools.synonymFinder;

import java.util.Comparator;

/**
 * Created by Andrea on 14/08/2017.
 */
public class SynPairsComparator implements Comparator<SynPairs> {

    @Override
    public int compare(SynPairs o1, SynPairs o2) {

        if((o1.getS1().equals(o2.getS1()) && o1.getS2().equals(o2.getS2())) ||
           (o1.getS1().equals(o2.getS2()) && o1.getS2().equals(o2.getS1())))
            return 0;
        if(o1.getScore()<o2.getScore())
            return 1;
        if(o1.getScore()>o2.getScore())
            return -1;
        return 1;
    }
}
