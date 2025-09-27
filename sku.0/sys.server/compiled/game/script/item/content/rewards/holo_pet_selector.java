package script.item.content.rewards;/*
@Origin: script.item.
@Author: BubbaJoeX
@Purpose: Grants a player a holo pet cube of their choice
*/

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.*;
import script.library.static_item;
import script.library.utils;

public class holo_pet_selector extends base_script
{
    public int MAX_PETS_PER_CACHE = 3;

    public int OnAttach(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public void setup(obj_id self)
    {
        setName(self, "Holo-Pet Cache");
        setDescriptionString(self, "A cache of three random holo-pets for you to display from within a Holo-Emitter. Select 'Retrieve Holo-Pet Cache' from the radial menu to claim your holo-pets.");
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (isIncapacitated(self) || isDead(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (isIncapacitated(getTopMostContainer(self)) || isDead(getTopMostContainer(self)))
        {
            return SCRIPT_CONTINUE;
        }
        if (utils.isNestedWithinAPlayer(self))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Retrieve Holo-Pets"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (hasVolume(player, MAX_PETS_PER_CACHE))
            {
                createHoloPets(self, player);
                LOG("ethereal", "[Holo-pet Selector]: " + getName(player) + " has claimed a holo-pet cache.");
            }
            else
            {
                broadcast(self, "You do not have enough inventory space to claim your holo-pets. Please make room and try again.");
                LOG("ethereal", "[Holo-pet Selector]: " + getName(player) + " attempted to claim a holo-pet cache but did not have enough inventory space.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void createHoloPets(obj_id self, obj_id player) throws InterruptedException
    {
        String[] petList = {
                "item_holopet_atat_data_cube_01_01",
                "item_holopet_cle004_data_cube_01_01",
                "item_holopet_data_cube_gift_01_01",
                "item_holopet_ewok_data_cube_01_01",
                "item_holopet_hoth_probe_droid_data_cube_01_01",
                "item_holopet_hoth_tauntaun_data_cube_01_01",
                "item_holopet_ins444_data_cube_01_01",
                "item_holopet_jawa_data_cube_01_01",
                "item_holopet_jax_data_cube_01_01",
                "item_holopet_pekopeko_data_cube_01_01",
                "item_holopet_quenker_data_cube_01_01",
                "item_holopet_rancor_data_cube_01_01",
                "item_holopet_slicehound_data_cube_01_01",
                "item_holopet_squall_data_cube_01_01",
                "item_holopet_veermok_data_cube_01_01"
        };
        obj_id pInv = utils.getInventoryContainer(player);
        static_item.createNewItemFunction("item_holopet_emitter_01_01", pInv);
        for (int i = 0; i < MAX_PETS_PER_CACHE; i++)
        {
            int randomIndex = rand(0, petList.length - 1);
            static_item.createNewItemFunction(petList[randomIndex], pInv);
            LOG("ethereal", "[Holo-pet Selector]: " + getName(player) + " has claimed a pet with the static item of " + petList[randomIndex] + " from their holo-pet cache.");
        }
        destroyObject(self);
    }

    public boolean hasVolume(obj_id player, int i) throws InterruptedException
    {
        return getVolumeFree(utils.getInventoryContainer(player)) >= i;
    }
}
