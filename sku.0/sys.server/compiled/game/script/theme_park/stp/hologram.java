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
public class hologram extends script.base_script
{
    public hologram()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setState(self, STATE_GLOWING_JEDI, true);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setState(self, STATE_GLOWING_JEDI, true);
        return SCRIPT_CONTINUE;
    }
}
