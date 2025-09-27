package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Gives buff on click
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 5/16/2024, at 8:46 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class tos_buffer extends script.base_script
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
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }

        if (isIdValid(player) && isIdValid(self))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Apply Buff"));
        }

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }

        if (item == menu_info_types.SERVER_MENU1)
        {
            if (isIdValid(player) && isIdValid(self))
            {
                buff.applyBuff(player, "event_combat", 3600);
            }
        }

        return SCRIPT_CONTINUE;
    }
}
