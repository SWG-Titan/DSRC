package script.terminal;/*
@Origin: dsrc.script.terminal
@Author:  BubbaJoeX
@Purpose: Daily and Weekly mission giver
@Requirements: <no requirements>
@Notes: Hook up with signals
@Created: Monday, 9/9/2024, at 4:30 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.repeatables;

public class terminal_repeatables extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        check(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        check(self);
        return SCRIPT_CONTINUE;
    }

    public int check(obj_id self)
    {
        String questType = getStringObjVar(self, "questType");
        if (questType == null)
        {
            setName(self, "a broken terminal");
        }
        else
        {
            setName(self, "Daily Missions");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Grant Missions"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            String questType = getStringObjVar(self, "questType");
            repeatables.grantRepeatable(player, questType);
        }
        return SCRIPT_CONTINUE;
    }
}
