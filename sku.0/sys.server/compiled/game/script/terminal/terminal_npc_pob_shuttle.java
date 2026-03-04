package script.terminal;

import script.*;
import script.library.*;
import script.space.npc_pob_ship_controller;

/**
 * Terminal for NPC-controlled POB shuttle load/unload.
 *
 * Setup:
 * - Terminal on ground at a stop: set objvar npc_pob.linkedShip = ship. Shows "Board Shuttle".
 * - Terminal inside the ship: uses getContainingShip. Shows "Disembark".
 * - God mode: "Link Terminal to NPC Shuttle" links only to ships with npc_pob.controller (NPC POB shuttle).
 */
public class terminal_npc_pob_shuttle extends script.base_script
{
    private static final String OBJVAR_LINKED_SHIP = "npc_pob.linkedShip";
    private static final float BOARD_RANGE = 128.0f;

    public terminal_npc_pob_shuttle()
    {
    }

    private obj_id getShip(obj_id self) throws InterruptedException
    {
        obj_id ship = space_transition.getContainingShip(self);
        if (isIdValid(ship))
            return ship;
        if (hasObjVar(self, OBJVAR_LINKED_SHIP))
            return getObjIdObjVar(self, OBJVAR_LINKED_SHIP);
        return null;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!space_transition.isAtmosphericFlightScene())
            return SCRIPT_CONTINUE;

        obj_id ship = getShip(self);
        obj_id playerShip = space_transition.getContainingShip(player);

        if (isIdValid(playerShip))
        {
            obj_id termShip = getShip(self);
            if (isIdValid(termShip) && termShip == playerShip)
                mi.addRootMenu(menu_info_types.SERVER_MENU1, string_id.unlocalized("Disembark"));
        }
        else if (isIdValid(ship) && exists(ship) && hasObjVar(ship, script.space.npc_pob_ship_controller.OBJVAR_CONTROLLER))
        {
            if (!space_transition.isShipParkedInWorld(ship))
                return SCRIPT_CONTINUE;
            float dist = getDistance(player, ship);
            if (dist <= BOARD_RANGE)
                mi.addRootMenu(menu_info_types.SERVER_MENU2, string_id.unlocalized("Board Shuttle"));
        }

        if (isGod(player) && space_transition.isAtmosphericFlightScene())
        {
            obj_id[] nearby = getObjectsInRange(self, 128.0f);
            if (nearby != null)
            {
                for (obj_id o : nearby)
                {
                    if (isIdValid(o) && getTopMostContainer(o) == o && space_utils.isShipWithInterior(o)
                            && hasObjVar(o, npc_pob_ship_controller.OBJVAR_CONTROLLER))
                    {
                        mi.addRootMenu(menu_info_types.SERVER_MENU3, string_id.unlocalized("Link Terminal to NPC Shuttle"));
                        break;
                    }
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            obj_id ship = space_transition.getContainingShip(player);
            if (!isIdValid(ship))
                return SCRIPT_CONTINUE;
            if (!space_transition.isAtmosphericFlightScene())
            {
                sendSystemMessage(player, string_id.unlocalized("You can only disembark during atmospheric flight."));
                return SCRIPT_CONTINUE;
            }
            if (isIdValid(getPilotId(ship)))
            {
                sendSystemMessage(player, string_id.unlocalized("You cannot disembark while the ship is being piloted."));
                return SCRIPT_CONTINUE;
            }
            space_transition.disembarkShip(player, ship);
            return SCRIPT_CONTINUE;
        }

        if (item == menu_info_types.SERVER_MENU2)
        {
            obj_id ship = getObjIdObjVar(self, OBJVAR_LINKED_SHIP);
            if (!isIdValid(ship) || !exists(ship))
            {
                sendSystemMessage(player, string_id.unlocalized("The shuttle is not available."));
                return SCRIPT_CONTINUE;
            }
            if (!space_transition.isShipParkedInWorld(ship))
            {
                sendSystemMessage(player, string_id.unlocalized("The shuttle is not parked here."));
                return SCRIPT_CONTINUE;
            }
            if (getDistance(player, ship) > BOARD_RANGE)
            {
                sendSystemMessage(player, string_id.unlocalized("You are too far from the shuttle to board."));
                return SCRIPT_CONTINUE;
            }
            if (!space_transition.boardShipFromGround(player, ship))
                return SCRIPT_CONTINUE;
            return SCRIPT_CONTINUE;
        }

        if (item == menu_info_types.SERVER_MENU3 && isGod(player))
        {
            obj_id[] nearby = getObjectsInRange(self, 128.0f);
            if (nearby != null)
            {
                for (obj_id o : nearby)
                {
                    if (isIdValid(o) && getTopMostContainer(o) == o && space_utils.isShipWithInterior(o)
                            && hasObjVar(o, npc_pob_ship_controller.OBJVAR_CONTROLLER))
                    {
                        setObjVar(self, OBJVAR_LINKED_SHIP, o);
                        sendSystemMessage(player, string_id.unlocalized("Terminal linked to NPC shuttle."));
                        return SCRIPT_CONTINUE;
                    }
                }
            }
            sendSystemMessage(player, string_id.unlocalized("No NPC POB shuttle within range to link."));
        }
        return SCRIPT_CONTINUE;
    }
}
