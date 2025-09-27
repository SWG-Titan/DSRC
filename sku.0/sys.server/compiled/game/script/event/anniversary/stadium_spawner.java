package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: Spawns the inputed mob string on objects in range that have the proper marker objvar
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 8/14/2024, at 9:49 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.sui;
import script.library.utils;

import java.util.ArrayList;
import java.util.Collections;

public class stadium_spawner extends stadium_lib
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
        if (isGod(player))
        {
            int papa = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Spawn Mobiles"));
            mi.addSubMenu(papa, menu_info_types.SERVER_MENU1, new string_id("Set Mobile"));
            mi.addSubMenu(papa, menu_info_types.SERVER_MENU2, new string_id("Set Spawn Point"));
            mi.addSubMenu(papa, menu_info_types.SERVER_MENU3, new string_id("Reset Spawner Data"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.ITEM_USE)
            {
                LOG("ethereal", "[Stadium]: Attempting to spawn mobile(s).");
                String mob = getStringObjVar(self, MOB_MARKER_KEY_VAR);
                LOG("ethereal", "[Stadium]: Mob type for spawning is: " + mob);
                if (mob == null || mob.isEmpty())
                {
                    broadcast(player, "You must set the mob type first.");
                    LOG("ethereal", "[Stadium]: No mob type set for spawner.");
                    return SCRIPT_CONTINUE;
                }
                obj_id[] stuff = getObjectsInRange(self, TOOL_RANGE);
                LOG("ethereal", "[Stadium]: Found " + stuff.length + " objects in range.");
                for (obj_id objId : stuff)
                {
                    //match the mob type to the marker type
                    if (hasObjVar(objId, MOB_MARKER_PREFIX + TRASH_MOB_MARKER_TARGET_VAR) && hasObjVar(self, MOB_MARKER_POINT_VAR + TRASH_MOB_MARKER_VAR))
                    {
                        obj_id creation = create.createCreature(mob, getLocation(objId), USE_AI);
                        attachScript(creation, "event.anniversary.stadium_loot_override");
                        LOG("ethereal", "[Stadium]: Created " + mob + " at trash point" + getLocation(objId));
                        setYaw(creation, 180f);
                    }
                    else if (hasObjVar(objId, MOB_MARKER_PREFIX + ELITE_MOB_MARKER_TARGET_VAR) && hasObjVar(self, MOB_MARKER_POINT_VAR + ELITE_MOB_MARKER_VAR))
                    {
                        obj_id creation = create.createCreature(mob, getLocation(objId), USE_AI);
                        attachScript(creation, "event.anniversary.stadium_loot_override");
                        LOG("ethereal", "[Stadium]: Created " + mob + " at elite point " + getLocation(objId));
                        setYaw(creation, 180f);
                    }
                    else if (hasObjVar(objId, MOB_MARKER_PREFIX + BOSS_MOB_MARKER_TARGET_VAR) && hasObjVar(self, MOB_MARKER_POINT_VAR + BOSS_MOB_MARKER_VAR))
                    {
                        obj_id creation = create.createCreature(mob, getLocation(objId), USE_AI);
                        attachScript(creation, "event.anniversary.stadium_loot_override");
                        LOG("ethereal", "[Stadium]: Created " + mob + " at boss point " + getLocation(objId));
                        setYaw(creation, 180f);
                    }
                    else
                    {
                        LOG("ethereal", "[Stadium]: No valid spawn points found.");
                        broadcast(self, "No valid spawn points found.");
                    }
                }
            }
            if (item == menu_info_types.SERVER_MENU1)
            {
                sui.inputbox(self, player, "Enter partial search term for mob to spawn.", sui.OK_CANCEL, "Mobile Selection", sui.INPUT_NORMAL, null, "handleMobLookup", null);
                LOG("ethereal", "[Stadium]: Mob lookup requested.");
            }
            if (item == menu_info_types.SERVER_MENU2)
            {
                sui.listbox(self, player, "Select spawn point type.", sui.OK_CANCEL, "Spawn Point Selection", new String[]{"Trash Mob", "Elite Mob", "Boss Mob"}, "handleSpawnPointChoice", true);
                LOG("ethereal", "[Stadium]: Spawn point selection requested.");
            }
            if (item == menu_info_types.SERVER_MENU3)
            {
                removeObjVar(self, MOB_MARKER_KEY_VAR);
                removeObjVar(self, MOB_MARKER_POINT_VAR + TRASH_MOB_MARKER_VAR);
                removeObjVar(self, MOB_MARKER_POINT_VAR + ELITE_MOB_MARKER_VAR);
                removeObjVar(self, MOB_MARKER_POINT_VAR + BOSS_MOB_MARKER_VAR);
                LOG("ethereal", "[Stadium]: Spawner data reset.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSpawnPointChoice(obj_id self, dictionary params) throws InterruptedException
    {
        int spawnPoint = sui.getListboxSelectedRow(params);
        if (spawnPoint == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (spawnPoint == 0)
        {
            setObjVar(self, MOB_MARKER_POINT_VAR + TRASH_MOB_MARKER_VAR, true);
            LOG("ethereal", "[Stadium]: Setting target points to Trash Mob");
        }
        if (spawnPoint == 1)
        {
            setObjVar(self, MOB_MARKER_POINT_VAR + ELITE_MOB_MARKER_VAR, true);
            LOG("ethereal", "[Stadium]: Setting target points to Elite Mob");
        }
        if (spawnPoint == 2)
        {
            setObjVar(self, MOB_MARKER_POINT_VAR + BOSS_MOB_MARKER_VAR, true);
            LOG("ethereal", "[Stadium]: Setting target points to Boss Mob");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleMobLookup(obj_id self, dictionary params) throws InterruptedException
    {
        String mob = sui.getInputBoxText(params);
        LOG("ethereal", "[Stadium]: Mob lookup for " + mob);
        if (mob == null || mob.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        String[] matchingMobs = getMatchingMobs(mob);
        utils.setScriptVar(self, MOB_SPAWNER_PLACEHOLDER, mob);
        sui.listbox(self, sui.getPlayerId(params), "Select mobile from search term.", sui.OK_CANCEL, "Mobile Selection", matchingMobs, "handleMobTypeChoice", true);
        return SCRIPT_CONTINUE;
    }

    public int handleMobTypeChoice(obj_id self, dictionary params) throws InterruptedException
    {
        int mob = sui.getListboxSelectedRow(params);
        if (mob == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String[] matchingMobs = getMatchingMobs(utils.getStringScriptVar(self, MOB_SPAWNER_PLACEHOLDER));
        if (mob >= matchingMobs.length)
        {
            return SCRIPT_CONTINUE;
        }
        LOG("ethereal", "[Stadium]: Mob selected: " + matchingMobs[mob]);
        setObjVar(self, MOB_MARKER_KEY_VAR, matchingMobs[mob]);
        return SCRIPT_CONTINUE;
    }

    private static String[] getMatchingMobs(String mob)
    {
        String MOB_TABLE = "datatables/mob/creatures.iff";
        String[] mobList = dataTableGetStringColumn(MOB_TABLE, "creatureName");

        ArrayList<String> matchingMobs = new ArrayList<>();
        for (String mobName : mobList)
        {
            if (mobName.contains(mob))
            {
                matchingMobs.add(mobName);
            }
        }
        Collections.sort(matchingMobs);
        return matchingMobs.toArray(new String[0]);
    }
}
