package script.structure.gating;/*
@Origin: script.structure.gating.
@Author: BubbaJoeX
@Purpose: Restricts entry if the player's ai faction is less than an amount. (Example: restrict access to a building unless you have X or greater amount of faction of "jabba" or what ever faction from factions.tab.)
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.*/


import script.library.factions;
import script.obj_id;

public class gating_faction_ai extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isPlayer(item))
        {
            String gatingFaction = getStringObjVar(self, "gating.faction");
            Float gatingFactionStanding = getFloatObjVar(self, "gating.faction_standing");
            if (factions.getFactionStanding(item, gatingFaction) < gatingFactionStanding)
            {
                broadcast(item, "You do not have the required faction standing to enter this structure.");
                LOG("ethereal", "[Gating]: " + getName(item) + " tried to enter a structure without the required faction standing of " + gatingFactionStanding + " for adhoc faction " + gatingFaction + ".");
                return SCRIPT_OVERRIDE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
