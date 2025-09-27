package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Script for foods on the Food Cart
@Created: Monday, 11/20/2023, at 10:06 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class event_food extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, "buff_name"))
        {
            names[idx] = utils.packStringId(new string_id("Effect"));
            attribs[idx] = utils.packStringId(new string_id(getStringObjVar(self, "display_name")));
            idx++;
        }
        if (hasObjVar(self, "duration"))
        {
            names[idx] = utils.packStringId(new string_id("Duration"));
            int duration = getIntObjVar(self, "duration");
            int effectiveness = getIntObjVar(self, "effectiveness");
            int totalDuration = duration * effectiveness + 3600;
            attribs[idx] = totalDuration / 60 + " minutes";
            idx++;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info item) throws InterruptedException
    {
        item.addRootMenu(menu_info_types.ITEM_USE, new string_id("Consume"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            String buffName = getStringObjVar(self, "buff_name");
            int duration = getIntObjVar(self, "duration");
            int effectiveness = getIntObjVar(self, "effectiveness");
            int totalDuration = duration * effectiveness + 3600;
            buff.applyBuff(player, buffName, totalDuration, effectiveness * 12);
            if (getCount(self) <= 1)
            {
                destroyObject(self);
            }
            else
            {
                decrementCount(self);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
