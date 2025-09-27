package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Barks a message on a timer
@Requirements: <no requirements>
@Notes: GM only
@Created: Wednesday, 4/24/2024, at 9:58 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.chat;
import script.library.sui;

public class barker extends base_script
{
    public int OnAttach(obj_id self)
    {
        setCondition(self, CONDITION_INTERESTING);
        setInvulnerable(self, true);
        setHologramType(self, HOLOGRAM_TYPE1_QUALITY4);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        kickOffChatter(self);
        return SCRIPT_CONTINUE;
    }

    public int kickOffChatter(obj_id self)
    {
        if (hasObjVar(self, "barkMessage"))
        {
            dictionary details = new dictionary();
            details.put("barkMessage", getStringObjVar(self, "barkMessage"));
            if (hasObjVar(self, "barkDelay"))
            {
                details.put("barkDelay", getFloatObjVar(self, "barkDelay"));
            }
            else
            {
                setObjVar(self, "barkDelay", 60.0f);
                details.put("barkDelay", 60.0f);
            }
            messageTo(self, "bark", details, 60.0f, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Start Cycle"));
            mi.addRootMenu(menu_info_types.SERVER_MENU2, new string_id("Set Message"));
            mi.addRootMenu(menu_info_types.SERVER_MENU3, new string_id("Set Delay"));
            mi.addRootMenu(menu_info_types.SERVER_MENU4, new string_id("Stop Cycle"));
            mi.addRootMenu(menu_info_types.SERVER_MENU5, new string_id("Speak Message Now (No Cycle)"));
        }
        else
        {
            debugConsoleMsg(player, "\\#DD1234This object does not interest you.\\#");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            dictionary details = new dictionary();
            if (!hasObjVar(self, "barkMessage"))
            {
                broadcast(player, "You must set a message first.");
                return SCRIPT_CONTINUE;
            }
            if (!hasObjVar(self, "barkDelay"))
            {
                broadcast(player, "You must set a delay in seconds first.");
                return SCRIPT_CONTINUE;
            }
            details.put("barkMessage", getStringObjVar(self, "barkMessage"));
            details.put("barkDelay", getFloatObjVar(self, "barkDelay"));
            messageTo(self, "bark", details, getFloatObjVar(self, "barkDelay"), false);
        }
        else if (item == menu_info_types.SERVER_MENU2)
        {
            sui.inputbox(self, player, "Enter the message you would like this entity to bark.", "handleMessageInput");
        }
        else if (item == menu_info_types.SERVER_MENU3)
        {
            sui.inputbox(self, player, "Enter the delay in seconds you would like this entity to bark.", "handleDelayInput");
        }
        else if (item == menu_info_types.SERVER_MENU4)
        {
            removeObjVar(self, "barkMessage");
            removeObjVar(self, "barkDelay");
            stopListeningToMessage(self, "bark");
            broadcast(player, "Barking cycle stopped.");
            return SCRIPT_CONTINUE;
        }
        else if (item == menu_info_types.SERVER_MENU5)
        {
            if (!hasObjVar(self, "barkMessage"))
            {
                broadcast(player, "You must set a message first.");
                return SCRIPT_CONTINUE;
            }
            String message = getStringObjVar(self, "barkMessage");
            chat.chat(self, message);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleMessageInput(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String message = sui.getInputBoxText(params);
        setObjVar(self, "barkMessage", message);
        broadcast(player, "Message set to: " + message);
        return SCRIPT_CONTINUE;
    }

    public int handleDelayInput(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        float delay = Float.parseFloat(sui.getInputBoxText(params));
        setObjVar(self, "barkDelay", delay);
        broadcast(player, "Delay set to: " + delay + " seconds.");
        return SCRIPT_CONTINUE;
    }

    public int bark(obj_id self, dictionary details) throws InterruptedException
    {
        String message = details.getString("barkMessage");
        chat.chat(self, message);
        messageTo(self, "bark", details, getFloatObjVar(self, "barkDelay"), false);
        return SCRIPT_CONTINUE;
    }

    public int OnStartNpcConversation(obj_id self, obj_id speaker) throws InterruptedException
    {
        chat.chat(self, getStringObjVar(self, "barkMessage"));
        return SCRIPT_CONTINUE;
    }
}
