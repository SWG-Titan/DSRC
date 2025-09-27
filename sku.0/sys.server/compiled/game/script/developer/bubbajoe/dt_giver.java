package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Origin: dsrc.script.developer.bubbajoe.dt_giver
@Author: BubbaJoeX
@Purpose: Extended Downtime Gift Giver -- No longer in use.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/


import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class dt_giver extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        String desc = "Radial this gift and select 'Claim' to receive your Remote Tactical Deployment Tool.";
        setDescriptionStringId(self, new string_id(desc));
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        String desc = "Radial this gift and select 'Claim' to receive your Remote Tactical Deployment Tool.";
        setDescriptionStringId(self, new string_id(desc));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int mi) throws InterruptedException
    {
        if (mi == menu_info_types.ITEM_USE)
        {
            if (hasObjVar(self, "dt_gift_" + player))
            {
                sendSystemMessage(player, "You have already claimed this gift.", null);
            }
            else
            {
                obj_id gift = createObject("object/tangible/loot/misc/picture_handheld_s02.iff", utils.getInventoryContainer(player), "");
                if (gift == null)
                {
                    sendSystemMessage(player, "Gift could not be created. Please contact a GM.", null);
                    return SCRIPT_CONTINUE;
                }
                attachScript(gift, "developer.bubbajoe.dt_gift");
                sendSystemMessage(player, "You have claimed this gift.", null);
                setObjVar(self, "dt_gift_" + player, 1);
            }
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
}
