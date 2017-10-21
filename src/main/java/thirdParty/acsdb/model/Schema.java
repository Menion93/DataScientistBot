package main.java.thirdParty.acsdb.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 23/07/2017.
 */
public class Schema {

    private List<String> schema;

    public Schema(String[] schema){
        this.schema = new LinkedList<>();
        for(String header : schema)
            addHeader(header.trim().toLowerCase());
    }

    public Schema(String headers){
        this(headers.split("-"));
    }

    public void addHeader(String header){
        schema.add(header);
    }

    public List<String> toList(){
        return schema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schema)) return false;

        Schema schema1 = (Schema) o;

        return schema != null ? schema.equals(schema1.schema) : schema1.schema == null;
    }

    @Override
    public int hashCode() {
        return schema != null ? schema.hashCode() : 0;
    }

    @Override
    public String toString() {

        String result="";

        for(String header : schema){
            result += header + " | ";
        }

        return result.trim();
    }
}