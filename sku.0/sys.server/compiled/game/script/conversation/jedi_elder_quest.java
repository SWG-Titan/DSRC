// ======================================================================
//
// jedi_elder_quest.java
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
import script.library.groundquests;
import script.library.utils;
import script.*;

public class jedi_elder_quest extends script.base_script
{
    public static String c_stringFile = "conversation/jedi_elder_quest";

// ======================================================================
// Script Constants
// ======================================================================

    public jedi_elder_quest()

    {

    }

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean jedi_elder_quest_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

// ----------------------------------------------------------------------

    public boolean jedi_elder_quest_condition_isJedi(obj_id player, obj_id npc) throws InterruptedException
    {
        return utils.getPlayerProfession(player) == utils.FORCE_SENSITIVE;
    }

// ======================================================================
// Script Actions
// ======================================================================

    public void jedi_elder_quest_action_grantElderRobeQuest(obj_id player, obj_id npc) throws InterruptedException
    {
        groundquests.grantQuest(player, "trial_of_the_elder");
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

    int jedi_elder_quest_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Hello there.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Hello, I was told you know of the 'ancient' ways. I am in need of certain attire and was told you could help me?
        if (response.equals("s_4"))
        {
            //-- [NOTE]
            if (jedi_elder_quest_condition__defaultCondition(player, npc))
            {
                //-- NPC: I think I know what you mean, and I can help, however you need to prove yourself.
                string_id message = new string_id(c_stringFile, "s_5");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Sounds great, what do I do?
                boolean hasResponse0 = false;
                if (jedi_elder_quest_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_6");

                    utils.setScriptVar(player, "conversation.jedi_elder_quest.branchId", 2);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.jedi_elder_quest.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Nice to meet you, but I have to run.
        if (response.equals("s_9"))
        {
            //-- [NOTE]
            if (jedi_elder_quest_condition__defaultCondition(player, npc))
            {
                //-- NPC: Farewell traveler!
                string_id message = new string_id(c_stringFile, "s_10");
                utils.removeScriptVar(player, "conversation.jedi_elder_quest.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int jedi_elder_quest_handleBranch2(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: I think I know what you mean, and I can help, however you need to prove yourself.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Sounds great, what do I do?
        if (response.equals("s_6"))
        {
            //-- [NOTE]
            if (jedi_elder_quest_condition__defaultCondition(player, npc))
            {
                //-- NPC: Complete these tasks and then I will have the items sent to you. We cannot be unsafe in these uncivil times. Good luck.
                string_id message = new string_id(c_stringFile, "s_8");
                utils.removeScriptVar(player, "conversation.jedi_elder_quest.branchId");

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
            detachScript(self, "conversation.jedi_elder_quest");
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
        detachScript(self, "conversation.jedi_elder_quest");

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
        if (jedi_elder_quest_condition__defaultCondition(player, self))
        {
            //-- NPC: Hello there.
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Hello, I was told you know of the 'ancient' ways. I am in need of certain attire and was told you could help me?
            boolean hasResponse0 = false;
            if (jedi_elder_quest_condition_isJedi(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: Nice to meet you, but I have to run.
            boolean hasResponse1 = false;
            if (jedi_elder_quest_condition__defaultCondition(player, self))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_9");

                utils.setScriptVar(player, "conversation.jedi_elder_quest.branchId", 1);

                npcStartConversation(player, self, "jedi_elder_quest", message, responses);
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
        if (!conversationId.equals("jedi_elder_quest"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.jedi_elder_quest.branchId");

        if (branchId == 1 && jedi_elder_quest_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 2 && jedi_elder_quest_handleBranch2(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.jedi_elder_quest.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}