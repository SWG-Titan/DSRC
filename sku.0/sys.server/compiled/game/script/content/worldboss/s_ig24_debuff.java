package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss
@Author:  BubbaJoeX
@Purpose: Removes IG-24's Droideka Shield upon clicking the object nearby.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 7/31/2024, at 10:09 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.buff;
import script.library.chat;
import script.library.titan_player;

public class s_ig24_debuff extends base_script
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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Send Feedback Pulse"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id[] possibleIG24 = getAllObjectsWithScript(getLocation(self), titan_player.WORLD_BOSS_CREDIT_RANGE, "theme_park.world_boss.master_controller_ig24");
            if (possibleIG24.length == 0)
            {
                broadcast(player, "This terminal seems to be malfunctioning.");
                return SCRIPT_CONTINUE;
            }
            for (obj_id potential : possibleIG24)
            {
                if (getTemplateName(potential).endsWith("ig_88_rocket.iff"))
                {
                    if (!buff.hasBuff(potential, "ig88_droideka_buff"))
                    {
                        broadcast(player, "The system seems to be locked down.");
                        return SCRIPT_CONTINUE;
                    }
                    obj_id[] players = getAllPlayers(getLocation(potential), titan_player.WORLD_BOSS_CREDIT_RANGE);
                    for (obj_id solo : players)
                    {
                        broadcast(solo, "IG-24 has had his shield knocked out. Attack now!");
                    }
                    chat.chat(self, "MALFUNCTION... MALFUNCTION...");
                    buff.removeAllBuffs(potential);
                    buff.removeAllDebuffs(potential);
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
