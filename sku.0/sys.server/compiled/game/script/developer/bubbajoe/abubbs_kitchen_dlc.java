package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Kitchen DLC -- Defunt, see below
@Requirements: dsrc.script.conversation.kitchen_salesperson
@Created: Friday, 10/27/2023, at 11:05 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class abubbs_kitchen_dlc extends script.base_script
{
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
        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Unpack"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (getContainedBy(self) != utils.getInventoryContainer(player))
            {
                broadcast(self, "Item must be in your inventory to unpack.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                int version = getIntObjVar(self, "version");
                switch (version)
                {
                    case 1:
                        obj_id sink = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id cab1 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id cab2 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id cab3 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id fridge = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        broadcast(self, "You have successfully unpacked this crate.");
                        destroyObject(self);
                        break;
                    case 2:
                        obj_id sink_02 = createObject("object/building//abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id cab1_02 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id cab2_02 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id cab3_02 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        obj_id fridge_02 = createObject("object/building/general/abubbs_kitchen.iff", utils.getInventoryContainer(player), "");
                        broadcast(self, "You have successfully unpacked this crate.");
                        destroyObject(self);
                        break;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
