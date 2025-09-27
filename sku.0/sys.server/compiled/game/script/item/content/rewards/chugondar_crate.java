package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Bundle three chu gon dar items into a single crate to give to players
@Created: Tuesday, 10/31/2023, at 9:04 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class chugondar_crate extends script.base_script
{
    public static String[] CHU_GON_DAR_COMPONENTS = {
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1a.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1b.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1c.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1d.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1e.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1f.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1g.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1h.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1i.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1j.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1k.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1l.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1m.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1n.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1o.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1p.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1q.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1r.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1s.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1t.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1u.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1v.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1w.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1x.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_1y.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2a.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2b.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2c.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2d.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2e.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2f.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2g.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2h.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2i.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2j.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2k.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2l.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2m.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2n.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2o.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2p.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2q.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2r.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2s.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_2t.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3a.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3b.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3c.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3d.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3e.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3f.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3g.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3h.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3i.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3j.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3k.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3l.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3m.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3n.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3o.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3p.iff",
            "object/tangible/loot/mustafar/cube_loot/cube_loot_3r.iff"
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
        setName(self, "Chu-Gon Dar: Mystery Crate");
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
                obj_id component = createObject(CHU_GON_DAR_COMPONENTS[rand(0, CHU_GON_DAR_COMPONENTS.length - 1)], inventory, "");
                setObjVar(component, "owner", player);
            }
            LOG("ethereal", "[Chu-Gon Dar]: Player " + getName(player) + " has retrieved all items from a Chu-Gon Dar Mystery Crate.");
            broadcast(self, "You have retrieved all items from this crate.");
            destroyObject(self);
        }
        return SCRIPT_CONTINUE;
    }
}
