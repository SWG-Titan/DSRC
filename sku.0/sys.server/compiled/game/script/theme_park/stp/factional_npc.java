package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.ai_lib;
import script.library.factions;
import script.obj_id;

/**
 * @author BubbaJoe
 */
public class factional_npc extends script.base_script
{
    public static String IMPERIAL_PREFIX = "TK-";
    public static String[] REBEL_SUFFIXES = {
            "(a Rebel trooper)",
            "(a Rebel officer)",
            "(a Rebel mercenary)",
            "(a Rebel officer)",
            "(a Rebel dispatcher)"
    };

    public factional_npc()
    {
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, "stp.faction"))
        {
            setObjVar(self, "stp.faction", "rebel");
        }
        if (getStringObjVar(self, "stp.faction").equals("imperial"))
        {
            factions.setFaction(self, "Imperial");
            setInvulnerable(self, true);
            setName(self, IMPERIAL_PREFIX + rand(1000, 9999));
            ai_lib.setDefaultCalmBehavior(self, 1);
        }
        if (getStringObjVar(self, "stp.faction").equals("rebel"))
        {
            factions.setFaction(self, "Imperial");
            setInvulnerable(self, true);
            if (getName(self).contains(("npc_")))
            {
                setName(self, "A generic name");
            }
            setName(self, getName(self) + " " + REBEL_SUFFIXES[rand(0, 4)]);
            ai_lib.setDefaultCalmBehavior(self, 1);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, "stp.faction"))
        {
            setObjVar(self, "stp.faction", "rebel");
        }
        if (getStringObjVar(self, "stp.faction").equals("imperial"))
        {
            factions.setFaction(self, "Imperial");
            setInvulnerable(self, true);
            setName(self, IMPERIAL_PREFIX + rand(1000, 9999));
            ai_lib.setDefaultCalmBehavior(self, 1);
        }
        if (getStringObjVar(self, "stp.faction").equals("rebel"))
        {
            factions.setFaction(self, "Rebel");
            setInvulnerable(self, true);
            setName(self, getName(self) + " " + REBEL_SUFFIXES);
            ai_lib.setDefaultCalmBehavior(self, 1);
        }
        return SCRIPT_CONTINUE;
    }
}
