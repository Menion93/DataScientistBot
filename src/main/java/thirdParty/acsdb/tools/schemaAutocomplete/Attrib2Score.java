package  main.java.thirdParty.acsdb.tools.schemaAutocomplete;

/**
 * Created by Andrea on 21/08/2017.
 */
public class Attrib2Score implements Comparable<Attrib2Score> {

    private String attrib;
    private float score;

    public Attrib2Score(String attrib, float score){
        this.attrib = attrib;
        this.score = score;
    }

    public String getAttrib() {
        return attrib;
    }


    public float getScore() {
        return score;
    }

    @Override
    public int compareTo(Attrib2Score o) {
        Float o1f = new Float(this.getScore());
        Float o2f = new Float(o.getScore());

        return o2f.compareTo(o1f);
    }
}