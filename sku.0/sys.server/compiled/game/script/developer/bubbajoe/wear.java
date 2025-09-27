package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe.wear
@Author: BubbaJoeX
@Purpose: This script is used to equip an item to an NPC when given.
@Created: Monday, 10/23/2023, at 3:28 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class wear extends script.base_script
{
    public wear()
    {
    }

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnGiveItem(obj_id self, obj_id item, obj_id giver) throws InterruptedException
    {
        equipOverride(item, self);
        if (isGod(giver))
        {
            broadcast(giver, "Attempting to give NPC : " + getEncodedName(self) + " item : " + getEncodedName(item));
        }
        String template = getTemplateName(item);
        if (template.contains("object/weapon/"))
        {
            return SCRIPT_OVERRIDE;
        }
        else if (template.contains("ring"))
        {
            return SCRIPT_OVERRIDE;
        }
        LOG("ethereal", "[Wardrobe] Trying to attach clothing to : " + getEncodedName(self) + " with item : " + getEncodedName(item));
        return SCRIPT_CONTINUE;
    }
}
