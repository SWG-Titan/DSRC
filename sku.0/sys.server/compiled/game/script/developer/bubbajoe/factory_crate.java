package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 8/24/2024, at 5:48 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class factory_crate extends script.base_script
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
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Generate Factory Crate"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                generateFactoryCrate(self, utils.getInventoryContainer(player));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int generateFactoryCrate(obj_id self, obj_id inventory)
    {
        if (!isIdValid(inventory) || !exists(inventory))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(self) || !exists(self))
        {
            return SCRIPT_CONTINUE;
        }
        String crateTemplate = "object/factory/factory_crate_clothing.iff";
        obj_id crate = createObject(crateTemplate, inventory, "");
        setObjVar(crate, "crafting.source_schematic", self);
        setObjVar(crate, "crafting.crafting_attributes.crafting:charges", rand(3, 9));
        setObjVar(crate, "crafting.crafting_attributes.crafting:complexity", rand(3, 36));
        setObjVar(crate, "crafting.crafting_attributes.crafting:hitPoints", 1000f);
        setObjVar(crate, "crafting.crafting_attributes.crafting:xp", rand(1f, 100f));
        setObjVar(crate, "draftSchematic", getTemplateCrcCreatedFromSchematic(self.toString()));
        setName(crate, getEncodedName(self));
        String[] scriptsToCopy = getScriptList(self);
        for (String scriptToCopy : scriptsToCopy)
        {
            if (!hasScript(crate, scriptToCopy))
            {
                attachScript(crate, scriptToCopy);
            }
        }
        setCount(crate, 250);
        return SCRIPT_CONTINUE;
    }
}
