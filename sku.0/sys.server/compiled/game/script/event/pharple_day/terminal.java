package script.event.pharple_day;/*
@Origin: dsrc.script.event.pharple_day
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 11/18/2024, at 4:59 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.groundquests;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class terminal extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Pharple Day Job Board");
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Pharple Day Job Board");
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Retrieve Job Posting"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!groundquests.hasCompletedQuest(player, "pharple_day"))
        {
            if (groundquests.isQuestActive(player, "pharple_day"))
            {
                broadcast(player, "You already have this quest.");
            }
            else
            {
                groundquests.grantQuest(player, "pharple_day");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
