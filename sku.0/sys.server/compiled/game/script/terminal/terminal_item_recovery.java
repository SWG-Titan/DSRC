package script.terminal;/*
@Origin: dsrc.script.DiscordWebhook
@Author: BubbaJoeX
@Purpose: Defunct recovery terminal script.
@Requirements: <no requirements>
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.badge;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class terminal_item_recovery extends script.base_script
{
    public static final String STF = "terminal_ui";
    public static final string_id SID_RECOVER = new string_id("Pickup Parcel");

    public terminal_item_recovery()
    {
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.SERVER_MENU1, SID_RECOVER);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            boolean hasJabba = badge.hasBadge(player, "bdg_jabba"); //@todo find correct badge name & check to make sure players who do have one, don't have another.
            if (hasJabba && (!hasObjVar(player, "recovery.jabba")))
            {
                obj_id recoveredItem = createObject("object/tangible/tcg/series6/decorative_jabba_bed.iff", player, "");
                setObjVar(recoveredItem, "recovery.item", recoveredItem);
                setObjVar(recoveredItem, "recovery.time", getGameTime());
                setObjVar(player, "recovery.jabba", recoveredItem);
                broadcast(player, "You have recovered this parcel."); //@todo dont break the 4th wall. MUH IMMERSION
            }

        }
        return SCRIPT_CONTINUE;
    }
}
