package script.event.pharple_day;

/*
@Origin: dsrc.script.event.pharple_day
@Author: BubbaJoeX
@Purpose: Spawns pharples.
@Requirements: <no requirements>
@Notes: Do not attach to creature.
@Created: Thursday, 8/22/2024, at 9:10 PM
*/

import script.dictionary;
import script.library.create;
import script.location;
import script.obj_id;

public class spawner extends pharple_day_lib
{
    private static final int MAX_PHARPLES = 15;
    private static final float SPAWN_INTERVAL = 600f; // 10 minutes
    private static final String PHARPLE_COUNT_VAR = "pharple_count";
    private static final String PHARPLE_LIST_VAR = "pharple_list";
    private static final String CREATURE_TEMPLATE = "giant_pharple";
    private static final String CREATURE_SCRIPT = "event.pharple_day.spawner_child";

    public spawner()
    {
    }

    public int OnAttach(obj_id self)
    {
        LOG("events", "[Pharple Day]: Spawner attached.");
        messageTo(self, "setupSpawner", null, 1, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        LOG("events", "[Pharple Day]: Spawner initialized.");
        messageTo(self, "setupSpawner", null, 1, false);
        return SCRIPT_CONTINUE;
    }

    public int setupSpawner(obj_id self, dictionary params) throws InterruptedException
    {
        int currentCount = getIntObjVar(self, PHARPLE_COUNT_VAR);
        obj_id[] spawnedPharples = getObjIdArrayObjVar(self, PHARPLE_LIST_VAR);

        if (currentCount < MAX_PHARPLES)
        {
            LOG("events", "[Pharple Day]: Spawning pharple.");
            obj_id newPharple = spawnPharple(self);
            if (isIdValid(newPharple))
            {
                setObjVar(self, PHARPLE_COUNT_VAR, currentCount + 1);
                updatePharpleList(self, newPharple);
                LOG("events", "[Pharple Day]: Pharple spawned and count updated.");
            }
            messageTo(self, "setupSpawner", null, SPAWN_INTERVAL, false);
        }
        else
        {
            LOG("events", "[Pharple Day]: Max pharples reached, waiting until space is available.");
        }

        return SCRIPT_CONTINUE;
    }

    private obj_id spawnPharple(obj_id self) throws InterruptedException
    {
        location spawnLocation = generateRandomLocation(self);
        obj_id pharple = create.object(CREATURE_TEMPLATE, spawnLocation);
        if (isIdValid(pharple))
        {
            attachScript(pharple, CREATURE_SCRIPT);
            setObjVar(pharple, "pharple_spawner", self);
        }
        return pharple;
    }

    private location generateRandomLocation(obj_id self) throws InterruptedException
    {
        location here = getLocation(self);
        float offsetX = rand(-15.0f, 15.0f);
        float offsetZ = rand(-15.0f, 15.0f);
        return new location(here.x + offsetX, here.y, here.z + offsetZ, here.area, here.cell);
    }

    private void updatePharpleList(obj_id self, obj_id newPharple)
    {
        obj_id[] pharples = getObjIdArrayObjVar(self, PHARPLE_LIST_VAR);
        obj_id[] updatedPharples;

        if (pharples == null)
        {
            updatedPharples = new obj_id[]{newPharple};
        }
        else
        {
            updatedPharples = new obj_id[pharples.length + 1];
            System.arraycopy(pharples, 0, updatedPharples, 0, pharples.length);
            updatedPharples[pharples.length] = newPharple;
        }

        setObjVar(self, PHARPLE_LIST_VAR, updatedPharples);
    }


    private void clearPharples(obj_id self, obj_id[] pharples) throws InterruptedException
    {
        if (pharples != null)
        {
            for (obj_id pharple : pharples)
            {
                if (isIdValid(pharple))
                {
                    destroyObject(pharple);
                }
            }
        }
        removeObjVar(self, PHARPLE_LIST_VAR);
        setObjVar(self, PHARPLE_COUNT_VAR, 0);
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (isGod(speaker))
        {
            if (text.equals("givePharples"))
            {
                messageTo(self, "setupSpawner", null, 1, false);
            }
            if (text.equals("killPharples"))
            {
                obj_id[] pharples = getObjIdArrayObjVar(self, PHARPLE_LIST_VAR);
                clearPharples(self, pharples);
                removeObjVar(self, PHARPLE_COUNT_VAR);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
