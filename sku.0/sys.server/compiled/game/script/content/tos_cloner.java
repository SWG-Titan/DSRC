package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Cloning for TOS
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 5:40 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.location;
import script.obj_id;

public class tos_cloner extends script.base_script
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
            params.put("name", "Rally Point Nova");
            params.put("buildout", "main");
            params.put("areaId", "");
            params.put("loc", cloneLoc);
            params.put("respawn", cloneRespawn);
            params.put("type", 0);
            messageTo(planet, "registerCloningFacility", params, 2.0f, true);
            LOG("ethereal", "[Tatooine Orbital Station]:  Cloning setup.");
        }
        return SCRIPT_CONTINUE;
    }
}
