package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Gives item upon click
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 7:53 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class tos_clicky extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (canManipulate(player, self, false, true, 0, true))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Pick Up"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id inventory = utils.getInventoryContainer(player);
            if (!isIdValid(inventory))
            {
                return SCRIPT_CONTINUE;
            }
            //
            obj_id token = createObject("object/tangible/loot/misc/marauder_token.iff", inventory, "");
            attachScript(token, "content.tcg_voucher_vendor");
            if (isIdValid(token))
            {
                broadcast(player, "You have picked up this object.");
                destroyObject(self);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
