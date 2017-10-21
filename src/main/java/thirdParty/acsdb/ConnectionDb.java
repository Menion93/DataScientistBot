package main.java.thirdParty.acsdb;

import main.java.thirdParty.acsdb.connector.PostgresConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionDb{
	
	PostgresConnection connection;
	
	public ConnectionDb() throws ClassNotFoundException{
		connection = new PostgresConnection();
	}
	
	public void closeConnection() throws SQLException {
        connection.closeConnection();
    }

    public Connection getConnection(){
        return connection.getConnection();
    }
}