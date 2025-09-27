package script.content;/*
@Origin: dsrc.script.content
@Author: BubbaJoeX
@Purpose: Barks about food.
@Created: Friday, 9/8/2023, at 10:55 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.chat;
import script.library.utils;
import script.obj_id;

public class generic_barker extends script.base_script
{
    public static String[] NAMES = {
            "a farmer",
            "a smuggler",
            "an artisan",
            "a medic",
            "a farmhand",
            "a field worker",
            "a miner",
            "a herder",
            "a warden"
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        this.OnInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        String name = "";
        if (hasObjVar(self, "content.creature_name"))
        {
            name = getStringObjVar(self, "content.creature_name");
            setName(self, getStringObjVar(self, "content.creature_name"));
        }
        else
        {
            setName(self, NAMES[rand(0, 8)]);
        }
        if (hasObjVar(self, "content.creature_bark"))
        {
            String barkMsg = "content.creature_bark.msg";
            dictionary details = new dictionary();
            details.put("barkMessage", barkMsg);
            messageTo(self, "bark", details, utils.stringToFloat(getStringObjVar(self, "content.creature_bark.duration")), false);
        }
        return SCRIPT_CONTINUE;
    }

    public int bark(obj_id self, dictionary details) throws InterruptedException
    {
        String message = details.getString("barkMessage");
        chat.chat(self, message);
        //messageTo();
        return SCRIPT_CONTINUE;
    }
}
