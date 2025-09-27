package script.event.anniversary.marker;/*
@Origin: dsrc.script.event.anniversary.marker
@Author:  BubbaJoeX
@Purpose: Sets elite mob marker and detaches.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 8/14/2024, at 10:26 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class elite extends script.event.anniversary.stadium_lib
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Elite Mob Spawn Point");
        setObjVar(self, MOB_MARKER_PREFIX + ELITE_MOB_MARKER_TARGET_VAR, true);
        detachScript(self, "event.anniversary.marker.elite");
        return SCRIPT_CONTINUE;
    }
}
