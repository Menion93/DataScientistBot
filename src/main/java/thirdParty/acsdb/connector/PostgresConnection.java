package main.java.thirdParty.acsdb.connector;

/**
 * Created by Andrea on 21/10/2017.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Andrea on 31/07/2017.
 */
public class PostgresConnection {

    private Connection connection;

    public PostgresConnection() throws ClassNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/Acsdb", "postgres", "postgres");
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public Connection getConnection(){
        return connection;
    }
}