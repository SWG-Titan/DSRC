package script.content.renown;/*
@Origin: dsrc.script.content.renown
@Author:  BubbaJoeX
@Purpose: Displays renown information of the player using the terminal.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 2/25/2025, at 11:25 PM, 
@Copyright © SWG: New Beginnings 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.renown;

public class terminal_renown extends base_script
{

    public static final boolean LOGGING = true;

    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int sync(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, string_id.unlocalized("Check Renown"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            blog(getPlayerFullName(player) + " is checking their renown.");
            renown.showRenownTable(player);
        }
        return SCRIPT_CONTINUE;
    }

    public void blog(String msg)
    {
        if (LOGGING)
        {
            LOG("ethereal", "[Renown Terminal]: " + msg);
        }
    }
}
