package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 5/9/2024, at 8:55 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.*;

public class tos_sunscreen_vendor extends base_script
{

    public String[] ITEM_NAMES_TO_PURCHASE = {
            "Thermal Paste"
    };
    public String[] ITEM_CODES_TO_PURCHASE = {
            "item_content_sunscreen",
    };

    public int OnAttach(obj_id self)
    {
        setName(self, "Burrhn (a scientist)");
        setDescriptionString(self, "Burrhn, a prominent scientist, has invented a wonderful paste to help protect you from planetary UV rays. Try some now!");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Burrhn (a scientist)");
        setDescriptionString(self, "Burrhn, a prominent scientist, has invented a wonderful paste to help protect you from planetary UV rays. Try some now!");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.CONVERSE_START, new string_id("Purchase Items"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.CONVERSE_START)
        {
            chat.chat(self, "Hey " + getPlayerFullName(player) + ", I have some items for sale. Would you like to take a look?");
            sui.listbox(self, player, "Select an item to purchase. \n\tAll items listed cost 1000 credits.", sui.OK_CANCEL, "Burrhn's Inventions", ITEM_NAMES_TO_PURCHASE, "handlePurchaseItem", true);
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePurchaseItem(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
        {
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String itemName = ITEM_NAMES_TO_PURCHASE[idx];
        String itemCode = ITEM_CODES_TO_PURCHASE[idx];
        if (getTotalMoney(player) < 1000)
        {
            broadcast(player, "You do not have enough credits to purchase this item.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            money.requestPayment(player, self, 1000, "no_handler", null, false);
            obj_id inventory = utils.getInventoryContainer(player);
            broadcast(player, "You have purchased " + itemName + " for 1000 credits.");
            static_item.createNewItemFunction(itemCode, inventory, 5);
        }
        return SCRIPT_CONTINUE;
    }
}
