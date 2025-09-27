package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 4/24/2024, at 11:35 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;
import script.library.sui;

public class tos_exit extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Planetary Transit Terminal");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Planetary Transit Terminal");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Depart from Rally Point Nova"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (combat.isInCombat(player))
            {
                broadcast(player, "You cannot travel while in combat.");
                return SCRIPT_CONTINUE;
            }
            if (isIncapacitated(player))
            {
                broadcast(player, "You cannot travel while in incapacitated.");
                return SCRIPT_CONTINUE;
            }
            if (isDead(player))
            {
                broadcast(player, "You cannot travel while dead.");
                return SCRIPT_CONTINUE;
            }
            //sui.msgbox(self, player, "\\#e3d005Are you sure you want to leave the station?\\#", sui.OK_CANCEL_ALL, "\\#e3d005Transit", "handleExit");
            String[] options = {"Coronet, Corellia", "Theed, Naboo", "Mos Eisley, Tatooine"};
            sui.listbox(self, player, "Select a destination to depart to:", sui.OK_CANCEL, "Departures", options, "handleExitSelection");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int handleExitSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (player == null)
        {
            return SCRIPT_CONTINUE;
        }
        int buttonPressed = sui.getIntButtonPressed(params);
        if (buttonPressed == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled your travels.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            int index = sui.getListboxSelectedRow(params);
            switch (index)
            {
                case 0:
                    warpPlayer(player, "corellia", -137.0f, 28.0f, -4723.0f, null, 0f, 0f, 0f, "noHandler", false);
                    break;
                case 1:
                    warpPlayer(player, "naboo", -4855.0f, 6.0f, 4167.0f, null, 0f, 0f, 0f, "noHandler", false);
                    break;
                case 2:
                    warpPlayer(player, "tatooine", 3627.0f, 5.0f, -4772.0f, null, 0f, 0f, 0f, "noHandler", false);
                    break;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleExit(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (player == null)
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(self, "You have chosen to remain on the station.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            sendPlayer(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public void sendPlayer(obj_id self, obj_id player) throws InterruptedException
    {
        warpPlayer(player, "tatooine", 3627.0f, 5.0f, -4772.0f, null, 0f, 0f, 0f, "noHandler", false);
    }
}
