package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: AOE Killer, smite thee
@Created: Saturday, 3/30/2024, at 4:03 AM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class smite extends script.base_script
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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Smite"));
        mi.addRootMenu(menu_info_types.SERVER_MENU10, new string_id("Set Damage"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU10)
            {
                sui.inputbox(self, player, "Enter the amount of damage to inflict.", "OnUpdateDamage", "OnUpdateDamage", 250, false, "100000");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        doAnimationAction(player, "laugh");
        location whereAmIGoing = getLocation(self);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        if (!isInWorldCell(player))
        {
            whereAmIGoing.cell = getContainedBy(player);
        }
        obj_id[] enemies = getCreaturesInRange(whereAmIGoing, 25f);
        if (enemies.length == 0)
        {
            broadcast(player, "There are no enemies in that area..");
            return SCRIPT_CONTINUE;
        }
        for (obj_id target : enemies)
        {
            if (target == player)
            {
                continue;
            }
            strike(self, target, player);
        }
        broadcast(player, "You have smited " + enemies.length + " plebeians.");
        return SCRIPT_CONTINUE;
    }

    public void strike(obj_id self, obj_id victim, obj_id player)
    {
        String EFFECT = "appearance/pt_hit_lightning.prt";
        String SOUNDEFFECT = "sound/wtr_lightning_strike.snd";
        obj_id[] players = getAllPlayers(getLocation(victim), 2000.0f);
        playClientEffectLoc(players, EFFECT, getLocation(victim), 0.0f);
        playClientEffectLoc(players, EFFECT, getLocation(victim), 2.0f);
        playClientEffectLoc(players, EFFECT, getLocation(victim), 5.5f);
        playClientEffectLoc(players, SOUNDEFFECT, getLocation(victim), 0.0f);
        if (!isPlayer(victim) && isMob(victim))
        {
            damage(victim, DAMAGE_ELEMENTAL_ELECTRICAL, HIT_LOCATION_BODY, getIntObjVar(self, "damage"));
        }
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
}
