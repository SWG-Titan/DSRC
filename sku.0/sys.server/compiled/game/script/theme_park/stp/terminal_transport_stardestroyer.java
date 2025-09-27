package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.menu_info;
import script.menu_info_data;
import script.menu_info_types;
import script.obj_id;

/**
 * @author BubbaJoe
 */
public class terminal_transport_stardestroyer extends script.base_script
{
    public static String STARD = "object/building/taanab/star_destroyer.iff";

    public terminal_transport_stardestroyer()
    {
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        menu_info_data data = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        if (data != null)
        {
            data.setServerNotify(true);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id[] targetLocs = getAllObjectsWithTemplate(getLocation(self), 16000.0f, STARD);
            obj_id cell = getCellId(targetLocs[0], "room01");
            warpPlayer(player, getCurrentSceneName(), 0.0f, 0.0f, 0.0f, cell, 0.0f, 0.0f, 0.0f);
        }
        return SCRIPT_CONTINUE;
    }
}
