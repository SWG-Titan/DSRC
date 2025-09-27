// ======================================================================
//
// refugee_helper.java
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

import script.library.ai_lib;
import script.library.chat;
import script.library.conversation;
import script.library.utils;
import script.*;

public class refugee_helper extends script.base_script
{
    public refugee_helper()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/refugee_helper";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean refugee_helper_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

// ======================================================================
// Script Actions
// ======================================================================

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

    int refugee_helper_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Welcome to Taanab, traveler. You're at the Pendath Refugee Camp. Han Solo dropped you and a few others off here to shake the Imperials. You're safe for now, but you'll need to find your own way from here.

        //-- [RESPONSE NOTE]
        //-- PLAYER: How do I leave?
        if (response.equals("s_4"))
        {
            //-- [NOTE]
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                //-- NPC: Some returning refugees managed to hijack a Lambda shuttle. Currently we make departures to Corellia, Naboo and Tatooine. Walk over to the travel terminal to take off.
                string_id message = new string_id(c_stringFile, "s_5");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Copy that. I'm on my way.
                boolean hasResponse0 = false;
                if (refugee_helper_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                if (hasResponse)
                {
                    int responseIndex = 0;
                    string_id responses[] = new string_id[numberOfResponses];

                    if (hasResponse0)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_6");

                    utils.setScriptVar(player, "conversation.refugee_helper.branchId", 2);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.refugee_helper.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Why couldn't Han Solo just drop me off at Tatooine?
        if (response.equals("s_7"))
        {
            //-- [NOTE] End conversation. :)
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "shrug_shoulders");

                doAnimationAction(player, "beckon");

                //-- NPC: Things sometimes just don't go according to plans. Don't worry, we can still get you to Mos Eisley.
                string_id message = new string_id(c_stringFile, "s_8");
                utils.removeScriptVar(player, "conversation.refugee_helper.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: This place seems familiar...
        if (response.equals("s_10"))
        {
            //-- [NOTE]
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                //-- NPC: Rightfully so! Taanab is quite the hot topic!
                string_id message = new string_id(c_stringFile, "s_11");
                utils.removeScriptVar(player, "conversation.refugee_helper.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int refugee_helper_handleBranch2(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Some returning refugees managed to hijack a Lambda shuttle. Currently we make departures to Corellia, Naboo and Tatooine. Walk over to the travel terminal to take off.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Copy that. I'm on my way.
        if (response.equals("s_6"))
        {
            //-- [NOTE] End conversation. :)
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                //-- NPC: Safe travels!
                string_id message = new string_id(c_stringFile, "s_9");
                utils.removeScriptVar(player, "conversation.refugee_helper.branchId");

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
            detachScript(self, "conversation.refugee_helper");
        }

        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);

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
        detachScript(self, "conversation.refugee_helper");

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
        obj_id npc = self;

        if (ai_lib.isInCombat(npc) || ai_lib.isInCombat(player))
            return SCRIPT_OVERRIDE;

        //-- [NOTE]
        if (refugee_helper_condition__defaultCondition(player, npc))
        {
            //-- NPC: Welcome to Taanab, traveler. You're at the Pendath Refugee Camp. Han Solo dropped you and a few others off here to shake the Imperials. You're safe for now, but you'll need to find your own way from here.
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: How do I leave?
            boolean hasResponse0 = false;
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: Why couldn't Han Solo just drop me off at Tatooine?
            boolean hasResponse1 = false;
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse1 = true;
            }

            //-- PLAYER: This place seems familiar...
            boolean hasResponse2 = false;
            if (refugee_helper_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse2 = true;
            }

            if (hasResponse)
            {
                int responseIndex = 0;
                string_id responses[] = new string_id[numberOfResponses];

                if (hasResponse0)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_4");

                if (hasResponse1)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_7");

                if (hasResponse2)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_10");

                utils.setScriptVar(player, "conversation.refugee_helper.branchId", 1);

                npcStartConversation(player, npc, "refugee_helper", message, responses);
            }
            else
            {
                chat.chat(npc, player, message);
            }

            return SCRIPT_CONTINUE;
        }

        chat.chat(npc, "Error:  All conditions for OnStartNpcConversation were false.");

        return SCRIPT_CONTINUE;
    }

// ----------------------------------------------------------------------

    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        if (!conversationId.equals("refugee_helper"))
            return SCRIPT_CONTINUE;

        obj_id npc = self;

        int branchId = utils.getIntScriptVar(player, "conversation.refugee_helper.branchId");

        if (branchId == 1 && refugee_helper_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 2 && refugee_helper_handleBranch2(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.refugee_helper.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}