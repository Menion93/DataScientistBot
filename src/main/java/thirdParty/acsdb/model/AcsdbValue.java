package main.java.thirdParty.acsdb.model;

/**
 * Created by Andrea on 23/07/2017.
 */
public class AcsdbValue {

    private int subHeader;
    private int trueHeader;

    public AcsdbValue(int trueHeader, int subHeader){
        this.subHeader = subHeader;
        this.trueHeader = trueHeader;
    }

    public int getSubHeader() {
        return subHeader;
    }

    public int getTrueHeader(){
        return trueHeader;
    }

}