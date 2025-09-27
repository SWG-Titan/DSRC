package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Bundle three chu gon dar items into a single crate to give to players
@Created: Tuesday, 10/31/2023, at 9:04 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.utils;

public class chugondar_crate_lava_crystal extends base_script
{
    //3l 3m 3o
    public static String[] CHU_GON_DAR_COMPONENTS = {
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3l.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3m.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3o.iff"
    };

    public int OnAttach(obj_id self)
    {
        reinit(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        reinit(self);
        return SCRIPT_CONTINUE;
    }

    public int reinit(obj_id self)
    {
        setName(self, "Chu-Gon Dar: Lava Crystal Crate");
        setDescriptionString(self, "This crate can be opened to receive three components for your Chu-Gon Dar cube.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        //check to see if in inventory, if not return
        if (!isInInventory(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Retrieve Components"));
        return SCRIPT_CONTINUE;
    }

    private boolean isInInventory(obj_id self, obj_id player) throws InterruptedException
    {
        return getContainedBy(self) == utils.getInventoryContainer(player);
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id inventory = utils.getInventoryContainer(player);
            if (inventory == null)
            {
                return SCRIPT_CONTINUE;
            }
            for (int i = 0; i < 3; i++)
            {
                obj_id component = createObject(CHU_GON_DAR_COMPONENTS[i], inventory, "");
                setObjVar(component, "owner", player);
            }
            LOG("ethereal", "[Chu-Gon Dar]: Player " + getName(player) + " has retrieved all items from a Chu-Gon Dar: Lava Crystal Crate");
            broadcast(self, "You have retrieved all items from this crate.");
            destroyObject(self);
        }
        return SCRIPT_CONTINUE;
    }
}
