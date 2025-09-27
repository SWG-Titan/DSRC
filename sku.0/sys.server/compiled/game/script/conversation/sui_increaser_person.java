// ======================================================================
//
// sui_increaser_person.java
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
import script.library.utils;
import script.*;

public class sui_increaser_person extends base_script
{
    public sui_increaser_person()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/sui_increaser_person";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean sui_increaser_person_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

// ----------------------------------------------------------------------

    public boolean sui_increaser_person_condition_talkedToUIPerson(obj_id player, obj_id npc) throws InterruptedException
    {
        if (!hasObjVar(player, "talkedToUIPerson"))
        {
            return true;
        }
        return false;
    }

// ======================================================================
// Script Actions
// ======================================================================

    public void sui_increaser_person_action_enableLargeFont(obj_id player, obj_id npc) throws InterruptedException
    {
        setObjVar(player, "ui.useLargeFont", true);
        setObjVar(player, "talkedToUIPerson", true);
    }

// ----------------------------------------------------------------------

    public void sui_increaser_person_action_disableLargeFont(obj_id player, obj_id npc) throws InterruptedException
    {
        removeObjVar(player, "ui.useLargeFont");
    }

// ----------------------------------------------------------------------

    public void sui_increaser_person_action_notify(obj_id player, obj_id npc) throws InterruptedException
    {
        broadcast(player, "The next time you are prompted with a scripted UI, it will attempt to use a larger font face.");
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

    int sui_increaser_person_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Hey there, friend. I couldn't help but notice you're new in town. Welcome! What brings you to this part of the galaxy?

        //-- [RESPONSE NOTE]
        //-- PLAYER: Thanks for the warm welcome! I just traveled here for a new gig. I'm still getting used to the place, but I'm excited to explore. By the way, do you know how to make the text fields on my datapad bigger? I can't help but squint!
        if (response.equals("s_8"))
        {
            //-- [NOTE]
            if (sui_increaser_person_condition__defaultCondition(player, npc))
            {
                //-- NPC: Sure thing! I let me hack into your datapad...
                string_id message = new string_id(c_stringFile, "s_9");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: <You watch intently>
                boolean hasResponse0 = false;
                if (sui_increaser_person_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_10");

                    utils.setScriptVar(player, "conversation.sui_increaser_person.branchId", 2);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.sui_increaser_person.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int sui_increaser_person_handleBranch2(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Sure thing! I let me hack into your datapad...

        //-- [RESPONSE NOTE]
        //-- PLAYER: <You watch intently>
        if (response.equals("s_10"))
        {
            sui_increaser_person_action_notify(player, npc);

            //-- [NOTE]
            if (sui_increaser_person_condition__defaultCondition(player, npc))
            {
                sui_increaser_person_action_enableLargeFont(player, npc);

                //-- NPC: All done!
                string_id message = new string_id(c_stringFile, "s_11");
                utils.removeScriptVar(player, "conversation.sui_increaser_person.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int sui_increaser_person_handleBranch4(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Hey stranger. Let me guess, wanting that datapad fixed to it's 'factory' state?

        //-- [RESPONSE NOTE]
        //-- PLAYER: You know it.
        if (response.equals("s_14"))
        {
            doAnimationAction(player, "2hot4u");

            sui_increaser_person_action_disableLargeFont(player, npc);

            //-- [NOTE]
            if (sui_increaser_person_condition__defaultCondition(player, npc))
            {
                //-- NPC: Alrighty, here ya go. Cya 'round kid.
                string_id message = new string_id(c_stringFile, "s_18");
                utils.removeScriptVar(player, "conversation.sui_increaser_person.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: On second thought, I am quite fond of my large text.
        if (response.equals("s_17"))
        {
            //-- [NOTE]
            if (sui_increaser_person_condition_talkedToUIPerson(player, npc))
            {
                //-- NPC: Hey there, friend. I couldn't help but notice you're new in town. Welcome! What brings you to this part of the galaxy?
                string_id message = new string_id(c_stringFile, "s_3");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Thanks for the warm welcome! I just traveled here for a new gig. I'm still getting used to the place, but I'm excited to explore. By the way, do you know how to make the text fields on my datapad bigger? I can't help but squint!
                boolean hasResponse0 = false;
                if (sui_increaser_person_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_8");

                    utils.setScriptVar(player, "conversation.sui_increaser_person.branchId", 1);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.sui_increaser_person.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

            //-- [NOTE]
            if (!sui_increaser_person_condition_talkedToUIPerson(player, npc))
            {
                //-- NPC: Hey stranger. Let me guess, wanting that datapad fixed to it's 'factory' state?
                string_id message = new string_id(c_stringFile, "s_13");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: You know it.
                boolean hasResponse0 = false;
                if (sui_increaser_person_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: On second thought, I am quite fond of my large text.
                boolean hasResponse1 = false;
                if (sui_increaser_person_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse1 = true;
                }

                if (hasResponse)
                {
                    int responseIndex = 0;
                    string_id responses[] = new string_id[numberOfResponses];

                    if (hasResponse0)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_14");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_17");

                    utils.setScriptVar(player, "conversation.sui_increaser_person.branchId", 4);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.sui_increaser_person.branchId");

                    npcEndConversationWithMessage(player, message);
                }

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
            detachScript(self, "conversation.sui_increaser_person");
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
        detachScript(self, "conversation.sui_increaser_person");

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
        if (sui_increaser_person_condition_talkedToUIPerson(player, self))
        {
            //-- NPC: Hey there, friend. I couldn't help but notice you're new in town. Welcome! What brings you to this part of the galaxy?
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Thanks for the warm welcome! I just traveled here for a new gig. I'm still getting used to the place, but I'm excited to explore. By the way, do you know how to make the text fields on my datapad bigger? I can't help but squint!
            boolean hasResponse0 = false;
            if (sui_increaser_person_condition__defaultCondition(player, self))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_8");

                utils.setScriptVar(player, "conversation.sui_increaser_person.branchId", 1);

                npcStartConversation(player, self, "sui_increaser_person", message, responses);
            }
            else
            {
                chat.chat(self, player, message);
            }

            return SCRIPT_CONTINUE;
        }

        //-- [NOTE]
        if (!sui_increaser_person_condition_talkedToUIPerson(player, self))
        {
            //-- NPC: Hey stranger. Let me guess, wanting that datapad fixed to it's 'factory' state?
            string_id message = new string_id(c_stringFile, "s_13");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: You know it.
            boolean hasResponse0 = false;
            if (sui_increaser_person_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: On second thought, I am quite fond of my large text.
            boolean hasResponse1 = false;
            if (sui_increaser_person_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse1 = true;
            }

            if (hasResponse)
            {
                int responseIndex = 0;
                string_id responses[] = new string_id[numberOfResponses];

                if (hasResponse0)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_14");

                if (hasResponse1)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_17");

                utils.setScriptVar(player, "conversation.sui_increaser_person.branchId", 4);

                npcStartConversation(player, self, "sui_increaser_person", message, responses);
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
        if (!conversationId.equals("sui_increaser_person"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.sui_increaser_person.branchId");

        if (branchId == 1 && sui_increaser_person_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 2 && sui_increaser_person_handleBranch2(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 4 && sui_increaser_person_handleBranch4(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.sui_increaser_person.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}