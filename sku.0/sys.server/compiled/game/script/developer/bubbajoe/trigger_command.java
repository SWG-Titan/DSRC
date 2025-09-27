package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Triggers a command to the player upon breaching.
@Created: Monday, 10/23/2023, at 3:28 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class trigger_command extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        createTriggerVolume("trigger_command", 5.0f, true);
        return SCRIPT_CONTINUE;
    }

    public int OnDestroy(obj_id self)
    {
        removeTriggerVolume("trigger_command");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        createTriggerVolume("trigger_command", 5.0f, true);
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (!isPlayer(breacher))
        {
            return SCRIPT_CONTINUE;
        }
        if (volumeName.equals("trigger_command"))
        {
            sendConsoleCommand("/" + getStringObjVar(self, "commandToExecute"), breacher);
            LOG("ethereal", "[Trigger Command]: " + getName(breacher) + " has triggered " + getStringObjVar(self, "commandToExecute") + " by entering volume egg " + getName(self) + ".");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeExited(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
}
