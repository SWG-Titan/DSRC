package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 11:18 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.player_structure;
import script.library.sui;
import script.library.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tos_emitter_sound extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "[DEVL] Sound Emitter");
        setDescriptionString(self, "This object emits sound effects");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "[DEVL] Sound Emitter");
        messageTo(self, "doCycle", null, 15.0f, false);
        setDescriptionString(self, "This object emits sound effects");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            int dad = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Emitter Configuration"));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU2, new string_id("Set Emitter Sound Effect"));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU3, new string_id("Set Emitter Delay"));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU4, new string_id("Start Cycle"));
            mi.addSubMenu(dad, menu_info_types.SERVER_MENU5, new string_id("Stop Cycle"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU2)
        {
            sui.inputbox(self, player, "Step 1 of 2:\n Enter search term for Sound Effect.", sui.OK_CANCEL, "Sound Effect Emitter", sui.INPUT_NORMAL, null, "handleEmitterSearch", null);
        }
        else if (item == menu_info_types.SERVER_MENU3)
        {
            sui.inputbox(self, player, "Enter the delay in seconds between each cycle", sui.OK_CANCEL, "Sound Effect Emitter", sui.INPUT_NORMAL, null, "handleSetEmitterDelay", null);
        }
        else if (item == menu_info_types.SERVER_MENU4)
        {
            startEmitterCycle(self);
        }
        else if (item == menu_info_types.SERVER_MENU5)
        {
            broadcast(player, "Stopping emitter cycle");
            stopListeningToMessage(self, "doCycle");
        }
        else if (item == menu_info_types.ITEM_USE)
        {
            obj_id[] occupants = player_structure.getPlayersInBuilding(getTopMostContainer(self));
            if (!isInWorldCell(player))
            {
                for (obj_id occupant : occupants)
                {
                    play2dNonLoopingSound(occupant, getStringObjVar(self, "effect"));
                }
            }
            else
            {
                obj_id[] playersInRange = getAllPlayers(getLocation(self), 64.0f);
                for (obj_id target : playersInRange)
                {
                    play2dNonLoopingSound(target, getStringObjVar(self, "effect"));
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleEmitterSearch(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        if (text == null || text.equals(""))
        {
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "search", text);
        sui.listbox(self, player, "Step 2 of 2: \n Select a Sound Effect from the list.", sui.OK_CANCEL, "Sound Effect Emitter", getClientEffects(self, text, true), "handleSetEmitterEffect", true);
        return SCRIPT_CONTINUE;
    }

    public int handleSetEmitterEffect(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String[] effects = getClientEffects(self, getStringObjVar(self, "search"), true);
        if (effects == null || effects.length == 0)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx >= effects.length)
        {
            return SCRIPT_CONTINUE;
        }
        String effect = effects[idx];
        setObjVar(self, "effect", "sound/" + effect);
        obj_id[] occupants = player_structure.getPlayersInBuilding(getTopMostContainer(self));
        for (obj_id occupant : occupants)
        {
            play2dNonLoopingSound(occupant, "sound/" + effect);
        }
        broadcast(player, "Sound Effect set to: " + effect);
        return SCRIPT_CONTINUE;
    }

    public int doCycle(obj_id self, dictionary params) throws InterruptedException
    {
        location here = getLocation(self);
        float delay = getFloatObjVar(self, "delay");
        if (delay > 0)
        {
            obj_id[] occupants = player_structure.getPlayersInBuilding(getTopMostContainer(self));
            for (obj_id occupant : occupants)
            {
                play2dNonLoopingSound(occupant, getStringObjVar(self, "effect"));
            }
            messageTo(self, "doCycle", null, delay, false);
        }
        else
        {
            obj_id[] occupants = player_structure.getPlayersInBuilding(getTopMostContainer(self));
            for (obj_id occupant : occupants)
            {
                play2dNonLoopingSound(occupant, getStringObjVar(self, "effect"));
            }
            messageTo(self, "doCycle", null, 15.0f, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetEmitterDelay(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        if (text == null || text.equals(""))
        {
            return SCRIPT_CONTINUE;
        }
        float delay = utils.stringToFloat(text);
        setObjVar(self, "delay", delay);
        broadcast(player, "Sound Effect Delay set to: " + delay);
        return SCRIPT_CONTINUE;
    }

    public int startEmitterCycle(obj_id self)
    {
        LOG("ethereal", "[Emitter Cycle]: Starting emitter cycle");
        location here = getLocation(self);
        float delay = getFloatObjVar(self, "delay");
        if (delay > 0)
        {
            playClientEffectLoc(self, getStringObjVar(self, "effect"), here, 1.0f);
            messageTo(self, "doCycle", null, delay, false);
        }
        else
        {
            playClientEffectLoc(self, getStringObjVar(self, "effect"), here, 1.0f);
            messageTo(self, "doCycle", null, 15.0f, false);
        }
        return SCRIPT_CONTINUE;
    }

    public String[] getClientEffects(obj_id self, String key, boolean recursive) throws InterruptedException
    {
        LOG("ethereal", "[Template Lookup]: Calling getAllTemplates(self, " + key + ", " + recursive + ")");
        String directoryPath = "/home/swg/swg-main/serverdata/sound";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        List<String> iffFilePaths = new ArrayList<>();
        if (files != null)
        {
            LOG("ethereal", "[Template Lookup]: Found " + files.length + " files in " + directoryPath);
            LOG("ethereal", "[Template Lookup]: Sorting indexing of files");
            Arrays.sort(files); //@Note: to lock the indexes of the files, we sort them. This means that the indexes will always be the same, which is needed for sui.getListboxSelectedRow
            for (File file : files)
            {
                LOG("ethereal", "[Template Lookup]: Found matching file, Adding  " + file.getName() + " to list");
                if (file.isFile() && file.getName().toLowerCase().endsWith(".snd") && file.getName().toLowerCase().contains(key.toLowerCase()))
                {
                    String filePath = file.getPath().replaceFirst("^/home/swg/swg-main/serverdata/sound/", "");
                    iffFilePaths.add(filePath);
                }
            }
        }
        else
        {
            LOG("ethereal", "[Template Lookup]: No files found in " + directoryPath + " for key " + key);
        }
        return iffFilePaths.toArray(new String[0]);
    }
}
