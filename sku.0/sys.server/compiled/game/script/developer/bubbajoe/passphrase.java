package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: NPC that does something when the player has spoken the correct word
@Created: Monday, 10/30/2023, at 1:47 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.chat;
import script.obj_id;

public class passphrase extends script.base_script
{
    String passphrase = "Coruscant";

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (!isPlayer(speaker))
        {
            return SCRIPT_CONTINUE;
        }
        if (text.equals(passphrase))
        {
            chat.chat(self, "Welcome!");
            //doSomethingHere();
        }
        return SCRIPT_CONTINUE;
    }
}
