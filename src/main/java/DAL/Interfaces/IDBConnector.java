package DAL.Interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnector {

    public default void DBConnector() throws SQLException{

    }

    public default Connection getConnection() throws SQLException{
        return null;
    }

    public default void closeConnection() throws SQLException{
    }
}
