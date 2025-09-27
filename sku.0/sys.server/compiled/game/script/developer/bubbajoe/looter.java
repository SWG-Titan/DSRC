package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Test loot with drag-n-drop giveItem
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 7/27/2024, at 8:44 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.menu_info;
import script.obj_id;

public class looter extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "GM Loot Token");
        setDescriptionString(self, "Development Item - Not for distribution");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "GM Loot Token");
        setDescriptionString(self, "Development Item - Not for distribution");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
}
