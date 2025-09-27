package script.structure.gating;/*
@Origin: script.structure.gating.
@Author: BubbaJoeX
@Purpose: Restricts entry if player does not have the required command. [/meditate for example]
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.*/

import script.obj_id;

public class gating_command extends script.base_script
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
            String gatingCommand = getStringObjVar(self, "gating.command");
            if (gatingCommand != null && !gatingCommand.isEmpty())
            {
                if (!hasCommand(item, gatingCommand))
                {
                    broadcast(item, "You must have the " + gatingCommand + " command to enter this structure.");
                    LOG("ethereal", "[Gating]: " + getName(item) + " tried to enter a structure without the " + gatingCommand + " command.");
                    return SCRIPT_OVERRIDE;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
