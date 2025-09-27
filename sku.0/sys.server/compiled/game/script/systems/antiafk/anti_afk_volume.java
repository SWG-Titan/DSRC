package script.systems.antiafk;/*
@Origin: dsrc.script.systems.antiafk
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 2/10/2025, at 12:34 AM, 
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.holiday;
import script.library.utils;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.menu_info;
import script.dictionary;

public class anti_afk_volume extends script.base_script
{
    private static final String VOLUME = "anti_macro_volume";
    private static final float RADIUS = 15f;

    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnDestroy(obj_id self)
    {
        LOG("ethereal", "[Anti-AFK]: Removing 15m anti-afk radius from " + getLocation(self).toLogFormat());
        removeTriggerVolume(VOLUME);
        return SCRIPT_CONTINUE;
    }

    public int sync(obj_id self)
    {
        setName(self, "Anti-AFK Volume");
        LOG("ethereal", "[Anti-AFK]: Locking down 15m from " + getLocation(self).toLogFormat());
        createTriggerVolume(VOLUME, RADIUS, false);
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = "volume";
        attribs[idx] = VOLUME;
        idx++;
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String name, obj_id who) throws InterruptedException
    {
        if (!name.equals(VOLUME))
        {
            return SCRIPT_CONTINUE;
        }
        handleMacroTermination(self, who);
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeExited(obj_id self, String name, obj_id who) throws InterruptedException
    {
        //you have left a macro-free area.
        return SCRIPT_CONTINUE;
    }

    private void handleMacroTermination(obj_id self, obj_id who)
    {
        if (isPlayer(who))
        {
            broadcast(who, "Your macros have been dumped.");
            sendConsoleCommand("/dumpPausedCommands", who);
            LOG("ethereal", "[Anti-AFK] " + getName(who) + " has had their macros turned off by " + self);
        }
    }

}
