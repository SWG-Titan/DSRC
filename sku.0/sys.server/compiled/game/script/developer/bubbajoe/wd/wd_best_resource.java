package script.developer.bubbajoe.wd;/*
@Origin: dsrc.script.developer.bubbajoe.wd
@Author:  BubbaJoeX
@Purpose: One use Best Resource granter
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 4/22/2024, at 11:54 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class wd_best_resource extends script.terminal.terminal_character_builder
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
        if (canManipulate(player, self, true, true, 15, true))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Retrieve Best Resource"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            handleOptionForBR(player);
        }
        return SCRIPT_CONTINUE;
    }

    public void handleOptionForBR(obj_id player) throws InterruptedException
    {
        obj_id self = getSelf();
        refreshMenu(player, "Select the desired resource category", "Resource Handler", BEST_RESOURCE_TYPES, "handleBestCategorySelection", true);
    }
}
