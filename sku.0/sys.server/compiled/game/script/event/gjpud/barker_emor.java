package script.event.gjpud;/*
@Origin: dsrc.script.event.gjpud.barker_emor
@Author: BubbaJoeX
@Purpose: Barks about GJPUD event.
@Notes;
    String is static, so it cannot be changed in an adhoc manner..
@Created: Sunday, 2/25/2024, at 11:42 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.chat;
import script.obj_id;

public class barker_emor extends script.base_script
{
    private static final String BARK_STRING = "Got Scrap?";

    public int OnAttach(obj_id self)
    {
        recoup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        recoup(self);
        return SCRIPT_CONTINUE;
    }

    public int recoup(obj_id self)
    {
        setName(self, "Emor (a traveling scrap peddler)");
        setDescriptionString(self, "Emor is a traveling peddler, he is looking for scrap heaps with his associates. Give his associates any you might find and he might reward you!");
        messageTo(self, "bark", null, 480f, false);
        return SCRIPT_CONTINUE;
    }

    public int bark(obj_id self, dictionary params) throws InterruptedException
    {
        chat.chat(self, BARK_STRING);
        messageTo(self, "bark", null, 480f, false);
        LOG("ethereal", "[GJPUD Associate]: Emor has barked, calling again in 8 minutes time.");
        return SCRIPT_CONTINUE;
    }
}
