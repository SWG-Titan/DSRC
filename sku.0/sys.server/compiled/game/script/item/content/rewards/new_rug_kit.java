package script.item.content.rewards;/*
@Origin: script.item.
@Author: BubbaJoeX
@Purpose: A kit to reward players with three of the 171 new rugs.
@Copyright © SWG: Titan 2024.
*/

import script.library.create;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

import java.util.HashSet;

public class new_rug_kit extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        reInitializeKit(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        reInitializeKit(self);
        return SCRIPT_CONTINUE;
    }

    public void reInitializeKit(obj_id self)
    {
        setName(self, "Abbub's Rug Kit");
        setDescriptionString(self, "This kit can be used to create three random rugs from 171 variations. Select 'Fabricate Rugs' to create your rugs.");
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
        if (utils.isNestedWithinAPlayer(self))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Fabricate Rugs"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (isIncapacitated(self) || isDead(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (getVolumeFree(utils.getInventoryContainer(player)) < 3)
            {
                broadcast(player, "You do not have enough inventory space to fabricate three rugs.");
                return SCRIPT_CONTINUE;
            }
            String rugPrefix = "object/tangible/tarkin_custom/decorative/rug/tarkin_rug_";
            String rugSuffix = ".iff";
            obj_id pInv = utils.getInventoryContainer(player);
            HashSet theSet = new HashSet();
            theSet.add(create.createObject(rugPrefix + (rand(1, 171)) + rugSuffix, pInv, ""));
            theSet.add(create.createObject(rugPrefix + (rand(1, 171)) + rugSuffix, pInv, ""));
            theSet.add(create.createObject(rugPrefix + (rand(1, 171)) + rugSuffix, pInv, ""));
            obj_id[] items = new obj_id[theSet.size()];
            for (int i = 0; i < theSet.size(); i++)
            {
                items[i] = (obj_id) theSet.toArray()[i];
                setName(items[i], "an exotic rug");
                setObjVar(items[i], "null_desc", "This exotic rug was fabricated from Abbub's Rug Kit. What a spectacular rug!");
                attachScript(items[i], "developer.bubbajoe.sync");
            }
            showLootBox(player, items);
            broadcast(player, "You have fabricated three rugs.");
            playClientEventObj(player, "sound/item_cloth_open.snd", player, "");
            LOG("ethereal", "[Rug Kit]: Player " + getName(player) + " has fabricated three rugs from " + self + ".");
            destroyObject(self);
        }
        return SCRIPT_CONTINUE;
    }
}
