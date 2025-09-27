package script.item.content.rewards;/*
@Origin: script.item.content.rewards
@Author: BubbaJoeX
@Purpose: This item allows you to place an extractor unit that will mine resources for you, using messagTo to generate the resource.
@Note: This is a work in progress and does not function as intended.
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class resource_extraction_beacon extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (!hasObjVar(self, "resource_extraction_beacon"))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Place Extraction Beacon"));
        }
        else
        {
            int parent = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Manage Extraction Beacon"));
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU2, new string_id("Collect Resources"));
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU3, new string_id("Select Resource"));
        }

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!hasObjVar(self, "resource_extraction_beacon"))
            {
                obj_id beacon = createObject("object/tangible/item/resource_extraction_beacon.iff", getLocation(player));
                setObjVar(beacon, "resource_extraction_beacon", 1);
                setObjVar(beacon, "owner", player);
                destroyObject(self);
            }

        }
        if (item == menu_info_types.SERVER_MENU2)
        {

        }
        if (item == menu_info_types.SERVER_MENU3)
        {
            String prompt = "Select a resource for sampling automation.";
            String title = "Resource Selection";
            obj_id[] resources = getResourceTypes("inorganic");
            String[] typeNames = getResourceNames(resources);
            int pid = sui.listbox(self, player, prompt, sui.OK_CANCEL, title, typeNames, "handleResourceSelection");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleResourceSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String[] typeNames = getResourceNames(getResourceTypes("resource"));
        String resource = typeNames[idx];
        obj_id resourceActual = getResourceTypeByName(resource);
        setObjVar(self, "resource_extraction_beacon.resource", resource);
        setObjVar(self, "resource_extraction_beacon.resourceActual", resourceActual);
        dictionary d = new dictionary();
        d.put("resource", resourceActual);
        messageTo(self, "firstPhase", d, 10, false);
        return SCRIPT_CONTINUE;
    }

    public int firstPhase(obj_id self, dictionary params)
    {
        oneTimeHarvest(params.getObjId("resource"), rand(69, 420), getLocation(self));
        return SCRIPT_CONTINUE;
    }

    public int secondPhase(obj_id self, dictionary params)
    {
        oneTimeHarvest(params.getObjId("resource"), rand(69, 420), getLocation(self));
        return SCRIPT_CONTINUE;
    }
}

