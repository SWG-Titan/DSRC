package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author:  BubbaJoeX
@Purpose: Controls spawns in the giftshop instance
@Requirements: <no requirements>
@Notes: This is a controller object, it's purely a dictionary.
@Created: Wednesday, 11/20/2024, at 2:30 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class gift_workshop_manager extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Gift Workshop Manager");
        setDescriptionString(self, "This object controls the spawns in the gift shop instance.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Gift Workshop Manager");
        setDescriptionString(self, "This object controls the spawns in the gift shop instance.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                spawnAll(self);
            }
            else if (item == menu_info_types.SERVER_MENU2)
            {
                despawnAll(self);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Spawn"));
            mi.addRootMenu(menu_info_types.SERVER_MENU2, new string_id("Clear"));
        }
        return SCRIPT_CONTINUE;
    }

    public void spawnAll(obj_id self) throws InterruptedException
    {
        obj_id[] objects = getAllObjectsWithScript(getLocation(self), 256f, "event.lifeday.gift_workshop_spawner");
        if (objects == null || objects.length == 0)
        {
            return;
        }
        for (int i = 0; i < objects.length; i++)
        {
            messageTo(objects[i], "spawnWorkshopChild", null, 0, false);
        }
    }

    public void despawnAll(obj_id self) throws InterruptedException
    {
        obj_id[] objects = getAllObjectsWithScript(getLocation(self), 256f, "event.lifeday.gift_workshop_mob");
        if (objects == null)
        {
            return;
        }
        for (obj_id object : objects)
        {
            destroyObject(object);
        }
    }
}
