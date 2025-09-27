package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: Moves god character to overlook
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 8/15/2024, at 12:44 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class stadium_ladder extends base_script
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
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Climb Ladder"));

        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (isGod(player))
            {
                warpPlayer(player, "adventure3", 3.9185257f, 7.3122206f, 4927.135f, null, 0, 0, 0, "", false);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
