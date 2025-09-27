package script.library;/*
@Origin: dsrc.script.library
@Author:  BubbaJoeX
@Purpose: Event Flags to enable/disable persistently.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 1/30/2025, at 1:09 PM, 
@Copyright © SWG: New Beginnings 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import java.sql.*;

public class event_flags extends script.base_script
{
    public static boolean isEventFlagActive(String eventFlag)
    {
        String query = "SELECT event_flag, description, status " +
                "FROM event_flags " +
                "WHERE event_flag = ? " +
                "AND status = 'active'";

        try (Connection conn = oracle.connect();
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setString(1, eventFlag);
            try (ResultSet rs = stmt.executeQuery())
            {
                return rs.next(); // returns true if an active event flag is found
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean disableEventFlag(String eventFlag)
    {
        String query = "MERGE INTO event_flags ef " +
                "USING (SELECT ? AS event_flag FROM dual) d " +
                "ON (ef.event_flag = d.event_flag) " +
                "WHEN MATCHED THEN " +
                "UPDATE SET ef.status = 'inactive' " +
                "WHERE ef.status = 'active' " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (event_flag, status) " +
                "VALUES (d.event_flag, 'inactive')";  // Insert case with status as inactive

        try (Connection conn = oracle.connect();
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setString(1, eventFlag);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if the flag was updated or inserted successfully
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean enableEventFlag(String eventFlag)
    {
        String query = "MERGE INTO event_flags ef " +
                "USING (SELECT ? AS event_flag FROM dual) d " +
                "ON (ef.event_flag = d.event_flag) " +
                "WHEN MATCHED THEN " +
                "UPDATE SET ef.status = 'active' " +
                "WHERE ef.status = 'inactive' " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (event_flag, status) " +
                "VALUES (d.event_flag, 'active')";  // Insert case with status as active

        try (Connection conn = oracle.connect();
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setString(1, eventFlag);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if the flag was updated or inserted successfully
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;  // Return false if an error occurs
        }
    }

}
