package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Spawns mobs inside the event room
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 6:19 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.sui;
import script.library.utils;

public class tos_event_spawner extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isGod(player) || hasObjVar(player, "event_helper"))
        {
            int dad = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Spawn: " + getStringObjVar(self, "event_mob")));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU1, new string_id("Set Mobile"));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU2, new string_id("Set Count | " + getIntObjVar(self, "event_count")));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU3, new string_id("Set Loot | " + getStringObjVar(self, "loot.lootTable") + " | " + getIntObjVar(self, "loot.numItems")));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player) || hasObjVar(player, "event_helper"))
        {
            if (item == menu_info_types.ITEM_USE)
            {
                if (hasObjVar(self, "event_mob"))
                {
                    location here = getLocation(self);
                    here.cell = getContainedBy(self);
                    obj_id[] mobs = getCreaturesInRange(here, 30);
                    for (obj_id objId : mobs)
                    {
                        if (hasObjVar(objId, "spawnerParent"))
                        {
                            destroyObject(objId);
                        }
                    }
                    for (int i = 0; i < getIntObjVar(self, "event_count"); i++)
                    {
                        here.x = here.x + rand(-5, 5);
                        here.z = here.z + rand(-5, 5);
                        obj_id mob = create.object(getStringObjVar(self, "event_mob"), here);
                        setObjVar(mob, "spawnerParent", self);
                        if (hasObjVar(self, "loot.lootTable"))
                        {
                            setObjVar(mob, "loot.lootTable", getStringObjVar(self, "loot.lootTable"));
                            setObjVar(mob, "loot.numItems", getIntObjVar(self, "loot.numItems"));
                        }
                        setHologramType(mob, HOLOGRAM_TYPE1_QUALITY4);
                    }
                }
                else
                {
                    broadcast(player, "You must set a mob to spawn first.");
                }
            }
            else if (item == menu_info_types.SERVER_MENU1)
            {
                broadcast(self, "Getting mob list. DO NOT SPAM.");
                String[] mobs = dataTableGetStringColumn("datatables/mob/creatures.iff", "creatureName");
                sui.inputbox(self, player, "Enter the mob string to spawn.", sui.OK_CANCEL, "Event Room Spawner", sui.INPUT_COMBO, mobs, "handleSetMobile", null);
            }
            else if (item == menu_info_types.SERVER_MENU2)
            {
                sui.inputbox(self, player, "Enter the number of mobs to spawn.", sui.OK_CANCEL, "Event Room Spawner", sui.INPUT_NORMAL, null, "handleSetCount", null);
            }
            else if (item == menu_info_types.SERVER_MENU3)
            {
                sui.inputbox(self, player, "Step 1/2: Enter the loot table to use. \nExample: rls/rare_loot", sui.OK_CANCEL, "Event Room Spawner", sui.INPUT_NORMAL, null, "handleSetLootTable", null);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetMobile(obj_id self, dictionary params) throws InterruptedException
    {
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String mob = sui.getInputBoxText(params);
        if (mob == null || mob.equals(""))
        {
            String selection = sui.getComboBoxText(params);
            broadcast(self, "Mob set to: " + mob);
            setObjVar(self, "event_mob", selection);
            return SCRIPT_CONTINUE;
        }
        else
        {
            setObjVar(self, "event_mob", mob);
            broadcast(self, "Mob set to: " + mob);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetCount(obj_id self, dictionary params) throws InterruptedException
    {
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String howMany = sui.getInputBoxText(params);
        int count = utils.stringToInt(howMany);
        setObjVar(self, "event_count", count);
        broadcast(self, "Mob Count set to: " + count);
        return SCRIPT_CONTINUE;
    }

    public int handleSetLootTable(obj_id self, dictionary params) throws InterruptedException
    {
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String lootTable = sui.getInputBoxText(params);
        if (lootTable == null || lootTable.equals(""))
        {
            broadcast(self, "You must enter a loot table, or exit to use the default loot table from creature.iff");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "loot.lootTable", lootTable);
        sui.inputbox(self, sui.getPlayerId(params), "Step 2/2: Enter the number of items to spawn.", sui.OK_CANCEL, "Event Room Spawner", sui.INPUT_NORMAL, null, "handleSetLootCount", null);
        return SCRIPT_CONTINUE;
    }

    public int handleSetLootCount(obj_id self, dictionary params) throws InterruptedException
    {
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String howMany = sui.getInputBoxText(params);
        if (howMany == null || howMany.equals(""))
        {
            broadcast(self, "You must enter a number of items to spawn.");
            return SCRIPT_CONTINUE;
        }
        int count = utils.stringToInt(howMany);
        setObjVar(self, "loot.numItems", count);
        broadcast(self, "Loot Count set to: " + count);
        return SCRIPT_CONTINUE;
    }
}
