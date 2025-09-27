package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author:  BubbaJoeX
@Purpose: Handles the mob and respawn timers
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 11/20/2024, at 2:36 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.ai_lib;
import script.library.loot;
import script.library.utils;
import script.obj_id;
import script.dictionary;

public class gift_workshop_mob extends script.base_script
{
    public static final float TRIGGER_CYCLE = 30f;

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!hasScript(self, "ai.ai"))
        {
            attachScript(self, "ai.ai");
        }
        ai_lib.setMovementWalk(self);
        ai_lib.setDefaultCalmBehavior(self, ai_lib.BEHAVIOR_LOITER);
        ai_lib.setMood(self, "puzzled");
        ai_lib.setLoiterRanges(self, 1f, 3f);
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        ai_lib.establishAgroLink(self, 6f);
        return SCRIPT_CONTINUE;
    }

    public int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id weapon, int[] damage) throws InterruptedException
    {
        if (isDead(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (utils.hasScriptVar(self, "trigger_cycle"))
        {
            dictionary params = new dictionary();
            params.put("attacker", attacker);
            params.put("weapon", weapon);
            params.put("damage", damage);
            messageTo(self, "triggerCycle", params, TRIGGER_CYCLE, false);
        }
        ai_lib.triggerAgroLinks(self, attacker);
        return SCRIPT_CONTINUE;
    }

    public int triggerCycle(obj_id self, dictionary params) throws InterruptedException
    {
        if (isDead(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (isIncapacitated(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id attacker = params.getObjId("attacker");
        obj_id weapon = params.getObjId("weapon");
        int[] damage = params.getIntArray("damage");
        if (utils.hasScriptVar(self, "trigger_cycle"))
        {
            dictionary newParams = new dictionary();
            newParams.put("attacker", attacker);
            newParams.put("weapon", weapon);
            newParams.put("damage", damage);
            messageTo(self, "triggerCycle", newParams, TRIGGER_CYCLE, false);
        }
        ai_lib.triggerAgroLinks(self, attacker);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        obj_id daddy = getObjIdObjVar(self, "daddy");
        if (daddy == null)
        {
            LOG("events", "[Life Day]: gift_workshop_mob.OnDeath: daddy is null");
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(daddy, "workshop_boss_spawner"))
        {
            messageTo(daddy, "spawnWorkshopBossChild", null, 60f * 5f, false);
        }
        else messageTo(daddy, "spawnWorkshopChild", null, 60f * 5f, false);
        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        String[] lootTables = dataTableGetStringColumn("datatables/adhoc/loot_tables.iff", "LOOT_TABLE");
        if (lootTables == null || lootTables.length == 0)
        {
            return SCRIPT_CONTINUE;
        }
        int lootTableIndex = rand(0, lootTables.length - 1);
        String lootTable = lootTables[lootTableIndex];
        if (lootTable == null || lootTable.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        int count;
        if (hasObjVar(self, "daddy_boss"))
        {
            count = 5;
        }
        else
        {
            count = 2;
        }

        loot.makeLootInContainer(self, lootTable, count, 300);
        return SCRIPT_CONTINUE;
    }
}
