package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: TOS Event Signup - Flag for waypoint creation
@Requirements: <no requirements>
@Notes: Usage for Wheres Watto only atm
@Created: Saturday, 6/15/2024, at 1:25 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class tos_event_signup extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Sign Up"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.msgbox(self, player, "Please opt-in to receive event information.", "handleEventConsent");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleEventConsent(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have opted out of events information.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            setObjVar(player, "eventOptIn", true);
            broadcast(player, "You have opted out of events information.");
        }
        return SCRIPT_CONTINUE;
    }
}
