package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Similiar to the teleporter_coord script, this script will allow for a cost to be set for the teleporter with the base functionality of the teleporter_coord script.
@Created: Sunday, 9/10/2023, at 5:54 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.money;
import script.library.sui;
import script.library.utils;

public class teleporter_cost extends script.base_script
{
    public static final String STF = "city/city";

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
        menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        int menu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel"));
        int menu2 = mi.addRootMenu(menu_info_types.SERVER_MENU8, new string_id("Information"));
        if (isGod(player))
        {
            int menu1 = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Setup"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU2, new string_id("Set Planet"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU3, new string_id("Set X"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU5, new string_id("Set Height"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU4, new string_id("Set Y"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU7, new string_id("Set About Information"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU9, new string_id("Set Cost"));
            mi.addSubMenu(menu1, menu_info_types.SERVER_MENU10, new string_id("Set Cell"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.msgbox(self, player, "Would you like to travel to the specified location for " + getIntObjVar(self, "teleport.cost") + " credits?", sui.OK_CANCEL, "Local Travel", "handleTravelVerification");
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            String planet = getStringObjVar(self, "teleport.scene");
            String xcoord = getStringObjVar(self, "teleport.x");
            String ycoord = getStringObjVar(self, "teleport.y");
            String zcoord = getStringObjVar(self, "teleport.z");
            int cost = getIntObjVar(self, "teleport.cost");
            int cellid = getIntObjVar(self, "teleport.cell");
            broadcast(player, "How to use: Follow menu prompts and enter coordinate values for each, as well as cost..");
            setName(self, "[" + planet + "] X: " + xcoord + " | Z (Height): " + zcoord + " | Y:" + ycoord + "| Cost: " + cost + " | Cell: " + cellid);
            broadcast(player, "Debug name set. Use /setName -target [name] to customize it.");
        }
        if (item == menu_info_types.SERVER_MENU2)
        {
            String scene = "";
            String buffer = "Enter a Planet name for this teleporter to teleport to:";
            String title = "Adhoc Transit - Planet";
            sui.filteredInputbox(self, player, buffer, title, "handleSceneRequest", scene);
        }
        if (item == menu_info_types.SERVER_MENU3)
        {
            String x = "";
            String buffer = "Enter the X coordinate";
            String title = "Adhoc Transit - X Coordinate";
            sui.filteredInputbox(self, player, buffer, title, "handleXRequest", x);
        }
        if (item == menu_info_types.SERVER_MENU5)
        {
            String height = "";
            String buffer = "Enter the Z (height) coordinate";
            String title = "Adhoc Transit - Height";
            sui.filteredInputbox(self, player, buffer, title, "handleZRequest", height);
        }
        if (item == menu_info_types.SERVER_MENU4)
        {
            String y = "";
            String buffer = "Enter the Y coordinate";
            String title = "Adhoc Transit - Y Coordinate";
            sui.filteredInputbox(self, player, buffer, title, "handleYRequest", y);
        }
        if (item == menu_info_types.SERVER_MENU7)
        {
            String about = "";
            String buffer = "Enter an about message that players can see.";
            String title = "Adhoc Transit - About";
            sui.filteredInputbox(self, player, buffer, title, "handleMSGRequest", about);
        }
        if (item == menu_info_types.SERVER_MENU9)
        {
            String defaultCost = "2500";
            String title = "Adhoc Transit - Travel Cost";
            String buffer = "Enter amount of credits required to use this teleporter device.";
            sui.filteredInputbox(self, player, buffer, title, "handleCostRequest", defaultCost);
        }
        if (item == menu_info_types.SERVER_MENU10)
        {
            String defaultCost = "0";
            String title = "Adhoc Transit - Destination Cell ID";
            String buffer = "Enter the CELL ID of the destination.";
            sui.filteredInputbox(self, player, buffer, title, "handleCellRequest", defaultCost);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSceneRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String message1 = sui.getInputBoxText(params);
        if (message1 == null)
        {
            broadcast(player, "Invalid entry.");
            return SCRIPT_CONTINUE;
        }
        if (message1.equals("tatooine"))
        {
            broadcast(player, "Note: Using Tatooine in densely populated areas is advised against.");
            setObjVar(self, "teleport.scene", message1);
            return SCRIPT_CONTINUE;
        }
        if (message1.equals("kashyyyk_main") || message1.contains("mustafar"))
        {
            broadcast(player, "Note: Kashyyyk and Mustafar may have offset coordinates.");
            setObjVar(self, "teleport.scene", message1);
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "teleport.scene", message1);
        return SCRIPT_CONTINUE;
    }

    public int handleCellRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id message1 = utils.stringToObjId(sui.getInputBoxText(params));
        if (message1 == null)
        {
            broadcast(player, "This is not a valid cell ID.");
            return SCRIPT_OVERRIDE;
        }
        setObjVar(self, "teleport.cell", message1);
        return SCRIPT_CONTINUE;
    }

    public int handleCostRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        int costInt = utils.stringToInt(sui.getInputBoxText(params));
        if (costInt == 0)
        {
            broadcast(player, "You must enter a valid number. 0 is not a valid number.");
            return SCRIPT_OVERRIDE;
        }
        setObjVar(self, "teleport.cost", costInt);
        return SCRIPT_CONTINUE;
    }

    public int handleXRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String message3 = sui.getInputBoxText(params);
        if (message3 == null)
        {
            broadcast(player, "Invalid entry.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "teleport.x", message3);
        return SCRIPT_CONTINUE;
    }

    public int handleYRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String message4 = sui.getInputBoxText(params);
        if (message4 == null)
        {
            broadcast(player, "Invalid entry.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "teleport.y", message4);
        return SCRIPT_CONTINUE;
    }

    public int handleZRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String message5 = sui.getInputBoxText(params);
        if (message5 == null)
        {
            broadcast(player, "Invalid entry.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "teleport.z", message5);
        return SCRIPT_CONTINUE;
    }

    public int handleMSGRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        String message6 = sui.getInputBoxText(params);
        if (message6 == null)
        {
            broadcast(player, "Invalid entry.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "teleport.msg", message6);
        return SCRIPT_CONTINUE;
    }

    public int handleTravelVerification(obj_id self, dictionary params) throws InterruptedException
    {
        int cost = getIntObjVar(self, "teleport.cost");
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (bp == sui.BP_OK)
        {
            if (money.requestPayment(player, self, cost, "pass_fail", null, true))
            {
                //broadcast(player, "You have paid " + cost + " credits to the Galactic Travel Committee");
                String planet = getStringObjVar(self, "teleport.scene");
                String xcoord = getStringObjVar(self, "teleport.x");
                String ycoord = getStringObjVar(self, "teleport.y");
                String zcoord = getStringObjVar(self, "teleport.z");
                float x = utils.stringToFloat(xcoord);
                float y = utils.stringToFloat(ycoord);
                float z = utils.stringToFloat(zcoord);
                obj_id cell = getObjIdObjVar(self, "teleport.cell");
                warpPlayer(player, planet, x, y, z, cell, x, y, z);
                LOG("ethereal", "[Teleporter]: Transporting " + getPlayerFullName(player) + " from " + getLocation(player) + " to " + planet + " " + x + " " + y + " " + z + " " + cell + " for " + cost + " credits.");
            }
            else
            {
                broadcast(player, "You do not have enough credits (" + cost + ")  to use this transport device.");
                LOG("ethereal", "[Teleporter]: " + getPlayerFullName(player) + " attempted to use a terminal but failed at transferBankCreditsToNamedAccount.");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
