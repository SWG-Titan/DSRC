package script.event.halloween;/*
@Origin: dsrc.script.event.halloween
@Author:  BubbaJoeX
@Purpose: GMF 2024
@Requirements: <no requirements>
@Notes: Allows players to open a treat pale if it's 100% full on Halloween. Only using the bucket is handled here, the filling is handled in another script.
@Created: Thursday, 8/22/2024, at 9:42 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.static_item;
import script.library.sui;
import script.library.utils;

import static script.library.utils.packStringId;

public class candy_bucket extends base_script
{
    public static String[] OPTIONS = new String[]
            {
                    "Event Token",
                    "Junk Cache (Large)",
                    "Pick-a-Stat Earring",
                    "Token Cache: Adventurer",
            };

    public static boolean isHalloweenCDT()
    {
        return treat_thief.isHalloween();
    }

    public int OnAttach(obj_id self)
    {
        setupBucket(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setupBucket(self);
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        if (isFilled(self))
        {
            setName(self, "Spooky Treat Bowl (Full)");
        }
        if (getFloatObjVar(self, "halloween_24") > 90.5f)
        {
            setName(self, "Spooky Treat Bowl (Almost Full)");
        }
        if (getFloatObjVar(self, "halloween_24") > 75f)
        {
            setName(self, "Spooky Treat Bowl (Nearly Full)");
        }
        if (getFloatObjVar(self, "halloween_24") > 50f)
        {
            setName(self, "Spooky Treat Bowl (Half Full)");
        }
        if (getFloatObjVar(self, "halloween_24") < 25f)
        {
            setName(self, "Spooky Treat Bowl (Nearly Empty)");
        }
        if (getFloatObjVar(self, "halloween_24") < 1f)
        {
            setName(self, "Spooky Treat Bowl (Empty)");
        }
        if (!isIdValid(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        String isFilled = (isFilled(self) ? "Yes" : "No");
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = packStringId(new string_id("Filled"));
        attribs[idx] = isFilled;
        idx++;
        names[idx] = packStringId(new string_id("Fill Level"));
        attribs[idx] = getFillLevel(self) + "%";
        idx++;
        names[idx] = packStringId(new string_id("Open Date"));
        attribs[idx] = "Any date after October 31st";
        idx++;
        return SCRIPT_CONTINUE;
    }

    private String getFillLevel(obj_id self)
    {
        return Float.toString(getFloatObjVar(self, "halloween_24"));
    }

    private void setupBucket(obj_id self)
    {
        setName(self, "Spooky Treat Bowl");
        setDescriptionString(self, "This treat bowl can be filed by slaying mobs or crafting items during Galactic Moon Festival. Once full, it can be opened for a reward on October 31st.");
        setObjVar(self, "halloween_24_tag", 1);
        if (!hasObjVar(self, "halloween_24"))
        {
            setObjVar(self, "halloween_24", 0.0f);
        }
        if (!hasObjVar(self, "candy_bucket_uses"))
        {
            setObjVar(self, "candy_bucket_uses", 5);
        }
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isFilled(self))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Dump Out Treat Pale"));
        }
        else if (!isFilled(self))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Check Fill Level"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (canManipulate(player, self, true, true, 5, true))
            {
                handleBucketOpen(player, self);
            }
            else
            {
                broadcast(player, "You can't open this until October 31st, you may only fill it.");
            }
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            broadcast(player, "This treat pale is " + getFillLevel(self) + "% full.");
        }
        return SCRIPT_CONTINUE;
    }

    public boolean isFilled(obj_id self)
    {
        return getFloatObjVar(self, "halloween_24") >= 100.0f;
    }

    private void handleBucketOpen(obj_id player, obj_id self) throws InterruptedException
    {
        sui.listbox(self, player, "Select a reward:", sui.OK_CANCEL, "Spooky Treat Bowl", OPTIONS, "handleBucketOpenCallback", true);
    }

    public int handleBucketOpenCallback(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String loot = OPTIONS[idx];
        String[] OPTIONS_INTERNAL = new String[]
                {
                        "item_event_token_01_01",
                        "item_junk_cache_large",
                        "item_jewelery_addon_earring_s0" + (rand(1, 15)),
                        "item_token_cache_adventurer",
                };

        obj_id inventory = utils.getInventoryContainer(player);
        int count = 0;
        if (!isIdValid(inventory) || !exists(inventory))
        {
            return SCRIPT_CONTINUE;
        }
        if (idx == 1)
        {
            count = 4;
        }
        if (idx == 2)
        {
            count = 1;
        }
        obj_id item = static_item.createNewItemFunction(OPTIONS_INTERNAL[idx], inventory, count);
        obj_id[] lootArray = new obj_id[5];
        lootArray[0] = item;
        if (!isIdValid(item) || !exists(item))
        {
            return SCRIPT_CONTINUE;
        }
        broadcast(player, "You have chosen " + loot + "!");
        play2dNonLoopingSound(player, "sound/utinni.snd");
        showLootBox(player, lootArray);
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }
}
