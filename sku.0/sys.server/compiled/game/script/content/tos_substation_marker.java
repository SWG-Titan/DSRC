package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 5/16/2024, at 4:08 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class tos_substation_marker extends base_script
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
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Mark"));
            mi.addRootMenu(menu_info_types.SERVER_MENU2, new string_id("Set Exit or Entrance"));
            mi.addRootMenu(menu_info_types.SERVER_MENU3, new string_id("Set Tag"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                if (!hasObjVar(self, "tag") || !hasObjVar(self, "destination"))
                {
                    broadcast(player, "Tag or destination not set.");
                    return SCRIPT_CONTINUE;
                }
                location loc = getLocation(self);
                loc.cell = getContainedBy(self);
                setObjVar(getPlanetByName("tatooine"), "tos_movement_marker." + getStringObjVar(self, "tag") + "." + getStringObjVar(self, "destination"), loc);
            }
            if (item == menu_info_types.SERVER_MENU2)
            {
                sui.listbox(self, player, "Select which location this is:", sui.OK_CANCEL, "Select Destination", new String[]{"Exit", "Entrance"}, "handleSetExitOrEntrance", true);
            }
            if (item == menu_info_types.SERVER_MENU3)
            {
                sui.inputbox(self, player, "Set Tag", "Enter a tag for this location: ", "handleSetTag", "");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetTag(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (bp == sui.BP_OK)
        {
            String tag = sui.getInputBoxText(params);
            setObjVar(self, "tag", tag);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetExitOrEntrance(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (bp == sui.BP_OK)
        {
            int idx = sui.getListboxSelectedRow(params);
            if (idx == 0)
            {
                setObjVar(self, "destination", "exit");
            }
            if (idx == 1)
            {
                setObjVar(self, "destination", "entrance");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
