package script.systems.kitchen;/*
@Origin: dsrc.script.systems.kitchen
@Author: BubbaJoeX
@Purpose: Sink script for giving water on a timer
@Note: Water can be retrieved every 60 seconds. This is a public action however, anyone can take the water.
@Requirements: <no requirements>
@Created: Sunday, 10/1/2023, at 3:32 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.date;
import script.library.resource;

public class sink extends base_script
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
        if (isIdValid(player))
        {
            if (isInWorldCell(player))
            {
                broadcast(player, "You cannot use a sink without installing it first!");
                return SCRIPT_CONTINUE;
            }
            else
            {
                mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Obtain Water"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (!canObtainWater(self, player))
            {
                broadcast(player, "You cannot get water at this time. Please wait, or try another sink");
                return SCRIPT_CONTINUE;
            }
            else
            {
                obtainWater(player, self);
            }
        }
        return SCRIPT_CONTINUE;
    }

    private void obtainWater(obj_id player, obj_id self) throws InterruptedException
    {
        resource.createRandom("water", rand(243, 1045), getLocation(player), self);
        broadcast(player, "You have obtained water from the sink.");
        setObjVar(self, "sink.lastDispense", getGameTime());
        LOG("ethereal", "[Kitchen]: Player " + getPlayerFullName(player) + " has obtained water from sink " + self + " at location " + getLocation(player) + " at time " + date.getFullDateWithSuffix(self) + ".");
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isIdValid(item))
        {
            if (getGameObjectType(item) == GOT_resource_container_inorganic_water)
            {
                return SCRIPT_CONTINUE;
            }
            else
            {
                broadcast(transferer, "You can only store water inside this sink.");
                return SCRIPT_OVERRIDE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean canObtainWater(obj_id self, obj_id who)
    {
        int time = getGameTime();
        int lastTime = getIntObjVar(self, "sink.lastDispense");
        return time - lastTime > 60;
    }

}
