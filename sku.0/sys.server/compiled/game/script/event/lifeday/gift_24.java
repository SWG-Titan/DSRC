package script.event.lifeday;

/*
@Origin: dsrc.script.event.lifeday
@Author: BubbaJoeX
@Purpose: Christmas Gift (2024 Edition)
@Created: Sunday, 12/6/2023, at 11:49 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.*;

public class gift_24 extends base_script
{
    public static final int GCW_AMOUNT = 3500;
    public static final int MANIPULATE_DISTANCE = 25;
    public static final int TOKEN_AMOUNT = 1; // Total tokens rewarded

    public void reInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "a present");
        setDescriptionString(self, "A special gift from SWG-OR (2024)");
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Unwrap Gift"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (canManipulate(player, self, true, true, MANIPULATE_DISTANCE, true))
            {
                grantStuff(self, player);
            }
            else
            {
                broadcast(player, "You cannot unwrap this gift.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void grantStuff(obj_id self, obj_id player) throws InterruptedException
    {
        for (int i = 0; i < TOKEN_AMOUNT; i++)
        {
            static_item.createNewItemFunction("item_event_token_01_01", player);
        }

        if (factions.isRebel(player) || factions.isImperial(player))
        {
            grantGCW(player);
        }

        broadcast(player, "You have opened your present and received your rewards!");
        destroyObject(self);
    }

    public void grantGCW(obj_id player) throws InterruptedException
    {
        gcw._grantGcwPoints(null, player, GCW_AMOUNT, false, gcw.GCW_POINT_TYPE_GROUND_PVE, "live event");
    }
}
