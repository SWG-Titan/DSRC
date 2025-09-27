package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Marks the location for the shuttle beacon to travel to
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 5/10/2024, at 12:28 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class tos_beacon_marker extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Mark"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            location here = getLocation(self);
            setObjVar(getPlanetByName("tatooine"), "tos", here);
            broadcast(player, "Location marked: " + here.toReadableFormat(true));
        }
        return SCRIPT_CONTINUE;
    }
}
