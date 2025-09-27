package script.systems.city;/*
@Origin: dsrc.script.systems.city
@Author: BubbaJoeX
@Purpose: Spawns the hireling on load.
@Requirements: script.systems.city.city_factional_hire
@Created: Saturday, 10/28/2023, at 6:35 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.create;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.dictionary;

public class city_factional_spawner extends script.base_script
{
    public int OnAttach(obj_id self) throws InterruptedException
    {
        spawnHireling(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        spawnHireling(self);
        return SCRIPT_CONTINUE;
    }

    public int spawnHireling(obj_id self) throws InterruptedException
    {
        int city_id = getCityAtLocation(getLocation(self), 0);
        if (city_id == 0)
        {
            destroyObject(self);
            LOG("city", "city_factional_spawner::spawnHireling - getCityAtLocation returned 0 for city_id: " + city_id);
            return SCRIPT_CONTINUE;
        }
        final int factionId = cityGetFaction(city_id);
        if (cityGetFaction(city_id) == 0)
        {
            destroyObject(self);
            LOG("city", "city_factional_spawner::spawnHireling - cityGetFaction returned 0 for city_id: " + city_id);
            return SCRIPT_CONTINUE;
        }
        if ((-615855020) == factionId)
        {
            obj_id recruiterImp = create.object("imperial_recruiter", getLocation(self));
            setCondition(recruiterImp, CONDITION_INTERESTING);
            LOG("city", "city_factional_spawner::spawnHireling - Imperial recruiter spawned at city_id: " + city_id);
        }
        else if ((370444368) == factionId)
        {
            obj_id recruiterImp = create.object("rebel_recruiter", getLocation(self));
            LOG("city", "city_factional_spawner::spawnHireling - Rebel recruiter spawned at city_id: " + city_id);
            setCondition(recruiterImp, CONDITION_INTERESTING);
        }
        return SCRIPT_CONTINUE;
    }

}
