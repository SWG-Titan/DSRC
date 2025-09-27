package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.tmp_barker
@Author: BubbaJoeX
@Note: Used in conjuction with /developer restrictArea/unrestrictArea
@Purpose: Expels players from a trigger volume named "no_enter_(volume suffix)"
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.*/

import script.obj_id;

public class expel extends script.base_script
{
    public int OnAttach(obj_id self) throws InterruptedException
    {
        float radius = getFloatObjVar(self, "expel_radius");
        String volumeIdentifier = getStringObjVar(self, "volume_suffix");
        createTriggerVolume("no_enter_" + volumeIdentifier, radius, true);
        setName(self, "no_enter_" + volumeIdentifier);
        LOG("ethereal", "[Expel]: " + getName(self) + " has been created with a radius of " + radius + " and a volume suffix of " + volumeIdentifier);
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (volumeName.equals("no_enter_" + getStringObjVar(self, "volume_suffix")))
        {
            if (!isPlayer(breacher))
            {
                return SCRIPT_CONTINUE;
            }
            if (!isGod(breacher))
            {
                broadcast(breacher, "This area is under an Imperial embargo. You are being expelled.");
                expelFromTriggerVolume(self, "no_enter_" + getStringObjVar(self, "volume_suffix"), breacher);
                return SCRIPT_CONTINUE;
            }
            LOG("ethereal", "[Expel]: " + getName(breacher) + " is a god and is not being expelled from " + getName(self) + " (TV: " + volumeName + ")");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnDestroy(obj_id self)
    {
        removeTriggerVolume("no_enter" + getStringObjVar(self, "volume_suffix"));
        return SCRIPT_CONTINUE;
    }

    public int OnDetach(obj_id self)
    {
        removeTriggerVolume("no_enter" + getStringObjVar(self, "volume_suffix"));
        return SCRIPT_CONTINUE;
    }

}
