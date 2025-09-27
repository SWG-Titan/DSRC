package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: Heals players within stadium
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 8/14/2024, at 9:05 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.buff;
import script.library.pclib;
import script.library.utils;

public class stadium_revive extends stadium_lib
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Stadium Revive Station");
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
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Revive Stadium Participants"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.ITEM_USE)
            {
                areaRevive(player);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int areaRevive(obj_id self) throws InterruptedException
    {
        obj_id[] players = getPlayerCreaturesInRange(self, TOOL_RANGE);
        if (players != null)
        {
            for (obj_id player : players)
            {
                if (isGod(player))
                {
                    broadcast(self, "Skipping revive on god character. ( " + getPlayerFullName(player) + ")");
                    continue;
                }
                revive(player);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int revive(obj_id player) throws InterruptedException
    {
        pclib.resurrectPlayer(player, true);
        return SCRIPT_CONTINUE;
    }
}
