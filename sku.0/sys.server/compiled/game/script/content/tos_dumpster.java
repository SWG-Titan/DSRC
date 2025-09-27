package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/14/2024, at 7:58 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.smuggler;
import script.library.sui;
import script.library.utils;

public class tos_dumpster extends base_script
{
    public int OnAttach(obj_id self)
    {
        runSetup(self);
        return SCRIPT_CONTINUE;
    }

    public int runSetup(obj_id self)
    {
        setName(self, "Station Dumpster");
        setDescriptionString(self, "This object allows you to sell all junk items in your inventory at once.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        runSetup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Sell All Junk"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (utils.outOfRange(self, player, 10.0f, true))
            {
                return SCRIPT_CONTINUE;
            }
            obj_id[] junkItems = smuggler.getAllJunkItems(player);
            if (junkItems == null || junkItems.length == 0)
            {
                broadcast(player, "You have no junk to mass sell.");
                return SCRIPT_CONTINUE;
            }
            sui.msgbox(self, player, "Would you like to sell all " + junkItems.length + " sellable items?", sui.OK_CANCEL, "Station Dumpster", "handleConfirmation");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int handleConfirmation(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (utils.outOfRange(self, player, 10.0f, true))
        {
            return SCRIPT_CONTINUE;
        }
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            broadcast(player, "You have declined to sell all junk.");
            return SCRIPT_CONTINUE;
        }
        obj_id[] junkItems = smuggler.getAllJunkItems(player);
        for (int i = 0; i < junkItems.length; i++)
        {
            smuggler.sellJunkItem(player, junkItems[i], false, false);
        }
        broadcast(player, "You have sold " + junkItems.length + " junk items.");
        return SCRIPT_CONTINUE;
    }
}
