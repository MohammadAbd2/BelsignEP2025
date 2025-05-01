package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {

    private final SQLServerDataSource ds;
    private Connection connection;

    // Constructor - Set up the SQL Server DataSource
    public DBConnector() {
        ds = new SQLServerDataSource();
        // Configure database connection properties
        ds.setServerName("EASV-DB4");      // Database server
        ds.setDatabaseName("QC_BelsignDatabase");  // Database name
        ds.setPortNumber(1433);           // Port
        ds.setUser("CSe2024b_e_1");      // Username
        ds.setPassword("CSe2024bE1!24"); // Password
        ds.setTrustServerCertificate(true); // Trust server certificate
    }

    // Method to get a database connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Establish connection to database
                connection = ds.getConnection();
                if (connection != null && !connection.isClosed()) {
                    System.out.println("Connection established successfully to the database.");
                } else {
                    throw new SQLException("Failed to establish a database connection.");
                }
            } catch (SQLException e) {
                // Log error message for debugging purposes
                System.err.println("Error during connection to the database:");
                System.err.println("Server: " + ds.getServerName());
                System.err.println("Database: " + ds.getDatabaseName());
                e.printStackTrace();
                throw new RuntimeException("Unable to establish a connection to the database.", e);
            }
        }
        return connection;
    }

    // Method to close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connection to the database has been closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error while closing the database connection:");
                e.printStackTrace();
            } finally {
                connection = null; // Reset the connection reference
            }
        } else {
            System.out.println("No active connection to close.");
        }
    }
}
