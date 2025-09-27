package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Wears item ongiveitem
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 6:04 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class tos_wear extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnGiveItem(obj_id self, obj_id item, obj_id player) throws InterruptedException
    {
        if (isPlayer(player))
        {
            equip(item, self);
            broadcast(player, "wear " + item);
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
}
