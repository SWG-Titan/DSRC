package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Returns to station
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 5/16/2024, at 4:12 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class tos_substation_movement extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel"));
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Select Destination"));
            mi.addRootMenu(menu_info_types.SERVER_MENU2, new string_id("Set Which Gate"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            String destination = getStringObjVar(self, "destination");
            if (destination == null || destination.equals(""))
            {
                broadcast(player, "Destination not set.");
                return SCRIPT_CONTINUE;
            }
            String which = getStringObjVar(self, "which");
            if (which == null || which.equals(""))
            {
                broadcast(player, "Gate not set.");
                return SCRIPT_CONTINUE;
            }
            location loc = getLocationObjVar(getPlanetByName("tatooine"), "tos_movement_marker." + destination + "." + which);
            if (loc == null)
            {
                broadcast(player, "Destination not found.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                warpPlayer(player, loc.area, loc.x, loc.y, loc.z, loc.cell, loc.x, loc.y, loc.z);
            }
        }
        else if (isGod(player) && item == menu_info_types.SERVER_MENU1)
        {
            sui.inputbox(self, player, "Select Destination", "Enter the destination tag: ", "handleSelectDestination", "");
        }
        else if (isGod(player) && item == menu_info_types.SERVER_MENU2)
        {
            sui.listbox(self, player, "Select which gate this is:", sui.OK_CANCEL, "Select Which Gate", new String[]{"Exit", "Entrance"}, "handleSetWhich", true);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetWhich(obj_id self, dictionary params) throws InterruptedException
    {
        int which = sui.getListboxSelectedRow(params);
        if (which == -1)
        {
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "which", which == 0 ? "exit" : "entrance");
        return SCRIPT_CONTINUE;
    }

    public int handleSelectDestination(obj_id self, dictionary params) throws InterruptedException
    {
        String destination = sui.getInputBoxText(params);
        if (destination == null || destination.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "destination", destination);
        return SCRIPT_CONTINUE;
    }
}
