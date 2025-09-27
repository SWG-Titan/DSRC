package script.event.gjpud.rewards;/*
@Origin: script.event.gjpud.rewards.
@Author: BubbaJoeX
@Purpose: Detects invisible metal caches on planets. (Think treasure chests, but beefed up and less stupid)
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.loot;
import script.*;

import static script.library.locations.getGoodLocationAroundLocationAvoidCollidables;

public class metal_detector extends base_script
{
    public String GJPUD_LOOT_DRUM = "object/tangible/container/drum/treasure_drum.iff";
    public String GJPUD_LOOT_TABLE = "gjpud/drum";

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Scan Area"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            getScanResult(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self)
    {
        setName(self, "Master Abubb's Metal Detector");
        setDescriptionString(self, "This is a metal detector. It can be used to detect metal caches on planets. It is a one time use item.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Master Abubb's Metal Detector");
        return SCRIPT_CONTINUE;
    }

    public int getPlanetBonus(obj_id self, obj_id player)
    {
        String scene = getCurrentSceneName();
        switch (scene)
        {
            case "tatooine": //core
            case "naboo":
            case "corellia":
                broadcast(player, "Your range bonus multiplier is currently at 2.");
                return 2;
            case "lok": //secondary
            case "rori":
            case "talus":
                broadcast(player, "Your range bonus multiplier is currently at 5.");
                return 5;
            case "dantooine": //adventure med
            case "dathomir":
                broadcast(player, "Your range bonus multiplier is currently at 10.");
                return 10;
            case "endor": //adventure high
            case "yavin4":
                broadcast(player, "Your range bonus multiplier is currently at 15.");
                return 15;
            default:
                // non Empire Divided planets return 0 because 0 x anything is 0. 5head move man
                broadcast(player, "This planet is currently not partaking in the Galactic Junk Pick Up Day activities.");
                return 0;

        }
    }

    public int getScanResult(obj_id self, obj_id player) throws InterruptedException
    {
        int bonus = getPlanetBonus(self, player);
        if (bonus == 0)
        {
            broadcast(player, "This planet does not have any caches to detect.");
        }
        location chestLoc = new location();
        chestLoc.x = chestLoc.x + (rand(50, 100 * bonus));
        chestLoc.y = getHeightAtLocation(chestLoc.x, chestLoc.y);
        chestLoc.z = chestLoc.z + (rand(50, 100 * bonus));
        location finalLoc = getGoodLocationAroundLocationAvoidCollidables(chestLoc, 250f, 250f, 250f, 250f, false, true, 10f);
        setObjVar(self, "gjpud.cacheLocation", finalLoc);
        spawnChest(self, player);
        return SCRIPT_CONTINUE;
    }

    public int spawnChest(obj_id self, obj_id player) throws InterruptedException
    {
        String table = GJPUD_LOOT_TABLE;
        int amt = getPlanetBonus(self, player);
        location treasureLoc = getLocationObjVar(self, "gjpud.cacheLocation");
        if (treasureLoc == null)
        {
            broadcast(player, "Your device is unable to retrieve location data at this time.");
        }
        obj_id treasureChest = createObject(GJPUD_LOOT_DRUM, treasureLoc);
        attachScript(treasureChest, "item.container.loot_crate_opened");
        setName(treasureChest, "an item cache");
        loot.makeLootInContainer(treasureChest, table, amt, 300);
        obj_id[] contents = getContents(treasureChest);
        for (obj_id content : contents)
        {
            if (hasScript(content, "item.special.nomove"))
            {
                detachScript(content, "item.special.nomove");
            }
            if (hasObjVar(content, "noTrade"))
            {
                removeObjVar(self, "noTrade");
            }
        }
        alertPlayer(player, treasureLoc);
        return SCRIPT_CONTINUE;
    }

    private void alertPlayer(obj_id player, location treasureLoc)
    {
        String planet = getCurrentSceneName();
        String loc = treasureLoc.toReadableFormat(false);
        String msg = "You have detected a cache of items at " + loc + ".";
        broadcast(player, msg);
    }
}
