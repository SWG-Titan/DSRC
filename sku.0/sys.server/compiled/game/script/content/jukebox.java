package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Swap out soundobjects on the fly.
@Requirements: <no requirements>
@Notes: Sounds might not stop playing when you swap them out. idk how to fix.
@Created: Monday, 5/6/2024, at 11:32 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class jukebox extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isGod(player))
        {
            int jukeboxMenu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Configure Jukebox"));
            if (hasObjVar(self, "currentSoundObject"))
            {
                mi.addSubMenu(jukeboxMenu, menu_info_types.SERVER_MENU2, new string_id("Remove SoundObject"));
            }
            else
            {
                mi.addSubMenu(jukeboxMenu, menu_info_types.SERVER_MENU1, new string_id("Select SoundObject"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public String[] getSoundObjects()
    {
        String directoryPath = "/home/swg/swg-main/data/sku.0/sys.server/compiled/game/object/soundobject/";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        List<String> iffFilePaths = new ArrayList<>();
        if (files != null)
        {
            Arrays.sort(files); //@Note: to lock the indexes of the files, we sort them. This means that the indexes will always be the same, which is needed for sui.getListboxSelectedRow
            for (File file : files)
            {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".iff"))
                {
                    String filePath = file.getPath().replaceFirst("^/home/swg/swg-main/data/sku.0/sys.server/compiled/game/", "");
                    iffFilePaths.add(filePath);
                }
            }
        }
        return iffFilePaths.toArray(new String[0]);
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            sui.listbox(self, player, "Select a soundobject template to place at the marker's location.", sui.OK_CANCEL, "Sound Emitter", getSoundObjects(), "handleSoundObjectInput", true, false);
        }
        else if (item == menu_info_types.SERVER_MENU2)
        {
            removeCurrentSoundObject(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSoundObjectInput(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int soundObjectIndex = sui.getListboxSelectedRow(params);
        String[] soundObjects = getSoundObjects();
        if (soundObjectIndex < 0 || soundObjectIndex >= soundObjects.length)
        {
            return SCRIPT_CONTINUE;
        }
        String soundObject = soundObjects[soundObjectIndex];
        obj_id emitter = createObject(soundObject, getLocation(self));
        if (isIdValid(emitter))
        {
            setObjVar(self, "currentSoundObject", emitter);
            persistObject(emitter);
        }
        else
        {
            broadcast(player, "Failed to create sound object. ID is invalid.");
        }
        return SCRIPT_CONTINUE;
    }

    private void removeCurrentSoundObject(obj_id self, obj_id player)
    {
        obj_id soundObject = getObjIdObjVar(self, "currentSoundObject");
        if (isIdValid(soundObject))
        {
            broadcast(player, "Removing sound object: " + soundObject);
            destroyObject(soundObject);
            removeObjVar(self, "currentSoundObject");
        }
    }
}
