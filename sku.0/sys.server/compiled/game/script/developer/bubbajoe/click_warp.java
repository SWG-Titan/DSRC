package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Click to warp to location.
@Requirements <no requirements>
@Created: Saturday, 3/30/2024, at 3:16 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.*;

public class click_warp extends script.developer.bubbajoe.deployable
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
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Warp"));
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        location whereAmIGoing = getLocation(self);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        if (!isInWorldCell(player))
        {
            whereAmIGoing.cell = getContainedBy(player);
        }
        setPosition(player, whereAmIGoing);
        return SCRIPT_CONTINUE;
    }

    private void setPosition(obj_id who, location location)
    {
        warpPlayer(who, location.area, location.x, location.y, location.z, location.cell, location.x, location.y, location.z);
        broadcast(who, "Moving to: " + location.toReadableFormat(true));
    }
}
