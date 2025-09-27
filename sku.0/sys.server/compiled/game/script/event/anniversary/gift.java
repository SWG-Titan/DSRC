package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: Grants gift upon click. limit one per character
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 8/14/2024, at 11:29 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.utils;

public class gift extends stadium_lib
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Anniversary Gift Dispenser");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (hasObjVar(player, GIFT_VAR))
        {
            broadcast(player, "You have already claimed your anniversary gift.");
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("(Already Claimed)"));
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Anniversary Gift"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (hasObjVar(player, GIFT_VAR))
            {
                broadcast(player, "You have already claimed your anniversary gift.");
                return SCRIPT_CONTINUE;
            }
            obj_id inventory = utils.getInventoryContainer(player);
            if (inventory == null)
            {
                return SCRIPT_CONTINUE;
            }
            obj_id gift = createObject(GIFT_TEMPLATE, inventory, "");
            if (isIdValid(gift))
            {
                attachScript(gift, "event.anniversary.gift_object");
                setObjVar(player, GIFT_VAR, true);
                broadcast(player, "You have claimed your anniversary gift.");
            }
        }
        sendDirtyObjectMenuNotification(self);
        return SCRIPT_CONTINUE;
    }
}
