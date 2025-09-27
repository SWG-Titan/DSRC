package script.content.jcs;/*
@Origin: dsrc.script.content.jcs
@Author:  BubbaJoeX
@Purpose: Grants the player's account an additional player slot upon consumption. max of 15, (5 base, 10 earned)
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 2/9/2025, at 9:43 AM, 
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
//import script.library.oracle;
import script.library.sui;
import script.library.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class character_slot_item extends base_script
{/*
    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int sync(obj_id self)
    {
        setObjVar(self, "noTradeShared", 1);
        setObjVar(self, "noTrade", 1);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!utils.isNestedWithinAPlayer(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (canManipulate(player, self, true, true, 4f, false))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, string_id.unlocalized("Add Character Slot to Account"));
        }
        else
        {
            broadcast(player,"This item does not concern you");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!utils.isNestedWithinAPlayer(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (canManipulate(player, self, true, true, 4f, false))
            {
                sui.msgbox(self, player, "Are you sure you wish to redeem this item? Doing so will grant one (1) character slot to your account, as long as your account does not exceed 15 character slots.", "handleChoice");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleChoice(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            broadcast(player, "You have chosen not to add a character slot to your account.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (increaseCharacterAmount(self, player, getPlayerStationId(player), 1, 1) == SCRIPT_CONTINUE)
            {
                broadcast(player, "You have added an additional character slot to your account. Please allow for 5 minutes to pass before making a new character.");
                destroyObject(self);
            }
            else
            {
                broadcast(player, "The system is unable to complete the transaction.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int increaseCharacterAmount(obj_id self, obj_id target, int stationId, int clusterId, int characterTypeId)
    {
        String checkQuery = "SELECT NUM_EXTRA_SLOTS FROM extra_character_slots WHERE STATION_ID = " + stationId +
                " AND CLUSTER_ID = " + clusterId + " AND CHARACTER_TYPE_ID = " + characterTypeId;

        String updateQuery = "UPDATE extra_character_slots SET NUM_EXTRA_SLOTS = NUM_EXTRA_SLOTS + 1 " +
                "WHERE STATION_ID = " + stationId + " AND CLUSTER_ID = " + clusterId +
                " AND CHARACTER_TYPE_ID = " + characterTypeId;

        String insertQuery = "INSERT INTO extra_character_slots (STATION_ID, CLUSTER_ID, CHARACTER_TYPE_ID, NUM_EXTRA_SLOTS) " +
                "VALUES (" + stationId + ", " + clusterId + ", " + characterTypeId + ", 1)";

        Connection conn = oracle.connect();
        try (Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt.executeQuery(checkQuery))
        {
            if (rs.next())
            {
                int currentSlots = rs.getInt("NUM_EXTRA_SLOTS");
                if (currentSlots >= 10)
                {
                    broadcast(target, "You have reached the server cap for extra character slots.");
                    return SCRIPT_CONTINUE;
                }
            }

            int rowsUpdated = stmt.executeUpdate(updateQuery);
            if (rowsUpdated > 0)
            {
                blog("Successfully increased character count for station " + stationId + ", cluster " + clusterId + ", type " + characterTypeId);
            }
            else
            {
                blog("No records found to update for station " + stationId + ", cluster " + clusterId + ", type " + characterTypeId);
                int rowsInserted = stmt.executeUpdate(insertQuery);
                if (rowsInserted > 0)
                {
                    blog("Successfully inserted new record for station " + stationId + ", cluster " + clusterId + ", type " + characterTypeId);
                }
            }
        }
        catch (SQLException e)
        {
            blog("Error while increasing character amount: " + e.getMessage());
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }


    public void blog(String msg)
    {
        LOG("ethereal", "[Character Slot Grant]: " + msg);
    }
*/
}
