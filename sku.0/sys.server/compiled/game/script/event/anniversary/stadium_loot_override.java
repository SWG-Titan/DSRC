package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: Adds or overrides loot spawned upon corpse prepare
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 8/17/2024, at 1:50 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.loot;
import script.library.static_item;
import script.library.utils;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.menu_info;
import script.dictionary;

public class stadium_loot_override extends script.base_script
{
    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        String[] VALID_LOOT_TABLES = {
                "droid/droid_81_90",
                "droid/elite_droid:elite_droid_81_90",
                "npc/npc_81_90",
                "npc_81_90:force_light",
                "npc_81_90:force_dark",
                "rls/comp_loot_generic",
                "treasure/treasure_81_90",
        };
        obj_id inv = utils.getInventoryContainer(self);
        int chance = rand(1, 100);
        if (chance > 50)
        {
            for (int i = 0; i < VALID_LOOT_TABLES.length; i++)
            {
                loot.makeLootInContainer(inv, VALID_LOOT_TABLES[rand(0, VALID_LOOT_TABLES.length - 1)], 3, rand(1, 149));
            }
        }
        else
        {
            for (int i = 0; i < VALID_LOOT_TABLES.length; i++)
            {
                loot.makeLootInContainer(inv, VALID_LOOT_TABLES[rand(0, VALID_LOOT_TABLES.length - 1)], 3, rand(1, 149));
            }
        }
        return SCRIPT_CONTINUE;
    }
}
