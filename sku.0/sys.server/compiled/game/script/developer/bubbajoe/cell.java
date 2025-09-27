package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.cell
@Author: BubbaJoeX
@Purpose: Adhoc cell permissions for player access. Event Staff only.
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.obj_id;

public class cell extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isPlayer(item))
        {
            if (isAllowedInCurrentCell(item, getContainedBy(item)))
            {
                return SCRIPT_CONTINUE;
            }
            else
            {
                broadcast(item, "You are not allowed in this room.");
                return SCRIPT_OVERRIDE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean isAllowedInCurrentCell(obj_id player, obj_id cell) throws InterruptedException
    {
        if (cell == null)
        {
            return false;
        }
        if (hasObjVar(cell, "roomPermissions"))
        {
            obj_id[] roomPermissions = getObjIdArrayObjVar(cell, "roomPermissions");
            for (obj_id roomPermission : roomPermissions)
            {
                if (roomPermission == player)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
