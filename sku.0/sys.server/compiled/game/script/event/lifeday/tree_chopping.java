package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author: BubbaJoeX
@Purpose: Chop tree for Deco or resources
@Created: Saturday, 11/18/2023, at 7:46 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.utils;

public class tree_chopping extends base_script
{
    public boolean SINGLE_USE = true;
    public boolean SINGLE_USE_AXE = false;

    public int reInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "Life Day Tree");
        setDescriptionString(self, "This tree can be chopped down for resources or decoration.");
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }

        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Chop Down Tree"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (hasAxe(player))
            {
                obj_id axe = getAxe(player);
                if (axe == null)
                {
                    return SCRIPT_CONTINUE;
                }
                String template = getTemplateName(self);
                obj_id inventory = utils.getInventoryContainer(player);
                obj_id tree = createObject(template, inventory, "");
                setObjVar(player, "tree_chopped", 1);
                broadcast(player, "You chop down the tree and place it in your inventory.");
                setOwner(tree, player);
                attachScript(tree, "event.lifeday.tree");
                if (SINGLE_USE)
                {
                    destroyObject(self);
                }
                if (SINGLE_USE_AXE)
                {
                    destroyObject(axe);
                }
            }
            else
            {
                broadcast(player, "You need a Heavy Duty Axe to chop down this tree.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    private boolean hasAxe(obj_id player) throws InterruptedException
    {
        return hasItem(player, "object/weapon/melee/axe/axe_heavy_duty.iff");
    }

    private boolean hasItem(obj_id player, String s) throws InterruptedException
    {
        obj_id container = utils.getInventoryContainer(player);
        obj_id[] contents = utils.getContents(container, true);
        for (obj_id content : contents)
        {
            if (getTemplateName(content).equals(s))
            {
                return true;
            }
        }
        return false;
    }

    public obj_id getAxe(obj_id player) throws InterruptedException
    {
        obj_id container = utils.getInventoryContainer(player);
        obj_id[] contents = utils.getContents(container, true);
        for (obj_id content : contents)
        {
            if (getTemplateName(content).equals("object/weapon/melee/axe/axe_heavy_duty.iff"))
            {
                if (hasObjVar(content, "lifeday.axe_object"))
                {
                    return content;
                }
            }
        }
        return null;
    }
}
