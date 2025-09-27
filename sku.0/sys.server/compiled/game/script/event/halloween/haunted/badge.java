package script.event.halloween.haunted;/*
@Origin: dsrc.script.event.halloween.haunted
@Author:  BubbaJoeX
@Purpose: Grants the Ethereal Feelin' collection slot.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 10/31/2024, at 1:44 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.pet_lib;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class badge extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (hasCompletedCollectionSlot(player, "gmf_haunted_house"))
            {
                sendSystemMessage(player, "You have already completed the Ethereal Feelin' collection slot.", null);
                return SCRIPT_CONTINUE;
            }
            else
            {
                modifyCollectionSlotValue(player, "gmf_haunted_house", 1);
                broadcast(player, "What a spooky feeling! You have slotted the Ethereal Feelin' collection.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (areEnemiesNearby(player, 5.0f))
        {
            broadcast(player, "You must defeat all enemies before slotting the Ethereal Feelin' collection.");
            return SCRIPT_CONTINUE;
        }
        if (hasCompletedCollectionSlot(player, "gmf_haunted_house"))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Victory"));
        return SCRIPT_CONTINUE;
    }

    private boolean areEnemiesNearby(obj_id player, float v) throws InterruptedException
    {
        obj_id[] enemies = getCreaturesInRange(player, v);
        if (enemies == null)
        {
            return false;
        }
        for (obj_id enemy : enemies)
        {
            if (!isPlayer(enemy) && !isDead(enemy) && !pet_lib.isPet(enemy))
            {
                return true;
            }
        }
        return false;
    }
}
