package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 6/20/2024, at 8:13 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.menu_info;
import script.obj_id;

public class toggle_radar extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        shouldToggleRadar(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        shouldToggleRadar(self);
        return SCRIPT_CONTINUE;
    }

    private void shouldToggleRadar(obj_id self)
    {
        if (hasObjVar(self, "hideFromRadar"))
        {
            boolean value = getBooleanObjVar(self, "hideFromRadar");
            setVisibleOnMapAndRadar(self, value);
        }
        LOG("ethereal", "[Content]: Tangible object has developer.bubbajoe.toggle_radar but does not have the hideFromRadar objvar | " + self);
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id listener, obj_id source, String speech) throws InterruptedException
    {
        if (!isGod(source))
        {
            return SCRIPT_CONTINUE;
        }
        if (speech.equals("hideFromRadar"))
        {
            setObjVar(listener, "hideFromRadar", true);
            setVisibleOnMapAndRadar(listener, false);
        }
        else if (speech.equals("showOnRadar"))
        {
            setObjVar(listener, "hideFromRadar", false);
            setVisibleOnMapAndRadar(listener, true);
        }
        return SCRIPT_CONTINUE;
    }

}
