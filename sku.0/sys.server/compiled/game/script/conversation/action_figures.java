// ======================================================================
//
// action_figures.java
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

public class action_figures extends script.base_script
{
    public action_figures()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/action_figures";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean action_figures_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

    // ======================================================================
// Script Actions
// ======================================================================
    public void openVendorWindow(obj_id self, obj_id player)
    {
        dictionary d = new dictionary();
        d.put("player", player);
        messageTo(self, "showInventorySUI", d, 1.5f, false);
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

    int action_figures_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Action Figures, come get your action figures! Chuck's Emporium has them all!

        //-- [RESPONSE NOTE]
        //-- PLAYER: I would like to browse your action figure collection.
        if (response.equals("s_4"))
        {
            doAnimationAction(player, "explain");

            //-- [NOTE] run openVendorWindow
            if (action_figures_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "nod");

                doAnimationAction(player, "nod");

                //-- NPC: Very well. I think  you will be most pleased!
                string_id message = new string_id(c_stringFile, "s_5");
                utils.removeScriptVar(player, "conversation.action_figures.branchId");
                openVendorWindow(npc, player);
                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: I have a question regarding these action figures.
        if (response.equals("s_6"))
        {
            doAnimationAction(player, "helpme");

            //-- [NOTE]
            if (action_figures_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "explain");

                //-- NPC: Sure. Ask away my friend!
                string_id message = new string_id(c_stringFile, "s_7");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: How do I clean my action figures?
                boolean hasResponse0 = false;
                if (action_figures_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: How many action figures can I have?
                boolean hasResponse1 = false;
                if (action_figures_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse1 = true;
                }

                //-- PLAYER: What do the action figures do?
                boolean hasResponse2 = false;
                if (action_figures_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_8");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_9");

                    if (hasResponse2)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_10");

                    utils.setScriptVar(player, "conversation.action_figures.branchId", 3);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.action_figures.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int action_figures_handleBranch3(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Sure. Ask away my friend!

        //-- [RESPONSE NOTE]
        //-- PLAYER: How do I clean my action figures?
        if (response.equals("s_8"))
        {
            //-- [NOTE]
            if (action_figures_condition__defaultCondition(player, npc))
            {
                //-- NPC: You can clean your action figure by purchasing the Action Figure Dusting Cloth from me. Over time, playtime causes the figure to accumulate dirt. The cloth will be used whenever the action figure becomes dirty.
                string_id message = new string_id(c_stringFile, "s_11");
                utils.removeScriptVar(player, "conversation.action_figures.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: How many action figures can I have?
        if (response.equals("s_9"))
        {
            //-- [NOTE]
            if (action_figures_condition__defaultCondition(player, npc))
            {
                //-- NPC: You can collect one of each. No more though, as that would be hoarding!
                string_id message = new string_id(c_stringFile, "s_12");
                utils.removeScriptVar(player, "conversation.action_figures.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: What do the action figures do?
        if (response.equals("s_10"))
        {
            //-- [NOTE]
            if (action_figures_condition__defaultCondition(player, npc))
            {
                //-- NPC: Each action figure will grant you a low-value statistic when played with.
                string_id message = new string_id(c_stringFile, "s_13");
                utils.removeScriptVar(player, "conversation.action_figures.branchId");

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
            detachScript(self, "conversation.action_figures");
        }

        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "Chuck (an Action Figure Enthusiast)");
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
        detachScript(self, "conversation.action_figures");

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
        if (action_figures_condition__defaultCondition(player, npc))
        {
            doAnimationAction(npc, "point_to_self");

            //-- NPC: Action Figures, come get your action figures! Chuck's Emporium has them all!
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: I would like to browse your action figure collection.
            boolean hasResponse0 = false;
            if (action_figures_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: I have a question regarding these action figures.
            boolean hasResponse1 = false;
            if (action_figures_condition__defaultCondition(player, npc))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_4");

                if (hasResponse1)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_6");

                utils.setScriptVar(player, "conversation.action_figures.branchId", 1);

                npcStartConversation(player, npc, "action_figures", message, responses);
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
        if (!conversationId.equals("action_figures"))
            return SCRIPT_CONTINUE;

        obj_id npc = self;

        int branchId = utils.getIntScriptVar(player, "conversation.action_figures.branchId");

        if (branchId == 1 && action_figures_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 3 && action_figures_handleBranch3(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.action_figures.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}