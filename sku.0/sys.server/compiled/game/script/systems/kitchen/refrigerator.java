package script.systems.kitchen;/*
@Origin: dsrc.script.systems.kitchen
@Author: BubbaJoeX
@Purpose: Script to store food inside a fridge object
@Note: Contents can only be GOT_misc_food
@Requirements: <no requirements>
@TODO: Add chef crafting components to be stored in fridge.
@Created: Sunday, 10/1/2023, at 3:32 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class refrigerator extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (isIdValid(item))
        {
            if (getGameObjectType(item) == GOT_misc_food || getGameObjectType(item) == GOT_component || getGameObjectType(item) == GOT_misc_drink || getGameObjectType(item) == GOT_misc_factory_crate)
            {
                return SCRIPT_CONTINUE;
            }
            else
            {
                broadcast(transferer, "You can only store food in a refrigerator.");
                LOG("ethereal", "[Kitchen]: " + getPlayerFullName(transferer) + " tried to store a non-food item in a refrigerator.");
                return SCRIPT_OVERRIDE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
