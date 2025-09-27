package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.ai_lib;
import script.obj_id;

/**
 * @author BubbaJoe
 */
public class shady extends script.base_script
{
    public static String[] NAMES = {
            "a thief",
            "an unethical farmer",
            "a cheapskate",
            "a gambler",
            "a thug",
            "a shady commoner",
            "an uptight gambler",
            "an anti-farmer commoner"
    };

    public shady()
    {
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setInvulnerable(self, true);
        setName(self, NAMES[rand(0, 7)]);
        ai_lib.setDefaultCalmBehavior(self, 1);
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setInvulnerable(self, true);
        setName(self, NAMES[rand(0, 7)]);
        ai_lib.setDefaultCalmBehavior(self, 1);
        return SCRIPT_CONTINUE;
    }
}
