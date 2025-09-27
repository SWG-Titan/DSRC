package script.item.container;

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.chat;
import script.obj_id;
import script.string_id;

public class community_dropbox extends script.base_script
{
    public community_dropbox()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "Community Dropbox");
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id srcContainer, obj_id transferer, obj_id item)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToLoseItem(obj_id self, obj_id destContainer, obj_id transferer, obj_id item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnLostItem(obj_id self, obj_id objDestinationContainer, obj_id objTransferer, obj_id objItem) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnGiveItem(obj_id self, obj_id item, obj_id player) throws InterruptedException
    {
        putIn(item, self);
        return SCRIPT_CONTINUE;
    }
}