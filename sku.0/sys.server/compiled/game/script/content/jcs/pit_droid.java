package script.content.jcs;/*
@Origin: dsrc.script.content.jcs
@Author:  BubbaJoeX
@Purpose: flavor npc for pit droids in the curio shop
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 2/9/2025, at 9:01 AM,
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

import java.util.HashSet;
import java.util.Set;

import static script.base_class.rand;

public class pit_droid extends script.base_script
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
        int randomNameSuffix = pit_droid_data.getUniqueNumber();
        if (randomNameSuffix == -1)
        {
            return SCRIPT_OVERRIDE;
        }

        setName(self, "JCS-PD-" + randomNameSuffix);
        return SCRIPT_CONTINUE;
    }
}

