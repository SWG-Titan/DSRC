package script.event.wheres_watto;/*
@Origin: dsrc.script.event.wheres_watto
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 6/15/2024, at 11:32 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.obj_id;

public class waypoint_destroyer extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        messageTo(self, "destroyMe", null, 59.30f * 5f, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int destroyMe(obj_id self, dictionary params)
    {
        LOG("ethereal", "[Where's Watto]: destroying previous hint for player.");
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }
}
