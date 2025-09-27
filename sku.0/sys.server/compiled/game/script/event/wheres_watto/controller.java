package script.event.wheres_watto;/*
@Origin: dsrc.script.event.wheres_watto.controller
@Author: BubbaJoeX
@Purpose: Handles the spawning and movement of Watto for the Where's Watto event.
@Notes;
    Watto will spawn in a random location within a 7250m radius of the controller.
    Watto will be named "Watto" with a custom color and a the holiday object condition
    Using the controller, GMs can spawn Watto and warp to his location.

@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;

public class controller extends base_script
{
    public String[] TOYDARIANS = {
            "toydarian_m_greeter",
            "toydarian_m_greeter_1",
            "toydarian_m_greeter_2",
    };

    public int OnAttach(obj_id self)
    {
        setObjVar(self, "watto_controller_master", 1);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isGod(player))
        {
            if (!hasObjVar(self, "made_watto"))
            {
                mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Make a Where's Watto"));
            }
            else
            {
                mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Go to Watto"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.ITEM_USE)
            {
                location watto_loc = new location(0, 0, 0, getCurrentSceneName(), null);
                watto_loc.x = watto_loc.x + (rand(-7250.0f, 7250.0f));
                watto_loc.z = watto_loc.z + (rand(-7250.0f, 7250.0f));
                watto_loc.y = getHeightAtLocation(watto_loc.x, watto_loc.z);
                obj_id watto = create.object(TOYDARIANS[rand(0, TOYDARIANS.length - 1)], watto_loc);
                attachScript(watto, "event.wheres_watto.wheres_watto");
                setName(watto, "Watto");
                setObjVar(watto, "watto_tag", 1);
                setObjVar(watto, "watto", watto);
                setObjVar(self, "made_watto", 1);
                LOG("events", "[Where's Watto]: Watto spawned at " + watto_loc);
                return SCRIPT_CONTINUE;
            }
            if (item == menu_info_types.SERVER_MENU1)
            {
                obj_id watto = getObjIdObjVar(self, "watto");
                if (isIdValid(watto))
                {
                    location watto_loc = getLocation(watto);
                    warpPlayer(player, watto_loc.area, watto_loc.x, watto_loc.y, watto_loc.z, null, 0, 0, 0);
                }
                else
                {
                    broadcast(self, "Watto is not spawned yet.");
                }
                LOG("events", "[Where's Watto]: Watto's location was requested by " + getPlayerFullName(player));
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
