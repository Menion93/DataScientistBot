package main.java.thirdParty.acsdb.tools.synonymFinder;

/**
 * Created by Andrea on 14/08/2017.
 */
public class SynPairs {

    private String s1;
    private String s2;
    private float score;

    public SynPairs(String s1, String s2, float score){
        this.s1 = s1;
        this.s2 = s2;
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }

    @Override
    public String toString(){
        return s1 + "\t" + s2 + "\t" + score;
    }

}
