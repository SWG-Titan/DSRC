/*
@Purpose: Pumpkin Object for Galactic Moon Festival

@Author: BubbaJoe

@Overview: This is the smash script. This script handles spawning and awarding of the pumpkin smashers.
    I never finished the collection. If you want to add in the collection stuff, go for it. Set collectionEnabled to true and add the collection stuff in.

    This uses what i'd call "testing" funcs. If you want to use this for a 100% vanilla live server, you'll need to refactor this scripts.

    TL;DR: WYSIWYG.
 */
package script.event.halloween;/*
@Origin: dsrc.script.event.halloween.pumpkin_smasher_object
@Author: BubbaJoeX
@Purpose: Handles the smashing of pumpkins for the Galactic Moon Festival.
@Notes;
    If you finish the collection stuff, set collectionEnabled to true and add the collection stuff in.
    There are three ints you might want to adjust: JUNK_LOOT_AMOUNT, COIN_AMOUNT, and COIN_AMOUNT_SPECIAL.
@Created: Sunday, 2/25/2024, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.library.colors;
import script.library.static_item;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class pumpkin_smasher_object extends script.base_script
{
    public static final string_id SID_USE = new string_id("Smash!");
    public static final String COLLECTION_NAME = "gmf_pumpkin_pulped";
    public static final String PULPED_ITER_OBJVAR = "halloween.pulped";
    public static final int COIN_AMOUNT = 10;
    public static final int COIN_AMOUNT_SPECIAL = 50;
    public static float EFFECT_DELTA = 2.0f;
    public static String[] LOOT_TABLES = {
            "creature/elite_insect:elite_insect_81_90",
            "npc/boss_npc:boss_npc_81_90",
            "creature/creature_81_90",
            "creature/elite_creature:elite_creature_81_90"
    };
    public int JUNK_LOOT_AMOUNT = rand(1, 2);
    public boolean collectionEnabled = true;

    public pumpkin_smasher_object()
    {
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, SID_USE);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (getState(player, STATE_RIDING_MOUNT) == 1)
            {
                broadcast(player, "You can't smash pumpkins while mounted.");
                return SCRIPT_CONTINUE;
            }
            if (getState(player, STATE_SWIMMING) == 1)
            {
                broadcast(player, "You will drown if you try to smash pumpkins while swimming.");
                return SCRIPT_CONTINUE;
            }
            if (getState(player, STATE_FEIGN_DEATH) == 1)
            {
                broadcast(player, "You can't smash pumpkins while feigning death.");
                return SCRIPT_CONTINUE;
            }
            if (!getCreatureCoverVisibility(player))
            {
                broadcast(player, "You can't smash pumpkins while in cover.");
                return SCRIPT_CONTINUE;
            }
            if (isAwayFromKeyBoard(player))
            {
                broadcast(player, "You can't smash pumpkins while away from keyboard.");
                return SCRIPT_CONTINUE;
            }
            if (buff.hasBuff(player, "gmf_slowdown"))
            {
                broadcast(player, "The last pumpkin you smashed made you feel weak. It's best you wait a few seconds.");
                return SCRIPT_CONTINUE;
            }
            if (hasObjVar(self, "specialPumpkin")) // CSR specific pumpkins. Feel free to place liberally around starports. :)
            {
                broadcast(player, "You have found a special pumpkin with an additional " + color("DAA520", String.valueOf(COIN_AMOUNT_SPECIAL)) + " Galactic Moon Coins inside!");
                showFlyText(player, new string_id("* EXTREME LUCK *"), 12.0f, colors.GREEN);
                static_item.createNewItemFunction("item_event_halloween_coin", utils.getInventoryContainer(player), COIN_AMOUNT_SPECIAL);
            }
            doAnimationAction(player, "stamp_feet");
            int baseUse = 0;
            if (!hasObjVar(player, PULPED_ITER_OBJVAR))
            {
                setObjVar(player, PULPED_ITER_OBJVAR, baseUse);
            }
            int coinChance = rand(1, 100);
            int junkChance = rand(1, 100);
            int devastationChance = rand(1, 100);
            int buffChance = rand(1, 3);
            if (coinChance <= 15)
            {
                broadcast(player, "You have found " + color("DAA520", String.valueOf(COIN_AMOUNT)) + " Galactic Moon Coins!");
                static_item.createNewItemFunction("item_event_halloween_coin", utils.getInventoryContainer(player), COIN_AMOUNT);
            }
            if (junkChance <= 25)
            {
                createJunkContents(self, utils.getInventoryContainer(player));
                showFlyText(player, new string_id("*LUCKY SALVAGE*"), 6.0f, colors.YELLOW);
                LOG("events", "[GMF Pumpkin Smashing]: " + getPlayerFullName(player) + " has found junk inside a pumpkin at " + getLocation(player).toReadableFormat(true));
            }
            if (devastationChance <= 5)
            {
                broadcast(player, "You feel sick from smashing that pumpkin!");
                buff.applyBuff(player, "acid", 12, 15);
                LOG("events", "[GMF Pumpkin Smashing]: " + getPlayerFullName(player) + " has been hit with acid from smashing a pumpkin at " + getLocation(player).toReadableFormat(true));
            }
            if (buffChance == 1)
            {
                if (!buff.hasBuff(player, "burstRun"))//@Note: don't override the burst run buff from the station entertainer.
                {
                    broadcast(player, "You feel better after smashing the pumpkin!");
                    buff.applyBuff(player, "burstRun", 10.0f, 20.0f);
                    showFlyText(player, new string_id("* VRRRMMMM *"), 12.0f, colors.ORANGERED);
                    LOG("events", "[GMF Pumpkin Smashing]: " + getPlayerFullName(player) + " has been buffed with burst run from smashing a pumpkin at " + getLocation(player).toReadableFormat(true));
                }
            }
            showFlyText(player, new string_id("* SPLURT *"), 12.0f, colors.GREEN);
            playClientEffectLoc(player, "clienteffect/item_egg_splurt.cef", getLocation(self), EFFECT_DELTA);
            int currentSmashed = getIntObjVar(player, PULPED_ITER_OBJVAR);
            setObjVar(player, PULPED_ITER_OBJVAR, currentSmashed + 1);
            if (currentSmashed >= 1)
            {
                broadcast(player, "You have smashed " + color("DAA520", String.valueOf(currentSmashed)) + " pumpkins.");
                if (collectionEnabled)
                {
                    if (!hasCompletedCollection(player, COLLECTION_NAME))
                    {
                        modifyCollectionSlotValue(player, COLLECTION_NAME, 1);
                    }
                }
            }
            LOG("events", "[GMF Pumpkin Smashing]: " + getPlayerFullName(player) + " has smashed pumpkin at " + getLocation(player).toReadableFormat(true));
            destroyObject(self);
        }
        return SCRIPT_CONTINUE;
    }

    private String getRandomLootTable(obj_id self)
    {
        int tableIndex = rand(0, LOOT_TABLES.length - 1);
        return LOOT_TABLES[tableIndex];
    }

    public String color(String color, String text) throws InterruptedException
    {
        return "\\#" + color + text + "\\#.";
    }

    public void createJunkContents(obj_id self, obj_id container) throws InterruptedException
    {
        String JUNK_TABLE = "datatables/crafting/reverse_engineering_junk.iff";
        String column = "note";
        int JUNK_COUNT = 5;
        for (int i = 0; i < JUNK_COUNT; i++)
        {
            String junk = dataTableGetString(JUNK_TABLE, rand(1, dataTableGetNumRows(JUNK_TABLE)), column);
            obj_id junkItem = static_item.createNewItemFunction(junk, container);
            if (isIdValid(junkItem))
            {
                if (junk.contains("heroic_") || junk.contains("_heroic_") || junk.contains("meatlump"))
                {
                    continue;
                }
                setCount(junkItem, JUNK_LOOT_AMOUNT);
            }
        }
    }
}
