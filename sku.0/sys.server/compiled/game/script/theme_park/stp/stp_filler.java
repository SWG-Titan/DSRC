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
public class stp_filler extends script.base_script
{
    public stp_filler()
    {
    }

    public int OnAttach(obj_id self)
    {
        setInvulnerable(self, true);
        setName(self, "a Taanab structure");
        // They can't see these but this is more for visual clean up.
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setInvulnerable(self, true);
        setName(self, "a Taanab structure");
        // They can't see these but this is more for visual clean up.
        return SCRIPT_CONTINUE;
    }
}
