package script;

import script.library.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class persistent_data extends base_script
{

    private final Connection connection;

    public persistent_data()
    {
        connection = getDatabaseConnection();
    }

    public boolean storeData(obj_id objectId, String key, String value)
    {
        String sql = "INSERT INTO OBJECT_DATA (OBJECT_ID, DATA_KEY, DATA_VALUE) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, Integer.parseInt(objectId.toString()));
            pstmt.setString(2, key);
            pstmt.setString(3, value);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e)
        {
            System.err.println("Error storing data: " + e.getMessage());
            return false;
        }
    }

    public String retrieveData(obj_id objectId, String key)
    {
        String sql = "SELECT DATA_VALUE FROM OBJECT_DATA WHERE OBJECT_ID = ? AND DATA_KEY = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, Integer.parseInt(objectId.toString()));
            pstmt.setString(2, key);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                return rs.getString("DATA_VALUE");
            }
            else
            {
                return null;
            }
        } catch (SQLException e)
        {
            System.err.println("Error retrieving data: " + e.getMessage());
            return null;
        }
    }

    public boolean updateData(obj_id objectId, String key, String newValue)
    {
        String sql = "UPDATE OBJECT_DATA SET DATA_VALUE = ? WHERE OBJECT_ID = ? AND DATA_KEY = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, Integer.parseInt(objectId.toString()));
            pstmt.setString(3, key);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e)
        {
            System.err.println("Error updating data: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteData(obj_id objectId, String key)
    {
        String sql = "DELETE FROM OBJECT_DATA WHERE OBJECT_ID = ? AND DATA_KEY = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, Integer.parseInt(objectId.toString()));
            pstmt.setString(2, key);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e)
        {
            System.err.println("Error deleting data: " + e.getMessage());
            return false;
        }
    }

    public boolean doesDataExist(obj_id objectId, String key)
    {
        String sql = "SELECT COUNT(*) FROM OBJECT_DATA WHERE OBJECT_ID = ? AND DATA_KEY = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, Integer.parseInt(objectId.toString()));
            pstmt.setString(2, key);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                return rs.getInt(1) > 0;
            }
            else
            {
                return false;
            }
        } catch (SQLException e)
        {
            System.err.println("Error checking if data exists: " + e.getMessage());
            return false;
        }
    }


    private Connection getDatabaseConnection()
    {
        return oracle.connect();
    }
}
