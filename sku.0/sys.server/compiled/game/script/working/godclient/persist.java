package script.working.godclient;/*
@Origin: dsrc.script.godclient
@Author: BubbaJoeX
@Purpose: Persist and detach (because it's annoying)
@Created: Tuesday, 11/28/2023, at 1:42 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class persist extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        persistObject(self);
        detachScript(self, "godclient.persist");
        return SCRIPT_CONTINUE;
    }
}
