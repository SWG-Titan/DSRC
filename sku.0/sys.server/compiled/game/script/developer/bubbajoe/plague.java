package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Poisons all enemies within a 25m radius of the grenade's impact point.
@TODO: Make range customizable.
@Created: Saturday, 3/30/2024, at 4:03 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

import java.util.Vector;

public class plague extends script.base_script
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
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Throw Grenade"));
        mi.addRootMenu(menu_info_types.SERVER_MENU10, new string_id("Set Damage"));
        mi.addRootMenu(menu_info_types.SERVER_MENU11, new string_id("Set Delay"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU10)
        {
            sui.inputbox(self, player, "Enter the amount of damage to inflict.", "OnUpdateDamage", "OnUpdateDamage", 250, false, "100000");
        }
        if (item == menu_info_types.SERVER_MENU11)
        {
            sui.inputbox(self, player, "Enter the poison delay in seconds.", "OnUpdateDelay", "OnUpdateDelay", 250, false, "3");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        doAnimationAction(player, "dance");
        location whereAmIGoing = getLocation(self);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        if (!isInWorldCell(player))
        {
            whereAmIGoing.cell = getContainedBy(player);
        }
        obj_id[] enemies = getCreaturesInRange(whereAmIGoing, 25f);
        for (obj_id target : enemies)
        {
            if (target == player)
            {
                continue;
            }
            startCombat(player, target);
            dioxis(self, target, player);
        }
        broadcast(player, "You have thrown a poison grenade, targeting " + enemies.length + " enemies.");
        return SCRIPT_CONTINUE;
    }

    public void dioxis(obj_id self, obj_id victim, obj_id player)
    {
        playClientEffectObj(player, "clienteffect/combat_grenade_poison.cef", victim, "head");
        playClientEffectObj(player, "clienteffect/combat_grenade_poison.cef", victim, "hand_r");
        playClientEffectObj(player, "clienteffect/combat_grenade_poison.cef", victim, "root");
        dictionary packedPlayer = new dictionary();
        packedPlayer.put("victim", victim);
        packedPlayer.put("attacker", player);
        messageTo(self, "damageCreature", packedPlayer, getFloatObjVar(self, "delay"), false);
    }

    public int damageCreature(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id victim = params.getObjId("victim");
        obj_id attacker = params.getObjId("attacker");
        final String ATTACK_TYPE = "combat_rangedspecialize_pistol";
        int damage = getIntObjVar(self, "damage");
        Vector attackerList = utils.getResizeableObjIdBatchScriptVar(victim, "creditForKills.attackerList.attackers");
        attackerList = utils.addElement(attackerList, self);
        utils.setBatchScriptVar(victim, "creditForKills.attackerList.attackers", attackerList);
        utils.setScriptVar(victim, "creditForKills.attackerList." + attacker + ".damage", damage);
        utils.setScriptVar(victim, "creditForKills.damageCount", 100);
        utils.setScriptVar(victim, "creditForKills.damageTally", damage);
        Vector types = utils.getResizeableObjIdBatchScriptVar(victim, "creditForKills.attackerList." + self + ".xp.types");
        types = utils.addElement(types, ATTACK_TYPE);
        utils.setBatchScriptVar(victim, "creditForKills.attackerList." + self + ".xp.types", types);
        utils.setScriptVar(victim, "creditForKills.attackerList." + self + ".xp." + ATTACK_TYPE, damage);
        damage(victim, DAMAGE_ELEMENTAL_ELECTRICAL, HIT_LOCATION_BODY, damage);
        broadcast(attacker, "You have poisoned " + (isMob(victim) ? getCreatureName(victim) : getName(victim)));
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateDamage(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        int damage = Integer.parseInt(sui.getInputBoxText(params));
        setObjVar(self, "damage", damage);
        sendSystemMessage(player, "Damage dealt set to " + damage + ".", null);
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateDelay(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        float delay = Float.parseFloat(sui.getInputBoxText(params));
        setObjVar(self, "delay", delay);
        sendSystemMessage(player, "Poison delay set to " + delay + " seconds.", null);
        return SCRIPT_CONTINUE;
    }
}
