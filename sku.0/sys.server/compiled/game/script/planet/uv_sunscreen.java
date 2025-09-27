package script.planet;/*
@Origin: dsrc.script.planet
@Author:  BubbaJoeX
@Purpose: Sunscreen handler
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 4/6/2024, at 6:34 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class uv_sunscreen extends script.planet.planet_uv
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
}
