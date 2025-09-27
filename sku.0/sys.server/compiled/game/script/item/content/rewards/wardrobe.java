/*
@Purpose: Wardrobe script for the Lifeday 2022 Reward

@Author: BubbaJoe
 */
package script.item.content.rewards;/*
@Origin: script.item.content.rewards
@Author: BubbaJoeX
@Purpose: This item allows you to place an extractor unit that will mine resources for you, using messagTo to generate the resource.
@Note: This is a work in progress and does not function as intended.
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

public class wardrobe extends script.base_script
{
    public wardrobe()
    {
    }

    public int OnAboutToBeTransferred(obj_id self, obj_id destContainer, obj_id transferer) throws InterruptedException
    {
        if (isPlayer(transferer) && (getVolumeFree(self) == getTotalVolume(self)))
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(transferer, "This container is too big to be carried full. Try removing some items, then try again.");
            LOG("ethereal", "[Wardrobe]: Player " + getName(transferer) + " attempted to move a wardrobe container that was too big to be carried full.");
        }
        return SCRIPT_OVERRIDE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (!getTemplateName(item).contains("object/tangible/wearables/"))
        {
            broadcast(transferer, "You can only put wearables in this container.");
            LOG("ethereal", "[Wardrobe]: Player " + getName(transferer) + " attempted to put a non-wearable item into a wardrobe.");
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(item, "wardrobe.ownedBy"))
        {
            setObjVar(item, "wardrobe.ownedBy", transferer);
            LOG("ethereal", "[Wardrobe]: Player " + getName(transferer) + " has added a wearable to their wardrobe var list.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToLoseItem(obj_id self, obj_id destContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        if (!getTemplateName(item).contains("object/tangible/wearables/"))
        {
            broadcast(transferer, "You can only retrieve wearables from this container.");
            LOG("ethereal", "[Wardrobe]: Player " + getName(transferer) + " attempted to retrieve an item that is not a wearable..");
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(item, "wardrobe.ownedBy"))
        {
            if (getObjIdObjVar(item, "wardrobe.ownedBy") != transferer)
            {
                broadcast(transferer, "You can only retrieve your own wearables from this container.");
                LOG("ethereal", "[Wardrobe]: Player " + getName(transferer) + " attempted to retrieve an item from a wardrobe that was not theirs.");
                return SCRIPT_CONTINUE;
            }
        }
        if (!hasScript(item, "item.special.recolor"))
        {
            attachScript(item, "item.special.recolor");
            LOG("ethereal", "[Wardrobe]: Player " + getName(transferer) + " has retrieved an item without the recolor script " + getName(item) + ". Adding it now.");
        }
        return SCRIPT_CONTINUE;
    }
}
