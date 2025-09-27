package script.event.halloween;/*
@Origin: dsrc.script.event.halloween
@Author:  BubbaJoeX
@Purpose: Spawns undead zombies at locations inside the building and handles respawn times per child.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 9/27/2024, at 3:02 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.ai_lib;
import script.library.create;
import script.library.sui;

public class zombie_spawner extends base_script
{
    public String OBJVAR_SPAWN_TIME = "zombie_spawn_time";
    public String OBJVAR_SPAWNED = "zombie_spawned";
    public String OBJVAR_SPAWNED_COUNT = "zombie_spawned_count";
    public String OBJVAR_SPAWNED_MAX = "zombie_spawned_max";
    public int OBJVAR_SPAWNED_MAX_VALUE = 5;
    public String OBJVAR_SPAWNED_TRACKER = "zombie_spawned_tracker";
    public String CREATURE_TYPE_1 = "event_halloween_zombie";
    public String CREATURE_TYPE_2 = "event_halloween_skeleton";

    public int OnAttach(obj_id self)
    {
        startSpawner(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        startSpawner(self);
        return SCRIPT_CONTINUE;
    }

    public void startSpawner(obj_id self)
    {
        if (hasObjVar(self, "zombie_spawn_time"))//skip setting objvars if they already exist
        {
            messageTo(self, "spawnZombies", null, 1, false); // Spawn zombies on attach
        }
        else
        {
            setObjVar(self, OBJVAR_SPAWN_TIME, 300); // Default spawn time
            setObjVar(self, OBJVAR_SPAWNED, 0); // Default spawned
            setObjVar(self, OBJVAR_SPAWNED_COUNT, 0); // Default spawned count
            setObjVar(self, OBJVAR_SPAWNED_MAX, OBJVAR_SPAWNED_MAX_VALUE); // Default spawned max
            setObjVar(self, OBJVAR_SPAWNED_TRACKER, 0); // Default spawned tracker
            messageTo(self, "spawnZombies", null, 1, false); // Spawn zombies on attach
        }
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            int parent = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Menu"));
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU1, new string_id("Get Information"));
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU2, new string_id("Set Respawn Time"));
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU3, new string_id("Force Spawn"));
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU4, new string_id("Kill All Zombies"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            broadcast(player, "Spawn Time: " + getIntObjVar(self, OBJVAR_SPAWN_TIME));
            broadcast(player, "Spawned: " + getIntObjVar(self, OBJVAR_SPAWNED));
            broadcast(player, "Spawned Count: " + getIntObjVar(self, OBJVAR_SPAWNED_COUNT));
            broadcast(player, "Spawned Max: " + getIntObjVar(self, OBJVAR_SPAWNED_MAX));
            broadcast(player, "Spawned Tracker: " + getIntObjVar(self, OBJVAR_SPAWNED_TRACKER));
        }
        if (item == menu_info_types.SERVER_MENU2)
        {
            sui.inputbox(self, player, "Enter the respawn time in seconds.", "setRespawnTime");
        }
        if (item == menu_info_types.SERVER_MENU3)
        {
            spawnZombies(self, null);
        }
        if (item == menu_info_types.SERVER_MENU4)
        {
            killAllZombies(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int spawnSkeletons(obj_id self, dictionary params)
    {
        return SCRIPT_CONTINUE;
    }

    public int spawnZombies(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id[] spawnPoints = getAllObjectsWithObjVar(getLocation(self), 500f, "zombie_spawn_point");
        location[] spawnLocations = new location[spawnPoints.length];
        for (int i = 0; i < spawnPoints.length; i++)
        {
            spawnLocations[i] = getLocation(spawnPoints[i]);
        }
        for (int i = 0; i < spawnLocations.length; i++)
        {
            if (getIntObjVar(self, OBJVAR_SPAWNED) < getIntObjVar(self, OBJVAR_SPAWNED_MAX))
            {
                obj_id zombie = create.object(CREATURE_TYPE_1, spawnLocations[i]);
                setObjVar(zombie, OBJVAR_SPAWNED_TRACKER, 1);
                setObjVar(self, OBJVAR_SPAWNED, getIntObjVar(self, OBJVAR_SPAWNED) + 1);
                setObjVar(self, OBJVAR_SPAWNED_COUNT, getIntObjVar(self, OBJVAR_SPAWNED_COUNT) + 1);
                if (!hasScript(zombie, "ai.ai"))
                {
                    attachScript(zombie, "ai.ai");
                }
                ai_lib.setDefaultCalmBehavior(zombie, ai_lib.BEHAVIOR_FRENZY);
                setName(zombie, "an undead");
                setDescriptionString(zombie, "This creature is a zombie. it's probably best to avoid it.");
                setNpcDifficulty(zombie, 1);
                setCondition(zombie, CONDITION_HOLIDAY_INTERESTING);
            }
            else
            {
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void killAllZombies(obj_id self)
    {
        obj_id[] zombies = getAllObjectsWithObjVar(getLocation(self), 100f, OBJVAR_SPAWNED_TRACKER);
        if (zombies != null)
        {
            for (obj_id zombie : zombies)
            {
                String EFFECT = "appearance/must_lightning_3.prt";
                String SOUNDEFFECT = "sound/wtr_lightning_strike.snd";
                playClientEffectLoc(zombie, EFFECT, getLocation(zombie), 0.0f);
                playClientEffectLoc(zombie, SOUNDEFFECT, getLocation(zombie), 2.0f);
                destroyObject(zombie);
            }
        }
    }
}
