package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose:  Rally Point Nova Enter
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 4/24/2024, at 11:25 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class tos_enter extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Transit Terminal");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Transit Terminal");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel to Rally Point Nova"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.msgbox(self, player, "\\#e3d005Are you sure you want to board the station?\\#.", sui.OK_CANCEL_ALL, "\\#e3d005Transit", "handleEnter");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleEnter(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (player == null)
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(self, "You have chosen to stay on the surface.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            sendPlayerTo(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public void sendPlayerTo(obj_id self, obj_id player) throws InterruptedException
    {
        location whereAmIGoing = getLocationObjVar(getPlanetByName("tatooine"), "hub_marker");
        warpPlayer(player, getCurrentSceneName(), whereAmIGoing.x, whereAmIGoing.y, whereAmIGoing.z, whereAmIGoing.cell, whereAmIGoing.x, whereAmIGoing.y, whereAmIGoing.z, "noHandler", true);
        setYaw(player, -57.f);
    }
}
