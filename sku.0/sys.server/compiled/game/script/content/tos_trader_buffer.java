package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: give players the event_quality_bonus or event_resource_bonus once per 3 hours
@Requirements: <no requirements>
@Notes: Shares SERVER_MENU's with the script barker_nv, be mindful.
@Created: Thursday, 5/9/2024, at 11:13 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.buff;
import script.library.chat;
import script.library.sui;

public class tos_trader_buffer extends base_script
{
    private static final float DURATION = 5400;

    public String[] QUESTIONS = {
            "I'd like a bonus to my resource gathering skills.",
            "I'd like a bonus to my crafting skills."
    };

    public int OnAttach(obj_id self)
    {
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.CONVERSE_START, new string_id("Seek Knowledge"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.CONVERSE_START)
        {
            chat.chat(self, "Howdy, " + getPlayerFullName(player));
            int currentTime = getGameTime();
            int lastTime = getIntObjVar(player, "trader_buffer.lastTime");
            if (currentTime - lastTime > 5400 && !buff.hasBuff(player, "event_resource_bonus") && !buff.hasBuff(player, "event_quality_bonus"))
            {
                String prompt = "Abubb says: What do you seek, young adventurer?";
                String title = "Trader Enhancement Selection";
                sui.listbox(self, player, prompt, sui.OK_CANCEL, title, QUESTIONS, "handleBuffSelection", true, false);
            }
            else
            {
                broadcast(player, "You have already received a bonus within the last 1.5 hours or currently have a buff and may not receive another.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleBuffSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx == 0)
        {
            buff.applyBuff(player, "event_survey_bonus", DURATION);
        }
        else if (idx == 1)
        {
            buff.removeBuff(player, "event_quality_bonus");
            buff.applyBuff(player, "event_quality_bonus", DURATION);
        }
        chat.chat(self, "Take it easy, " + getPlayerFullName(player) + ", and good luck!");
        setObjVar(player, "trader_buffer.lastTime", getGameTime());
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (isGod(speaker))
        {
            if (text.equals("Tally-ho!"))
            {
                broadcast(speaker, "Removing lockout :P");
                removeObjVar(speaker, "trader_buffer.lastTime");
            }
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
}
