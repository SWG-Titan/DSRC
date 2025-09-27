package script.systems.museum;/*
@Origin: dsrc.script.systems.museum
@Author: BubbaJoeX
@Purpose: Script for displaying creatures in a museum
@Created: Sunday, 10/1/2023, at 12:27 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.ai.ai;
import script.obj_id;

public class display extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        taxidermy(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        taxidermy(self);
        return SCRIPT_CONTINUE;
    }

    public void taxidermy(obj_id self)
    {
        setScale(self, 0.455f);
        setName(self, getCreatureName(self) + " (Taxidermied)");
        if (!isInvulnerable(self))
        {
            setInvulnerable(self, true);
        }
        ai.stop(self);
        setDescriptionString(self, "This " + getCreatureName(self) + " has been taxidermied and is on display here at this museum.");
    }
}
