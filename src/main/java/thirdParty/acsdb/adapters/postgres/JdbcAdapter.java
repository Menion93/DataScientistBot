package main.java.thirdParty.acsdb.adapters.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import  main.java.thirdParty.acsdb.ConnectionDb;
import  main.java.thirdParty.acsdb.DBAdapter;
import  main.java.thirdParty.acsdb.model.AcsdbValue;
import  main.java.thirdParty.acsdb.model.Schema;

/**
 * Created by Andrea on 15/08/2017.
 */
public class JdbcAdapter extends DBAdapter {

    Map<String, AcsdbValue> commonAttrCache;

    public JdbcAdapter(ConnectionDb connection){
        super(connection);
        commonAttrCache = new HashMap<>();
    }

    public ResultSet executeQuery(String query) throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = connection.getConnection().createStatement();
            rs = stmt.executeQuery(query);

        } catch (SQLException e ) {
            e.printStackTrace();
        }

        return rs;
    }

    public void executeUpdate(String sqlQ) throws SQLException {
        Statement stmt = null;
        try {

            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate(sqlQ);

        } catch (SQLException e ) {
            e.printStackTrace();
        }

        finally {
            if (stmt != null) {
                stmt.close(); }
        }

    }

    private String getTempTable(String attrib) throws SQLException {
        ResultSet rs = executeQuery("Select name from tempcatalog where attrib like '" + attrib + "'");

        if(!rs.next()){
            rs.close();
            return null;
        }

        return rs.getString("name");

    }

    private int getAndIncrementCounter() throws SQLException {
        ResultSet rs = executeQuery("SELECT count FROM counter");
        rs.next();
        int count = rs.getInt("count");
        count++;
        executeUpdate("UPDATE counter SET count=" + count);
        rs.close();
        return count;
    }

    public Set<Long> findSchemasForAttrib(String attrib) throws SQLException {

        Set<Long> result = new HashSet<>();

        String qGetSchemas = "select schema from attrib2schema where attrib like '" + attrib +"'";

        ResultSet rs = executeQuery(qGetSchemas);

        while (rs.next()) {
            String ids = rs.getString("schema");

            for(String id : ids.split("-")){
                result.add(Long.parseLong(id));
            }
        }
        rs.close();

        System.out.println(result.size());
        return result;

    }


    public String createTempTable(String attr, Set<Long> ids) throws SQLException {
        String tempTable = "temp" + getAndIncrementCounter();
        executeUpdate("CREATE TABLE " + tempTable + "(id int, PRIMARY KEY(id))" );
        executeUpdate("INSERT INTO tempCatalog VALUES ('" + attr + "','" + tempTable + "')");

        for(Long id : ids){
            String sqlQ = "INSERT INTO " + tempTable + " VALUES (" + id + ")";
            executeUpdate(sqlQ);
        }

        return tempTable;
    }

    @Override
    public Map<String, AcsdbValue> getContextSchemas(Schema context, boolean trueHeader) throws SQLException {

        String getOnlyTrue = "";

        if(trueHeader)
            getOnlyTrue = " and adb.true_header > 0";

        StringBuilder sb = new StringBuilder();
        sb.append("Select schema, true_header, sub_header from ");

        int i = 0;

        for(String attr : context.toList()){
            String tempTable = getTempTable(attr);
            if(tempTable ==  null){
                // Create table temp
                tempTable = createTempTable(attr, findSchemasForAttrib(attr));
            }

            i++;
            sb.append(tempTable + " " + "t" + i + ", ");
        }

        sb.append("acsdb adb WHERE ");
        sb.append("adb.id=t1.id");

        for(i=2; i<=context.toList().size(); i++){
            sb.append(" and t1.id=t" + i + ".id");
        }

        sb.append(getOnlyTrue);

        ResultSet rs = executeQuery(sb.toString());

        commonAttrCache = new HashMap<>();

        while(rs.next()){
            commonAttrCache.put(rs.getString("schema"),
                    new AcsdbValue(rs.getInt("true_header"),
                                   rs.getInt("sub_header")));
        }

        rs.close();

        return commonAttrCache;
    }

    @Override
    public Set<String> getCommonAttrInContext(Schema context) {

        Set<String> resultSet = new HashSet<>();

        for(String key : commonAttrCache.keySet()){
            for(String attr : key.split("-"))
                resultSet.add(attr);
        }

        commonAttrCache = new HashMap<>();

        return resultSet;
    }

    public int getTrueSchemaCount() throws SQLException {
        ResultSet rs = executeQuery("select sum(true_header) as trueSum from acsdb");
        rs.next();
        int true_count = rs.getInt("trueSum");
        rs.close();
        return true_count;
    }

    @Override
    public List<Schema> findAcsdbValuesByIds(Set<Long> ids, String attrib) throws SQLException {

        List<Schema> attribDB = new LinkedList<>();

        String tempTable = getTempTable(attrib);

        if(tempTable == null){
            long time = System.currentTimeMillis();

            tempTable = "temp" + getAndIncrementCounter();
            executeUpdate("CREATE TABLE " + tempTable + "(id int, PRIMARY KEY(id))" );
            executeUpdate("INSERT INTO tempCatalog VALUES ('" + attrib + "','" + tempTable + "')");

            for(Long id : ids){
                String sqlQ = "INSERT INTO " + tempTable + " VALUES (" + id + ")";
                executeUpdate(sqlQ);
            }

            System.out.println("time for 1 query = " + (System.currentTimeMillis()-time));
        }

        executeJoin(attribDB, tempTable);

        return attribDB;
    }
    
    private void executeJoin(List<Schema> attribDB, String tempTable) throws SQLException {

        String query = "select schema from true_acsdb a join " + tempTable + " t on a.id=t.id";

        long time = System.currentTimeMillis();
        ResultSet rs = executeQuery(query);
        System.out.println("time for join query = " + (System.currentTimeMillis()-time));

        while (rs.next())
            attribDB.add(new Schema(rs.getString("schema")));

        rs.close();
    }

}