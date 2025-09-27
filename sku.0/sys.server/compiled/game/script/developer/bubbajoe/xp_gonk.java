package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: Handler for scrapped battlefield 2.0 grenade droid.
@Note: This script is a work in progress and is not yet functional.
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class xp_gonk extends script.base_script
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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Summon Bomb Droid"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int mi) throws InterruptedException
    {
        if (mi == menu_info_types.ITEM_USE)
        {
            setupGonk(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int setupGonk(obj_id self, obj_id owner) throws InterruptedException
    {
        obj_id gonkie = createObject("object/mobile/eg6_power_droid.iff", getLocation(owner));
        setName(gonkie, "Experimental EG-6 Grenadier Droid");
        buff.applyBuff(gonkie, "healOverTime", 3600, 10);
        attachScript(gonkie, "developer.bubbajoe.bf2_ammo");
        obj_id[] players = getPlayerCreaturesInRange(getLocation(self), 100.0f);
        for (obj_id single : players)
        {
            broadcast(single, "An Experimental EG-6 Grenadier Droid has been activated.");
        }
        setObjVar(self, "loaded", 1);
        setObjVar(gonkie, "owner", owner);
        return SCRIPT_CONTINUE;
    }
}
