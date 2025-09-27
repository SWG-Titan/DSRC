package script.systems.loot;/*
@Origin: dsrc.script.systems.loot
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 9/6/2024, at 12:49 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

import java.awt.*;

public class dynamic_loot extends script.base_script
{
    public static String applyGradient(String input, Color startColor, Color endColor)
    {
        return rare_item.applyGradient(input, startColor, endColor);
    }

    public int OnAttach(obj_id self)
    {
        String input = getStringObjVar(self, "dynamic_loot_tag");
        String gradientText = applyGradient(input, new Color(194, 82, 0, 255), new Color(241, 139, 2));
        setObjVar(self, "dynamic_loot_tag", gradientText);
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        if (names == null || attribs == null || names.length != attribs.length)
        {
            return SCRIPT_CONTINUE;
        }

        if (self == null || !isIdValid(self))
        {
            return SCRIPT_CONTINUE;
        }

        int free = getFirstFreeIndex(names);
        if (free == -1 || free >= names.length)
        {
            return SCRIPT_CONTINUE;
        }
        names[free] = "rare_loot_category";
        attribs[free] = applyGradient("Dynamic Item", new Color(57, 129, 68), new Color(91, 22, 189, 255));

        free++;
        if (free < names.length)
        {

        }

        return SCRIPT_CONTINUE;
    }
}
