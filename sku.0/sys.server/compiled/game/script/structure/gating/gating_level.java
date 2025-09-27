package script.structure.gating;/*
@Origin: script.structure.gating.
@Author: BubbaJoeX
@Purpose: Restricts entry if player is not of the correct level.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.*/


import script.obj_id;

public class gating_level extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item)
    {
        if (isPlayer(item))
        {
            int gatingLevel = getIntObjVar(self, "gating.level");
            if (gatingLevel > 0)
            {
                if (getLevel(item) <= gatingLevel)
                {
                    broadcast(item, "You must be at least level " + gatingLevel + " to enter this structure.");
                    LOG("ethereal", "[Gating]: " + getName(item) + " tried to enter a structure without being at least level " + gatingLevel + ".");
                    return SCRIPT_OVERRIDE;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
