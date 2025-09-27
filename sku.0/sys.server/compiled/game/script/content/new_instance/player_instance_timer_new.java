package script.content.new_instance;/*
@Origin: dsrc.script.content.new_instance
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 6/3/2024, at 4:57 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.location;
import script.obj_id;

public class player_instance_timer_new extends script.base_script
{

    public float MAX_INSTANCE_TIME = 60f * 90f; // 90 minutes
    public float MSG_KICKOFF = 60f * 5f; // 5 minutes

    public int OnAttach(obj_id self)
    {
        messageTo(self, "notifyTimer", null, MSG_KICKOFF, false);
        messageTo(self, "handleInstanceExit", null, MAX_INSTANCE_TIME, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }


    public int notifyTimer(obj_id self, dictionary params) throws InterruptedException
    {
        broadcast(self, "You have 85 minutes left to complete this instance.");
        return SCRIPT_CONTINUE;
    }

    public int handleInstanceExit(obj_id self, dictionary params) throws InterruptedException
    {
        //This is only ran if the player has not completed the instance in the time limit. Otherwise, the exit terminal will be placed within the instance.
        broadcast(self, "You have ran out of time to complete this instance.");
        play2dNonLoopingSound(self, "sound/music_themequest_fail_criminal.snd");
        location loc = new location(0, 0, 0, "tatooine", null);
        warpPlayer(self, loc.area, loc.x, loc.y, loc.z, loc.cell, 0, 0, 0, "", false);
        return SCRIPT_CONTINUE;
    }
}
