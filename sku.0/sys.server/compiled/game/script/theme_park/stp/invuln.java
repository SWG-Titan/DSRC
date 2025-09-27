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
public class invuln extends script.base_script
{
    public invuln()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, "stp.npc.name"))
        {
            setName(self, getStringObjVar(self, "stp.npc.name"));
        }
        detachScript(self, "systems.combat.credit_for_kills");
        setInvulnerable(self, true);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, "stp.npc.name"))
        {
            setName(self, getStringObjVar(self, "stp.npc.name"));
        }
        detachScript(self, "systems.combat.credit_for_kills");
        setInvulnerable(self, true);
        return SCRIPT_CONTINUE;
    }
}
