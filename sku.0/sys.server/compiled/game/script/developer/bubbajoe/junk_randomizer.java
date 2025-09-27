package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Leash enemies from ground target loc based upon range, formation, speed.
@Created: Saturday, 3/30/2024, at 9:54 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.utils;

public class junk_randomizer extends base_script
{
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
        reinit(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        reinit(self);
        return SCRIPT_CONTINUE;
    }

    public int reinit(obj_id self)
    {
        setName(self, "GM Junk Generator");
        setDescriptionString(self, "This object will spawn a random piece of junk at the location you select.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Spawn Junk at Location"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        location whereAmIGoing = getLocation(player);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        obj_id junk = createObject("object/tangible/gjpud/gjpud_junk_s0" + rand(1, 5) + ".iff", whereAmIGoing);
        attachScript(junk, "event.gjpud.junk");
        setObjVar(junk, "gjpudObject", 1);
        setYaw(junk, rand(0.0f, 359.9f));
        String randomName = NAME_VARIATIONS[rand(0, NAME_VARIATIONS.length - 1)];
        setName(junk, randomName);
        LOG("events", "[GJPUD Adhoc]: Player " + getPlayerFullName(player) + " has spawned a piece of scrap at" + getLocation(junk).toReadableFormat(true) + " with the name of " + randomName);
        return SCRIPT_CONTINUE;
    }
}
