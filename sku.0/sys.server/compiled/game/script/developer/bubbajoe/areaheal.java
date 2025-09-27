package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Three click modes: Heal, Revive, Buff. For events.
@Created: Saturday, 3/30/2024, at 7:12 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.buff;
import script.library.pclib;
import script.library.sui;
import script.library.utils;

public class areaheal extends script.base_script
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
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        int base = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Use Area Ability"));
        mi.addSubMenu(base, menu_info_types.SERVER_MENU10, new string_id("Set Mode"));
        mi.addSubMenu(base, menu_info_types.SERVER_MENU12, new string_id("Set Range"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU10)
        {
            sui.inputbox(self, player, "Enter the mode you want to use. 1 = Heal, 2 = Revive, 3 = Buff", "Mode Setting", "OnUpdateMode", "1");
        }
        else if (item == menu_info_types.SERVER_MENU12)
        {
            sui.inputbox(self, player, "Enter the range you want to heal/revive/buff players in.", "Range Setting", "OnUpdateRange", "25");
        }
        return SCRIPT_CONTINUE;
    }

    private int getMode(obj_id self)
    {
        return getIntObjVar(self, "gm.heal_mode");
    }

    private int setMode(obj_id self, int mode)
    {
        if (mode == 1)
        {
            setObjVar(self, "gm.heal_mode", 1);
        }
        else if (mode == 2)
        {
            setObjVar(self, "gm.heal_mode", 2);
        }
        else
        {
            setObjVar(self, "gm.heal_mode", 3);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        location whereAmIGoing = getLocation(self);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        if (!isInWorldCell(player))
        {
            whereAmIGoing.cell = getContainedBy(player);
        }
        float range = getFloatObjVar(self, "gm.heal_range");
        obj_id[] players = getPlayerCreaturesInRange(whereAmIGoing, range);
        if (players.length == 0)
        {
            broadcast(player, "There are no players in that area.");
            return SCRIPT_CONTINUE;
        }
        for (obj_id objId : players)
        {
            /*if (player == players[i])
            {
                continue;
            }*/
            switch (getMode(self))
            {
                case 1:
                    gmHeal(player, objId);
                    break;
                case 2:
                    gmRevive(player, objId);
                    break;
                case 3:
                    gmBuff(player, objId);
                    break;
            }
        }

        return SCRIPT_CONTINUE;
    }

    private void gmBuff(obj_id player, obj_id breacher) throws InterruptedException
    {
        int buffTime = 3600;
        buff.applyBuff(breacher, "me_buff_health_2", (float) buffTime, 245);
        buff.applyBuff(breacher, "me_buff_action_3", (float) buffTime, 245);
        buff.applyBuff(breacher, "me_buff_strength_3", (float) buffTime, 75);
        buff.applyBuff(breacher, "me_buff_agility_3", (float) buffTime, 75);
        buff.applyBuff(breacher, "me_buff_precision_3", (float) buffTime, 75);
        buff.applyBuff(breacher, "me_buff_melee_gb_1", (float) buffTime, 10);
        buff.applyBuff(breacher, "me_buff_ranged_gb_1", (float) buffTime, 5);
        LOG("ethereal", "[AreaBuff]: Player " + getPlayerFullName(breacher) + " has been buffed with a full medic buff set. Values set to [245, 245, 75, 75, 75, 10 5].");
        broadcast(player, "You have buffed " + getPlayerFullName(breacher) + " with a full medic buff set.");
        play2dNonLoopingSound(player, "sound/tcg_buff_large.snd");
        showFlyText(player, new string_id("[EVENT] BUFF"), 2.5f, color.DEEPPINK);
    }

    public void gmRevive(obj_id player, obj_id subject) throws InterruptedException
    {
        if (buff.hasBuff(player, "cloning_sickness"))
        {
            buff.removeBuff(player, "cloning_sickness");
        }
        pclib.clearCombatData(subject);
        buff.removeAllDebuffs(subject);
        removeObjVar(subject, "combat.intIncapacitationCount");
        setPosture(subject, POSTURE_UPRIGHT);
        queueCommand(subject, (-1465754503), subject, "", COMMAND_PRIORITY_IMMEDIATE);
        queueCommand(subject, (-562996732), subject, "", COMMAND_PRIORITY_IMMEDIATE);
        utils.removeScriptVar(subject, "pvp_death");
        play2dNonLoopingSound(player, "sound/vo_meddroid_01.snd");
        prose_package pp = new prose_package();
        String[] FUNNY_REVIVE_MESSAGES = {
                "Get up, you lazy bum!",
                "You're not dead yet, get up!",
                "You're not getting out of this that easily!",
                "You're not dead until I say you're dead!",
                "Stop diddling around and get up!",
                "Stop being a baby and get up!",
                "Han Solo would have shot you by now!",
                "Chewbacca would have ripped your arms off by now!",
                "Even a Stormtrooper could have hit you by now!",
        };
        String finalMessage = FUNNY_REVIVE_MESSAGES[rand(0, FUNNY_REVIVE_MESSAGES.length - 1)];
        pp.stringId = new string_id(finalMessage);
        pp.actor.set(player);
        pp.target.set(subject);
        pp.other.set("");
        showFlyText(player, new string_id("[EVENT] REVIVE"), 2.5f, color.GOLDENROD);
        commPlayer(player, subject, pp);
    }

    public int gmHeal(obj_id owner, obj_id player)
    {
        int currentHealth = getHealth(player);
        int currentAction = getAction(player);
        if (currentHealth <= 0)
        {
            broadcast(owner, getPlayerFullName(player) + " is dead and cannot be healed. Switch to revive mode.");
            return SCRIPT_CONTINUE;
        }
        int maxHealth = getMaxHealth(player);
        int maxAction = getMaxAction(player);
        int healingNeeded = maxHealth - currentHealth;
        if (healingNeeded <= 0)
        {
            broadcast(owner, getPlayerFullName(player) + " is already at full health.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            setHealth(player, maxHealth);
            setAction(player, maxAction);
            play2dNonLoopingSound(player, "sound/tcg_buff_small.snd");
            playClientEffectLoc(player, "clienteffect/bacta_bomb.cef", getLocation(player), 1.0f);
            showFlyText(player, new string_id("[EVENT] HEAL"), 2.5f, color.RED);
            broadcast(owner, "You have healed " + getPlayerFullName(player) + " for " + healingNeeded + " health.");
            LOG("ethereal", "[AreaHeal]: Player " + getPlayerFullName(player) + " has been healed by the Event Team.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateMode(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            setMode(self, utils.stringToInt(text));
            LOG("ethereal", "[AreaHeal]: " + getPlayerFullName(player) + " has modified area heal object to use mode " + text);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateRange(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            setObjVar(self, "gm.heal_range", utils.stringToFloat(text));
            LOG("ethereal", "[AreaHeal]: " + getPlayerFullName(player) + " has modified area heal object to use range " + text);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = utils.packStringId(new string_id("Utiility Mode"));
        if (getMode(self) == 1)
        {
            attribs[idx] = "Area Heal";
        }
        else if (getMode(self) == 2)
        {
            attribs[idx] = "Area Revive";
        }
        else if (getMode(self) == 3)
        {
            attribs[idx] = "Area Buff";
        }
        else
        {
            attribs[idx] = "Unknown";
        }
        return SCRIPT_CONTINUE;
    }
}
