package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Buffer
@Requirements: <no requirements>
@Notes: May the 4th be with you.
@Created: Sunday, 5/5/2024, at 1:47 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class adhoc_buffer extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Enhancement Unit (\\#e3d005Event\\#.)");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Enhancement Unit (\\#e3d005Event\\#.)");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Bolster"));
        if (isGod(player))
        {
            //toggle visibility of the object
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Toggle Visibility"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (getDistance(player, self) > 0.3f)
            {
                broadcast(player, "You must be in the enhancement unit to receive a bolstering effect.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                buff.applyBuff(player, "crystal_buff", 7200f, 15.0f);
                buff.applyBuff(player, "healOverTime", 7200f, 10.0f);
                buff.applyBuff(player, "treasure_bonus_combat_dodge", 7200f, 25.0f);
                buff.applyBuff(player, "treasure_bonus_heal_health_action", 7200f, 25.0f);
            }
        }
        else if (item == menu_info_types.SERVER_MENU1)
        {
            if (hasObjVar(self, "visible"))
            {
                removeObjVar(self, "visible");
                broadcast(player, "Enhancement unit is now hidden.");
                hideFromClient(self, true);
            }
            else
            {
                setObjVar(self, "visible", false);
                broadcast(player, "Enhancement unit is now visible.");
                hideFromClient(self, false);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
