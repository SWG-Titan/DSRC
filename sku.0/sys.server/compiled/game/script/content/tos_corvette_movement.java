package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Moves players to one of the corvettes
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 10:50 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.space_dungeon;
import script.library.space_dungeon_data;
import script.library.sui;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

import java.util.Objects;
import java.util.Vector;

public class tos_corvette_movement extends script.base_script
{
    public static String getTicketDungeonName(obj_id ticket) throws InterruptedException
    {
        if (!isIdValid(ticket))
        {
            LOG("ethereal", "[Tatooine Orbital Station]: Ticket is not valid.");
            return null;
        }
        else
        {
            LOG("ethereal", "[Tatooine Orbital Station]: Ticket is valid. Valid ticket is " + ticket);
        }
        if (hasObjVar(ticket, space_dungeon.VAR_TICKET_DUNGEON))
        {
            return getStringObjVar(ticket, space_dungeon.VAR_TICKET_DUNGEON);
        }
        else
        {
            return null;
        }
    }

    public static string_id getDungeonNameStringId(String name) throws InterruptedException
    {
        if (name == null || name.length() < 1)
        {
            LOG("ethereal", "[Tatooine Orbital Station]: string_id name is null or empty.");
            return null;
        }
        return utils.unpackString("@dungeon/space_dungeon:" + name);
    }

