package script.content;/*
@Origin: dsrc.script.content.nb_quest
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 5/16/2024, at 9:00 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class tos_jail_exit extends base_script
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
        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Leave Jail"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            String title = "Agreement";
            String prompt = "By clicking OK, you agree to leave the jail and will not offend again. If I do offend again, I will be banned from the server. Click \"OK\" to leave the jail";
            String handler = "handleLeaveJail";
            sui.msgbox(self, player, prompt, sui.OK_CANCEL, title, handler);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleLeaveJail(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (player == null || player == obj_id.NULL_ID)
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_OK)
        {
            location jail = getLocationObjVar(getPlanetByName("tatooine"), "tos_movement_marker.tos_jail.exit." + player);
            if (jail == null)
            {
                jail = getLocationObjVar(getPlanetByName("tatooine"), "tos");
            }
            warpPlayer(self, jail.area, jail.x, jail.y, jail.z, null, jail.x, jail.y, jail.z, "", true);
        }
        else
        {
            broadcast(player, "You have chosen to remain in jail. You will not be able to leave until you agree to the terms.");
        }
        return SCRIPT_CONTINUE;
    }
}
