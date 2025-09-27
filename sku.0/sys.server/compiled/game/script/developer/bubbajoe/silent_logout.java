package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Logs players out if they are inactive for a certain amount of time. This is a silent logout script.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/28/2024, at 8:35 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.obj_id;

public class silent_logout extends script.base_script
{
    public static final float CYCLE_TIME = 60.0f;
    public boolean closeClientInsteadOfLogout = false;

    public int OnAttach(obj_id self)
    {
        messageTo(self, "activityPulse", null, 1, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int activityPulse(obj_id self, dictionary params)
    {
        if (isAwayFromKeyBoard(self))
        {
            if (closeClientInsteadOfLogout)
            {
                quit(self);
            }
            else
            {
                logout(self);
            }
        }
        if (getCurrentSceneName().contains("space_"))
        {
            if (closeClientInsteadOfLogout)
            {
                quit(self);
            }
            else
            {
                logout(self);
            }
        }
        messageTo(self, "nextPulse", null, 60f, false);
        return SCRIPT_CONTINUE;
    }

    public int logout(obj_id self)
    {
        sendConsoleCommand("/dumpPausedCommands", self);
        sendConsoleCommand("/echo You are being logged out due to lack of activity.", self);
        disconnectPlayer(self);
        detachScript(self, "developer.bubbajoe.silent_logout");
        return SCRIPT_CONTINUE;
    }

    public int quit(obj_id self)
    {
        sendConsoleCommand("/quit", self);
        detachScript(self, "developer.bubbajoe.silent_logout");
        return SCRIPT_CONTINUE;
    }

    public int nextPulse(obj_id self, dictionary params)
    {
        messageTo(self, "activityPulse", null, CYCLE_TIME, false);
        return SCRIPT_CONTINUE;
    }

}
