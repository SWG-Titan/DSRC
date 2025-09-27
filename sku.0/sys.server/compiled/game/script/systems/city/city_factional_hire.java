package script.systems.city;/*
@Origin: dsrc.script.systems.city
@Author: BubbaJoeX
@Purpose: Allows mayors to place faction recruiters.
@Requirements: script.systems.city.city_factional_spawner
@Created: Saturday, 10/28/2023, at 6:33 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.city;

public class city_factional_hire extends base_script
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
        if (isIdValid(player))
        {
            if (!isInWorldCell(player))
            {
                broadcast(player, "You cannot place a factional hireling inside a building.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Hire Factional Recruiter"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!canHireFactionalNPC(self, player))
            {
                broadcast(player, "You cannot place this factional hireling.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                hireFactionalNPC(player, self);
            }
        }
        return SCRIPT_CONTINUE;
    }

    private int hireFactionalNPC(obj_id player, obj_id self)
    {
        int city_id = getCityAtLocation(getLocation(player), 0);
        String spawnEgg = "object/tangible/spawning/spawn_egg.iff";
        String faction = "";
        obj_id egg = createObject(spawnEgg, getLocation(player));
        int factionValue = pvpGetAlignedFaction(player);
        if (factionValue == (-615855020))
        {
            faction = "imperial";
            setObjVar(egg, "npc.name", "imperial_recruiter");
            setObjVar(egg, "npc.flair_text", "Hireling: Imperial Recruiter");
        }
        else if (factionValue == (370444368))
        {
            faction += "rebel";
            setObjVar(egg, "npc.name", "rebel_recruiter");
            setObjVar(egg, "npc.flair_text", "Hireling: Rebel Recruiter");
        }
        else
        {
            broadcast(player, "You are not aligned with a faction. You must be Rebel or Imperial to use this.");
            destroyObject(egg);
            return SCRIPT_CONTINUE;
        }
        setObjVar(egg, "spawn.faction", faction);
        setObjVar(egg, "spawn.issuer", player);
        attachScript(egg, "systems.city.city_factional_spawner");
        setObjVar(egg, "city_id", city_id);
        setObjVar(obj_id.getObjId(city_id), "city.factional_hireling", egg);
        return SCRIPT_CONTINUE;
    }

    private boolean canHireFactionalNPC(obj_id self, obj_id player) throws InterruptedException
    {
        //Similar to the city actors, only allow mayors, militia, or god mode (admin or objvar set *city_decorator*] to place the hireling.
        int city_id = getCityAtLocation(getLocation(player), 0);
        boolean isMayor = city.isTheCityMayor(player, city_id);
        if (hasObjVar(player, "city_decorator"))
        {
            return true;
        }
        else if (city.isMilitiaOfCity(player, city_id))
        {
            return true;
        }
        else if (isMayor)
        {
            return true;
        }
        return isGod(player);
    }
}
