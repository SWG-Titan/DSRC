package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: static posture
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 9:00 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class tos_posture extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        if (hasObjVar(self, "posture"))
        {
            setPosture(self, getIntObjVar(self, "posture"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        if (hasObjVar(self, "posture"))
        {
            setPosture(self, getIntObjVar(self, "posture"));
        }
        return SCRIPT_CONTINUE;
    }
}
