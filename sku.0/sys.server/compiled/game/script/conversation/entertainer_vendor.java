package script.conversation;/*
@Origin: dsrc.script.conversation
@Author: BubbaJoeX
@Purpose: Vendor for Entertainers
@Created: Wednesday, 11/1/2023, at 8:13 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.dictionary;

import script.*;
import script.library.ai_lib;
import script.library.chat;
import script.library.utils;

public class entertainer_vendor extends script.base_script
{
    public static String c_stringFile = "conversation/entertainer_vendor";

    public entertainer_vendor()
    {
    }

    public boolean entertainer_vendor_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

    public void entertainer_vendor_action_showVendorSui(obj_id player, obj_id npc) throws InterruptedException
    {
        dictionary d = new dictionary();
        d.put("player", player);
        messageTo(npc, "showNonClassInventory", d, 0, false);
    }

    public int entertainer_vendor_handleBranch13(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        if (response.equals("s_2"))
        {
            string_id message = new string_id(c_stringFile, "s_4");
            int numberOfResponses = 0;
            boolean hasResponse = false;
            boolean hasResponse0 = false;
            if (entertainer_vendor_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }
            if (hasResponse)
            {
                int responseIndex = 0;
                string_id[] responses = new string_id[numberOfResponses];
                if (hasResponse0)
                {
                    responses[responseIndex++] = new string_id(c_stringFile, "s_8");
                }
                utils.setScriptVar(player, "conversation.entertainer_vendor.branchId", 16);
                npcSpeak(player, message);
                npcSetConversationResponses(player, responses);
            }
            else
            {
                utils.removeScriptVar(player, "conversation.entertainer_vendor.branchId");
                npcEndConversationWithMessage(player, message);
            }
            return SCRIPT_CONTINUE;
        }
        if (response.equals("s_3"))
        {
            if (entertainer_vendor_condition__defaultCondition(player, npc))
            {
                entertainer_vendor_action_showVendorSui(player, npc);
                string_id message = new string_id(c_stringFile, "s_7");
                utils.removeScriptVar(player, "conversation.entertainer_vendor.branchId");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int entertainer_vendor_handleBranch16(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        if (entertainer_vendor_condition__defaultCondition(player, npc))
        {
            doAnimationAction(npc, "shrug_hands");
            string_id message = new string_id(c_stringFile, "s_6");
            utils.removeScriptVar(player, "conversation.entertainer_vendor.branchId");
            npcEndConversationWithMessage(player, message);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if ((!isTangible(self)) || (isPlayer(self)))
        {
            detachScript(self, "conversation.entertainer_vendor");
        }
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "Entertainer Vendor");
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
        detachScript(self, "conversation.entertainer_vendor");
        return SCRIPT_CONTINUE;
    }

    public boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses) throws InterruptedException
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }

    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        if (ai_lib.isInCombat(self) || ai_lib.isInCombat(player))
        {
            return SCRIPT_OVERRIDE;
        }
        string_id message = new string_id(c_stringFile, "s_1");
        int numberOfResponses = 0;
        boolean hasResponse = false;
        boolean hasResponse0 = false;
        if (entertainer_vendor_condition__defaultCondition(player, self))
        {
            ++numberOfResponses;
            hasResponse = true;
            hasResponse0 = true;
        }
        boolean hasResponse1 = false;
        if (entertainer_vendor_condition__defaultCondition(player, self))
        {
            ++numberOfResponses;
            hasResponse = true;
            hasResponse1 = true;
        }
        if (hasResponse)
        {
            int responseIndex = 0;
            string_id[] responses = new string_id[numberOfResponses];
            if (hasResponse0)
            {
                responses[responseIndex++] = new string_id(c_stringFile, "s_2");
            }
            if (hasResponse1)
            {
                responses[responseIndex++] = new string_id(c_stringFile, "s_3");
            }
            utils.setScriptVar(player, "conversation.entertainer_vendor.branchId", 13);
            npcStartConversation(player, self, "entertainer_vendor", message, responses);
        }
        else
        {
            chat.chat(self, player, message);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        if (!conversationId.equals("entertainer_vendor"))
        {
            return SCRIPT_CONTINUE;
        }
        int branchId = utils.getIntScriptVar(player, "conversation.entertainer_vendor.branchId");
        if (branchId == 13 && entertainer_vendor_handleBranch13(player, self, response) == SCRIPT_CONTINUE)
        {
            return SCRIPT_CONTINUE;
        }
        if (branchId == 16 && entertainer_vendor_handleBranch16(player, self, response) == SCRIPT_CONTINUE)
        {
            return SCRIPT_CONTINUE;
        }
        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");
        utils.removeScriptVar(player, "conversation.entertainer_vendor.branchId");
        return SCRIPT_CONTINUE;
    }
}

