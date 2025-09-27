package script.systems.city;/*
@Origin: script.systems.city.city_decor_converter
@Author: BubbaJoeX
@Purpose: Converts storyteller props and tangible items into city decorations. Also allows you to create city actor deeds if you are a GM.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.static_item;
import script.library.utils;

import static script.library.storyteller.STORYTELLER_DATATABLE;

public class item_name_ripper extends base_script
{
    public item_name_ripper()
    {
    }

    //@Converts a story teller object to a city decor object
    public static void createPropObject(obj_id self, obj_id token) throws InterruptedException
    {
        obj_id prop = null;
        String itemName = getStaticItemName(token);
        int row = dataTableSearchColumnForString(itemName, "name", STORYTELLER_DATATABLE);
        dictionary dict = dataTableGetRow(STORYTELLER_DATATABLE, itemName);
        String template = dict.getString("template_name");
        prop = create.createObject(template, self, "");
        if (isIdValid(prop))
        {
            setName(prop, " ");
            obj_id mainInv = utils.getTopMostContainer(self);
            putIn(prop, mainInv);
            destroyObject(token);
        }
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "Static Decor Converter");
        setDescriptionString(self, "This object will convert any item into a static decoration with no functionality upon item transfer.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitalize(obj_id self) throws InterruptedException
    {
        setName(self, "Static Decor Converter");
        setDescriptionString(self, "This object will convert any item into a static decoration with no functionality upon item transfer.");
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id destContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isIdValid(item))
        {
            if (hasObjVar(item, "noTrade") || (hasScript(item, "item.special.nomove")))
            {
                broadcast(transferer, "You cannot strip these items.");
                return SCRIPT_OVERRIDE;
            }
            setName(item, " ");
            setDescriptionString(item, " ");
            detachAllScripts(item);
        }
        else
        {
            broadcast(transferer, "This item is not valid.");
        }
        return SCRIPT_CONTINUE;
    }

    //@Note: Converts a story teller object to a city actor object
    public void createActorProp(obj_id self, obj_id item, obj_id transferer) throws InterruptedException
    {
        String itemName = getStaticItemName(item);
        int row = dataTableSearchColumnForString(itemName, "name", STORYTELLER_DATATABLE);
        dictionary dict = dataTableGetRow(STORYTELLER_DATATABLE, itemName);
        String template = dict.getString("template_name");
        obj_id prop = create.createObject(template, utils.getInventoryContainer(transferer), "");
        if (isIdValid(prop))
        {
            detachAllScripts(prop);
            attachScript(prop, "systems.city.city_actor");
            destroyObject(item);
        }
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("[GM] Create Actor Deed"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {

        if (item == menu_info_types.SERVER_MENU1)
        {
            obj_id deed = static_item.createNewItemFunction("item_city_actor_deed", utils.getInventoryContainer(player));
            if (isIdValid(deed))
            {
                debugConsoleMsg(player, "Deed Object: " + deed);
                if (!hasScript(deed, "systems.city.city_hire"))
                {
                    attachScript(deed, "systems.city.city_hire");
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

}
