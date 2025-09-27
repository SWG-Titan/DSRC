package script.structure.gating;/*
@Origin: script.structure.gating.
@Author: BubbaJoeX
@Purpose: Restricts entry if player does not have the skill specified via an objvar.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.*/

import script.obj_id;

public class gating_skill extends script.base_script
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
            String gatingSkill = getStringObjVar(self, "gating.skill");
            if (gatingSkill != null && !gatingSkill.isEmpty())
            {
                if (!hasSkill(item, gatingSkill))
                {
                    broadcast(item, "You do not have the required skill to enter this structure.");
                    LOG("ethereal", "[Gating]: " + getName(item) + " tried to enter a structure without the required skill of [" + gatingSkill + "].");
                    return SCRIPT_OVERRIDE;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
