package script.event.gjpud;/*
package script.event.gjpud;/*
@Origin: dsrc.script.event.gjpud.gating
@Author: BubbaJoeX
@Purpose: This gates players from entering event specific areas.
@Notes;
    This script ejects players who do not possess the gating objvar: $gate
@Created: Sunday, 2/20/2024, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class gating extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id destinationCell, obj_id transferrer, obj_id item) throws InterruptedException
    {
        if (!isPlayer(item))
        {
            return SCRIPT_CONTINUE;
        }
        int gating = getIntObjVar(item, "gjpud.gating");
        if (gating != 1)
        {
            broadcast(item, "It would be impolite to enter a private residence.");
            if (isPlayer(item))
            {
                LOG("events", "[GJPUD Gating]: " + getPlayerFullName(item) + " tried to enter a private residence.");
            }
            else
            {
                LOG("events", "[GJPUD Gating]: " + getName(item) + " tried to enter a private residence. [" + getLocation(self).toReadableFormat(true) + "]");
            }
            return SCRIPT_OVERRIDE;
        }
        else
        {
            return SCRIPT_CONTINUE;
        }
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
}
