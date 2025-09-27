/*
@Filename: script.conversation._convert_no_trade
@Author: BubbaJoeX
@Purpose: This script allows players to talk to an npc to convert their inventory full of no-trade items into tradeable items that have been edited in the code base as such, without retroactive changes.
*/

package script.conversation;

// ======================================================================
// Library Includes
// ======================================================================

import script.library.ai_lib;
import script.library.chat;
import script.library.sui;
import script.library.utils;
import script.*;

public class convert_no_trade extends script.base_script
{
    public static String c_stringFile = "conversation/convert_no_trade";

    public convert_no_trade()
    {

    }

    public boolean convert_no_trade_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if ((!isMob(self)) || (isPlayer(self)))
        {
            detachScript(self, "conversation.convert_no_trade");
        }
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setInvulnerable(self, true);
        setName(self, "a local exporter");
        createTriggerVolume("convert_no_trade", 5, true);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        int menu = menuInfo.addRootMenu(menu_info_types.CONVERSE_START, null);
        menu_info_data menuInfoData = menuInfo.getMenuItemById(menu);
        menuInfoData.setServerNotify(false);
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.convert_no_trade");
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String name, obj_id who) throws InterruptedException
    {
        if (!name.startsWith("convert_no_trade"))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isPlayer(who))
        {
            return SCRIPT_CONTINUE;
        }
        debugConsoleMsg(who, "You have entered the proximity of a local exporter. Speak to them to convert your no-trade items into tradeable items.");

        if (!utils.hasScriptVar(who, "can_convert_no_trade"))
        {
            int TIME_TO_CHECK = 864000; // 10 days to be able to convert no-trade items.
            if (getPlayerBirthDate(who) + TIME_TO_CHECK > getCalendarTime())
            {
                broadcast(who, "You cannot convert No-Trade items, as your character's age is less than 10 days old.");
                return SCRIPT_CONTINUE;
            }
            utils.setScriptVar(who, "can_convert_no_trade", 1);
        }
        return SCRIPT_CONTINUE;
    }

    boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses)
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }

// ----------------------------------------------------------------------

    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        chat.chat(self, "Utinni");
        expelFromTriggerVolume(self, "convert_no_trade", player);
        utils.removeScriptVar(player, "can_convert_no_trade");
        handleNoTradeRemoval(player);
        return SCRIPT_CONTINUE;
    }

    public int handleNoTradeRemoval(obj_id player) throws InterruptedException
    {
        //@TODO: Read table for template lists that we want to make retroactive.
        obj_id inventory = utils.getInventoryContainer(player);
        obj_id[] contents = getContents(inventory);
        for (obj_id content : contents)
        {
            if (hasScript(content, "item.heroic_token_box"))
            {
                broadcast(player, "The Box of Achievements will never be tradable.");
                continue;
            }
            if (hasObjVar(content, "noTrade"))
            {
                LOG("ethereal", "[No Trade Removal]: " + getPlayerFullName(player) + " removed no trade from " + getTemplateName(content));
                removeObjVar(content, "noTrade");
            }
            if (hasObjVar(content, "noTradeShared"))
            {
                LOG("ethereal", "[No Trade Removal]: " + getPlayerFullName(player) + " removed no trade shared from " + getTemplateName(content));
                removeObjVar(content, "noTradeShared");
            }
            if (hasScript(content, "item.special.nomove"))
            {
                LOG("ethereal", "[No Trade Removal]: " + getPlayerFullName(player) + " removed item.special.nomove from " + getTemplateName(content));
                detachScript(content, "item.special.nomove");
            }
        }
        //broadcast(player, "Items flagged as *No Trade* inside your inventory are now be tradeable. NOTE: This will revert upon logout/login. This is only meant to trade items between characters. Players caught selling No-Trade items on the Bazaar or misusing this service will be banned for 1 week.");
        String warning = "Items flagged as *No Trade* inside your inventory are now be tradable. \nNOTE: This will revert upon logout/login. \n\nThis is only meant to trade items between characters until a proper alteration is in place.\n\nAnyone caught selling No-Trade items on the market or misusing this service will be ***banned for one (1) week****.";
        String title = "NOTICE";
        sui.msgbox(player, player, warning, sui.OK_CANCEL_ALL, title, "consent");
        return SCRIPT_CONTINUE;
    }

    public int consent(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        broadcast(player, "You have agreed to the terms and conditions.");
        return SCRIPT_CONTINUE;
    }
}