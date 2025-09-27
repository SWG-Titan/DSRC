package script.theme_park.dungeon.death_watch_bunker;

import script.library.utils;
import script.menu_info;
import script.menu_info_data;
import script.menu_info_types;
import script.obj_id;

public class component_crate extends script.base_script
{
    public component_crate()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "Component Crate");
        setDescriptionString(self, "This crate contains a random piece of required to make Mandalorian Armor.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        if (mid == null)
        {
            return SCRIPT_CONTINUE;
        }
        mid.setServerNotify(true);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            createItems(self, player, (rand(1, 2) == 1) ? "art" : "components");
        }
        return SCRIPT_CONTINUE;
    }

    public void createItems(obj_id self, obj_id player, String type) throws InterruptedException
    {
        obj_id inv = utils.getInventoryContainer(player);
        int choice;

        if (type.equals("art"))
        {
            choice = rand(1, 3);
            switch (choice)
            {
                case 1:
                    createObject("object/tangible/furniture/all/frn_all_decorative_sm_s4.iff", inv, null);
                    break;
                case 2:
                    createObject("object/tangible/furniture/all/frn_all_decorative_lg_s1.iff", inv, null);
                    break;
                default:
                    createObject("object/tangible/furniture/all/frn_all_decorative_lg_s2.iff", inv, null);
                    break;
            }
        }
        else if (type.equals("components"))
        {
            choice = rand(1, 5);
            switch (choice)
            {
                case 1:
                    createObject("object/tangible/loot/dungeon/death_watch_bunker/fuel_dispersion_unit.iff", inv, null);
                    break;
                case 2:
                    createObject("object/tangible/loot/dungeon/death_watch_bunker/fuel_injector_tank.iff", inv, null);
                    break;
                case 3:
                    createObject("object/tangible/loot/dungeon/death_watch_bunker/fuel_regulator.iff", inv, null);
                    break;
                case 4:
                    createObject("object/tangible/loot/dungeon/death_watch_bunker/mining_drill_reward.iff", inv, null);
                    break;
                case 5:
                    createObject("object/tangible/loot/dungeon/death_watch_bunker/jetpack_base.iff", inv, null);
                    break;
                default:
                    broadcast(self, "No more items to give.");
                    break;
            }
        }

        destroyObject(self);
    }
}
