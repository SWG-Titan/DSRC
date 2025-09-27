package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: Extended Downtime Gift -- No longer in use.
@Origin: dsrc.script.developer.bubbajoe.dt_gift
@Requirements: script.developer.bubbajoe.dt_giver
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/


import script.library.static_item;
import script.library.utils;
import script.*;

public class dt_gift extends script.base_script
{
    public static int COOLDOWN_TIME = 7200; // 2 hours
    public static int currentGameTime = getCalendarTime();

    public dt_gift()
    {
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = "effect";
        attribs[idx] = utils.packStringId(new string_id("Remote Tactical Deployment"));
        idx++;
        if (idx >= names.length)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = utils.packStringId(new string_id("Owner"));
        attribs[idx] = "\\#00FF00" + getPlayerName(getObjIdObjVar(self, "owner"));
        idx++;
        if (idx >= names.length)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, "lastUsed"))
        {
            names[idx] = "time_remaining";
            int last_used = getIntObjVar(self, "lastUsed");
            int secRemain = COOLDOWN_TIME - (getGameTime() - last_used);
            attribs[idx] = " " + parseTimeMsg(parseTimeRemaining(secRemain), false);
            idx++;
            if (idx >= names.length)
            {
                return SCRIPT_CONTINUE;
            }
        }
        if (hasObjVar(self, "charges"))
        {
            names[idx] = "charges";
            int charges = getIntObjVar(self, "charges");
            attribs[idx] = " " + charges;
            idx++;
            if (idx >= names.length)
            {
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self)
    {
        setObjVar(self, "charges", 15);
        setName(self, "Remote Tactical Deployment Tool");
        String desc = "This tool allows adventurers to summon tactical enhancements to aid them in combat. It can be used once every 2 hours. It has 15 charges.";
        setDescriptionStringId(self, new string_id(desc));
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Remote Tactical Deployment Tool");
        String desc = "This tool allows adventurers to summon tactical enhancements to aid them in combat. It can be used once every 2 hours. It has 15 charges.";
        setDescriptionStringId(self, new string_id(desc));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Request Enhancements"));
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
            if (!hasObjVar(self, "owner"))
            {
                setObjVar(self, "owner", player);
            }
            int lastUsed = getIntObjVar(self, "used.timestamp");
            if (currentGameTime < (lastUsed + COOLDOWN_TIME) && !isGod(player))
            {
                broadcast(player, "You cannot use this yet.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                obj_id[] players = getAllPlayers(getLocation(player), 124.0f);
                playClientEffectLoc(players, "appearance/rebel_transport_touch_and_go.prt", getLocation(player), 0.9f);
                int charges = getIntObjVar(self, "charges");
                if (charges < 0)
                {
                    broadcast(player, "You have used your last charge.");
                    destroyObject(self);
                }
                else
                {
                    setObjVar(self, "charges", charges - 1);
                }
                String message = "Copy that. Dispensing supplies, " + getFirstName(player) + "!";
                prose_package commP = new prose_package();
                commP.stringId = new string_id(message);
                commPlayer(self, player, commP, "object/mobile/dressed_tatooine_mos_eisley_police_officer.iff");
                location loc = getLocation(player);
                obj_id crate = createObject("object/tangible/container/drum/supply_drop_crate.iff", loc);
                utils.setScriptVar(crate, "supply_drop.crateOwner", player);
                attachScript(crate, "systems.combat.combat_supply_drop_crate");
                obj_id tempStims = static_item.createNewItemFunction("item_off_temp_stimpack_02_06", crate, 5);
                obj_id tempBuff = static_item.createNewItemFunction("item_off_temp_tactical_buff_02_06", crate, 5);
                obj_id tempFood = static_item.createNewItemFunction("item_event_air_cake_01_02", crate, 5);
                obj_id tempDrink = static_item.createNewItemFunction("item_event_energy_drink_01_02", crate, 5);
                obj_id[] items = {
                        tempStims, tempBuff, tempFood, tempDrink
                };
                for (int i = 0; i < items.length; i++)
                {
                    if (isIdValid(items[i]))
                    {
                        setName(items[i], "Tactical Supplies");
                    }
                }
                setObjVar(self, "used.timestamp", currentGameTime);
                System.out.print("Remote Tactical Deployment Tool used by " + player + " at " + loc);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int[] parseTimeRemaining(int seconds) throws InterruptedException
    {
        int[] time =
                {
                        0,
                        0,
                        0
                };
        int minutes = seconds / 60;
        time[2] = seconds % 60;
        if (minutes == 0)
        {
            return time;
        }
        int hours = minutes / 60;
        time[1] = minutes % 60;
        if (hours == 0)
        {
            return time;
        }
        time[0] = hours;
        return time;
    }

    public String parseTimeMsg(int[] time, boolean verbose) throws InterruptedException
    {
        String h = "h";
        String m = "m";
        String s = "s";
        if (verbose)
        {
            if (time[0] == 1)
            {
                h = " hour";
            }
            else
            {
                h = " hours";
            }
            if (time[1] == 1)
            {
                m = " minute";
            }
            else
            {
                m = " minutes";
            }
            if (time[2] == 1)
            {
                s = " second";
            }
            else
            {
                s = " seconds";
            }
        }
        String ret = "";
        if (time[0] > 0)
        {
            ret += time[0] + h + " ";
        }
        if (time[1] > 0)
        {
            ret += time[1] + m + " ";
        }
        if (time[2] > 0)
        {
            ret += time[2] + s;
        }
        return ret;
    }
}
