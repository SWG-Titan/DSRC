package script.event.gjpud;/*
@Origin: dsrc.script.event.gjpud.junk
@Author: BubbaJoeX
@Purpose: This script handles clicking on junk objects for the Galactic Junk Pick Up Day event.
@Notes;
    This script handles the logic for clicking on junk objects, and returning the appropriate message.
@Created: Sunday, 2/20/2024, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.static_item;
import script.library.utils;
import script.*;

import java.lang.reflect.InvocationTargetException;

public class junk extends base_script
{
    public String GJPUD_ITEM = "item_gjpud_scrap_heap";
    public String[] NAME_VARIATIONS = {
            "Rugged Scrap Fragment",
            "Rusty Metal Piece",
            "Solid Scrap Shard",
            "Dented Debris Fragment",
            "Oxidized Scrap Remnant",
            "Dusty Scrap Fragment",
            "Tarnished Scrap Piece",
            "Worn-out Scrap Segment",
            "Corroded Metal Fragment",
            "Aged Scrap Remnant",
            "Battered Scrap Shard",
            "Tainted Scrap Fragment",
            "Weathered Scrap Piece",
            "Forgotten Scrap Fragment",
            "Ancient Scrap Remnant"
    };

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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Scavenge Junk"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException, InvocationTargetException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            int currentSmashed = getIntObjVar(player, "gjpud.total");
            setObjVar(player, "gjpud.total", currentSmashed + 1);
            broadcast(player, "You have collected a piece of scrap.");
            spawnSubJunk(self, player);
            destroyObject(self);
            static_item.createNewItemFunction(GJPUD_ITEM, utils.getInventoryContainer(player));
            LOG("events", "[GJPUD Junk Object]: Player " + getPlayerFullName(player) + " has picked up a piece of scrap at" + getLocation(self).toReadableFormat(true));
        }
        return SCRIPT_CONTINUE;
    }

    public int spawnSubJunk(obj_id self, obj_id player) throws InterruptedException
    {
        int spawnChance = rand(1, 100);
        if (spawnChance <= 15)
        {
            return SCRIPT_CONTINUE;
        }
        broadcast(player, "You notice something shiny in the distance...");
        int count = (rand(3, 5));
        for (int i = 0; i <= count; i++)
        {
            getLocation(self).x = getLocation(self).x + rand(-768f, 768f);
            getLocation(self).z = getLocation(self).z + rand(-768f, 768f);
            getLocation(self).y = getHeightAtLocation(getLocation(self).x, getLocation(self).z);
            obj_id junk = createObject("object/tangible/gjpud/gjpud_junk_s0" + rand(1, 5) + ".iff", getLocation(self));
            attachScript(junk, "event.gjpud.junk");
            setObjVar(junk, "gjpudObject", 1);
            setYaw(junk, rand(0.0f, 359.9f));
            String randomName = NAME_VARIATIONS[rand(0, NAME_VARIATIONS.length - 1)];
            if (randomName == null)
            {
                randomName = "Piece of Junk";
            }
            setName(junk, randomName);
        }
        LOG("events", "[GJPUD Controller]: Spawning " + count + " trailing junk spawns from " + getLocation(self).toReadableFormat(true) + " within a range of " + "768" + "m.");
        return SCRIPT_CONTINUE;
    }
}
