package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Plays client effects at cursor location.
@Created: Saturday, 3/30/2024, at 8:53 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class effectspam extends script.base_script
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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Play Effect"));
        mi.addRootMenu(menu_info_types.SERVER_MENU10, new string_id("Set Effect"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU10)
            {
                sui.inputbox(self, player, "Enter the filename of the sound, particle, or clienteffect or clientdata you wish to play. Full path from root.", "OnUpdateEffect", "OnUpdateEffect", 250, false, "100000");
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
        doAnimationAction(player, "draw_datapad");
        location whereAmIGoing = getLocation(self);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        if (!isInWorldCell(player))
        {
            whereAmIGoing.cell = getContainedBy(player);
        }
        obj_id[] players = getAllPlayers(whereAmIGoing, 64.0f);
        String effect = getStringObjVar(self, "effect");
        if (effect.startsWith("sound/"))
        {
            for (obj_id singlePlayer : players)
            {
                play2dNonLoopingSound(singlePlayer, effect);
            }
            return SCRIPT_CONTINUE;
        }
        else if (effect.startsWith("music/"))
        {
            for (obj_id singlePlayer : players)
            {
                play2dNonLoopingMusic(singlePlayer, effect);
            }
            return SCRIPT_CONTINUE;
        }
        else if (effect.startsWith("clienteffect/"))
        {
            playClientEffectLoc(players, effect, whereAmIGoing, 1.0f);
            return SCRIPT_CONTINUE;
        }
        else if (effect.startsWith("appearance/"))
        {
            playClientEffectLoc(players, effect, whereAmIGoing, 1.0f);
            return SCRIPT_CONTINUE;
        }
        else if (effect.startsWith("clientdata/"))
        {
            playClientEffectLoc(players, effect, whereAmIGoing, 1.0f);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateEffect(obj_id self, dictionary params) throws InterruptedException
    {
        String text = sui.getInputBoxText(params);
        obj_id player = sui.getPlayerId(params);
        if (text == null || text.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        if (text.startsWith("sound/"))
        {
            setObjVar(self, "effect", text);
            broadcast(player, "You have set the effect to " + text);
            return SCRIPT_CONTINUE;
        }
        else if (text.startsWith("clienteffect/"))
        {
            setObjVar(self, "effect", text);
            broadcast(player, "You have set the effect to " + text);
            return SCRIPT_CONTINUE;
        }
        else if (text.startsWith("music/"))
        {
            setObjVar(self, "effect", text);
            broadcast(player, "You have set the effect to " + text);
            return SCRIPT_CONTINUE;
        }
        else if (text.startsWith("appearance/"))
        {
            setObjVar(self, "effect", text);
            broadcast(player, "You have set the effect to " + text);
            return SCRIPT_CONTINUE;
        }
        else if (text.startsWith("clientdata/"))
        {
            setObjVar(self, "effect", text);
            broadcast(player, "You have set the effect to " + text);
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Invalid effect file. Must be sound/, music/, clienteffect/, clientdata/ or appearance/");
            return SCRIPT_CONTINUE;
        }
    }
}
