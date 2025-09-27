package script.content.nb_quest;/*
@Origin: dsrc.script.content.nb_quest
@Author:  BubbaJoeX
@Purpose: Trial of the Elder quest
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 9/7/2024, at 3:23 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.groundquests;
import script.library.utils;
import script.*;

public class trial_kickoff extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "a datapad");
        setDescriptionString(self, "A datapad with a message on it.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "a datapad");
        setDescriptionString(self, "A datapad with a message on it.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (utils.isProfession(player, utils.FORCE_SENSITIVE) && !groundquests.isQuestActive(player, "quest/trial_of_the_elder") || isGod(player))
        {
            int mnu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Establish Communications"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (isDead(player) || isIncapacitated(player))
            {
                return SCRIPT_CONTINUE;
            }
            if (utils.isProfession(player, utils.FORCE_SENSITIVE) && !groundquests.isQuestActiveOrComplete(player, "quest/trial_of_the_elder") || isGod(player))
            {
                groundquests.grantQuest(player, "quest/trial_of_the_elder");
                prose_package p = new prose_package();
                p.stringId = new string_id("Complete these tasks my young force-wielder, and you will be rewarded with the knowledge of the Elder and the robes to match. May the Force be with you.");
                commPlayer(self, player, p, "object/mobile/dressed_jedi_trainer_old_human_male_01.iff");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
