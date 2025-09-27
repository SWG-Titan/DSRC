package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author:  BubbaJoeX
@Purpose: Spawns the lifeday mobs
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 11/20/2024, at 2:34 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.create;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.menu_info;
import script.dictionary;

public class gift_workshop_spawner extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        if (!hasObjVar(self, "workshop_spawner") || !hasObjVar(self, "workshop_boss_spawner"))
        {
            setObjVar(self, "workshop_spawner", 1);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        if (hasObjVar(self, "workshop_boss_spawner"))
        {
            messageTo(self, "spawnWorkshopBossChild", null, 60f * 5f, true);
            return SCRIPT_CONTINUE;
        }
        else messageTo(self, "spawnWorkshopChild", null, 60f * 5f, true);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int spawnWorkshopChild(obj_id self, dictionary params) throws InterruptedException
    {
        location loc = getLocation(self);
        obj_id child = create.object("event_lifeday_workshop_droid", loc);
        attachScript(child, "event.lifeday.gift_workshop_mob");
        setObjVar(child, "daddy", self);
        return SCRIPT_CONTINUE;
    }

    public int spawnWorkshopBossChild(obj_id self, dictionary params) throws InterruptedException
    {
        location loc = getLocation(self);
        obj_id child = create.object("event_lifeday_workshop_boss", loc);
        attachScript(child, "event.lifeday.gift_workshop_mob");
        setObjVar(child, "daddy", self);
        setObjVar(child, "daddy_boss", 1);
        return SCRIPT_CONTINUE;
    }
}
