package script.event.halloween;

/*
@Origin: dsrc.script.event.halloween
@Author: BubbaJoeX
@Purpose: Halloween "Tag, You're It!" game logic
@Requirements: Players take turns being "It" in a tag game
@Notes: <no notes>
@Created: Saturday, 9/7/2024, at 6:14 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class tag extends base_script
{
    private static final String VAR_TAG_STATE = "tag.isIt";
    private static final String VAR_LAST_TAG_TIME = "tag.lastTagTime";
    private static final int TAG_TIMEOUT = 120;

    public int OnAttach(obj_id self)
    {
        setObjVar(self, VAR_TAG_STATE, 0); // Player is not "It" initially
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        // Ensure tag state resets if the player is initialized
        if (!hasObjVar(self, VAR_TAG_STATE))
        {
            setObjVar(self, VAR_TAG_STATE, 0);
        }
        if (isIt(self))
        {
            startTagTimer(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isIt(player))
        {
            final boolean b = getDistance(self, player) > 10;
            if (b)
            {
                int tagMenu = mi.addRootMenu(menu_info_types.SERVER_MENU35, new string_id("Tag!"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE && isIt(player))
        {
            obj_id target = getLookAtTarget(player);
            if (isValidId(target) && exists(target))
            {
                if (isPlayer(target))
                {
                    tagPlayer(player, target);
                }
                else
                {
                    broadcast(player, "You can only tag other players.");
                }
            }
            else
            {
                broadcast(player, "No valid target selected for tagging.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    private boolean isIt(obj_id player)
    {
        return getIntObjVar(player, VAR_TAG_STATE) == 1;
    }

    private void tagPlayer(obj_id itPlayer, obj_id newItPlayer)
    {
        if (itPlayer == newItPlayer)
        {
            broadcast(itPlayer, "You can't tag yourself!");
            return;
        }

        setObjVar(itPlayer, VAR_TAG_STATE, 0); // Remove "It" status from the current player
        setObjVar(newItPlayer, VAR_TAG_STATE, 1); // Assign "It" status to the new player

        broadcast(itPlayer, "You've tagged " + getName(newItPlayer) + "!");
        broadcast(newItPlayer, "You are now 'It'! Run and tag someone else!");

        startTagTimer(newItPlayer); // Start the timer for the new "It" player
    }

    private void startTagTimer(obj_id player)
    {
        setObjVar(player, VAR_LAST_TAG_TIME, getGameTime());
        messageTo(player, "handleTagTimeout", null, TAG_TIMEOUT, false);
    }

    public int handleTagTimeout(obj_id self, dictionary params) throws InterruptedException
    {
        if (isIt(self))
        {
            int lastTagTime = getIntObjVar(self, VAR_LAST_TAG_TIME);
            if (getGameTime() - lastTagTime >= TAG_TIMEOUT)
            {
                showFlyText(self, new string_id("LOSER!"), 1.5f, color.RED);
                broadcastMessage(self, "LOSER! The 'It' player failed to tag someone within the time limit.");
                setObjVar(self, VAR_TAG_STATE, 0); // Remove "It" status
                obj_id[] nearbyPlayers = getPlayersInRange(self, 50.0f);
                if (nearbyPlayers != null && nearbyPlayers.length > 0)
                {
                    obj_id newItPlayer = nearbyPlayers[(int) (Math.random() * nearbyPlayers.length)];
                    setObjVar(newItPlayer, VAR_TAG_STATE, 1);
                    broadcast(newItPlayer, "You are now 'It'! Run and tag someone else!");
                    startTagTimer(newItPlayer);
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    private obj_id[] getPlayersInRange(obj_id self, float range)
    {
        return getObjectsInRange(self, range);
    }

    private void broadcastMessage(obj_id self, String message)
    {
        obj_id[] nearbyPlayers = getPlayersInRange(self, 50.0f);
        if (nearbyPlayers != null)
        {
            for (obj_id player : nearbyPlayers)
            {
                broadcast(player, message);
            }
        }
    }

    public void killTagScript(obj_id self)
    {
        detachScript(self, "event.halloween.tag");
    }
}