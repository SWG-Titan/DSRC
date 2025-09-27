package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 7:51 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.chat;
import script.location;
import script.obj_id;

public class tos_mouse_droid extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        messageTo(self, "dropClicky", null, 15f, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int dropClicky(obj_id self, dictionary params) throws InterruptedException
    {
        location here = getLocation(self);
        here.cell = getContainedBy(self);
        here.x = here.x + 0.5f;
        obj_id clicky = createObject("object/tangible/loot/misc/marauder_token.iff", here);
        setName(clicky, "Event Token");
        setDescriptionString(clicky, "These tokens can be used to claim a prize at the event vendor.");
        attachScript(clicky, "content.tos_clicky");
        setObjVar(self, "lastToken", clicky);
        chat.chat(self, "[LOUD CLUNK]");
        float randomTime = rand(60.0f, 60.f * 5.0f);
        messageTo(self, "dropClicky", null, randomTime, false);
        return SCRIPT_CONTINUE;
    }
}
