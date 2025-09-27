package script.item.content.rewards;/*
@Origin: script.item.
@Author: BubbaJoeX
@Purpose:
*/

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.*;
import script.library.utils;

public class loot_roll_item extends base_script
{
    public static String LR_VAR = "loot_roll";
    public static String LR_INCREASE_VAR = "loot_roll.charges";

    public loot_roll_item()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int setup(obj_id self) throws InterruptedException
    {
        setName(self, "Holographic Item Synthesizer");
        setDescriptionString(self, "This item grants you additional loot rolls on heroic enemies.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!hasObjVar(self, LR_INCREASE_VAR))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Test Your Luck"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (!hasObjVar(self, LR_INCREASE_VAR))
            {
                int charges = rand(1, 3);
                setObjVar(self, LR_INCREASE_VAR, charges);
                broadcast(player, "You have been granted " + charges + " additional loot rolls to use on a heroic or world boss enemy.");
                LOG("ethereal", "[Loot Roll Bonus]: Player " + getName(player) + " has been granted " + charges + " additional loot rolls on item " + self + ".");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, LR_INCREASE_VAR))
        {
            names[idx] = utils.packStringId(new string_id("Loot Roll Bonus"));
            attribs[idx] = String.valueOf(getIntObjVar(self, LR_INCREASE_VAR));
            idx++;
            if (idx >= names.length)
            {
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

}