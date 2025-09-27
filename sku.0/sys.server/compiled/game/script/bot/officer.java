package script.bot;/*
@Origin: script.bot.officer
@Author: BubbaJoeX
@Purpose: AI for a stationary officer bot.
@Requirements: <no requirements>
@Note: Attach to a creature object. This script will allow players to receive a crate of Tactical Buffs and Stimpacks.
@Copyright © SWG: Titan 2025
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.chat;
import script.library.static_item;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class officer extends script.base_script
{

    private static final int COOLDOWN_DURATION = 5400;

    public int reInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "Officer Supply Droid");
        setDescriptionStringId(self, string_id.unlocalized("This droid will supply you with Officer related enhancements."));
        setInvulnerable(self, true);
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isDead(self) || isIncapacitated(self) || isIncapacitated(player) || isDead(player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Request Stimpacks"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(self) || isIncapacitated(self) || isIncapacitated(player) || isDead(player) || getState(player, STATE_COMBAT) == 1)
        {
            return SCRIPT_CONTINUE;
        }

        if (item == menu_info_types.SERVER_MENU1)
        {
            handleStimpackRequest(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    private void handleStimpackRequest(obj_id self, obj_id player) throws InterruptedException
    {
        if (hasCooldown(player, "supply_droid.cooldown"))
        {
            int cooldown = getIntObjVar(player, "supply_droid.cooldown");
            if (getGameTime() >= cooldown)
            {
                dispenseStimpacks(self, player);
            }
            else
            {
                notifyCooldown(player, cooldown);
            }
        }
        else
        {
            dispenseStimpacks(self, player);
        }
    }

    private boolean hasCooldown(obj_id player, String objVarName)
    {
        return hasObjVar(player, objVarName);
    }

    private boolean checkAndNotifyCooldown(obj_id player, String objVarName, int buffTime)
    {
        if (hasCooldown(player, objVarName))
        {
            int cooldown = getIntObjVar(player, objVarName);
            if (getGameTime() > cooldown)
            {
                broadcast(player, "You must wait " + ((cooldown - getGameTime()) / 60) + " minutes before receiving another enhancement of this caliber.");
                return false;
            }
        }
        return true;
    }

    private void notifyCooldown(obj_id player, int cooldown)
    {
        broadcast(player, "You must wait " + ((cooldown - getGameTime()) / 60) + " minutes before receiving additional stimpacks.");
    }

    private void dispenseStimpacks(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id crate = createSupplyCrate(player);
        fillSupplyCrate(crate);
        setCooldown(player);
        chat.chat(self, "Directive received, dispensing stimpacks...");
    }

    private obj_id createSupplyCrate(obj_id player) throws InterruptedException
    {
        obj_id crate = createObject("object/tangible/container/drum/supply_drop_crate.iff", getLocation(player));
        utils.setScriptVar(crate, "supply_drop.crateOwner", player);
        attachScript(crate, "systems.combat.combat_supply_drop_crate");
        return crate;
    }

    private void fillSupplyCrate(obj_id crate) throws InterruptedException
    {
        static_item.createNewItemFunction("item_off_temp_stimpack_02_06", crate, 25);
        static_item.createNewItemFunction("item_off_temp_tactical_buff_02_06", crate, 25);
    }

    private void setCooldown(obj_id player)
    {
        setObjVar(player, "supply_droid.cooldown", getGameTime() + COOLDOWN_DURATION);
    }
}