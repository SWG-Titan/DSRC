package script.theme_park.stp.wendle;

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
public class instrument_player extends script.base_script
{
    public instrument_player()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        grantSkill(self, "social_entertainer_novice");
        detachScript(self, "systems.combat.credit_for_kills");
        setName(self, "a dancer");
        setInvulnerable(self, true);
        ai_lib.setMood(self, "themepark_oola");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        grantSkill(self, "social_entertainer_novice");
        detachScript(self, "systems.combat.credit_for_kills");
        setName(self, "a dancer");
        setInvulnerable(self, true);
        ai_lib.setMood(self, "themepark_oola");
        return SCRIPT_CONTINUE;
    }
}
