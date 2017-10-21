package main.java.thirdParty.acsdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import main.java.thirdParty.acsdb.model.AcsdbValue;
import main.java.thirdParty.acsdb.model.Schema;
import java.util.Map;
import java.util.List;

/**
 * Created by Andrea on 14/08/2017.
 */
public class AcsdbRepo {

    private DBAdapter adapter;

    private String basePath = "F:/Documenti/git/DataScientistBot/src/main/java/thirdParty/acsdb/resources/";
    private String outputPath1 = basePath + "ACSDb1field.txt";
    private String outputPath2 = basePath + "ACSDb2fields.txt";
    private String outputPath3 = basePath + "ACSDb3fields.txt";
    
    private Map<String, AcsdbValue> acsdb1Values;
    private Map<String, AcsdbValue> acsdb2Values;
    private Map<String, AcsdbValue> acsdb3Values;


    public AcsdbRepo(DBAdapter adapter){
        this.adapter = adapter;
    }
    
    public List<Schema> getSchemasWithAttrib(String attrib) throws SQLException {
        System.out.println("Creating AttribDb for attrib=" + attrib);
        long time = System.currentTimeMillis();

        List<Schema> result =  adapter.findAcsdbValuesByIds(adapter.findSchemasForAttrib(attrib), attrib);

        System.out.println("AttribDb created in " + (System.currentTimeMillis()-time) + " milliseconds");
        return result;
    }
  
    public Map<String, AcsdbValue> getContextSchemas(Schema context, boolean trueHeader) throws SQLException {
        return adapter.getContextSchemas(context, trueHeader);
    }

    public Set<String> getCommonAttrInContext(Schema context) {
        return adapter.getCommonAttrInContext(context);
    }

    public int getTrueSchemaCount() throws SQLException {
        return adapter.getTrueSchemaCount();
    }

    public Map<String, AcsdbValue> getAcsdb1Value() throws FileNotFoundException, UnsupportedEncodingException {
        if(acsdb1Values == null){
            acsdb1Values = new HashMap<>();
            wrangleAcsdb(outputPath1, acsdb1Values);
        }
        return acsdb1Values;
    }
    
    public Map<String, AcsdbValue> getAcsdb2Values() throws FileNotFoundException, UnsupportedEncodingException {
        if(acsdb2Values == null){
            acsdb2Values = new HashMap<>();
            wrangleAcsdb(outputPath2, acsdb2Values);
        }
        return acsdb2Values;
    }
    
    public Map<String, AcsdbValue> getAcsdb3Values() throws FileNotFoundException, UnsupportedEncodingException {
        if(acsdb3Values == null){
            acsdb3Values = new HashMap<>();
            wrangleAcsdb(outputPath3, acsdb3Values);
        }
        return acsdb3Values;
    }

    public void wrangleAcsdb(String input, Map<String, AcsdbValue> map) throws FileNotFoundException, UnsupportedEncodingException {

        System.out.println("Loading started...");
        long time = System.currentTimeMillis();


        try (Scanner sc = new Scanner(new File(input), "UTF-8")) {

            while (sc.hasNextLine()) {

                String line = sc.nextLine();

                // parse line
                parseLine(line, map);
            }

            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                sc.ioException().printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(input.split("/")[input.split("/").length-1] + " loaded");
        System.out.println("Load time: " + (System.currentTimeMillis()-time)/1000 +" seconds");

    }

    private void parseLine(String line, Map<String, AcsdbValue> map) {

        try{
            String[] splitted = line.split("\t");

            int subHeader = Integer.parseInt(splitted[1]);
            int trueHeader = Integer.parseInt(splitted[2]);
            map.put(splitted[0], new AcsdbValue(trueHeader, subHeader));

        }

        catch (Exception e){
            //System.out.println("exception!");
        }
    }
}