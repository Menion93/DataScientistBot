package  main.java.thirdParty.acsdb.tools.synonymFinder;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;
import  main.java.thirdParty.acsdb.AcsdbRepo;
import  main.java.thirdParty.acsdb.model.AcsdbValue;
import  main.java.thirdParty.acsdb.model.Schema;

/**
 * Created by Andrea on 14/08/2017.
 */
public class SynFinding {

    private AcsdbRepo repo;
    private Map<String, AcsdbValue> oneVal;

    public SynFinding(AcsdbRepo repo) throws FileNotFoundException, UnsupportedEncodingException {
        this.repo = repo;
        oneVal = repo.getAcsdb1Value();
    }

    public Set<SynPairs> findPossibleSynonyms(Schema schema) throws SQLException {

        Map<String, AcsdbValue> contextDb = repo.getContextSchemas(schema, false);
        List<String> attributes = new ArrayList<>(repo.getCommonAttrInContext(schema));
        int true_count = repo.getTrueSchemaCount();

        Set<SynPairs> result = new TreeSet<>(new SynPairsComparator());


        for(int i=0; i<attributes.size(); i++)
            for(int j=0; j<attributes.size();j++){

                if(!attributes.get(i).equals(attributes.get(j)) &&
                   !attributes.get(i).equals("") &&
                   !attributes.get(j).equals("")){

                    float score = syn(attributes.get(i), attributes.get(j),
                            contextDb, attributes, schema, true_count);

                    result.add(new SynPairs(attributes.get(i), attributes.get(j), score ));
                }
            }

        return result;

    }

    private float syn(String s1, String s2, Map<String, AcsdbValue> contextDb, List<String> attributes,
                      Schema context, int true_count) {

        if(context.toList().contains(s1) || context.toList().contains(s2))
            return 0;

        AcsdbValue v1 = oneVal.get(s1);
        AcsdbValue v2 = oneVal.get(s2);

        if(v1 == null || v2 == null)
            return 0;

        float den = 0.0001f;

        for(String attr : attributes){

            if(context.toList().contains(attr) ||
               attr.equals(s1) || attr.equals(s2) ||
               attr.equals(""))
                continue;

            AcsdbValue probDenS1Val = contextDb.get(getSortedHeader(context.toList(), new String[]{s1}));
            AcsdbValue probDenS2Val = contextDb.get(getSortedHeader(context.toList(), new String[]{s2}));
            AcsdbValue probNumS1Val = contextDb.get(getSortedHeader(context.toList(), new String[]{s1, attr}));
            AcsdbValue probNumS2Val = contextDb.get(getSortedHeader(context.toList(), new String[]{s2, attr}));

            AcsdbValue[] factors = {probDenS1Val, probDenS2Val, probNumS1Val, probNumS2Val};

            Float probDenS1 = null, probDenS2= null, probNumS1 = null, probNumS2 = null;

            Float[] numbers = {probDenS1, probDenS2, probNumS1, probNumS2};

            int i = 0;

            while (i<4){
                if(factors[i] != null)
                    numbers[i] = (float) factors[i].getSubHeader();
                else
                    numbers[i] = null;
                i++;
            }

            den += Math.pow(divide(probNumS1, probDenS1) - divide(probNumS2, probDenS2),2);
        }

        return v1.getSubHeader()*v2.getSubHeader()/(den*true_count*true_count);
    }

    private String getSortedHeader(List<String> strings, String[] arr) {

        List<String> copy = new LinkedList<>();

        for(String s : strings)
            copy.add(s);

        copy.addAll(Arrays.asList(arr));
        copy.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();

        for(String s : copy)
            sb.append(s + "-");

        return sb.toString().substring(0, sb.length()-1);
    }

    private float divide(Float num, Float den) {

        if(den == null || den == 0 || num == null)
            return 0;
        return num/den;
    }
}
