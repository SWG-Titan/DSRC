package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author: BubbaJoeX
@Purpose: SWG-OR Christmas Gift
@Created: Sunday, 12/24/2023, at 11:49 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.*;

import java.util.ArrayList;
import java.util.Arrays;

public class gift_23 extends base_script
{
    public static final int GCW_AMOUNT = 3500;
    public static final int MANIPULATE_DISTANCE = 25;
    public static final int COLLECTION_AMOUNT = 5;

    public void reInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "a present");
        setDescriptionString(self, "A present from SWG-OR!");
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Unwrap Gift"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (canManipulate(player, self, true, true, MANIPULATE_DISTANCE, true))
            {
                grantPresents(self, player);
            }
            else
            {
                broadcast(player, "You cannot this unwrap this gift.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void grantPresents(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id painting = static_item.createNewItemFunction("item_painting_newbeginings", player);
        obj_id dance_device = static_item.createNewItemFunction("item_event_dance_party_device_03_01", player);
        obj_id dance_device_two = static_item.createNewItemFunction("item_event_dance_party_device_03_02", player);
        obj_id respec_device = static_item.createNewItemFunction("item_tcg_loot_reward_series4_ball_of_peace_02_01", player);
        obj_id[] collectionItems = new obj_id[COLLECTION_AMOUNT];
        for (int i = 0; i < COLLECTION_AMOUNT; i++)
        {
            obj_id collectionItem = collection.grantRandomCollectionItem(player, "datatables/loot/loot_items/collectible/magseal_loot.iff", "collections");
            collectionItems[i] = collectionItem;
        }
        ArrayList<obj_id> collectionItemsList = new ArrayList<obj_id>(Arrays.asList(collectionItems));
        collectionItemsList.add(painting);
        collectionItemsList.add(dance_device);
        collectionItemsList.add(dance_device_two);
        collectionItemsList.add(respec_device);
        obj_id[] collectionItemsArray = collectionItemsList.toArray(new obj_id[0]);
        showLootBox(player, collectionItemsArray);
        if (factions.isRebel(player) || factions.isImperial(player))
        {
            grantGCW(player);
        }
        broadcast(player, "You have opened your present.");
        destroyObject(self);
    }

    public void grantGCW(obj_id who) throws InterruptedException
    {
        gcw._grantGcwPoints(null, who, GCW_AMOUNT, false, gcw.GCW_POINT_TYPE_GROUND_PVE, "live event");
    }
}
