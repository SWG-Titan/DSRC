package script.space;

import script.*;
import script.library.*;

/**
 * Spawner for NPC POB shuttle with repeating waypoint cycle.
 * Attach to an object in Naboo (e.g. invisible beacon). Spawns a Sorosuub Space Yacht
 * and cycles through Naboo city waypoints.
 *
 * Uses object/ship/player/player_sorosuub_space_yacht.iff
 */
public class npc_pob_ship_spawner extends script.base_script
{
    private static final String SHIP_TEMPLATE = "object/ship/player/player_sorosuub_space_yacht.iff";
    private static final String SCENE = "naboo";
    private static final float SPAWN_ALTITUDE = 200.0f;
    private static final int TICK_INTERVAL = 60;
    private static final int DWELL_SECONDS = 30;

    private static final String OBJVAR_SHIP = "npc_pob.spawner.ship";
    private static final String OBJVAR_WAYPOINT_INDEX = "npc_pob.spawner.waypointIndex";
    private static final String OBJVAR_LAST_ARRIVAL = "npc_pob.spawner.lastArrival";

    // Naboo city waypoints (x, z) - Theed, Keren, Moenia, Kaadara, Deeja Peak
    private static final float[][] WAYPOINTS = new float[][]{
        {-4858.0f, 4164.0f},   // Theed
        {1440.0f, 2800.0f},    // Keren
        {4700.0f, -1556.0f},   // Moenia
        {5200.0f, 6688.0f},    // Kaadara
        {-1700.0f, 5200.0f}    // Deeja Peak
    };

    public npc_pob_ship_spawner()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!space_transition.isAtmosphericFlightScene())
            return SCRIPT_CONTINUE;

        obj_id ship = spawnShip(self);
        if (isIdValid(ship))
        {
            setObjVar(self, OBJVAR_SHIP, ship);
            setObjVar(self, OBJVAR_WAYPOINT_INDEX, 0);
            flyToWaypoint(ship, 0);
        }
        messageTo(self, "npcPobSpawnerTick", null, TICK_INTERVAL, false);
        return SCRIPT_CONTINUE;
    }

    public int npcPobSpawnerTick(obj_id self, dictionary params) throws InterruptedException
    {
        if (!space_transition.isAtmosphericFlightScene())
        {
            messageTo(self, "npcPobSpawnerTick", null, TICK_INTERVAL, false);
            return SCRIPT_CONTINUE;
        }

        obj_id ship = hasObjVar(self, OBJVAR_SHIP) ? getObjIdObjVar(self, OBJVAR_SHIP) : null;
        if (!isIdValid(ship) || !exists(ship))
        {
            ship = spawnShip(self);
            if (isIdValid(ship))
            {
                setObjVar(self, OBJVAR_SHIP, ship);
                setObjVar(self, OBJVAR_WAYPOINT_INDEX, 0);
            }
        }

        if (isIdValid(ship) && exists(ship))
        {
            boolean autopilotActive = shipIsAutopilotActive(ship);
            obj_id pilot = getPilotId(ship);
            boolean hasPilot = isIdValid(pilot);

            if (!autopilotActive && !hasPilot)
            {
                int lastArrival = hasObjVar(self, OBJVAR_LAST_ARRIVAL) ? getIntObjVar(self, OBJVAR_LAST_ARRIVAL) : 0;
                int now = getGameTime();
                if (now - lastArrival >= DWELL_SECONDS)
                {
                    int idx = hasObjVar(self, OBJVAR_WAYPOINT_INDEX) ? getIntObjVar(self, OBJVAR_WAYPOINT_INDEX) : 0;
                    idx = (idx + 1) % WAYPOINTS.length;
                    setObjVar(self, OBJVAR_WAYPOINT_INDEX, idx);
                    setObjVar(self, OBJVAR_LAST_ARRIVAL, now);
                    flyToWaypoint(ship, idx);
                }
            }
        }

        messageTo(self, "npcPobSpawnerTick", null, TICK_INTERVAL, false);
        return SCRIPT_CONTINUE;
    }

    private obj_id spawnShip(obj_id self) throws InterruptedException
    {
        float x = WAYPOINTS[0][0];
        float z = WAYPOINTS[0][1];
        float groundY = getHeightAtLocation(x, z);
        float y = groundY + SPAWN_ALTITUDE;
        location loc = new location(x, y, z, SCENE, null);

        obj_id ship = createObject(SHIP_TEMPLATE, loc);
        if (!isIdValid(ship))
            return ship;

        if (!hasScript(ship, "space.npc_pob_ship_controller"))
            attachScript(ship, "space.npc_pob_ship_controller");
        if (!hasScript(ship, "space.ship.ship_atmospheric_boarding"))
            attachScript(ship, "space.ship.ship_atmospheric_boarding");

        setName(ship, "Naboo Shuttle");
        setObjVar(self, OBJVAR_LAST_ARRIVAL, getGameTime());
        return ship;
    }

    private void flyToWaypoint(obj_id ship, int index) throws InterruptedException
    {
        if (index < 0 || index >= WAYPOINTS.length)
            return;
        float x = WAYPOINTS[index][0];
        float z = WAYPOINTS[index][1];
        dictionary params = new dictionary();
        params.put("x", x);
        params.put("z", z);
        messageTo(ship, "npcPobFlyTo", params, 0, false);
    }
}
