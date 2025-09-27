package script.event.pharple_day;/*
@Origin: dsrc.script.event.pharple_day
@Author:  BubbaJoeX
@Purpose: Script to handle feather plucking
@Requirements: <no requirements>
@Notes: Attach ONLY to creatures
@Created: Thursday, 8/22/2024, at 9:11 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class spawner_child extends pharple_day_lib
{
    public spawner_child()
    {
    }

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
        if (canPluck(player, self) && !isDead(self) || !isIncapacitated(self))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Pluck Feather"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            handlePluck(player, self, utils.getInventoryContainer(player));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self)
    {
        obj_id spawner = getObjIdObjVar(self, "pharple_spawner");
        int currentCount = getIntObjVar(spawner, "pharple_count");
        setObjVar(spawner, "pharple_count", --currentCount);
        return SCRIPT_CONTINUE;
    }
}
