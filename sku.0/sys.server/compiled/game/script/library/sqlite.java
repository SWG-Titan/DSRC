package script.library;/*
@Origin: dsrc.script.library
@Author: BubbaJoeX
@Purpose: SQLite class
@Requirements: $SQLIte connector and classpath addition.
@Created: Sunday, 3/10/2024, at 8:32 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

import java.sql.*;

public class sqlite extends script.base_script
{
    private static final String MONEY_DB = "jdbc:sqlite:/home/swg/swg-main/admin/sqdb/money.db";
    private static final String MONEY_STATEMENT = "UPDATE players SET amount = ? WHERE name = ?";
    private static final String MONEY_OUT_STATEMENT = "UPDATE named_accounts SET amount = ? WHERE account = ?";
    private static final String GM_DB = "jdbc:sqlite:/home/swg/swg-main/admin/sqdb/gm.db";


    public static void updatePlayerMoney(obj_id player, double newMoney) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(MONEY_DB))
        {
            PreparedStatement pstmt = conn.prepareStatement(MONEY_STATEMENT);
            pstmt.setDouble(1, newMoney);
            pstmt.setString(2, getPlayerFullName(player));
            pstmt.executeUpdate();
            LOG("ethereal", "[Money]: Updated cash balance for " + getPlayerFullName(player));
        } catch (SQLException e)
        {
            LOG("ethereal", "[Money]: " + e.getMessage());
        }
    }

    public static void updateNamedAccount(String account, double newMoney) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(MONEY_DB))
        {
            PreparedStatement pstmt = conn.prepareStatement(MONEY_OUT_STATEMENT);
            pstmt.setDouble(1, newMoney);
            pstmt.setString(2, account);
            pstmt.executeUpdate();
            LOG("ethereal", "[Money]: Updated cash balance for account " + account);
        } catch (SQLException e)
        {
            LOG("ethereal", "[Money]: " + e.getMessage());
        }
    }


    public static void updateNamedAccount(String accountName, int incrementValue) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        // Check if the row with the account name exists, if not, insert a new row
        if (!rowExists(accountName, "named_accounts"))
        {
            createNamedAccount(accountName, incrementValue);
        }
        else
        {
            // Row exists, update the account number
            String sql = "UPDATE named_accounts SET amount = amount + ? WHERE account = ?";

            try (Connection conn = DriverManager.getConnection(MONEY_DB);
                 PreparedStatement pstmt = conn.prepareStatement(sql))
            {

                // Set parameters
                pstmt.setInt(1, incrementValue);
                pstmt.setString(2, accountName);

                // Execute update
                pstmt.executeUpdate();
                System.out.println("Named account updated successfully.");

            } catch (SQLException e)
            {
                System.err.println("Error updating named account: " + e.getMessage());
            }
        }
    }

    public static void updatePlayerAccount(obj_id account, int incrementValue) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        // Check if the row with the account name exists, if not, insert a new row
        if (!rowExists(getPlayerFullName(account), "players"))
        {
            createPlayerAccount(getPlayerFullName(account), incrementValue);
        }
        else
        {
            // Row exists, update the account number
            String sql = "UPDATE players SET amount = amount + ? WHERE account = ?";

            try (Connection conn = DriverManager.getConnection(MONEY_DB);
                 PreparedStatement pstmt = conn.prepareStatement(sql))
            {

                // Set parameters
                pstmt.setInt(1, incrementValue);
                pstmt.setString(2, getPlayerFullName(account));

                // Execute update
                pstmt.executeUpdate();
                System.out.println("Named account updated successfully.");

            } catch (SQLException e)
            {
                System.err.println("Error updating named account: " + e.getMessage());
            }
        }
    }

    private static boolean rowExists(String accountName, String table) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE account = ?";
        try (Connection conn = DriverManager.getConnection(MONEY_DB);
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, accountName);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e)
        {
            LOG("ethereal", "[Money]: Error checking if row exists: " + e.getMessage());
            return false;
        }
    }

    private static void createNamedAccount(String accountName, int initialValue) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        String sql = "INSERT INTO named_accounts (account, amount) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(MONEY_DB);
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, accountName);
            pstmt.setInt(2, initialValue);
            pstmt.executeUpdate();
            LOG("ethereal", "[Money]: New named account created successfully.");
        } catch (SQLException e)
        {
            LOG("ethereal", "[Money]: Error creating new named account: " + e.getMessage());
        }
    }

    private static void createPlayerAccount(String accountName, int initialValue) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        String sql = "INSERT INTO players (name, amount) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(MONEY_DB);
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, accountName);
            pstmt.setInt(2, initialValue);
            pstmt.executeUpdate();
            LOG("ethereal", "[Money]: New named account created successfully.");
        } catch (SQLException e)
        {
            LOG("ethereal", "[Money]: Error creating new named account: " + e.getMessage());
        }
    }

    private static boolean tableExists(String tableName, String database) throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (Connection conn = DriverManager.getConnection(database);
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e)
        {
            LOG("ethereal", "[Money]: " + e.getMessage());
            return false;
        }
    }

    private static int createMoneyTable() throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        if (tableExists("money", MONEY_DB))
        {
            LOG("ethereal", "[Money]: Attempted to overwrite money table. Returning.");
            return SCRIPT_OVERRIDE;
        }
        else
        {
            String sql = "CREATE TABLE IF NOT EXISTS players (\n"
                    + "    id INTEGER PRIMARY KEY,\n"
                    + "    name TEXT NOT NULL,\n"
                    + "    amount REAL\n"
                    + ");";
            try (Connection conn = DriverManager.getConnection(MONEY_DB);
                 Statement stmt = conn.createStatement())
            {
                stmt.execute(sql);
                LOG("ethereal", "[Money]: " + " Money table created.");
            } catch (SQLException e)
            {
                LOG("ethereal", "[Money]: " + e.getMessage());
            }
            return SCRIPT_CONTINUE;
        }
    }

    private static int createNamedMoneyTable() throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        if (!tableExists("named_accounts", MONEY_DB))
        {
            LOG("ethereal", "[Money]: Attempted to overwrite named_accounts table. Returning.");
            return SCRIPT_OVERRIDE;
        }
        else
        {
            String sql = "CREATE TABLE IF NOT EXISTS named_accounts (\n"
                    + "    id INTEGER PRIMARY KEY,\n"
                    + "    account TEXT NOT NULL,\n"
                    + "    amount REAL\n"
                    + ");";
            try (Connection conn = DriverManager.getConnection(MONEY_DB);
                 Statement stmt = conn.createStatement())
            {
                stmt.execute(sql);
                LOG("ethereal", "[Money]: " + " Named accounts table created.");
            } catch (SQLException e)
            {
                LOG("ethereal", "[Money]: " + e.getMessage());
            }
            return SCRIPT_CONTINUE;
        }
    }
}