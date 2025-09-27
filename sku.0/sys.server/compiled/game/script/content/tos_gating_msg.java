package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Message to tell players when entering a locked cell
@Requirements: <no requirements>
@Notes: help me obi-jaun
@Created: Tuesday, 5/7/2024, at 1:22 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class tos_gating_msg extends script.base_script
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
            broadcast(item, getStringObjVar(self, "cellLockMessage"));
        }
        return SCRIPT_OVERRIDE;
    }
}
