package script.space;

/*
 * NPC-controlled POB ship controller.
 * Attach to a POB ship (with interior) for scripted atmospheric destinations.
 *
 * Setup: Set objvar npc_pob.controller = 1 (or controller NPC id) on the ship.
 * Ship must be in atmospheric flight scene, have no pilot, and have an interior.
 *
 * Script destinations via: messageTo(ship, "npcPobFlyTo", {x=<float>, z=<float>}, 0, false);
 * Example: dictionary d = new dictionary(); d.put("x", 100.0f); d.put("z", -200.0f); messageTo(ship, "npcPobFlyTo", d, 0, false);
 */

import script.*;
import script.library.*;

public class npc_pob_ship_controller extends script.base_script
{
    public static final String OBJVAR_CONTROLLER = "npc_pob.controller";
    public static final String OBJVAR_WAYPOINT_QUEUE = "npc_pob.waypointQueue";

    public npc_pob_ship_controller()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, OBJVAR_CONTROLLER))
            setObjVar(self, OBJVAR_CONTROLLER, 1);
        if (space_utils.isShipWithInterior(self))
            space_transition.setBoardingIsPublic(self, true);
        return SCRIPT_CONTINUE;
    }

    public int npcPobFlyTo(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, OBJVAR_CONTROLLER))
            return SCRIPT_CONTINUE;
        if (!space_transition.isAtmosphericFlightScene())
            return SCRIPT_CONTINUE;
        if (!space_utils.isShipWithInterior(self))
            return SCRIPT_CONTINUE;
        if (isIdValid(getPilotId(self)))
            return SCRIPT_CONTINUE;

        float x = params.getFloat("x");
        float z = params.getFloat("z");
        dictionary engageParams = new dictionary();
        engageParams.put("x", x);
        engageParams.put("z", z);
        engageParams.put("npcControlled", true);
        obj_id owner = getOwner(self);
        engageParams.put("owner", isIdValid(owner) ? owner : self);
        messageTo(self, "shipAutoPilotEngage", engageParams, 0, false);
        return SCRIPT_CONTINUE;
    }
}
