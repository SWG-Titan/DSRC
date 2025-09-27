package script.content.npe2;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Cloning for TOS
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 5:40 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.location;
import script.obj_id;

public class cloner extends script.base_script
{
    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "[DEVL] Cloner");
        mark(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "[DEVL] Cloner");
        mark(self);
        return SCRIPT_CONTINUE;
    }

    public int mark(obj_id self) throws InterruptedException
    {
        location cloneLoc = getWorldLocation(self);
        location cloneRespawn = getLocation(self);
        obj_id planet = getPlanetByName(cloneLoc.area);
        if (isIdValid(planet))
        {
            dictionary params = new dictionary();
            params.put("id", self);
            params.put("name", "Pendath Refugee Camp");
            params.put("buildout", "taanab");
            params.put("areaId", "");
            params.put("loc", cloneLoc);
            params.put("respawn", cloneRespawn);
            params.put("type", 0);
            messageTo(planet, "registerCloningFacility", params, 4.0f, false);
            LOG("ethereal", "[NPE2]:  Cloning setup.");
        }
        return SCRIPT_CONTINUE;
    }
}
