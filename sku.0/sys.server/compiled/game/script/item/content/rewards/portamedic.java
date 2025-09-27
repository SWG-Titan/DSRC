package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Consumable object to get a non-cooldown full heal.
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

@SuppressWarnings("unused")
public class portamedic extends script.base_script
{
    public static int COOLDOWN_TIME = 6300; // 2 hours
    public static float BUFF_MODIFIER = 75.0f;
    public static float LOW_BUFF_MODIFIER = 5.0f;
    public int currentGameTime = getCalendarTime();

    public portamedic()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "Bacta Canister");
        if (hasScript(self, "item.full_heal_item"))
        {
            detachScript(self, "item.full_heal_item");
        }
        if (hasScript(self, "item.static_item_base"))
        {
            detachScript(self, "item.static_item_base");
        }
        if (hasScript(self, "object.autostack"))
        {
            detachScript(self, "object.autostack");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        int lastUsed = getIntObjVar(self, "used.timestamp");
        names[idx] = "last_used";
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(self, "used.timestamp"));
        idx++;
        names[idx] = "next_use";
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(self, "used.timestamp") + COOLDOWN_TIME);
        idx++;
        String NO = "\\#DD1234" + "No" + "\\#FFFFFF";
        String YES = "\\#32CD32" + "Yes" + "\\#FFFFFF";
        if (currentGameTime < (lastUsed + COOLDOWN_TIME))
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

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Dispense Bacta"));
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
            if (currentGameTime < (lastUsed + COOLDOWN_TIME))
            {
                broadcast(player, "You cannot use this yet.");
                LOG("ethereal", "[Portamedic]: Player " + getName(player) + " attempted to use a Portamedic but it was on cooldown.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                buff.applyBuff((player), "me_buff_health_2", (float) COOLDOWN_TIME, BUFF_MODIFIER);
                buff.applyBuff((player), "me_buff_action_3", (float) COOLDOWN_TIME, BUFF_MODIFIER);
                buff.applyBuff((player), "me_buff_strength_3", (float) COOLDOWN_TIME, BUFF_MODIFIER);
                buff.applyBuff((player), "me_buff_agility_3", (float) COOLDOWN_TIME, BUFF_MODIFIER);
                buff.applyBuff((player), "me_buff_precision_3", (float) COOLDOWN_TIME, BUFF_MODIFIER);
                buff.applyBuff((player), "me_buff_melee_gb_1", (float) COOLDOWN_TIME, LOW_BUFF_MODIFIER);
                buff.applyBuff((player), "me_buff_ranged_gb_1", (float) COOLDOWN_TIME, LOW_BUFF_MODIFIER);
                setObjVar(self, "used.timestamp", currentGameTime);
                LOG("ethereal", "[Portamedic]: Player " + getName(player) + " has used a Portamedic.");
            }
        }
        return SCRIPT_CONTINUE;
    }
}
