package script.content.fun;/*
@Origin: dsrc.script.content.fun
@Author:  BubbaJoeX
@Purpose: Triggers an asteroid shower
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 2/25/2025, at 7:56 PM, 
@Copyright © SWG: New Beginnings 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class asteroid_spawner extends base_script
{

    public static final boolean LOGGING = false;
    private static final float SPAWN_RANGE = 7530.0f;
    private static final int ASTEROID_COUNT = 5;
    private static final String ASTEROID_TEMPLATE = "object/tangible/usable/asteroid.iff";

    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int sync(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!isGod(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Trigger Asteroid Shower"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!isGod(self))
        {
            return SCRIPT_CONTINUE;
        }

        if (item != menu_info_types.ITEM_USE || !isIdValid(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }

        spawnAsteroids(self);
        broadcast(player, "Making asteroid shower");
        return SCRIPT_CONTINUE;
    }

    private void spawnAsteroids(obj_id self)
    {
        location here = new location(0, 0, 0, getCurrentSceneName());
        for (int i = 0; i < ASTEROID_COUNT; i++)
        {
            float angle = (float) (2.0f * Math.PI * i / ASTEROID_COUNT);
            float distance = rando(0.0f, SPAWN_RANGE);

            float dx = distance * (float) Math.cos(angle);
            float dz = distance * (float) Math.sin(angle);

            location spawnLoc = new location(here.x + dx, here.y, here.z + dz);
            obj_id asteroid = createObject(ASTEROID_TEMPLATE, spawnLoc);

            if (isIdValid(asteroid))
            {
                attachScript(asteroid, "content.fun.fallen_asteroid");
                setName(asteroid, "Fallen Asteroid");
                blog("Spawned asteroid at: " + spawnLoc.toLogFormat());
            }
        }
    }

    public float rando(float min, float max)
    {
        return min + (float) Math.random() * (max - min);
    }

    public void blog(String msg)
    {
        if (LOGGING)
        {
            LOG("ethereal", "[asteroid_spawner]: " + msg);
        }
    }
}
