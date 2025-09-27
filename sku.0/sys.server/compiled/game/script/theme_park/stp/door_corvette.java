package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.*;
import script.library.colors_hex;
import script.library.sui;

/**
 * @author BubbaJoe
 */
public class door_corvette extends base_script
{
    public door_corvette()
    {
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        int menu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Depart"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            String title = "Confirm Departure";
            String prompt = "Are you sure you want to head back to Mos Deltro?";
            sui.msgbox(self, player, prompt, sui.OK_CANCEL, title, "handleConfirm");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleConfirm(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_OK)
        {
            warpPlayer(player, "tatooine", -2667.0f, 0.0f, -5758.0f, null, 0, 0, 0, "noHandler", true);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
}
