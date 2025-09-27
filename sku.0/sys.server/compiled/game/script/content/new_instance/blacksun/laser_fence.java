package script.content.new_instance.blacksun;/*
@Origin: dsrc.script.content.new_instance.blacksun
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 6/3/2024, at 5:26 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class laser_fence extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        LOG("ethereal", "[New Instance | Debug] laser_fence.OnAttach(" + self + ")" + " | " + getLocation(self) + " | " + getTemplateName(self) + " | setting door state to 0");
        setObjVar(self, "originalLocation", getLocation(self));
        setObjVar(self, "doorState", 0);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }
}
