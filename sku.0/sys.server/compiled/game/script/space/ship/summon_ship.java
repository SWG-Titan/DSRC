package script.space.ship;

import script.*;
import script.library.space_transition;
import script.library.space_utils;
import script.library.utils;

public class summon_ship extends script.base_script
{
    public static final float SUMMON_TAKEOFF_ALT  = 500.0f;
    public static final float SUMMON_LANDING_ALT  = 50.0f;

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!isAtmosphericFlightScene())
            return SCRIPT_CONTINUE;

        if (utils.getContainingPlayer(self) != player)
            return SCRIPT_CONTINUE;

        obj_id ship = findDeployedShipForPlayer(player);
        if (!isIdValid(ship))
            return SCRIPT_CONTINUE;

        if (space_transition.getContainingShip(player) == ship)
            return SCRIPT_CONTINUE;

        mi.addRootMenu(menu_info_types.SERVER_MENU1, string_id.unlocalized("Summon Ship"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item != menu_info_types.SERVER_MENU1)
            return SCRIPT_CONTINUE;

        if (!isAtmosphericFlightScene())
        {
            sendSystemMessageTestingOnly(player, "You can only summon your ship during atmospheric flight.");
            return SCRIPT_CONTINUE;
        }

        if (utils.getContainingPlayer(self) != player)
            return SCRIPT_CONTINUE;

        obj_id ship = findDeployedShipForPlayer(player);
        if (!isIdValid(ship))
        {
            sendSystemMessageTestingOnly(player, "You do not have a ship deployed in this area.");
            return SCRIPT_CONTINUE;
        }

        if (space_transition.getContainingShip(player) == ship)
        {
            sendSystemMessageTestingOnly(player, "You are already aboard this ship.");
            return SCRIPT_CONTINUE;
        }

        if (!space_utils.isShipWithInterior(ship))
        {
            sendSystemMessageTestingOnly(player, "Only ships with an interior can be summoned via auto-pilot.");
            return SCRIPT_CONTINUE;
        }

        if (shipIsAutopilotActive(ship))
        {
            sendSystemMessageTestingOnly(player, "Your ship is already en route. Please wait for it to arrive.");
            return SCRIPT_CONTINUE;
        }

        obj_id pilot = getPilotId(ship);
        if (isIdValid(pilot))
        {
            sendSystemMessageTestingOnly(player, "Your ship is currently being piloted and cannot be summoned.");
            return SCRIPT_CONTINUE;
        }

        location playerLoc = getWorldLocation(player);
        float targetX = playerLoc.x;
        float targetZ = playerLoc.z;

        dictionary wpParams = new dictionary();
        wpParams.put("x", targetX);
        wpParams.put("z", targetZ);
        wpParams.put("owner", player);
        wpParams.put("summon", true);
        messageTo(ship, "shipSummonEngage", wpParams, 0, false);

        sendSystemMessageTestingOnly(player, "Summoning your ship to your location...");
        return SCRIPT_CONTINUE;
    }

    private obj_id findDeployedShipForPlayer(obj_id player) throws InterruptedException
    {
        obj_id[] scds = space_transition.findShipControlDevicesForPlayer(player);
        if (scds == null)
            return null;

        for (obj_id scd : scds)
        {
            if (!isIdValid(scd))
                continue;

            obj_id[] contents = getContents(scd);
            if (contents != null && contents.length > 0)
                continue;

            if (!hasObjVar(scd, "ship"))
                continue;

            obj_id ship = getObjIdObjVar(scd, "ship");
            if (isIdValid(ship) && exists(ship))
                return ship;
        }
        return null;
    }
}
