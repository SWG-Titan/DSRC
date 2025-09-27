package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: heals over time
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 9/16/2024, at 8:21 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.library.pclib;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.menu_info;
import script.dictionary;

import static script.library.utils.removeScriptVar;

public class heal_over_time extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        broadcast(self, "Bubba's Life Shield Active.");
        messageTo(self, "checkAndHealPlayer", null, 1, false);
        messageTo(self, "resetCooldown", null, 0.25f, false);
        return SCRIPT_CONTINUE;
    }

    public int OnDetach(obj_id self)
    {
        broadcast(self, "Bubba's Life Shield Deactivated.");
        stopListeningToMessage(self, "checkAndHealPlayer");
        stopListeningToMessage(self, "resetCooldown");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int checkAndHealPlayer(obj_id self, dictionary params) throws InterruptedException
    {
        if (isDead(self))
        {
            revivePlayer(self);
            healPlayer(self);
        }

        if (isIncapacitated(self))
        {
            revivePlayer(self);
            healPlayer(self);
        }

        if (getHealth(self) < getMaxHealth(self))
        {
            healPlayer(self);
        }
        messageTo(self, "checkAndHealPlayer", null, 15, false);
        return SCRIPT_CONTINUE;
    }

    private void revivePlayer(obj_id player) throws InterruptedException
    {
        pclib.clearEffectsForDeath(player);
        setAttrib(player, HEALTH, getMaxHealth(player));
        setAttrib(player, ACTION, getMaxAction(player));
        int[] buffList = buff.getAllBuffs(player);
        messageTo(player, "handlePlayerResuscitated", null, 0, true);
        buff.removeAllDebuffs(player);
        for (int i = 0; i < buffList.length; i++)
        {
            if (buff.hasBuff(player, buffList[i]))
            {
                if (buff.isDebuff(buffList[i]))
                {
                    continue;
                }
                float time = buff.getDuration(buffList[i]);
                float value = buff.getEffectValue(buffList[i], 1);
                buff.applyBuff(player, buffList[i], time, value);
            }
        }
        broadcast(player, "You have been restored.");
    }

    private void healPlayer(obj_id self)
    {
        setHealth(self, getMaxHealth(self));
        setAction(self, getMaxAction(self));
    }

    public int resetCooldown(obj_id self, dictionary params) throws InterruptedException
    {
        sendConsoleCommand("/resetCooldowns", self);
        messageTo(self, "resetCooldown", null, 0.5f, false);
        return SCRIPT_CONTINUE;
    }
}
