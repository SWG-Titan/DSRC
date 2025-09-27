package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: hotfix for avatar platform dropping you to the ground.
@Note: This script is a hotfix for the avatar platform dropping you to the ground. It will teleport you to the correct location. As of 10/23/2023, this script is no longer in use.
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.space_dungeon;
import script.location;
import script.obj_id;

public class avatar_mover extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "[Debug] Avatar Mover");
        createTriggerVolume("avatar_mover", 64, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String name, obj_id who) throws InterruptedException
    {
        if (!isPlayer(who))
        {
            return SCRIPT_CONTINUE;
        }
        location here = getLocation(self);
        location avatarLoc = new location(here.x, 41.f, here.z, getCurrentSceneName());
        if (isInWorldCell(who) && here.y < 41)
        {
            space_dungeon.sendGroupToDungeonWithoutTicketCollector(who, "avatar_platform", "quest_type");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeExited(obj_id self, String name, obj_id who) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

}
