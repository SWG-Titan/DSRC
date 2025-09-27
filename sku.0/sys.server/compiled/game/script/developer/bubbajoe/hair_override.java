package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Wear hair that is not allowed for your species
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 9/16/2024, at 7:11 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.menu_info;
import script.string_id;
import script.dictionary;

public class hair_override extends script.base_script
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
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Wear"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                equip(self, player, "hair");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
