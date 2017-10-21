package main.java.thirdParty.acsdb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import  main.java.thirdParty.acsdb.model.AcsdbValue;
import  main.java.thirdParty.acsdb.model.Schema;

/**
 * Created by Andrea on 31/07/2017.
 */
public abstract class DBAdapter {

    protected ConnectionDb connection;

    public DBAdapter(ConnectionDb conn){
        connection = conn;
    }

    public abstract Set<String> getCommonAttrInContext(Schema context);
    public abstract Map<String, AcsdbValue> getContextSchemas(Schema context, boolean true_header) throws SQLException;
    public abstract Set<Long> findSchemasForAttrib(String attrib) throws SQLException;
    public abstract List<Schema> findAcsdbValuesByIds(Set<Long> ids, String attrib) throws SQLException;
    public abstract int getTrueSchemaCount() throws SQLException;


}