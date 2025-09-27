package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.obj_id;

/**
 * @author BubbaJoe
 */
public class stp_cantina_access extends script.base_script
{
    public stp_cantina_access()
    {
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id destinationCell, obj_id transferrer, obj_id item) throws InterruptedException
    {
        if (!isPlayer(item))
        {
            return SCRIPT_CONTINUE;
        }
        int gating = getIntObjVar(item, "stp_cantina_access");
        if (gating != 1)
        {
            broadcast(item, "You don't have permission to go back here. It is probably best if you turn around.");
            return SCRIPT_OVERRIDE;
        }
        else
        {
            return SCRIPT_CONTINUE;
        }
    }
}
