package  main.java.thirdParty.acsdb.tools.schemaAutocomplete;

import java.sql.SQLException;
import java.util.*;
import  main.java.thirdParty.acsdb.AcsdbRepo;
import  main.java.thirdParty.acsdb.model.AcsdbValue;
import  main.java.thirdParty.acsdb.model.Schema;

/**
 * Created by Andrea on 21/08/2017.
 */
public class SchemaAutocomplete{

    private AcsdbRepo repo;

    public SchemaAutocomplete(AcsdbRepo repo) {
        this.repo = repo;
    }

    public List<Attrib2Score> getNextAttributes(Schema schema) throws SQLException {

        Map<String, AcsdbValue> contextSchemas = repo.getContextSchemas(schema, true);
        Map<String, Float> word2Count = new HashMap<>();

        float totalCount = 0;

        System.out.println(totalCount);

        for(Map.Entry<String,AcsdbValue> entry : contextSchemas.entrySet()){
            parseLine(word2Count, entry.getKey(), entry.getValue().getTrueHeader(), schema);
            totalCount += entry.getValue().getTrueHeader();
        }

        List<Attrib2Score> entries = new LinkedList<>();

        for(Map.Entry<String, Float> entry : word2Count.entrySet())
        {
            entries.add(new Attrib2Score(entry.getKey(), entry.getValue()/totalCount));
        }

        Collections.sort(entries);

        return entries;
    }

    private void parseLine(Map<String,Float> word2Count,String header, float true_count, Schema context){

        String[] lineHeader = header.split("-");

        for(int i=0; i<lineHeader.length; i++){

            boolean match = false;

            for(String searchHeader : context.toList()){
                match = match || searchHeader.toLowerCase().equals(lineHeader[i].trim().toLowerCase());
            }

            if(!match){
                if(!word2Count.containsKey(lineHeader[i]))
                    word2Count.put(lineHeader[i], 0.f);

                word2Count.put(lineHeader[i], word2Count.get(lineHeader[i]) + true_count);
            }

        }
    }

}