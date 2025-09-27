package script.content.jcs;/*
@Origin: dsrc.script.content.jcs
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 2/9/2025, at 9:09 AM, 
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class guard extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return sync(self);
    }

    public int OnInitialize(obj_id self)
    {
        return sync(self);
    }

    public int sync(obj_id self)
    {
        setName(self, "a Gammorean Lookout");
        setInvulnerable(self, true);
        return SCRIPT_CONTINUE;
    }
}
