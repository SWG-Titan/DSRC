package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Dispenses a slice of cake. One per character.
@Created: Wednesday, 12/13/2023, at 2:39 AM,
@Requirements: script.developer.bubbajoe.bday_gift
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.create;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class cake_dispenser extends script.base_script
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
        if (!hasObjVar(player, "bday_gift.taken"))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Retrieve Slice of Cake"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!hasObjVar(player, "bday_gift.taken"))
            {
                obj_id slice = create.createObject("object/tangible/food/crafted/dessert_air_cake.iff", utils.getInventoryContainer(player), "");
                setName(slice, "Slice of Birthday Cake");
                setDescriptionStringId(slice, new string_id("Cut from the most beautiful cake Master Abbub has ever made, this tasty slice will make you feel all cozy inside."));
                attachScript(slice, "developer.bubbajoe.bday_gift");
                setObjVar(self, "bday_gift.taken", 1);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
