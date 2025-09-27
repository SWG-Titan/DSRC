package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Removes nametags of all items in the same cell as the script object for use in a later tool for IL files
@Requirements: Know what you are doing!!!
@Notes: <no notes>
@Created: Friday, 9/13/2024, at 9:38 AM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class cell_spam_remover extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Spam Remover");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Spam Remover");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player) || hasObjVar(player, "stripper"))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Remove All Nametags"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            int count = 0;
            obj_id[] objects = getContents(getContainedBy(self));  // Get contents of the same container (cell) as the script object
            for (obj_id object : objects)
            {
                if (isPlayer(object) || isMob(object) || hasScript(object, "terminal.terminal_structure"))
                {
                    continue;
                }
                if (hasScript(object, "item.static_item_base"))
                {
                    detachScript(object, "item.static_item_base");
                }
                if (hasScript(object, "developer.bubbajoe.spam_remover"))
                {
                    continue;
                }
                setName(object, "\\#.");
                count++;
            }
            broadcast(player, "Removed " + count + " nametags in cell " + getCellName(getContainedBy(self)));
        }
        return SCRIPT_CONTINUE;
    }
}
