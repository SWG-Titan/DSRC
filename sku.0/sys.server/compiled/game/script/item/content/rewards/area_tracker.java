package script.item.content.rewards;

/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Consumable object to track non-playable creatures.
@Note: This script tracks nearby creatures and allows player selection.
@Created: Friday, 9/22/2023, at 9:24 PM,
*/

import script.*;
import script.library.sui;
import script.library.utils;

import java.util.ArrayList;
import java.util.Comparator;

public class area_tracker extends script.base_script
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
        setName(self, "Area Tracker");
        setDescriptionString(self, "This device tracks nearby creatures within 512 meters.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true) || isGod(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Track Creatures"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            trackCreatures(self, player, 512);
        }
        return SCRIPT_CONTINUE;
    }

    public void trackCreatures(obj_id self, obj_id player, float radius) throws InterruptedException
    {
        location searchOrigin = getLocation(player);
        obj_id[] creatures = getObjectsInRange(searchOrigin, radius);

        if (creatures == null || creatures.length == 0)
        {
            broadcast(player, "No creatures found in range.");
            return;
        }

        ArrayList<obj_id> trackableCreaturesList = new ArrayList<>();
        ArrayList<String> creatureEntriesList = new ArrayList<>();
        ArrayList<Float> distances = new ArrayList<>();

        for (obj_id creature : creatures)
        {
            if (isMob(creature) && !hasObjVar(creature, "ai.pet") && !isInvulnerable(creature) && isInWorldCell(creature))
            {
                float distance = getDistance(searchOrigin, getLocation(creature));
                distance = Math.max(distance, 0.1f);
                String entry = getEncodedName(creature) + " | " + Math.round(distance) + "m away";
                trackableCreaturesList.add(creature);
                creatureEntriesList.add(entry);
                distances.add(distance);
            }
        }

        if (trackableCreaturesList.isEmpty())
        {
            broadcast(player, "No trackable creatures found.");
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
                    // Warp player to creature’s location if god. Otherwise make a waypoint.
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