    public static obj_id[] findValidDungeonTickets(obj_id player, obj_id ticket_collector) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            LOG("ethereal", "[Tatooine Orbital Station]:   player is not valid.");
            return null;
        }
        if (!isIdValid(ticket_collector))
        {
            LOG("ethereal", "[Tatooine Orbital Station]:  ticket_collector is not valid.");
            return null;
        }
        String collector_dungeon = getTicketDungeonName(ticket_collector);
        if (collector_dungeon == null || collector_dungeon.length() < 1)
        {
            LOG("ethereal", "[Tatooine Orbital Station]:  ticket collector " + ticket_collector + " does not have a dungeon name.");
            return null;
        }
        String collector_point = getTicketPointName(ticket_collector);
        if (collector_point == null || collector_point.length() < 1)
        {
            LOG("ethereal", "[Tatooine Orbital Station]: ticket collecter " + ticket_collector + " does not have a point name.");
            return null;
        }
        String collector_planet = getCurrentSceneName();
        obj_id inv = getObjectInSlot(player, "inventory");
        if (inv == null)
        {
            LOG("ethereal", "[Tatooine Orbital Station]:   player " + player + " inventory is null.");
            return null;
        }
        obj_id[] inv_contents = utils.getContents(inv, true);
        Vector valid_tickets = new Vector();
        valid_tickets.setSize(0);
        if (inv_contents != null)
        {
            for (obj_id inv_content : inv_contents)
            {
                if (hasObjVar(inv_content, space_dungeon.VAR_TICKET_ROOT))
                {
                    LOG("ethereal", "[Tatooine Orbital Station]:  Found a ticket in inventory. Owner: " + player);
                    String ticket_point = getTicketPointName(inv_content);
                    LOG("ethereal", "[Tatooine Orbital Station]: Ticket point is " + ticket_point + ". Owner: " + player);
                    if (ticket_point != null && ticket_point.equals(collector_point))
                    {
                        String ticket_planet = getTicketPlanetName(inv_content);
                        LOG("ethereal", "[Tatooine Orbital Station]: Ticket planet is " + ticket_planet + ". Owner: " + player);
                        if (ticket_planet != null && ticket_planet.equals(collector_planet))
                        {
                            String ticket_dungeon = getTicketDungeonName(inv_content);
                            LOG("ethereal", "[Tatooine Orbital Station]: Ticket dungeon is " + ticket_dungeon + ". Owner: " + player);
                            if (ticket_dungeon != null && ticket_dungeon.equals(collector_dungeon))
                            {
                                LOG("ethereal", "[Tatooine Orbital Station]: Adding ticket " + inv_content + " to valid_tickets.");
                                utils.addElement(valid_tickets, inv_content);
                            }
                        }
                    }
                }
            }
        }
        if (valid_tickets.size() > 0)
        {
            LOG("ethereal", "[Tatooine Orbital Station]: Found " + valid_tickets.size() + " valid tickets.");
            obj_id[] _valid_tickets = new obj_id[0];
            if (valid_tickets != null)
            {
                _valid_tickets = new obj_id[valid_tickets.size()];
                valid_tickets.toArray(_valid_tickets);
            }
            return _valid_tickets;
        }
        else
        {
            return null;
        }
    }

    public static String getTicketPointName(obj_id ticket) throws InterruptedException
    {
        if (!isIdValid(ticket))
        {
            LOG("ethereal", "[Tatooine Orbital Station]: getTicketPointName ticket is not valid.");
            return null;
        }
        if (hasObjVar(ticket, space_dungeon.VAR_TICKET_POINT))
        {
            return getStringObjVar(ticket, space_dungeon.VAR_TICKET_POINT);
        }
        else
        {
            return null;
        }
    }

    public static String getTicketPlanetName(obj_id ticket) throws InterruptedException
    {
        if (!isIdValid(ticket))
        {
            LOG("ethereal", "[Tatooine Orbital Station]: getTicketPlanetName ticket is not valid.");
            return null;
        }
        if (hasObjVar(ticket, space_dungeon.VAR_TICKET_PLANET))
        {
            return getStringObjVar(ticket, space_dungeon.VAR_TICKET_PLANET);
        }
        else
        {
            return null;
        }
    }

    public static boolean activateDungeonTicket(obj_id player, obj_id ticket, obj_id ticket_collector) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            LOG("ethereal", "[Tatooine Orbital Station]: activateDungeonTicket --  player is null.");
            return false;
        }
        if (!isIdValid(ticket))
        {
            LOG("ethereal", "[Tatooine Orbital Station]: activateDungeonTicket --  ticket is invalid.");
            return false;
        }
        String dungeon_name = getTicketDungeonName(ticket);
        String point_name = getTicketPointName(ticket);
        String planet_name = getTicketPlanetName(ticket);
        if (dungeon_name == null || point_name == null || planet_name == null)
        {
            LOG("ethereal", "[Tatooine Orbital Station]: activateDungeonTicket --  dungeon name on ticket " + ticket + " has one or more null travel values.");
            return false;
        }
        String collector_name = getTicketDungeonName(ticket_collector);
        String collector_point = getTicketPointName(ticket_collector);
        String collector_planet = getCurrentSceneName();
        if (!dungeon_name.equals(collector_name) || !point_name.equals(collector_point) || !planet_name.equals(collector_planet))
        {
            sendSystemMessage(player, space_dungeon.SID_ILLEGAL_TICKET);
            return false;
        }
        int request_id = getClusterWideData("dungeon", dungeon_name + "*", true, ticket_collector);
        if (request_id < 1)
        {
            string_id fail = space_dungeon_data.getDungeonFailureString(dungeon_name);
            sendSystemMessage(player, Objects.requireNonNullElse(fail, space_dungeon.SID_UNABLE_TO_FIND_DUNGEON));
            return false;
        }
        setObjVar(player, space_dungeon.VAR_TICKET_USED, ticket);
        String script_var_name = space_dungeon.SCRIPT_VAR_TRAVELERS + request_id;
        utils.setScriptVar(ticket_collector, script_var_name, player);
        sendSystemMessage(player, space_dungeon.SID_VALIDATING_TICKET);
        LOG("ethereal", "[Tatooine Orbital Station]: activateDungeonTicket --  ticket " + ticket + " is valid. Returning true.");
        return true;
    }

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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Enter Airlock"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (selectDungeonTicket(self, player))
            {
                broadcast(self, "Please select which ticket you would like to use.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean selectDungeonTicket(obj_id self, obj_id player) throws InterruptedException
    {
        if (utils.hasScriptVar(player, space_dungeon.SCRIPT_VAR_VALID_TICKETS))
        {
            return false;
        }
        String name = space_dungeon.getTicketDungeonName(self);
        if (name == null || name.length() < 1)
        {
            LOG("ethereal", "[Tatooine Orbital Station]: ticket collector " + self + " does not have a dungeon name.");
            return false;
        }
        else LOG("ethereal", "[Tatooine Orbital Station]: Ticket name is " + name);
        if (hasObjVar(player, space_dungeon.VAR_TICKET_USED) || hasObjVar(player, space_dungeon.VAR_TICKET_DUNGEON))
        {
            broadcast(player, "You have an outstanding ticket request. Please wait for the current request to complete before requesting another ticket.");
            LOG("ethereal", "[Tatooine Orbital Station]: Player " + player + " has an outstanding ticket request.");
            return false;
        }
        obj_id[] valid_tickets = findValidDungeonTickets(player, self);
        if (valid_tickets != null)
        {
            if (valid_tickets.length == 1)
            {
                space_dungeon.activateDungeonTicket(player, valid_tickets[0], self);
            }
            else
            {
                String[] dsrc = new String[valid_tickets.length];
                for (int i = 0; i < valid_tickets.length; i++)
                {
                    String dungeon_name = getTicketDungeonName(valid_tickets[i]);
                    String dungeon_name_loc = localize(getDungeonNameStringId(dungeon_name));
                    dsrc[i] = dungeon_name_loc;
                }
                utils.setScriptVar(player, space_dungeon.SCRIPT_VAR_VALID_TICKETS, valid_tickets);
                sui.listbox(player, player, "Ticket Confirmation", sui.OK_CANCEL, "Ticket Confirmation", dsrc, "msgSelectDungeonTicket");
                return true;
            }
        }
        else
        {
            broadcast(player, "You do not have any valid tickets for this dungeon.");
        }
        return false;
    }
}
