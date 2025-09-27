package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Summons a poi with a terrain layer modifier at large, medium, or small size.
@Note: Do not use more than 3 of these at once. The more layers the more confusion.
@Created: Saturday, 3/30/2024, at 3:29 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class cliffblaster extends script.base_script
{
    public String[] SIZES = {
            "Large",
            "Medium",
            "Small"
    };
    public String[] TEMPLATES = {
            "object/building/poi/generic_flatten_large.iff",
            "object/building/poi/generic_flatten_medium.iff",
            "object/building/poi/generic_flatten_small.iff"
    };

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Sculpt Terrain"));
        mi.addRootMenu(menu_info_types.SERVER_MENU10, new string_id("Set Flatten Type"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU10)
            {
                handleSize(self, player);
            }
            else
            {
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isInWorldCell(player))
        {
            broadcast(self, "You cannot use this inside.");
            return SCRIPT_CONTINUE;
        }
        location here = new location();
        here.x = x;
        here.y = y;
        here.z = z;
        here.area = getCurrentSceneName();
        String template = "";
        if (hasObjVar(self, "cliffblaster.template"))
        {
            template = getStringObjVar(self, "cliffblaster.template");
        }
        createObject(template, here);
        playClientEffectLoc(player, "clienteffect/int_camshake_heavy.cef", here, 1.0f);
        return SCRIPT_CONTINUE;
    }

    public int handleSize(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, "Select Flatten Size", sui.OK_ONLY, "Terrain Sculpter", SIZES, "handleSizeSelection");
        return SCRIPT_CONTINUE;
    }

    public int handleSizeSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int selection = sui.getListboxSelectedRow(params);
        switch (selection)
        {
            case 0:
                setObjVar(self, "cliffblaster.template", TEMPLATES[0]);
                broadcast(player, "Selection: Large");
                break;
            case 1:
                setObjVar(self, "cliffblaster.template", TEMPLATES[1]);
                broadcast(player, "Selection: Medium");
                break;
            case 2:
                setObjVar(self, "cliffblaster.template", TEMPLATES[2]);
                broadcast(player, "Selection: Small");
                break;
        }
        return SCRIPT_CONTINUE;
    }
}
