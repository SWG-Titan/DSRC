package script.content.new_instance.blacksun;/*
@Origin: dsrc.script.content.new_instance.blacksun
@Author:  BubbaJoeX
@Purpose: Moves the gate under the world to allow players inside the first gate.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 6/3/2024, at 5:24 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class laser_fence_controller extends base_script
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
        obj_id nearestGate = getAllGates(self);
        if (getDoorState(nearestGate) == 0)
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Open"));
        }
        else
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Close"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        obj_id nearestGate = getAllGates(self);
        if (item == menu_info_types.ITEM_USE)
        {
            if (getDoorState(nearestGate) == 0)
            {
                location loc = getLocationObjVar(nearestGate, "originalLocation");
                loc.y = -1000;
                setLocation(nearestGate, loc);
            }
            else
            {
                location loc = getLocationObjVar(nearestGate, "originalLocation");
                setLocation(nearestGate, loc);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int getDoorState(obj_id self)
    {
        return getIntObjVar(self, "doorState");
    }

    public obj_id getAllGates(obj_id self)
    {
        obj_id[] objObjects = getObjectsInRange(getLocation(self), 10);
        for (obj_id objObject : objObjects)
        {
            if (hasScript(objObject, "content.new_instance.blacksun.laser_fence"))
            {
                return objObject;
            }
        }
        return null;
    }
}
