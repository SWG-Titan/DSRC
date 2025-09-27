package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.no_macro_volume
@Author: BubbaJoeX
@Purpose: Creates a no-macro volume around the object.
@Note: The range var needs to be set on the object you want to use (i.e. an egg) and the volume will be created around it.
@Requirements: developer.bubbajoe.player_anti_afk && developer.bubbajoe.player_developer (gm player only)
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/


import script.location;
import script.obj_id;

public class no_macro_volume extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        if (!hasObjVar(self, "anti_macro_range"))
        {
            setObjVar(self, "anti_macro_range", 5.0f);
        }
        createTriggerVolume("anti_macro_volume", getFloatObjVar(self, "anti_macro_range"), true);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int setupVolume(obj_id self)
    {
        String macroVolume = "anti_macro_volume";
        location here = getLocation(self);
        createTriggerVolume(macroVolume, getFloatObjVar(self, "anti_afk_range"), true);
        return SCRIPT_CONTINUE;
    }
}
