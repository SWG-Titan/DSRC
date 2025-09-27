package script.systems.loot;

import script.obj_id;

import java.awt.*;

public class rare_item extends script.base_script
{
    public static String applyGradient(String input, Color startColor, Color endColor)
    {
        int length = input.length();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            int r = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * ((float) i / (length - 1)));
            int g = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * ((float) i / (length - 1)));
            int b = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * ((float) i / (length - 1)));
            String hexColor = String.format("\\#%02X%02X%02X", r, g, b);
            result.append(hexColor).append(input.charAt(i));
        }
        return result.toString();
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
        attribs[free] = applyGradient("Rare Item", new Color(243, 39, 119), new Color(209, 37, 46, 255));

        free++;
        if (free < names.length)
        {

        }

        return SCRIPT_CONTINUE;
    }

}