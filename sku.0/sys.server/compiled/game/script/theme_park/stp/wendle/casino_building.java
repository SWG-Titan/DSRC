package script.theme_park.stp.wendle;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.obj_id;

/**
 * @author BubbaJoe
 */
public class casino_building extends script.base_script
{
    public casino_building()
    {
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id destinationCell, obj_id transferrer, obj_id item) throws InterruptedException
    {
        if (!isPlayer(item))
        {
            return SCRIPT_CONTINUE;
        }
        int gating = getIntObjVar(item, "stp.can_gamble");
        if (gating != 1)
        {
            broadcast(item, "You must be a gambler to enter the casino.");
            return SCRIPT_OVERRIDE;
        }
        else
        {
            return SCRIPT_CONTINUE;
        }
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "Wendle's Casino");
        return SCRIPT_CONTINUE;
    }
}
