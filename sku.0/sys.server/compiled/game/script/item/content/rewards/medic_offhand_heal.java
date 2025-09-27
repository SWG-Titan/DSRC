package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author:  BubbaJoeX
@Purpose: Allows a partial heal on the medic's group members regardless of circumstances
@Requirements: <no requirements>
@Notes: World Boss reward
@Created: Wednesday, 7/31/2024, at 1:59 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.group;
import script.library.utils;

public class medic_offhand_heal extends script.base_script
{
    public static final float TIME_DELAY = 10.0f;
    public static final int REUSE_TIMER = 900;
    public static final float USE_RANGE = 128f;
    public static final String REUSE_VAR = "offhandHeal.lastTime";

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
        if (utils.getPlayerProfession(player) == utils.MEDIC)
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Disperse Bacta"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (canDisperseBacta(player))
            {
                if (group.isGrouped(player))
                {
                    obj_id[] groupMembers = group.getGroupMemberIds(getGroupObject(player));
                    for (obj_id solo : groupMembers)
                    {
                        if (getHealth(solo) == getMaxHealth(player))
                        {
                            continue;
                        }
                        else
                        {
                            if (getDistance(player, solo) > USE_RANGE)
                            {
                                playClientEffectObj(groupMembers, "clienteffect/bacta_bomb.cef", solo, "");
                                setHealth(solo, getMaxHealth(player));
                                LOG("ethereal", "[Bacta Dispersal Tank]: " + getPlayerFullName(solo) + " has been healed by " + getPlayerFullName(solo));
                            }
                            else
                            {
                                broadcast(player, getPlayerFullName(solo) + " was out of range!");
                            }
                        }
                    }
                }
                else
                {
                    if (getHealth(player) == getMaxHealth(player))
                    {
                        broadcast(player, "You have no damage to heal.");
                        return SCRIPT_CONTINUE;
                    }
                    else
                    {
                        playClientEffectObj(player, "clienteffect/bacta_bomb.cef", player, "");
                        setHealth(player, getMaxHealth(player));
                    }
                }
                int time = getCalendarTime();
                LOG("ethereal", "[Bacta Disperal Tank]: " + getPlayerFullName(player) + " has used the tank, setting time used timestamp to " + time + " with a reuse timestamp of " + (time + REUSE_TIMER));
                setObjVar(player, REUSE_VAR, getCalendarTime());
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean canDisperseBacta(obj_id owner) throws InterruptedException
    {
        if (utils.getPlayerProfession(owner) != utils.MEDIC)
        {
            return false;
        }
        if (getCalendarTime() < (getIntObjVar(owner, REUSE_VAR) + REUSE_TIMER))
        {
            int timeLeft = REUSE_TIMER - (getCalendarTime() - getIntObjVar(owner, REUSE_VAR));
            broadcast(owner, "You cannot use this Bacta Tank for another " + timeLeft + " seconds!");
            return false;
        }
        else
        {
            return true;
        }
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        int lastUsed = getIntObjVar(player, REUSE_VAR);
        names[idx] = utils.packStringId(new string_id("Last used"));
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(player, REUSE_VAR));
        idx++;
        names[idx] = utils.packStringId(new string_id("Next use"));
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(player, REUSE_VAR) + REUSE_TIMER);
        idx++;
        String NO = "\\#DD1234" + "No" + "\\#FFFFFF";
        String YES = "\\#32CD32" + "Yes" + "\\#FFFFFF";
        if (getCalendarTime() < (lastUsed + REUSE_TIMER))
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
        names[idx] = utils.packStringId(new string_id("Range"));
        attribs[idx] = "128m";
        idx++;
        names[idx] = utils.packStringId(new string_id("Profession"));
        attribs[idx] = "Medic";
        idx++;
        return SCRIPT_CONTINUE;
    }
}
