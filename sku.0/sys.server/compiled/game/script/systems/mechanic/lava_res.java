package script.systems.mechanic;/*
@Origin: dsrc.script.systems.mechanic
@Author:  BubbaJoeX
@Purpose: Adds lava resistance to a vehicle upon use. Must be near garage.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 2/24/2025, at 12:49 AM, 
@Copyright © SWG: New Beginnings 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.callable;
import script.library.utils;
import script.library.vehicle;

public class lava_res extends toolkit
{
    @Override
    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    @Override
    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public void sync(obj_id self)
    {
        setObjVar(self, "tinkered", 1);
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!utils.isNestedWithinAPlayer(self))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, string_id.unlocalized("Apply to Vehicle"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id vehicleId = getMountId(player);
            if (vehicleId == null)
            {
                broadcast(player, "You must be in a vehicle to apply lava resistance.");
                return SCRIPT_CONTINUE;
            }
            obj_id rider = getRiderId(vehicleId);
            if (rider == null)
            {
                broadcast(player, "You must be in your vehicle to apply lava resistance to it.");
                return SCRIPT_CONTINUE;
            }

            if (!isNearGarage(player))
            {
                broadcast(player, "You must be near a garage to apply this kit to a vehicle.");
                return SCRIPT_CONTINUE;
            }

            setObjVar(vehicleId, "vehicle.lava_resistance", true);
            obj_id vehicleControlDevice = callable.getCallableCD(vehicleId);
            setObjVar(vehicleControlDevice, "vehicle.lava_resistance", true);
            broadcast(rider, "Lava resistance has applied to your vehicle.");
            doLavaSplash(rider);
            destroyObject(self);
        }
        return SCRIPT_CONTINUE;
    }

    @Override
    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, "tinkered"))
        {
            names[idx] = utils.packStringId(new string_id("Lava resistance"));
            attribs[idx] = "100.00%";
            idx++;
        }
        return SCRIPT_CONTINUE;
    }

    private void doLavaSplash(obj_id vehicle)
    {
        location corpsePosition = getLocation(vehicle);
        float radius = 4.0f;
        location[] offsets = new location[16];

        for (int i = 0; i < 16; i++)
        {
            float angle = (float) (i * Math.PI / 8);
            float xOffset = radius * (float) Math.cos(angle);
            float zOffset = radius * (float) Math.sin(angle);
            offsets[i] = new location(corpsePosition.x + xOffset, corpsePosition.y + 2.0f, corpsePosition.z + zOffset, corpsePosition.area, corpsePosition.cell);
        }

        for (location offset : offsets)
        {
            if (isInWorldCell(vehicle))
            {
                playClientEffectLoc(vehicle, "clienteffect/lava_player_burning.cef", offset, 1f);
            }
        }
    }
}
