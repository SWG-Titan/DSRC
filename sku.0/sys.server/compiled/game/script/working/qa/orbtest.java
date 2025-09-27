package script.working.qa;

import script.dictionary;
import script.library.create;
import script.library.loot;
import script.library.utils;
import script.location;
import script.obj_id;

public class orbtest extends script.base_script
{
    public orbtest()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        broadcast(self, "orbtest script attached. Say 'GO' to start the test");
        return SCRIPT_CONTINUE;
    }

    public int OnSpeaking(obj_id self, String text) throws InterruptedException
    {
        if (text.equalsIgnoreCase("go"))
        {
            if (utils.hasScriptVar(self, "testingOn"))
            {
                broadcast(self, "testing is already in progress... say 'STOP' to abort");
            }
            else
            {
                if (getConfigSetting("GameServer", "enableLevelUpLoot") != null)
                {
                    broadcast(self, "testing in progress. Say 'STOP' to abort");
                    utils.setScriptVar(self, "testingOn", true);
                    utils.setScriptVar(self, "spawnAttempts", 0);
                    messageTo(self, "handleSpawnMob", null, 1, false);
                }
                else
                {
                    broadcast(self, "[GameServer] enableLevelUpLoot config not set! Testing aborted");
                }
            }
        }
        else if (text.equalsIgnoreCase("stop"))
        {
            utils.removeScriptVar(self, "testingOn");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSpawnMob(obj_id self, dictionary params) throws InterruptedException
    {
        if (!utils.hasScriptVar(self, "testingOn"))
        {
            int spawnAttempts = utils.getIntScriptVar(self, "spawnAttempts");
            broadcast(self, "testing aborted after " + (spawnAttempts * 10) + " real kills");
            return SCRIPT_CONTINUE;
        }
        int spawnAttempts = utils.getIntScriptVar(self, "spawnAttempts");
        spawnAttempts++;
        utils.setScriptVar(self, "spawnAttempts", spawnAttempts);
        broadcast(self, "spawning mob " + spawnAttempts);
        location spawnLoc = getLocation(self);
        spawnLoc.x += 2;
        obj_id critter = create.object("meatlump_cretin", spawnLoc);
        params.put("critter", critter);
        messageTo(self, "handleKillCritter", params, 0.5f, false);
        return SCRIPT_CONTINUE;
    }

    public int handleKillCritter(obj_id self, dictionary params) throws InterruptedException
    {
        broadcast(self, "killing mob");
        obj_id critter = params.getObjId("critter");
        setHealth(critter, 0);
        messageTo(self, "handleCreateLoot", params, 0.5f, false);
        return SCRIPT_CONTINUE;
    }

    public int handleCreateLoot(obj_id self, dictionary params) throws InterruptedException
    {
        broadcast(self, "creating loot 10 times");
        obj_id critter = params.getObjId("critter");
        for (int i = 0; i < 10; i++)
        {
            loot.addLoot(critter);
            if (utils.hasScriptVar(critter, "orbCreated"))
            {
                break;
            }
        }
        if (utils.hasScriptVar(critter, "orbCreated"))
        {
            int spawnAttempts = utils.getIntScriptVar(self, "spawnAttempts");
            obj_id objOrb = utils.getObjIdScriptVar(critter, "orbCreated");
            broadcast(self, "LEVEL UP ORB FOUND! It is Object Id " + objOrb);
            broadcast(self, "Total real kills to spawn: " + (spawnAttempts * 10));
            utils.removeScriptVar(self, "testingOn");
        }
        else
        {
            broadcast(self, "No orb found");
            destroyObject(critter);
            messageTo(self, "handleSpawnMob", null, 0, false);
        }
        return SCRIPT_CONTINUE;
    }
}
