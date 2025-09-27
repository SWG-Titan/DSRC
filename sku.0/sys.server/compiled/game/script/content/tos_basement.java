package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Rebreather required
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 5/15/2024, at 11:39 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.obj_id;

public class tos_basement extends script.base_script
{
    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isPlayer(item))
        {
            if (hasRebreather(item))
            {
                return SCRIPT_CONTINUE;
            }
            else
            {
                broadcast(item, "You must have a rebreather to enter the room.");
                return SCRIPT_OVERRIDE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToLoseItem(obj_id self, obj_id destContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isPlayer(item))
        {
            if (hasRebreather(item))
            {
                broadcast(item, "Your rebreather has been reset.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean hasRebreather(obj_id player) throws InterruptedException
    {
        obj_id[] objContents = utils.getContents(player, true);
        if (objContents != null)
        {
            for (obj_id objContent : objContents)
            {
                String strItemTemplate = getTemplateName(objContent);
                if (strItemTemplate.equals("object/tangible/wearables/goggles/rebreather.iff"))
                {
                    if (utils.isEquipped(objContent))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
