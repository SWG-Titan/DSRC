package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 5/15/2024, at 10:14 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.obj_id;

public class tos_ent_pulse_controller extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnReceivedItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isPlayer(item))
        {
            if (!hasScript(item, "content.tos_ent_pulse") && utils.getPlayerProfession(item) == utils.ENTERTAINER)
            {
                broadcast(item, "As an entertainer, you can earn Entertainer Tokens in the Cantina, if you dance for longer than 5 minutes and maintain a dance or music performance.");
                setObjVar(item, "entertainer_system.startTime", getGameTime());
                attachScript(item, "content.tos_ent_pulse");
                LOG("ethereal", "[Entertainer System]: Entertainer: " + getPlayerFullName(item) + " has added to the Entertainer System on the Orbital Station.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnLostItem(obj_id self, obj_id destContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isPlayer(item))
        {
            if (hasScript(item, "content.tos_ent_pulse") && utils.getPlayerProfession(item) == utils.ENTERTAINER)
            {
                broadcast(item, "You have left the Station Cantina.");
                removeObjVar(item, "entertainer_system");
                detachScript(item, "content.tos_ent_pulse");
                cancelRecurringMessageTo(item, "token_pulse");
                if (hasMessageTo(item, "token_pulse"))
                {
                    stopListeningToMessage(item, "token_pulse");
                }
                LOG("ethereal", "[Entertainer System]: Entertainer: " + getPlayerFullName(item) + " has been removed from the Entertainer System on the Orbital Station.");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
