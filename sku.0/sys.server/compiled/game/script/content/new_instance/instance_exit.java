package script.content.new_instance;/*
@Origin: dsrc.script.content.new_instance
@Author:  BubbaJoeX
@Purpose: Returns players to Mos Eisley Starport Ring
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 6/3/2024, at 4:44 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class instance_exit extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Return to Tatooine"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.msgbox(self, player, "Are you sure you wish to depart?\n***You cannot get back here without being grouped with 4 other players.***", sui.YES_NO, "Departure Confirmation", "sendToMosEisleyStarportRing");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int sendToMosEisleyStarportRing(obj_id self, dictionary params) throws InterruptedException
    {
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(self, "You have canceled your departure home.");
            return SCRIPT_CONTINUE;
        }
        detachScript(self, "content.new_instance.player_instance_timer_new");
        location loc = new location(3627.619f, 5.0f, -4772.5166f, "tatooine", null);
        warpPlayer(self, loc.area, loc.x, loc.y, loc.z, loc.cell, 0, 0, 0, "", true);
        return SCRIPT_CONTINUE;
    }
}
