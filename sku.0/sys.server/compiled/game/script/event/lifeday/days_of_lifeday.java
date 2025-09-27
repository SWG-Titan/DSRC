package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author:  BubbaJoeX
@Purpose: Tweakable script to give an item based on the day. Spawn 17 of these on the station and set each one to a different day leading up to christmas.
@Requirements: 3.1
@Notes: Life Day 2025
@Created: Tuesday, 11/12/2024, at 9:06 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.hue;
import script.library.static_item;
import script.library.sui;
import script.library.utils;
import script.systems.loot.rare_item;

import java.awt.*;
import java.util.Calendar;

import static script.library.utils.packStringId;

public class days_of_lifeday extends base_script
{
    private static final String REWARD_VAR = "lifeday.reward";
    private static final String DAY_VAR = "lifeday.day";
    private static final String CHECK_VAR = "lifeday_2024.";

    public int OnAttach(obj_id self)
    {
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            int main = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reward Menu"));
            mi.addSubMenu(main, menu_info_types.SERVER_MENU2, new string_id("Configure Day #"));
            mi.addSubMenu(main, menu_info_types.SERVER_MENU3, new string_id("Configure Reward"));
            mi.addSubMenu(main, menu_info_types.SERVER_MENU4, new string_id("Information"));
            mi.addSubMenu(main, menu_info_types.SERVER_MENU5, new string_id("Test Reward"));
            mi.addSubMenu(main, menu_info_types.SERVER_MENU6, new string_id("Reset"));
            if (hasObjVar(self, DAY_VAR))
            {
                setName(self, "Advent Calendar: 12/" + getIntObjVar(self, DAY_VAR));
                setDescriptionString(self, "A gift for day #" + getIntObjVar(self, DAY_VAR) + " of Life Day.");
                if (hasObjVar(self, "bubba_bonus"))
                {
                    setName(self, rare_item.applyGradient("??????", new Color(75, 177, 60), new Color(166, 58, 67, 255)));
                    setDescriptionString(self, "Bubba's Special Surprise!");
                }
            }
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Gift"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU2) // Set Day
        {
            sui.inputbox(self, player, "Enter the Life Day date (8-25):", "Days of Life Day - Set Day", "handleSetDayInput", "");
        }
        else if (item == menu_info_types.SERVER_MENU3) // Set Daily Reward
        {
            String title = "Days of Life Day - Set Reward";
            String prompt = "Enter a static item, or a *tangible* .iff filepath to reward the player for the specified date. Additionally, you can award multiple items by separating them with a comma (no spaces).";
            sui.inputbox(self, player, prompt, title, "handleSetRewardInput", getStringObjVar(self, REWARD_VAR));
        }
        else if (item == menu_info_types.SERVER_MENU4)
        {
            String BLOCK = "please see bubba for info on how this works";
            setCondition(self, CONDITION_HOLIDAY_INTERESTING);
            sui.msgbox(player, BLOCK);
        }
        else if (item == menu_info_types.SERVER_MENU5)
        {
            overrideGift(self, player);
        }
        else if (item == menu_info_types.SERVER_MENU6) // Reset
        {
            removeObjVar(self, DAY_VAR);
            removeObjVar(self, REWARD_VAR);
            broadcast(player, "Life Day settings have been reset.");
        }
        else if (item == menu_info_types.ITEM_USE)
        {
            shiftColor(self, player);
            attemptGift(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int overrideGift(obj_id self, obj_id player) throws InterruptedException
    {
        String stationIdVarPrefix = CHECK_VAR + getPlayerStationId(player);
        String reward = getStringObjVar(self, REWARD_VAR);
        if (reward != null && !reward.isEmpty())
        {
            if (reward.contains(","))
            {
                String[] rewards = reward.split(",");
                //give all rewards
                for (String r : rewards)
                {
                    if (r == null || r.isEmpty())
                    {
                        continue;
                    }
                    if (r.endsWith(".iff"))
                    {
                        //use create.object for .iff spawns in the player inventory, otherwise use static_item.createNewItemFunction
                        createObject(r, utils.getInventoryContainer(player), "");
                    }
                    else
                    {
                        static_item.createNewItemFunction(r, utils.getInventoryContainer(player));
                    }
                }
            }
            else
            {
                if (reward.endsWith(".iff"))
                {
                    createObject(reward, utils.getInventoryContainer(player), "");
                }
                else
                {
                    static_item.createNewItemFunction(reward, utils.getInventoryContainer(player));
                }
            }
            broadcast(player, "Check to see if rewards were issued.");
        }
        else
        {
            broadcast(player, "No gift has been set for this day.");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSetDayInput(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }

        String dayString = sui.getInputBoxText(params);
        int day;
        try
        {
            day = Integer.parseInt(dayString);
            if (day < 1 || day > 31) throw new NumberFormatException();
        } catch (NumberFormatException e)
        {
            broadcast(player, "Invalid day. Please enter a number between 8 and 25. (17 days before christmas)");
            return SCRIPT_CONTINUE;
        }

        setObjVar(self, DAY_VAR, day);
        broadcast(player, "Life Day date set to: " + day);
        return SCRIPT_CONTINUE;
    }

    public int handleSetRewardInput(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }

        String reward = sui.getInputBoxText(params);
        if (reward == null || reward.isEmpty())
        {
            broadcast(player, "Invalid gift. Please enter a valid item code.");
            return SCRIPT_CONTINUE;
        }

        setObjVar(self, REWARD_VAR, reward);
        broadcast(player, "Life Day gift set to item code: " + reward);
        return SCRIPT_CONTINUE;
    }

    public void attemptGift(obj_id self, obj_id player) throws InterruptedException
    {
        //We need 5 inventory slots to give the players any available rewards
        if (getVolumeFree(utils.getInventoryContainer(player)) < 5)
        {
            broadcast(player, "You do not have enough inventory space to receive any gifts. Please free up some space and try again.");
            return;
        }
        String stationIdVarPrefix = CHECK_VAR + getPlayerStationId(player);
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int configuredDay = getIntObjVar(self, DAY_VAR);
        if (configuredDay > currentDay)
        {
            broadcast(player, "The Life Day present is not available yet. Check back on the correct date.");
            return;
        }
        if (hasObjVar(self, stationIdVarPrefix))
        {
            broadcast(player, "You have already received Day #" + configuredDay + "!");
            return;
        }
        String reward = getStringObjVar(self, REWARD_VAR);
        if (reward != null && !reward.isEmpty())
        {
            //give all rewards
            if (reward.contains(","))
            {
                String[] rewards = reward.split(",");
                for (String r : rewards)
                {
                    if (r == null || r.isEmpty())
                    {
                        continue;
                    }
                    //use create.object for .iff spawns in the player inventory, otherwise use static_item.createNewItemFunction
                    if (r.endsWith(".iff"))
                    {
                        createObject(r, utils.getInventoryContainer(player), "");
                    }
                    else
                    {
                        static_item.createNewItemFunction(r, utils.getInventoryContainer(player));
                    }
                }
            }
            else
            {
                if (reward.endsWith(".iff"))
                {
                    createObject(reward, utils.getInventoryContainer(player), "");
                }
                else
                {
                    static_item.createNewItemFunction(reward, utils.getInventoryContainer(player));
                }
            }
            setObjVar(self, stationIdVarPrefix, true);
            broadcast(player, "Happy Life Day! Day #" + configuredDay + "'s gift has been added to your inventory.");
        }
        else
        {
            broadcast(player, "No gift has been set for this day.");
        }
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }

        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }

        addOpenDateAttribute(self, names, attribs, idx);
        addRewardAttribute(self, player, names, attribs, idx);

        return SCRIPT_CONTINUE;
    }

    private void addOpenDateAttribute(obj_id self, String[] names, String[] attribs, int idx)
    {
        if (hasObjVar(self, DAY_VAR))
        {
            names[idx] = packStringId(new string_id("Open Date"));
            attribs[idx] = "12/" + getIntObjVar(self, DAY_VAR);
            idx++;
        }
    }

    private void addRewardAttribute(obj_id self, obj_id player, String[] names, String[] attribs, int idx) throws InterruptedException
    {
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int configuredDay = hasObjVar(self, DAY_VAR) ? getIntObjVar(self, DAY_VAR) : -1;

        if (hasObjVar(self, REWARD_VAR))
        {
            if (isGod(player))
            {
                names[idx] = packStringId(new string_id("(GM) Reward Code"));
                attribs[idx] = getStringObjVar(self, REWARD_VAR);
                idx++;
            }
            else if (configuredDay != -1 && configuredDay <= currentDay)
            {
                addRewardName(self, names, attribs, idx);
            }
            else
            {
                names[idx] = packStringId(new string_id("Reward"));
                attribs[idx] = "No peeking!";
                idx++;
            }
        }
    }

    private void addRewardName(obj_id self, String[] names, String[] attribs, int idx) throws InterruptedException
    {
        //we need to check if there is a comma in the reward string if so we need to split it and add each reward to the list
        if (hasObjVar(self, REWARD_VAR) && getStringObjVar(self, REWARD_VAR).contains(","))
        {
            String[] rewards = getStringObjVar(self, REWARD_VAR).split(",");
            for (String reward : rewards)
            {
                if (reward.endsWith(".iff"))
                {
                    continue;
                }
                dictionary staticItem = static_item.getMasterItemDictionary(reward);
                if (staticItem == null)
                {
                    return;
                }
                names[idx] = packStringId(new string_id("Reward"));
                attribs[idx] = staticItem.getString("string_name");
                idx++;
            }
        }
        else
        {
            dictionary staticItem = static_item.getMasterItemDictionary(getStringObjVar(self, REWARD_VAR));
            if (staticItem == null)
            {
                return;
            }

            names[idx] = packStringId(new string_id("Reward"));
            attribs[idx] = staticItem.getString("string_name");
            idx++;
        }
        if (hasObjVar(self, "bubba_bonus"))
        {
            names[idx] = packStringId(new string_id("Reward"));
            attribs[idx] = "BUBBA SURPRISE!";
            idx++;
        }
        else
        {
            dictionary staticItem = static_item.getMasterItemDictionary(getStringObjVar(self, REWARD_VAR));
            if (staticItem == null)
            {
                return;
            }

            names[idx] = packStringId(new string_id("Reward"));
            attribs[idx] = staticItem.getString("string_name");
            idx++;
        }
    }

    public void shiftColor(obj_id self, obj_id player) throws InterruptedException
    {
        hue.setColor(self, hue.INDEX_1, rand(0, 8));
        hue.setColor(self, hue.INDEX_2, rand(0, 8));
    }

}
