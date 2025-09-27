package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Non-decremental version of Katara's Toy.
@Note:
    Apply a buff to the player that gives them a 50% chance to receive a bonus item on salvage for 1 hour.
@Created: Friday, 9/22/2023, at 9:24 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class toy extends script.base_script
{
    public static int COOLDOWN_TIME = 14400; // 4 hours

    public toy()
    {
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        int lastUsed = getIntObjVar(self, "used.timestamp");
        names[idx] = utils.packStringId(new string_id("Last used"));
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(self, "used.timestamp"));
        idx++;
        names[idx] = utils.packStringId(new string_id("Next use"));
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(self, "used.timestamp") + 14400);
        idx++;
        String NO = "\\#DD1234" + "No" + "\\#FFFFFF";
        String YES = "\\#32CD32" + "Yes" + "\\#FFFFFF";
        if (getCalendarTime() < (lastUsed + COOLDOWN_TIME))
        {
            names[idx] = "ready";
            attribs[idx] = NO;
            idx++;
        }
        else
        {
            names[idx] = "ready";
            attribs[idx] = YES;
            idx++;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "Katiara's Toy (Enhancement)");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Feel Lucky"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int mi) throws InterruptedException
    {
        if (mi == menu_info_types.ITEM_USE)
        {
            if (getContainedBy(self) != utils.getInventoryContainer(player))
            {
                sendSystemMessage(player, new string_id("spam", "must_be_in_inventory"));
                return SCRIPT_CONTINUE;
            }
            int lastUsed = getIntObjVar(self, "used.timestamp");
            if (getCalendarTime() < (lastUsed + COOLDOWN_TIME))
            {
                broadcast(player, "You cannot use this yet.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                buff.applyBuff(player, "nova_orion_rank6_lucky_salvage", 3600, 50);
                setObjVar(self, "used.timestamp", getCalendarTime());
                LOG("ethereal", "[Katara's Toy]: Player " + getName(player) + " used Katara's Toy and was granted a 50% chance to receive a bonus item on salvage for 1 hour.");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
