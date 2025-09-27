package script.hub.building;/*
@Origin: dsrc.script.hub.building
@Author: BubbaJoeX
@Purpose: Transit to the hub
@Created: Saturday, 11/4/2023, at 3:55 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.consts;
import script.*;

public class transport extends script.base_script
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
        menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel to Rally Point Tau"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (canGoToHub(player))
            {
                warpPlayer(player, getLocationObjVar(getPlanetByName("tatooine"), "hub"));
            }
            else
            {
                broadcast(player, "You do not meet the requirements to travel to Rally Point Tau.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean canGoToHub(obj_id player)
    {
        return getLevel(player) >= consts.PLAYER_LEVEL;
    }

    public void warpPlayer(obj_id player, location loc) throws InterruptedException
    {
        warpPlayer(player, loc.area, loc.x, loc.y, loc.z, loc.cell, 0, 0, 0);
    }
}
