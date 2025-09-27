package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Takes players to the corvette
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 10:02 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.factions;
import script.library.money;
import script.library.space_dungeon;
import script.library.sui;

public class tos_corvette extends base_script
{

    public String[] TYPES = {
            "Destroy",
            "Assassinate",
            "Rescue",
    };
    public String[] TYPES_SUFFIX = {
            "destroy",
            "assassin",
            "rescue",
    };

    public String[] FACTIONS = {
            "Rebel",
            "Imperial",
            "Neutral",
    };

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.listbox(self, player, "This terminal will allow you to purchase tickets to the Corellian Corvette based on your faction and selected mission type.\nFirst select a mission type: ", sui.OK_CANCEL, "Departure Airlock: Mission Type", TYPES, "handleMissionType");
        }
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                removeObjVar(player, "dungeon.cc.lastMissionType");
                removeObjVar(player, "space_dungeon");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Purchase Ticket"));
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reset Player"));
        }
        return SCRIPT_CONTINUE;
    }

    public int handleMissionType(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        String faction = factions.getFaction(player);
        if (faction == null)
        {
            faction = "Neutral";
        }
        String missionType = TYPES[sui.getListboxSelectedRow(params)];
        setObjVar(player, "dungeon.cc.lastMissionType", TYPES_SUFFIX[sui.getListboxSelectedRow(params)]);
        sui.msgbox(self, player, "Your faction is " + faction + ", and you have chosen the " + missionType + " mission.", sui.OK_CANCEL, "Departure Airlock: Confirmation", "handleTicketCreation");
        return SCRIPT_CONTINUE;
    }

    public int handleTicketCreation(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (money.requestPayment(player, self, 25000, "pass_fail", null, true))
        {
            createTicket(player);
        }
        else
        {
            sui.msgbox(self, player, "You do not have enough money to purchase a ticket.", sui.OK_ONLY, "noHandler");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int createTicket(obj_id who) throws InterruptedException
    {
        int factionHashCode = factions.pvpGetAlignedFaction(who);
        String factionName = factions.getFactionNameByHashCode(factionHashCode);
        if (factionName == null)
        {
            factionName = "Neutral";
        }
        String planetName = "";
        if (factionName.equals("Rebel"))
        {
            planetName = "corellia";
        }
        else if (factionName.equals("Imperial"))
        {
            planetName = "naboo";
        }
        else
        {
            planetName = "tatooine";
        }
        obj_id ticket = space_dungeon.createTicket(who, planetName, "corvette_" + toLower(factionName) + "_pilot", "corvette_" + toLower(factionName));
        setObjVar(ticket, "space_dungeon.ticket.quest_type", factionName.toLowerCase() + "_" + getStringObjVar(who, "dungeon.cc.lastMissionType"));
        setObjVar(ticket, "corl_corvette.ticket_owner", who);
        setObjVar(ticket, "noTrade", true);
        attachScript(ticket, "item.special.nomove");
        attachScript(ticket, "theme_park.dungeon.corvette.corvette_quest_cleanup");
        setName(ticket, "Travel Authorization Ticket -  " + factionName + " " + toUpper(getStringObjVar(who, "dungeon.cc.lastMissionType"), 0) + " Mission");
        return SCRIPT_CONTINUE;
    }
}
