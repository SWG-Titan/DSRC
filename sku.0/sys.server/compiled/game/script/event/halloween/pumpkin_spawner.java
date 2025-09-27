package script.event.halloween;/*
@Origin: dsrc.script.event.halloween.pumpkin_spawner
@Author: BubbaJoeX
@Purpose: Handles the spawning of pumpkins for the Galactic Moon Festival.
@Notes;
    Pumpkins are spawned in a 7250m radius around the spawner which needs to be placed in the direct center of the planet
    This script is meant to be used by GMs to spawn pumpkins for the event.
    This script is not meant to be used by players.
    No more than 250 pumpkins should be spawned per planet.
    The premise of this event is Jabba had his henchmen scatter pumpkins around the galaxy for his amusement.

@Created: Sunday, 2/25/2024, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.utils;

import java.lang.reflect.InvocationTargetException;

public class pumpkin_spawner extends base_script
{
    public static final string_id DSID_USE = new string_id("Generate Pumpkin Field");
    public static int PUMPKIN_COUNT = 3000;
    public static float RANGE = 7610.0f;
    public String[] NAME_VARIATIONS = {
            "a plump pumpkin",
            "a regular pumpkin",
            "a scrawny pumpkin",
            "a nasty pumpkin",
            "a scary pumpkin",
            "a jagged pumpkin",
            "a wicked pumpkin",
            "a spooky pumpkin",
            "a creepy pumpkin",
            "a festive pumpkin",
            "a pumpkin",
    };

    public void pumpkin_spawner()
    {
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        if (isGod(player))
        {
            int idx = utils.getValidAttributeIndex(names);
            if (idx == -1)
            {
                return SCRIPT_CONTINUE;
            }
            names[idx] = utils.packStringId(new string_id("Planetary pumpkin count"));
            attribs[idx] = "" + getPumpkins(getLocation(self));
            idx++;
            names[idx] = utils.packStringId(new string_id("Last issuer"));
            attribs[idx] = getPlayerFullName(getObjIdObjVar(self, "event.halloween.pumpkin_issuer"));
            idx++;
            names[idx] = utils.packStringId(new string_id("Last issued"));
            attribs[idx] = getCalendarTimeStringLocal(getIntObjVar(self, "event.halloween.pumpkin_issued"));
            idx++;
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int getPumpkins(location where) throws InterruptedException
    {
        obj_id[] pumpkins = getAllObjectsWithScript(where, RANGE, "event.halloween.pumpkin_smasher_object");
        int count = 0;
        if (pumpkins != null)
        {
            count = pumpkins.length;
        }
        return count;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "[Event] Pumpkin Spawner");
        setDescriptionString(self, "Use this terminal to spawn pumpkins for the Galactic Moon Festival.\n*Do not spawn more than 3000 pumpkins per day.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        String halloweenRunning = getConfigSetting("GameServer", "halloween");
        if (halloweenRunning != null && halloweenRunning.equals("true"))
        {
            messageTo(self, "delayedWorldSpawn", null, 60f * 5f, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, DSID_USE);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException, InvocationTargetException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            broadcast(player, "Greetings, " + getPlayerFullName(player) + ". Last user: " + getPlayerFullName(getObjIdObjVar(self, "event.halloween.pumpkin_issuer")) + ".");
            handleWorldSpawn(self, player);
            broadcast(player, "Spawning " + PUMPKIN_COUNT + " pumpkins.");
            debugConsoleMsg(player, "Do not click this terminal more than 1 time per 24 hours per planet.");
            setObjVar(self, "event.halloween.pumpkin_issuer", player);
        }
        return SCRIPT_CONTINUE;
    }

    public void handleWorldSpawn(obj_id self, obj_id player) throws InterruptedException
    {
        for (int i = 0; i <= PUMPKIN_COUNT; i++)
        {
            location pumpkin_loc = new location();
            pumpkin_loc.area = getCurrentSceneName();
            pumpkin_loc.x = pumpkin_loc.x + (rand(-RANGE, RANGE));
            pumpkin_loc.z = pumpkin_loc.z + (rand(-RANGE, RANGE));
            pumpkin_loc.y = getHeightAtLocation(pumpkin_loc.x, pumpkin_loc.z);
            //from the current location, get the lower left and upper right corners of the area
            location locLowerLeft = new location(pumpkin_loc);
            locLowerLeft.x = locLowerLeft.x - 15.0f;
            locLowerLeft.z = locLowerLeft.z - 15.0f;
            location locUpperRight = new location(pumpkin_loc);
            locUpperRight.x = locUpperRight.x + 15.0f;
            locUpperRight.z = locUpperRight.z + 15.0f;
            location goodLoc = getGoodLocationAvoidCollidables(15.0f, 15.0f, locLowerLeft, locUpperRight, false, false, 6.5f);
            obj_id pumpkin = create.object("object/tangible/holiday/halloween/pumpkin_object.iff", goodLoc);
            if (!isIdValid(pumpkin))
            {
                continue;
            }
            attachScript(pumpkin, "event.halloween.pumpkin_smasher_object");
            setYaw(pumpkin, rand(0.0f, 360.0f));
            modifyRoll(pumpkin, rand(-2f, 2.0f));
            modifyPitch(pumpkin, rand(-2f, 2.0f));
            String randomName = getRandFromArray(NAME_VARIATIONS);
            if (randomName == null)
            {
                randomName = color("EEAB19", "a pumpkin");
            }
            setName(pumpkin, color("EEAB19", randomName));
            setDescriptionString(pumpkin, color("EEAB19", "Smash this pumpkin to get a reward!"));
        }
        setObjVar(self, "event.halloween.pumpkin_issued", getCalendarTime());
        setObjVar(self, "event.halloween.pumpkin_issuer", player);
        LOG("events", "[GMF Pumpkin Spawning]: Spawner (" + self + ") has generated " + PUMPKIN_COUNT + " pumpkins, issued by " + getPlayerFullName(getObjIdObjVar(self, "event.halloween.pumpkin_issuer")) + " at " + getLocation(self).toReadableFormat(true) + ".");
    }

    public int delayedWorldSpawn(obj_id self, dictionary params) throws InterruptedException
    {
        handleWorldSpawn(self, null);
        return SCRIPT_CONTINUE;
    }

    public String getRandFromArray(String[] array) throws InterruptedException
    {
        int randIndex = rand(0, array.length - 1);
        return array[randIndex];
    }

    public String color(String color, String text) throws InterruptedException
    {
        return "\\#" + color + text + "\\#.";
    }
}

