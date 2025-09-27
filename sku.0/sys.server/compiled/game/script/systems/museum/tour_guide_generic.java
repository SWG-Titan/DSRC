package script.systems.museum;/*
@Origin: dsrc.script.systems.museum
@Author: BubbaJoeX
@Purpose: Barks about the current display
@Created: Sunday, 10/1/2023, at 12:31 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.


 @NOTE:

    Object Vars that need to be set dynamically:

    museum.displayName - The name of the display this script will call. Example: Welcome to the museum. This display is of a 'Krayt Dragon'.
    museum.index - The index of the display. Example: museum_display_1, this is set in order to create the trigger volume.

*/

import script.library.chat;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.dictionary;

public class tour_guide_generic extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (breacher == self)
        {
            return SCRIPT_CONTINUE;
        }
        if (isGod(breacher))
        {
            return SCRIPT_CONTINUE;
        }
        if (volumeName.equals("museum_display_" + getStringObjVar(self, "museum.index")))
        {
            String message = "Welcome to the museum. This display is of a " + getStringObjVar(self, "museum.display");
            bark(self, message);
        }
        return SCRIPT_CONTINUE;
    }

    public int setup(obj_id self)
    {
        String volumeName = "museum_display_" + getStringObjVar(self, "museum.index");
        createTriggerVolume(volumeName, 5, true);
        return SCRIPT_CONTINUE;
    }

    public int bark(obj_id self, String message) throws InterruptedException
    {
        setAnimationMood(self, "happy");
        chat.chat(self, message);
        return SCRIPT_CONTINUE;
    }
}
