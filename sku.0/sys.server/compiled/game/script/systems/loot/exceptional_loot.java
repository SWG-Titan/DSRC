package script.systems.loot;

import script.obj_id;

import java.awt.*;

public class exceptional_loot extends script.base_script
{
    public static String applyGradient(String input, Color startColor, Color endColor)
    {
        return rare_item.applyGradient(input, startColor, endColor);
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
        attribs[free] = applyGradient("Exceptional Item", new Color(204, 68, 68), new Color(17, 204, 155, 255));

        free++;
        if (free < names.length)
        {

        }

        return SCRIPT_CONTINUE;
    }
}