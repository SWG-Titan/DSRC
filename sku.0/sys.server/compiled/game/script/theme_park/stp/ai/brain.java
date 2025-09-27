package script.theme_park.stp.ai;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.chat;
import script.obj_id;

/**
 * @author BubbaJoe
 */
public class brain extends script.base_script
{

    public brain()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, String speech) throws InterruptedException
    {
        if (speech.equals("test"))
        {
            chat.chat(self, "HELP I HAVE LOST MY MIND!");
        }
        return SCRIPT_CONTINUE;
    }
}
