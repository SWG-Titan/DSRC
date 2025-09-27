package script.content;/*
@Origin: dsrc.script.content
@Author: BubbaJoeX
@Purpose: Allows traders to purchase best resource for X amount of credits.
@Created: Friday, 11/17/2023, at 8:11 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;


public class trader_resource_buyout extends base_script
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
        if (isTrade(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Purchase Resources"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isTrade(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                handlePurchaseResources(player);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePurchaseResources(obj_id player) throws InterruptedException
    {
        String prompt = "How many resources would you like to purchase?";
        String title = "Purchase Resources";
        int pid = sui.inputbox(player, player, prompt, sui.OK_CANCEL, title, sui.INPUT_NORMAL, null, "handleAmount");
        return SCRIPT_CONTINUE;
    }

    public int handleAmount(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String amount = sui.getInputBoxText(params);
        int amountInt = utils.stringToInt(amount);
        setObjVar(player, "trader.resource.amount", amountInt);
        String selectionPrompt = "What resource would you like to purchase?";
        String selectionTitle = "Purchase Resources";
        String[] resourceList = dataTableGetStringColumn("datatables/resource/resource_tree.iff", "name");
        int pid = sui.listbox(player, player, selectionPrompt, sui.OK_CANCEL, selectionTitle, resourceList, "handleResourceSelection", true);
        return SCRIPT_CONTINUE;

    }

    public int handleResourceSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        String[] resourceList = dataTableGetStringColumn("datatables/resource/resource_tree.iff", "name");//@TODO: Change to resource table
        String resource = resourceList[idx];
        setObjVar(player, "trader.resource.name", resource);
        String pricePrompt = "Are you sure?";
        String priceTitle = "Purchase Resources";
        int pid = sui.inputbox(player, player, pricePrompt, sui.OK_CANCEL, priceTitle, sui.INPUT_NORMAL, null, "handleCreation");
        return SCRIPT_CONTINUE;
    }

    public int handleCreation(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String price = sui.getInputBoxText(params);
        int priceInt = utils.stringToInt(price);
        setObjVar(player, "trader.resource.price", priceInt);
        String resource = getStringObjVar(player, "trader.resource.name");
        int amount = getIntObjVar(player, "trader.resource.amount");
        int total = getIntObjVar(player, "trader.resource.price");//cpu
        int totalCost = amount * total;
        if (totalCost > getTotalMoney(player))
        {
            sendSystemMessage(player, "You do not have enough credits to purchase any resources.", null);
            return SCRIPT_CONTINUE;
        }
        else
        {
            sendSystemMessage(player, "You have purchased " + amount + " " + resource + " for " + totalCost + " credits.", null);
            return SCRIPT_CONTINUE;
        }
    }

    public boolean isTrade(obj_id who) throws InterruptedException
    {
        return utils.getPlayerProfession(who) == utils.TRADER;
    }
}
