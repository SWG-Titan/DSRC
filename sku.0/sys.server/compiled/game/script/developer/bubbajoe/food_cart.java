package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Dispense low level foods with customizable templates and values.
@Note: To add foods to this food cart, go to /datatables/adhoc/food_cart.tab and add values. Follow the header formats and make sure all values are assigned and properly typed. Keep in mind some buffs do not have an "effect value" which will cause some of the stats to be -1 or null(f).
@Created: Friday, 9/1/2023, at 10:55 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.money;
import script.library.sui;
import script.library.utils;

public class food_cart extends script.base_script
{

    public String TRACKING_VAR = "eventFoodItem";
    public String FOOD_TABLE = "datatables/adhoc/food_cart.iff";
    public String[] FOOD_NAMES = dataTableGetStringColumnNoDefaults("datatables/adhoc/food_cart.iff", "name");

    public int OnAttach(obj_id self)
    {
        setName(self, "Food Cart: Local Specialties");
        setDescriptionString(self, "This food cart allows you to purchase limited time specialty goods.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Food Cart: Local Specialties");
        setDescriptionString(self, "This food cart allows you to purchase limited time specialty goods.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info item) throws InterruptedException
    {
        if (getTotalMoney(player) > 1000)
        {
            item.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Purchase Consumables"));
        }
        else
        {
            broadcast(player, "You do not have enough money to purchase from the food cart.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            sui.listbox(self, player, "\\#FFD700Please select the item you would like to purchase.\\#." + "\n\n\t" + "***All items listed cost 15,000 credits each***", "Food Cart", FOOD_NAMES, "handleFoodSelection");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleFoodSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        int selectionIndex = sui.getListboxSelectedRow(params);
        dictionary rowData = dataTableGetRow(FOOD_TABLE, selectionIndex);
        String name = rowData.getString("name");
        String template = rowData.getString("template");
        String buff = rowData.getString("buffName");
        String displayName = rowData.getString("displayName");
        int stackSize = rowData.getInt("stackSize");
        int cost = rowData.getInt("cost");
        float duration = rowData.getFloat("duration"); //# this
        float effectiveness = rowData.getFloat("effectiveness"); //# times that
        if (duration > 3.6f)
        {
            duration = 3.6f;
        }
        if (effectiveness > 3.6f)
        {
            effectiveness = 3.6f;
        } // # equals this
        //TODO: split this into two handlers. One to prep the item, and the other to put in their inventory and subtract the monies
        int creditTotal = getTotalMoney(player);
        if (creditTotal > cost)
        {
            if (money.requestPayment(player, self, cost, "pass_fail", null, true))
            {
                broadcast(player, "You have purchased " + name + " for " + cost + " credits.");
                obj_id playerContainer = utils.getInventoryContainer(player);
                obj_id foodItem = create.createObject(template, playerContainer, "");
                if (hasScript(foodItem, "item.food"))
                {
                    detachScript(foodItem, "item.food");
                }
                attachScript(foodItem, "developer.bubbajoe.event_food");
                setObjVar(foodItem, TRACKING_VAR, true);
                setObjVar(foodItem, "effectiveness", effectiveness);// 1.2 = 12
                setObjVar(foodItem, "duration", duration); // 1.2 = 12 -- 12 + 12 = duration
                setObjVar(foodItem, "buff_name", buff);
                setObjVar(foodItem, "display_name", displayName);
                setCount(foodItem, stackSize);
                setName(foodItem, name);
                setObjVar(foodItem, "noTradeShared", 1);
                attachScript(foodItem, "item.special.nomove");
                setCrafter(foodItem, player);
                setDescriptionString(foodItem, "This item was purchased from a Live Event Food Cart. \n\n\\#DDAA25Type: " + name + "\n\n\\#DDAA25Cost: " + cost + " credits");
                LOG("ethereal", "[Abbubs Food Cart]: " + getPlayerFullName(player) + " has purchased " + name + " for " + cost + " credits.");
            }
            else
            {
                broadcast(player, "You do not have enough credits to purchase " + name + ".");
                LOG("ethereal", "[Abbubs Food Cart]: " + getPlayerFullName(player) + " has attempted to purchase " + name + " for " + cost + " credits but failed.  (Not enough credits)");
                return SCRIPT_CONTINUE;
            }
        }
        else
        {
            broadcast(player, "You do not have enough credits to purchase " + name + ".");
            LOG("ethereal", "[Abbubs Food Cart]: " + getPlayerFullName(player) + " has attempted to purchase " + name + " for " + cost + " credits but failed. (Not enough credits)");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public void setNoTrade(obj_id what)
    {
        setObjVar(what, "noTrade", 1);
        attachScript(what, "item.special.nomove");
    }
}
