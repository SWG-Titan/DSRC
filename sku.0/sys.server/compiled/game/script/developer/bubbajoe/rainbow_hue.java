package script.developer.bubbajoe;

/**
 * @Origin: script.developer.bubbajoe
 * @Author: BubbaJoeX
 * @Purpose: Randomize the hue of an object periodically.
 * @Note: This script is under development and not yet stable for production.
 *         <p>
 *                         Copyright © SWG-OR 2024. Unauthorized usage, viewing, or sharing is prohibited.
 */

import script.*;
import script.library.hue;

public class rainbow_hue extends script.base_script
{

    private static final String PAL_MAIN = "/private/index_color_1";
    private static final String PAL_SECONDARY = "/private/index_color_2";
    private static final String PAL_TERTIARY = "/private/index_color_3";
    private static final float CYCLE_TIME = 30.0f;
    private static final String LOOP_LOCK = "loopLock";

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        int rootMenu = mi.addRootMenu(menu_info_types.SERVER_MENU48, new string_id("hue_options", "Rainbowize"));
        mi.addSubMenu(rootMenu, menu_info_types.SERVER_MENU49, new string_id("hue_options", "Stop"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU48)
        {
            if (!hasObjVar(self, LOOP_LOCK))
            {
                setObjVar(self, LOOP_LOCK, 1); // Prevent multiple activations
                startHueLoops(self);
            }
        }
        else if (item == menu_info_types.SERVER_MENU49)
        {
            if (hasObjVar(self, LOOP_LOCK))
            {
                removeObjVar(self, LOOP_LOCK);
                broadcast(player, "Stopped the rainbow effect.");
            }
            else
            {
                broadcast(player, "Rainbow effect is not active.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    private void startHueLoops(obj_id self) throws InterruptedException
    {
        messageTo(self, "hueCycle", createHueParams(PAL_MAIN), CYCLE_TIME, false);
        messageTo(self, "hueCycle", createHueParams(PAL_SECONDARY), CYCLE_TIME, false);
        messageTo(self, "hueCycle", createHueParams(PAL_TERTIARY), CYCLE_TIME, false);
    }

    public int hueCycle(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, LOOP_LOCK))
        {
            return SCRIPT_CONTINUE;
        }

        String palette = params.getString("palette");

        int randomColor = rand(0, 255);
        hue.setColor(self, palette, randomColor);

        messageTo(self, "hueCycle", params, CYCLE_TIME, false);
        return SCRIPT_CONTINUE;
    }

    private dictionary createHueParams(String palette)
    {
        dictionary params = new dictionary();
        params.put("palette", palette);
        return params;
    }
}
