package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Trigger volume playground
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 11/2/2024, at 2:37 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.utils;

public class forcefield extends script.base_script
{
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (isGod(self))
        {
            broadcast(self, "sorry :(");
            detachMe(self);
        }
        broadcast(self, "Bubba's Rayshield is online.");
        createForceField(self);
        return SCRIPT_CONTINUE;
    }

    public int OnDetach(obj_id self) throws InterruptedException
    {
        removeForceField(self);
        broadcast(self, "Bubba's Rayshield is offline.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        createForceField(self);
        return SCRIPT_CONTINUE;
    }

    public void createForceField(obj_id self)
    {
        createTriggerVolume("gm-forcefield", 4.0f, true);
    }

    public void removeForceField(obj_id self)
    {
        removeTriggerVolume("gm-forcefield");
        removeTriggerVolumeEventSource("gm-forcefield", self);
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (volumeName.equals("gm-forcefield"))
        {
            if (isMob(breacher) && !isInvulnerable(breacher) && (getCondition(breacher) != CONDITION_CONVERSABLE) && !isDead(breacher))
            {
                setHealth(self, getMaxHealth(self));
                startCombat(breacher, self);
                playClientEffectLoc(breacher, "clienteffect/combat_slg_1_hit_creature.cef", getLocation(breacher), 1.2f);
                int damageAmt = rand(8979, 12929) * 20;
                int elementalAmt = rand(79, 129);
                int hitLoc = rand(0, 4);
                combat_engine.hit_result attack = new combat_engine.hit_result();
                attack.success = true;
                attack.damage = damageAmt;
                attack.dodge = false;
                attack.damageType = DAMAGE_ENERGY;
                attack.elementalDamageType = DAMAGE_ELEMENTAL_HEAT;
                attack.elementalDamage = elementalAmt;
                attack.bleedDamage = 50;
                attack.glancing = true;
                attack.hitLocation = hitLoc;
                attack.finalRoll = 100;
                attack.rawDamage = damageAmt;
                attack.miss = false;
                attack.strikethrough = true;
                attack.strikethroughAmmount = 100f;
                attack.blockedDamage = 0;
                if (doDamage(self, breacher, utils.getHeldWeapon(breacher), attack))
                {
                    broadcast(self, "Frying " + getName(breacher) + " with " + attack.damage + " damage and " + attack.elementalDamage + " heat damage.");
                }
                else
                {
                    broadcast(self, "Could not damage " + getName(breacher) + " with " + attack.damage + " damage and " + attack.elementalDamage + " heat damage.");
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnSpeaking(obj_id self, String text) throws InterruptedException, ClassNotFoundException
    {
        if (!isGod(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (text.equalsIgnoreCase("rayshieldoff"))
        {
            detachMe(self);
        }
        return SCRIPT_CONTINUE;
    }

    public void detachMe(obj_id self)
    {
        detachScript(self, "developer.bubbajoe.forcefield");
    }
}
