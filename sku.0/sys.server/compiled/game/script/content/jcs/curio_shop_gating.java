package script.content.jcs;/*
@Origin: dsrc.script.content.jcs
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 2/9/2025, at 8:24 AM, 
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.groundquests;
import script.obj_id;

public class curio_shop_gating extends script.base_script
{
    public int OnAboutToReceiveItem(obj_id self, obj_id destinationCell, obj_id transferrer, obj_id item) throws InterruptedException
    {
        if (!isPlayer(item))
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(item) < 35)
        {
            broadcast(item, "You must be level 35 before entering the Curio Shop");
            return SCRIPT_OVERRIDE;
        }
        boolean canPass = groundquests.hasCompletedQuest(item, "quest/jabba_the_hutt_v2");
        if (!canPass)
        {
            broadcast(item, "You must gain Jabba's favor before entering the Curio Shop.");
            return SCRIPT_OVERRIDE;
        }
        else
        {
            return SCRIPT_CONTINUE;
        }
    }
}
