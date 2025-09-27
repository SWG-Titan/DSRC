package script.hub.building;/*
@Origin: dsrc.script.hub.building
@Author: BubbaJoeX
@Purpose: Warp object
@Created: Saturday, 11/4/2023, at 3:51 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.location;
import script.obj_id;

public class transit extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        obj_id tatooine = getPlanetByName("tatooine");
        location here = getLocation(self);
        setObjVar(tatooine, "hub", here);
        return SCRIPT_CONTINUE;
    }
}
