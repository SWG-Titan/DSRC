// ======================================================================
//
// tos_beacon_salesperson.java
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

import script.library.*;
import script.*;

public class tos_beacon_salesperson extends script.base_script
{
    public static String c_stringFile = "conversation/tos_beacon_salesperson";

// ======================================================================
// Script Constants
// ======================================================================

    public tos_beacon_salesperson()

    {

    }

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean tos_beacon_salesperson_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
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

    public int tos_beacon_salesperson_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Greetings.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Hello there.
        if (response.equals("s_4"))
        {
            //-- [NOTE]
            if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
            {
                //-- NPC: What can I do for you?
                string_id message = new string_id(c_stringFile, "s_5");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: I heard you can sell me a comlink that can aid in getting me back here quickly?
                boolean hasResponse0 = false;
                if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
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

                    utils.setScriptVar(player, "conversation.tos_beacon_salesperson.branchId", 2);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.tos_beacon_salesperson.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    public int tos_beacon_salesperson_handleBranch2(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: What can I do for you?

        //-- [RESPONSE NOTE]
        //-- PLAYER: I heard you can sell me a comlink that can aid in getting me back here quickly?
        if (response.equals("s_6"))
        {
            //-- [NOTE]
            if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
            {
                //-- NPC: Indeed. If you'd like to purchase one, it will cost 50,000 credits.
                string_id message = new string_id(c_stringFile, "s_7");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: That sounds fair. Here you go.
                boolean hasResponse0 = false;
                if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: No way! That is expensive!
                boolean hasResponse1 = false;
                if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_8");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_9");

                    utils.setScriptVar(player, "conversation.tos_beacon_salesperson.branchId", 3);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.tos_beacon_salesperson.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    public int tos_beacon_salesperson_handleBranch3(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        obj_id[] contents = utils.getInventoryAndEquipment(player);
        for (obj_id sample : contents)
        {
            if (hasScript(sample, "content.tos_shuttle_beacon"))
            {
                broadcast(player, "You already possess a shuttle beacon to Rally Point Nova!");
                chat.chat(npc, "Sorry, I actually can't help you!");
                npcEndConversation(player);
                return SCRIPT_CONTINUE;
            }
        }
        //-- [BRANCH NOTE]
        //-- NPC: Indeed. If you'd like to purchase one, it will cost 150,000 credits.

        //-- [RESPONSE NOTE] player -> pay npc and give item
        //-- PLAYER: That sounds fair. Here you go.
        if (response.equals("s_8"))
        {
            //-- [NOTE]
            if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
            {
                //-- NPC: It's a pleasure doing business with ya.
                string_id message = new string_id(c_stringFile, "s_10");
                utils.removeScriptVar(player, "conversation.tos_beacon_salesperson.branchId");
                purchaseBeacon(npc, player);
                doAnimationAction(npc, "celebrate");
                doAnimationAction(player, "celebrate");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: No way! That is expensive!
        if (response.equals("s_9"))
        {
            //-- [NOTE]
            if (tos_beacon_salesperson_condition__defaultCondition(player, npc))
            {
                //-- NPC: Come back if you change your mind!
                string_id message = new string_id(c_stringFile, "s_11");
                utils.removeScriptVar(player, "conversation.tos_beacon_salesperson.branchId");
                doAnimationAction(npc, "wave_on_dismissing");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

    private int purchaseBeacon(obj_id npc, obj_id player) throws InterruptedException
    {
        if (getTotalMoney(player) < 150000)
        {
            broadcast(player, "You do not have enough credits to purchase this item.");
        }
        else
        {
            String itemCode = "item_content_tos_beacon";
            money.requestPayment(player, npc, 150000, "no_handler", null, false);
            obj_id inventory = utils.getInventoryContainer(player);
            broadcast(player, "You have purchased the Shuttle Beacon for 150,000 credits.");
            static_item.createNewItemFunction(itemCode, inventory);
        }
        return SCRIPT_CONTINUE;
    }

// ----------------------------------------------------------------------

// ======================================================================
// User Script Triggers
// ======================================================================

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if ((!isMob(self)) || (isPlayer(self)))
        {
            detachScript(self, "conversation.tos_beacon_salesperson");
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
        detachScript(self, "conversation.tos_beacon_salesperson");

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
        if (tos_beacon_salesperson_condition__defaultCondition(player, self))
        {
            //-- NPC: Greetings.
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Hello there.
            boolean hasResponse0 = false;
            if (tos_beacon_salesperson_condition__defaultCondition(player, self))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_4");

                utils.setScriptVar(player, "conversation.tos_beacon_salesperson.branchId", 1);

                npcStartConversation(player, self, "tos_beacon_salesperson", message, responses);
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
        if (!conversationId.equals("tos_beacon_salesperson"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.tos_beacon_salesperson.branchId");

        if (branchId == 1 && tos_beacon_salesperson_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 2 && tos_beacon_salesperson_handleBranch2(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 3 && tos_beacon_salesperson_handleBranch3(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.tos_beacon_salesperson.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}