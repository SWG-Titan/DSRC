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

import script.library.buff;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class stadium_buffer extends stadium_lib
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Stadium Buff Station");
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
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Enhance Stadium Participants"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.ITEM_USE)
            {
                areaBuff(player);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int areaBuff(obj_id self) throws InterruptedException
    {
        obj_id[] players = getPlayerCreaturesInRange(self, TOOL_RANGE);
        if (players != null && players.length > 0)
        {
            for (obj_id player : players)
            {
                if (isGod(player))
                {
                    broadcast(self, "Skipping buff on god character. ( " + getPlayerFullName(player) + ")");
                    continue;
                }
                buff(player);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int buff(obj_id player) throws InterruptedException
    {
        buff.applyBuff(player, "me_buff_health_2", 7200f, 245);
        buff.applyBuff(player, "me_buff_action_3", 7200f, 245);
        buff.applyBuff(player, "me_buff_strength_3", 7200f, 75);
        buff.applyBuff(player, "me_buff_agility_3", 7200f, 75);
        buff.applyBuff(player, "me_buff_precision_3", 7200f, 75);
        buff.applyBuff(player, "me_buff_melee_gb_1", 7200f, 10);
        buff.applyBuff(player, "me_buff_ranged_gb_1", 7200f, 5);
        buff.applyBuff((player), "of_buff_def_9", 7200);
        buff.applyBuff((player), "of_focus_fire_6", 7200);
        buff.applyBuff((player), "of_tactical_drop_6", 7200);
        buff.applyBuff((player), "event_buff_gm", 7200, 25);
        setHealth(player, getMaxHealth(player));
        return SCRIPT_CONTINUE;
    }
}
