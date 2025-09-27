package script.item.content.rewards;

/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Consumable object to track objects.
@Note: This script tracks nearby creatures and allows player selection.
@Created: Friday, 9/22/2023, at 9:24 PM,
*/

import script.*;
import script.library.sui;
import script.library.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class object_tracker extends base_script
{
    public int OnAttach(obj_id self)
    {
        reInit(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        reInit(self);
        return SCRIPT_CONTINUE;
    }

    public int reInit(obj_id self)
    {
        setName(self, "Object Tracker");
        setDescriptionString(self, "This device tracks nearby objects within 512 meters.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true) || isGod(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Track Objects"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            trackObjects(self, player, 512);
        }
        return SCRIPT_CONTINUE;
    }

    public void trackObjects(obj_id self, obj_id player, float radius) throws InterruptedException
    {
        location searchOrigin = getLocation(player);
        obj_id[] objects = getObjectsInRange(searchOrigin, radius);

        if (objects == null || objects.length == 0)
        {
            broadcast(player, "No objects found in range.");
            return;
        }

        ArrayList<obj_id> trackableCreaturesList = new ArrayList<>();
        ArrayList<String> creatureEntriesList = new ArrayList<>();
        ArrayList<Float> distances = new ArrayList<>();
        int[] ALLOWED_GOTS = {
                GOT_misc_item_usable,
                GOT_misc_sign,
                GOT_misc_furniture,
                GOT_terminal,
                GOT_misc_container
        };
        String[] BAD_NAMES = {
                "object/tangible/ground_spawning",
                "object/tangible/spawning",
                "object/static/",
                "object/mobile/",
                "object/creature/",
                "object/weapon/",
        };
        ArrayList<Integer> allowedGots = new ArrayList<>();
        for (int g : ALLOWED_GOTS) allowedGots.add(g);
        ArrayList<String> badNames = new ArrayList<>(Arrays.asList(BAD_NAMES));
        for (obj_id indi : objects)
        {
            if (!allowedGots.contains(getGameObjectType(indi)) || badNames.contains(getTemplateName(indi)))
            {
                continue;
            }
            float distance = getDistance(searchOrigin, getLocation(indi));
            distance = Math.max(distance, 0.1f);
            String entry = "";
            if (isInWorldCell(indi))
            {
                entry = getEncodedName(indi) + " | " + Math.round(distance) + "m away";
            }
            else
            {
                if (getEncodedName(indi).startsWith("\\#"))
                {
                    entry = getTemplateName(indi) + " | inside a building " + (isGod(player) ? getTopMostContainer(indi) : " somewhere.");
                }
                else
                {
                    entry = getEncodedName(indi) + " | inside a building " + (isGod(player) ? getTopMostContainer(indi) : " somewhere.");
                }
            }
            trackableCreaturesList.add(indi);
            creatureEntriesList.add(entry);
            distances.add(distance);
        }

        if (trackableCreaturesList.isEmpty())
        {
            broadcast(player, "No trackable objects found.");
            return;
        }

        // Sort by distance
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < distances.size(); i++) indices.add(i);
        indices.sort(Comparator.comparing(distances::get));

        // Prepare sorted entries
        String[] creatureEntries = new String[indices.size()];
        obj_id[] trackableCreatures = new obj_id[indices.size()];
        for (int i = 0; i < indices.size(); i++)
        {
            int index = indices.get(i);
            creatureEntries[i] = creatureEntriesList.get(index);
            trackableCreatures[i] = trackableCreaturesList.get(index);
        }

        dictionary creatureDict = new dictionary();
        creatureDict.put("trackableCreatures", trackableCreatures);

        // Show the listbox on the player, with self as callback context
        sui.listbox(self, player, "Select a creature to track.", sui.OK_CANCEL, "Creature Tracker", creatureEntries, "handleCreatureSelection", true, false);
        utils.setScriptVar(self, "creatureTracker.trackableCreatures", trackableCreatures);
        utils.setScriptVar(self, "creatureTracker.creatureEntries", creatureEntries);

    }

    public int handleCreatureSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id issuer = sui.getPlayerId(params);
        int buttonPressed = sui.getIntButtonPressed(params);
        int selectedIdx = sui.getListboxSelectedRow(params);

        // Enhanced logging
        broadcast(issuer, "Button pressed: " + buttonPressed);
        broadcast(issuer, "Selected index: " + selectedIdx);

        if (buttonPressed == sui.BP_OK && selectedIdx >= 0)
        {
            obj_id[] trackableCreatures = utils.getObjIdArrayScriptVar(self, "creatureTracker.trackableCreatures");
            String[] creatureNames = utils.getStringArrayScriptVar(self, "creatureTracker.creatureEntries");

            if (trackableCreatures != null && selectedIdx < trackableCreatures.length)
            {
                obj_id selectedCreature = trackableCreatures[selectedIdx];
                broadcast(issuer, "Now tracking: " + creatureNames[selectedIdx]);
                setObjVar(self, "creatureTracker.trackedCreature", selectedCreature);
                location creatureLocation = getLocation(selectedCreature);
                if (isGod(issuer))
                {
                    // Warp player to creature’s location
                    broadcast(issuer, "Warping to creature location: " + creatureLocation);
                    movePlayer(issuer, creatureLocation, "handleWarp", false);
                }
                else
                {
                    obj_id waypoint = createWaypointInDatapad(issuer, creatureLocation);
                    setWaypointColor(waypoint, "yellow");
                    setWaypointActive(waypoint, true);
                    setWaypointName(waypoint, creatureNames[selectedIdx]);
                    broadcast(issuer, "Waypoint created for: " + creatureNames[selectedIdx]);
                }

            }
            else
            {
                broadcast(issuer, "Selection out of range or list empty.");
            }
        }
        else
        {
            broadcast(issuer, "No creature selected.");
        }

        utils.removeScriptVar(self, "creatureTracker.trackableCreatures");
        return SCRIPT_CONTINUE;
    }

    public void movePlayer(obj_id player, location loc, String callback, boolean forceLoadScreen)
    {
        warpPlayer(player, loc.area, loc.x, loc.y, loc.z, loc.cell, 0f, 0f, 0f, callback, forceLoadScreen);
    }

    public int handleWarp(obj_id self, dictionary params) throws InterruptedException
    {
        broadcast(self, "You have arrived at the creature's location.");
        return SCRIPT_CONTINUE;
    }
}
