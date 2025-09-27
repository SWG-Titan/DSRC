package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 4:00 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;
import script.library.sui;

import static script.library.money.requestPayment;

public class tos_imperial_airlock extends script.terminal.terminal_character_builder
{
    public int COST = 15000;

    public int OnAttach(obj_id self)
    {
        setName(self, "Planetary Transit Terminal");
        setDescriptionString(self, "This terminal will allow players to travel to select locations for a fee.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Planetary Transit Terminal");
        setDescriptionString(self, "This terminal will allow players to travel to select locations for a fee.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Depart"));
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
            sui.msgbox(self, player, "Are you sure you want to depart?\nThis service costs " + COST + " credits and is one-way.", sui.YES_NO, "Priority Departure Airlock", "handleDepart");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleDepart(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        int money = getTotalMoney(player);
        if (button == sui.BP_OK)
        {
            if (money <= COST)
            {
                broadcast(player, "You do not have enough credits to depart.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                requestPayment(player, self, COST, "no_handler", null, false);
                handleWarpOption(player);
            }
        }
        else
        {
            broadcast(self, "You have chosen not to depart.");
        }
        return SCRIPT_CONTINUE;
    }
}
