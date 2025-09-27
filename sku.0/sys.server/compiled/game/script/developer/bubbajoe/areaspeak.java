package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Force all creatures within a certain range to speak a message.
@Created: Saturday, 3/30/2024, at 9:29 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.ai.ai;
import script.*;
import script.library.chat;
import script.library.sui;
import script.library.utils;

public class areaspeak extends script.base_script
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
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Spread the Gospel"));
        mi.addRootMenu(menu_info_types.SERVER_MENU10, new string_id("Set Message"));
        mi.addRootMenu(menu_info_types.SERVER_MENU11, new string_id("Set Animation"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU10)
            {
                sui.inputbox(self, player, "Enter the message.", "OnUpdateMessage", "OnUpdateMessage", 350, false, "");
            }
            if (item == menu_info_types.SERVER_MENU11)
            {
                sui.inputbox(self, player, "Enter the animation.", "OnUpdateAnimation", "OnUpdateAnimation", 350, false, "");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        location whereAmIGoing = getLocation(player);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        obj_id[] mobs = getCreaturesInRange(whereAmIGoing, 10.5f);
        for (obj_id creature : mobs)
        {
            if (isMob(creature) && !isPlayer(creature))
            {
                ai.stop(creature);
                chat.chat(creature, getStringObjVar(self, "message"));
                doAnimationAction(creature, getStringObjVar(self, "animation"));
                faceTo(creature, player);
                broadcast(player, "You have spread the gospel to " + creature);
            }
        }
        broadcast(player, "You have spread the gospel to " + mobs.length + " creatures.");
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateMessage(obj_id self, dictionary params) throws InterruptedException
    {
        String message = sui.getInputBoxText(params);
        if (message != null && !message.equals(""))
        {
            setObjVar(self, "message", message);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateAnimation(obj_id self, dictionary params) throws InterruptedException
    {
        String animation = sui.getInputBoxText(params);
        if (animation != null && !animation.equals(""))
        {
            setObjVar(self, "animation", animation);
        }
        return SCRIPT_CONTINUE;
    }
}
