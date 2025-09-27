package script.event.halloween.haunted;/*
@Origin: dsrc.script.event.halloween.haunted
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 9/12/2024, at 4:28 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.obj_id;

public class corvette extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        blab(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        blab(self);
        return SCRIPT_CONTINUE;
    }

    public void blab(obj_id self)
    {
        obj_id[] cells = getContents(self);
        if (cells == null)
        {
            return;
        }
        for (obj_id cell : cells)
        {
            LOG("events", "[Haunted House]: " + cell + " (" + getCellName(cell) + "):  /teleportto " + cell);
        }
    }

    public int childDied(obj_id self, dictionary params)
    {
        obj_id child = params.getObjId("child");
        if (child == null)
        {
            return SCRIPT_CONTINUE;
        }

        return SCRIPT_CONTINUE;
    }
}
