package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.chat;
import script.library.groundquests;
import script.obj_id;

/**
 * @author BubbaJoe
 */
public class spammer extends script.base_script
{
    public spammer()
    {
    }

    public static int OnAttach(obj_id self) throws InterruptedException
    {
        createTriggerVolume("hub_spammer", 2.0f, true);
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (!isPlayer(breacher))
        {
            return SCRIPT_CONTINUE;
        }
        if (volumeName.equals("hub_spammer"))
        {
            if (groundquests.isQuestActive(breacher, "quest/taan_preq"))
            {
                chat.chat(self, "Scanning Target....");
                chat.chat(self, "Target locked: " + getFirstName(breacher).toUpperCase());
                if (hasObjVar(breacher, "hub.generator_quest"))
                {
                    chat.chat(self, "Hostile target determined. Intializing Combat Routine");
                    startCombat(self, breacher);
                }
                else
                {
                    chat.chat(self, "No hostile targets found. Recycling...");
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
