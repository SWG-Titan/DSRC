package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.tmp_barker
@Author: BubbaJoeX
@Purpose: Barks a string on a loop.
@Note: Static string. No customization.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.ai_lib;
import script.library.chat;
import script.obj_id;

public class tmp_barker extends script.base_script
{
    public int OnAttach(obj_id self) throws InterruptedException
    {
        ai_lib.stop(self);
        ai_lib.setDefaultCalmBehavior(self, ai_lib.BEHAVIOR_STOP);
        setInvulnerable(self, true);
        setCondition(self, CONDITION_INTERESTING);
        createTriggerVolume("barker", 5, true);
        chirp(self);
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (volumeName.equals("barker"))
        {
            if (isPlayer(breacher))
            {
                chirp(self);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeExited(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (volumeName.equals("barker"))
        {
            if (isPlayer(breacher))
            {
                chat.chat(self, "Goodbye, " + getName(breacher) + "!");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public void chirp(obj_id self) throws InterruptedException
    {
        String bark = "Halt! This building is off limits until further notice. Please take all questions, comments or concerns up with your local Imperial Garrison.";
        chat.chat(self, bark);
    }
}
