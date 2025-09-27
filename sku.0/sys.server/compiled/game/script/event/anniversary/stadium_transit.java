package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 8/14/2024, at 9:37 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;

public class stadium_transit extends base_script
{
    public int OnAttach(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int setup(obj_id self)
    {
        setName(self, "Stadium Taxi");
        setDescriptionString(self, "Transit to or from the event stadium.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        String setting = getStringObjVar(self, "setting");
        if (setting == null || setting.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        if (setting.equals("stadium_enter"))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Depart to the Stadium"));
        }
        else if (setting.equals("stadium_exit"))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Return to Mos Eisley"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (getDistance(player, self) > 5f)
        {
            broadcast(self, "You are too far away to use this.");
            return SCRIPT_CONTINUE;
        }
        if (combat.isInCombat(player))
        {
            broadcast(self, "You cannot use this while in combat.");
            return SCRIPT_CONTINUE;
        }
        if (isDead(player))
        {
            broadcast(self, "You cannot use this while dead.");
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            String setting = getStringObjVar(self, "setting");
            if (setting == null || setting.isEmpty())
            {
                return SCRIPT_CONTINUE;
            }
            if (setting.equals("stadium_enter"))
            {
                location loc = new location(24.971825f, 0.0f, 4910.655f, "adventure3", null);
                warpPlayer(player, loc, null, true);
            }
            else if (setting.equals("stadium_exit"))
            {
                location loc = new location(3528f, 0f, -4804f, "tatooine", null);
                warpPlayer(player, loc, null, true);
            }
            else
            {
                broadcast(player, "This taxi is currently out of service.");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
