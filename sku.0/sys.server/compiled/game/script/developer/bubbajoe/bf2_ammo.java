package script.developer.bubbajoe;

/*
@Origin: script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Commando grenade droid. Scrapped Battlefield 2.0 content.
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing, or sharing of this file is prohibited.
 */

import script.*;
import script.library.ai_lib;
import script.library.colors;
import script.library.combat;

import java.util.ArrayList;

public class bf2_ammo extends script.base_script
{
    private static final String BOMB_DROID_PROMPT = "Processing request...";
    private static final String BOMB_DROID_BOOM = "goBoom";
    private static final float TARGET_SCAN_RANGE = 64.0f;

    public int OnAttach(obj_id self)
    {
        setDescriptionString(self, "This EG-6 grenade droid is a prototype model. It is designed to follow its owner and lob grenades at its master's enemies.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Activate Bomb Droid"));
        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Dismiss"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int mi) throws InterruptedException
    {
        if (mi == menu_info_types.ITEM_USE)
        {
            if (!hasObjVar(self, "loaded"))
            {
                activateBombDroid(self, player);
            }
            else
            {
                broadcast(player, "This bomb droid is already active.");
            }
        }
        else if (mi == menu_info_types.SERVER_MENU1)
        {
            dismissBombDroid(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    private void activateBombDroid(obj_id self, obj_id player) throws InterruptedException
    {
        setInvulnerable(self, true);
        setObjVar(self, "loaded", true);
        setLevel(self, 85);
        setOwner(self, player);
        follow(self, player, 0, 5);
        setMovementRun(self);
        setMovementPercent(self, 2.4f);
        setRandomColor(self);
        setHealth(self, 25000);
        setHitpoints(self, 25000);

        if (getGroupObject(player) != null)
        {
            ai_lib.establishAgroLink(self, getGroupMemberIds(getGroupObject(player)));
        }
        else
        {
            ai_lib.establishAgroLink(self, 64f);
        }

        // Start continuous grenade lobbing
        messageTo(self, BOMB_DROID_BOOM, null, 5f, true);
    }

    private void dismissBombDroid(obj_id self, obj_id player)
    {
        destroyObject(self);
        broadcast(player, "You have dismissed an Experimental EG-6 Grenadier Droid.");
    }

    public void setRandomColor(obj_id self) throws InterruptedException
    {
        int r = rand(0, 255);
        int g = rand(0, 255);
        int b = rand(0, 255);
        setPalcolorCustomVarClosestColor(self, "/private/index_color_1", r, g, b, 1);
        setPalcolorCustomVarClosestColor(self, "/private/index_color_2", r, g, b, 1);
    }

    public void goBoom(obj_id self) throws InterruptedException
    {
        obj_id owner = getObjIdObjVar(self, "owner");
        obj_id[] targets = getCreaturesInRange(getLocation(self), TARGET_SCAN_RANGE);

        if (targets == null || targets.length == 0)
        {
            showFlyText(self, new string_id("No targets detected"), 1.5f, colors.RED);
        }
        else
        {
            obj_id target = selectRandomTarget(self, targets);
            if (isIdValid(target))
            {
                executeGrenadeAttack(self, target);
            }
        }

        // Repeat the grenade lobbing every 15 seconds if a valid target is nearby
        messageTo(self, BOMB_DROID_BOOM, null, 15, true);
        setRandomColor(self);
    }

    private obj_id selectRandomTarget(obj_id self, obj_id[] targets)
    {
        ArrayList<obj_id> validTargets = new ArrayList<>();
        for (obj_id target : targets)
        {
            // Ensure target is not the droid itself
            if (target != self)
            {
                if (isInvulnerable(target) || !isMob(target)) continue;
                if (isPlayer(target)) continue;
                if (isDead(target)) continue;
                if (isIncapacitated(target)) continue;
                validTargets.add(target);
            }
        }
        if (validTargets.isEmpty())
        {
            return null;
        }
        return validTargets.get(rand(0, validTargets.size() - 1));
    }


    private void executeGrenadeAttack(obj_id self, obj_id target) throws InterruptedException
    {
        combat.startCombat(self, target);
        wait(100);
        int damage = rand(getMaxHealth(target) / 4, getMaxHealth(target) / 2);
        playClientEffectLoc(target, "clienteffect/avatar_explosion_01.cef", getLocation(target), 5.0f);
        playClientEffectLoc(target, "clienteffect/avatar_explosion_02.cef", getLocation(target), 2.0f);

        combat_engine.hit_result hitData = new combat_engine.hit_result();
        hitData.success = true;
        hitData.damage = damage;
        hitData.critDamage = damage / 2 / 4;
        hitData.critical = true;
        hitData.attackVal = 100f;

        doDamage(self, target, getHeldWeapon(self), hitData);
        showFlyText(self, new string_id("Grenade launched at " + getName(target)), 1.5f, colors.YELLOW);

        broadcast(getObjIdObjVar(self, "owner"), "The EG-6 Power Droid has lobbed a grenade at " + getEncodedName(target) + "!");
    }
}

