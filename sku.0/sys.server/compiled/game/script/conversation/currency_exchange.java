// ======================================================================
//
// currency_exchange.java
// Copyright 2004-2020, Sony Online Entertainment
// All Rights Reserved.
//
// Created with SwgConversationEditor 1.37 - DO NOT EDIT THIS AUTO-GENERATED FILE!
//Roachie says herro UwU
// ======================================================================

package script.conversation;

// ======================================================================
// Library Includes
// ======================================================================

import script.*;
import script.library.ai_lib;
import script.library.chat;
import script.library.utils;

public class currency_exchange extends script.base_script
{
    public static String c_stringFile = "conversation/currency_exchange";

// ======================================================================
// Script Constants
// ======================================================================

    public currency_exchange()

    {

    }


// ======================================================================
// Script Conditions
// ======================================================================

    public boolean currency_exchange_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

// ======================================================================
// Script Actions
// ======================================================================

    public void currency_exchange_action_openCurrencyWindow(obj_id player, obj_id npc) throws InterruptedException
    {
        dictionary d = new dictionary();
        d.put("player", player);
        messageTo(npc, "showInventorySUI", d, 1.2f, false);
    }

// ----------------------------------------------------------------------

    public void currency_exchange_action_endConvo(obj_id player, obj_id npc) throws InterruptedException
    {
        //currency_exchange_action_endConvo
        npcEndConversation(player);
    }

// ======================================================================
// Script %TO Tokens
// ======================================================================

// ======================================================================
// Script %DI Tokens
// ======================================================================

// ======================================================================
// Script %DF Tokens
// ======================================================================

// ======================================================================
// handleBranch<n> Functions 
// ======================================================================

    int currency_exchange_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Hey there, looking to exchange some commodities?

        //-- [RESPONSE NOTE]
        //-- PLAYER: Yes please.
        if (response.equals("s_4"))
        {
            //-- [NOTE]
            if (currency_exchange_condition__defaultCondition(player, npc))
            {
                currency_exchange_action_openCurrencyWindow(player, npc);

                //-- NPC: No refunds! -grins-
                string_id message = new string_id(c_stringFile, "s_5");
                utils.removeScriptVar(player, "conversation.currency_exchange.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: I'll keep my goods, thank you.
        if (response.equals("s_6"))
        {
            //-- [NOTE]
            if (currency_exchange_condition__defaultCondition(player, npc))
            {
                currency_exchange_action_endConvo(player, npc);

                //-- NPC: You'll be back! I hope...
                string_id message = new string_id(c_stringFile, "s_7");
                utils.removeScriptVar(player, "conversation.currency_exchange.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

// ======================================================================
// User Script Triggers
// ======================================================================

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if ((!isMob(self)) || (isPlayer(self)))
        {
            detachScript(self, "conversation.currency_exchange");
        }
        setName(self, "Vex Horada");
        setDescriptionString(self, "This trader will allow you to trade various currencies against one another.");
        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "Vex Horada");
        setDescriptionString(self, "This trader will allow you to trade various currencies against one another.");
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
        detachScript(self, "conversation.currency_exchange");

        return SCRIPT_CONTINUE;
    }

// ======================================================================
// Script Triggers
// ======================================================================

    //-- This function should move to base_class.java
    boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses)
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }

// ----------------------------------------------------------------------

    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {

        if (ai_lib.isInCombat(self) || ai_lib.isInCombat(player))
            return SCRIPT_OVERRIDE;

        //-- [NOTE]
        if (currency_exchange_condition__defaultCondition(player, self))
        {
            //-- NPC: Hey there, looking to exchange some commodities?
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Yes please.
            boolean hasResponse0 = false;
            if (currency_exchange_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: I'll keep my goods, thank you.
            boolean hasResponse1 = false;
            if (currency_exchange_condition__defaultCondition(player, self))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_4");

                if (hasResponse1)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_6");

                utils.setScriptVar(player, "conversation.currency_exchange.branchId", 1);

                npcStartConversation(player, self, "currency_exchange", message, responses);
            }
            else
            {
                chat.chat(self, player, message);
            }

            return SCRIPT_CONTINUE;
        }

        chat.chat(self, "Error:  All conditions for OnStartNpcConversation were false.");

        return SCRIPT_CONTINUE;
    }

// ----------------------------------------------------------------------

    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        if (!conversationId.equals("currency_exchange"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.currency_exchange.branchId");

        if (branchId == 1 && currency_exchange_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.currency_exchange.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}