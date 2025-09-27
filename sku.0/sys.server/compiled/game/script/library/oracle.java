package script.library;/*
@Origin: dsrc.script.library.oracle
@Author: BubbaJoeX
@Purpose: Oracle connect script
@Requirements
    This script contains many unhandled and unchecked operations. Use at your own risk.
@Notes:

@Copyright © SWG-OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static script.base_class.LOG;

public class oracle
{

    private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:swg";
    private static final String USER = "swg";
    private static final String PASSWORD = "swg";

    public static boolean LOGGING = true;

    public static void blog(String text)
    {
        if (LOGGING)
        {
            LOG("ethereal", "[Oracle Driver]: " + text);
        }
    }

    public static void initializeDriver()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
            blog("Oracle JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e)
        {
            blog("Error: Oracle JDBC driver not found! " + e.getMessage());
        }
    }

    // Establish a database connection
    public static Connection connect()
    {
        try
        {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (LOGGING)
            {
                blog("Connected to Oracle Database successfully.");
            }
            return conn;
        } catch (SQLException e)
        {
            blog("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Execute a query and return the result set
    public static ResultSet executeQuery(Connection conn, String query) throws SQLException
    {
        if (query == null || query.isEmpty())
        {
            throw new IllegalArgumentException("Query cannot be null or empty.");
        }
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    // Execute an update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(Connection conn, String query) throws SQLException
    {
        if (query == null || query.isEmpty())
        {
            throw new IllegalArgumentException("Query cannot be null or empty.");
        }
        Statement stmt = conn.createStatement();
        return stmt.executeUpdate(query);
    }

    // Close the connection
    public static void closeConnection(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
                if (LOGGING)
                {
                    blog("Connection closed.");
                }
            } catch (SQLException e)
            {
                blog("Error closing connection: " + e.getMessage());
            }
        }
    }
}