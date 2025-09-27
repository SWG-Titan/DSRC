package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 7/31/2024, at 9:57 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;
import script.dictionary;

public class s_ig24_add extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Reprogrammed CWW8 Battle Droid");
        setDescriptionString(self, "These reprogrammed CWW8's pack a hefty wallop.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: IG-24 add defeated.");
        return SCRIPT_CONTINUE;
    }
}
