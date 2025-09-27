package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author:  BubbaJoeX
@Purpose: Allows players to buy 10 (small) or 20 (large) random items
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 7/31/2024, at 2:22 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.static_item;
import script.library.utils;

import java.util.HashSet;

public class junk_cache extends script.base_script
{
    public String JUNK_AMOUNT_VAR = "junk_cache.numItems";

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
        if (canManipulate(player, self, true, true, 15f, true))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Open Cache"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id pInv = utils.getInventoryContainer(player);
            int count = getIntObjVar(self, JUNK_AMOUNT_VAR);
            if (getVolumeFree(pInv) >= 20)
            {
                if (count == 0) //This shouldn't be a thing but just in case :P
                {
                    broadcast(player, "This cache seems to be empty. (Contact a GM)");
                }
                else
                {
                    String JUNK_TABLE = "datatables/crafting/reverse_engineering_junk.iff";
                    String column = "note";
                    for (int i = 0; i < count; i++)
                    {
                        String junk = dataTableGetString(JUNK_TABLE, rand(1, dataTableGetNumRows(JUNK_TABLE)), column);
                        obj_id junkItem = static_item.createNewItemFunction(junk, pInv);
                        if (isIdValid(junkItem))
                        {
                            if (count > 10)//Junk Cache: Large
                            {
                                setCount(junkItem, rand(10, 16));
                            }
                            else //Junk Cache: Small
                            {
                                setCount(junkItem, rand(5, 11));
                            }
                        }
                    }
                    broadcast(player, "You have opened this junk cache and retrieved it's contents.");
                    LOG("ethereal", "[Junk Cache]: " + getPlayerFullName(player) + " has opened a junk cache. Received " + count + " junk items at various stack amounts");
                    destroyObject(self);
                }
            }
            else
            {
                broadcast(player, "You need a minimum of 20 inventory slots free to open this cache.");
            }

        }
        return SCRIPT_CONTINUE;
    }
}
