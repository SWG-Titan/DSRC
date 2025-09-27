package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.magic_satchel
@Author: BubbaJoeX
@Purpose: Clones an object 1:1 wet (with scripts/scriptvars/objvars) and places it in a satchel inside your inventory.
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

@SuppressWarnings("unused")
public class magic_satchel extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Magic Satchel");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            int mainMenu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Clone"));
            mi.addSubMenu(mainMenu, menu_info_types.SERVER_MENU1, new string_id("Clear"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException, NullPointerException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id pInv = utils.getInventoryContainer(player);
            if (getVolumeFree(pInv) <= 5)
            {
                broadcast(player, "Your inventory is full.");
                return SCRIPT_CONTINUE;
            }
            obj_id[] dupeContents = utils.getContents(self);
            for (obj_id original : dupeContents)
            {
                String template = getTemplateName(original);
                obj_id dupe = createObject(template, pInv, "");
                if (isIdValid(dupe))
                {
                    // Copy scripts
                    String[] scripts = getScriptList(original);
                    for (String script : scripts)
                    {
                        attachScript(dupe, script);
                    }
                    // Copy all objvars
                    String allObjVars = getPackedObjvars(original);
                    setPackedObjvars(dupe, allObjVars);

                    //Copy count
                    if (getCount(original) > 1)
                    {
                        setCount(dupe, getCount(original));
                    }

                    if (isIdValid(getCrafter(original)))
                    {
                        setCrafter(dupe, getCrafter(original));
                    }

                    broadcast(player, "Cloned " + original + " to " + dupe + "!");
                }
                else
                {
                    broadcast(player, "Failed to create a duplicate of " + original + ".");
                }
            }
        }

        if (item == menu_info_types.SERVER_MENU1)
        {
            obj_id[] dupeContents = getContents(self);
            for (obj_id converted : dupeContents)
            {
                destroyObject(converted);
            }
            broadcast(player, "Contents cleared.");
        }
        return SCRIPT_CONTINUE;
    }
}
