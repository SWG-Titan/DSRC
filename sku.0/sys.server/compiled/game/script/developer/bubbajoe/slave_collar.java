package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.slave_collar
@Author: BubbaJoeX
@Purpose: [WIP] Dragging a slave collar on a mob will allow them to be controlled by the player like a pet, or factional hire.
@Note: This script is a work in progress and is not yet functional.
@Created: Saturday, 3/30/2024, at 9:45 AM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class slave_collar extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToBeTransferred(obj_id self, obj_id destContainer, obj_id transferer) throws InterruptedException
    {
        broadcast(transferer, "OnAboutToBeTransferred -- destContainer: " + destContainer + " transferer: " + transferer);
        return SCRIPT_CONTINUE;
    }

    public int OnGiveItem(obj_id self, obj_id item, obj_id player) throws InterruptedException
    {
        broadcast(player, "OnGiveItem -- item: " + item + " player: " + player);
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        broadcast(transferer, "OnReceivedItem -- item: " + item + " src container: " + srcContainer + " transferer: " + transferer);
        return SCRIPT_CONTINUE;
    }
}
