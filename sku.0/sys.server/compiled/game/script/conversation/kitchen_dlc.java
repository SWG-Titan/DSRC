// ======================================================================
//
// kitchen_dlc.java
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

public class kitchen_dlc extends script.base_script
{
    public kitchen_dlc()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/kitchen_dlc";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean kitchen_dlc_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
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

    int kitchen_dlc_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Welcome to my appliance emporium! May I interest you in any of my products?

        //-- [RESPONSE NOTE]
        //-- PLAYER: I would like to purchase a sink!
        if (response.equals("s_6"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Wonderful! The sink will cost you 15,000 credits.
                string_id message = new string_id(c_stringFile, "s_8");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Ok! [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: No way!
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_12");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_28");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 2);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: I am thinking a refrigerator.
        if (response.equals("s_16"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: And in what style would you like your refrigerator?
                string_id message = new string_id(c_stringFile, "s_18");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Plain!
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: Elegant!
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse1 = true;
                }

                //-- PLAYER: Cheap!
                boolean hasResponse2 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse2 = true;
                }

                //-- PLAYER: Modern!
                boolean hasResponse3 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse3 = true;
                }

                if (hasResponse)
                {
                    int responseIndex = 0;
                    string_id responses[] = new string_id[numberOfResponses];

                    if (hasResponse0)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_20");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_31");

                    if (hasResponse2)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_41");

                    if (hasResponse3)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_58");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 5);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: I need a set of cabinets.
        if (response.equals("s_70"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Very good choice. They come in sets of three for 5,000 credits. What style?
                string_id message = new string_id(c_stringFile, "s_72");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Technical!
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: Elegant!
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse1 = true;
                }

                //-- PLAYER: Modern!
                boolean hasResponse2 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_74");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_82");

                    if (hasResponse2)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_94");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 18);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: No thanks.
        if (response.equals("s_106"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Come back soon!
                string_id message = new string_id(c_stringFile, "s_108");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch2(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Wonderful! The sink will cost you 15,000 credits.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Ok! [Purchase]
        if (response.equals("s_12"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_13");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");
                purchaseItem(player, "object/tangible/borrie/kitchen/sink_s01.iff", 15000);
                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: No way!
        if (response.equals("s_28"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_44");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

    public int purchaseItem(obj_id player, String template, int cost) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        if (pInv == null)
        {
            return SCRIPT_CONTINUE;
        }
        if (money.hasFunds(player, money.MT_TOTAL, cost))
        {
            money.requestPayment(player, getSelf(), cost, "pass_fail", null, true);
            obj_id item = createObject(template, pInv, "");
            if (isIdValid(item))
            {
                sendSystemMessage(player, "You have purchased " + getName(item) + " for " + cost + " credits.", null);
            }
        }
        else
        {
            sendSystemMessage(player, "You do not have enough credits to purchase this item.", null);
        }
        return SCRIPT_CONTINUE;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch5(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: And in what style would you like your refrigerator?

        //-- [RESPONSE NOTE]
        //-- PLAYER: Plain!
        if (response.equals("s_20"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Ok, that will be 15,000 credits!
                string_id message = new string_id(c_stringFile, "s_22");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Ok! [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: On second thought, no thank you.
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_25");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_27");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 6);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Elegant!
        if (response.equals("s_31"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Very good selection.. That will be 30,000 credits please.
                string_id message = new string_id(c_stringFile, "s_33");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Ok. [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: That's robbery! Goodbye!
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_35");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_38");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 9);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Cheap!
        if (response.equals("s_41"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: A wise choice, given your current status... Anyways, that will be 1,000 credits.
                string_id message = new string_id(c_stringFile, "s_48");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Ok! Here you go [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: Sorry, nevermind.
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_50");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_54");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 12);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Modern!
        if (response.equals("s_58"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: A wise choice indeed! That will be 2,000 credits
                string_id message = new string_id(c_stringFile, "s_60");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Ok! [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: That's too steep! Sorry.
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_62");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_66");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 15);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch6(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Ok, that will be 15,000 credits!

        //-- [RESPONSE NOTE]
        //-- PLAYER: Ok! [Purchase]
        if (response.equals("s_25"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Enjoy!
                string_id message = new string_id(c_stringFile, "s_42");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: On second thought, no thank you.
        if (response.equals("s_27"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_43");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch9(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Very good selection.. That will be 30,000 credits please.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Ok. [Purchase]
        if (response.equals("s_35"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_45");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: That's robbery! Goodbye!
        if (response.equals("s_38"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_46");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch12(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: A wise choice, given your current status... Anyways, that will be 1,000 credits.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Ok! Here you go [Purchase]
        if (response.equals("s_50"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_52");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Sorry, nevermind.
        if (response.equals("s_54"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_56");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch15(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: A wise choice indeed! That will be 2,000 credits

        //-- [RESPONSE NOTE]
        //-- PLAYER: Ok! [Purchase]
        if (response.equals("s_62"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_64");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: That's too steep! Sorry.
        if (response.equals("s_66"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_68");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch18(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Very good choice. They come in sets of three for 5,000 credits. What style?

        //-- [RESPONSE NOTE]
        //-- PLAYER: Technical!
        if (response.equals("s_74"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: Wise choice! That will be 5,000 credits.
                string_id message = new string_id(c_stringFile, "s_76");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Very well. [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_78");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 19);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Elegant!
        if (response.equals("s_82"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: A swell choice! That will be 5,000 credits please.
                string_id message = new string_id(c_stringFile, "s_84");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Okie dokie! [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: Sorry, that is too expensive!
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_86");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_90");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 21);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Modern!
        if (response.equals("s_94"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: A good choice for the regalist. That will be 5,000 credits please.
                string_id message = new string_id(c_stringFile, "s_96");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: Agreeable. [Purchase]
                boolean hasResponse0 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: That's too expensive!
                boolean hasResponse1 = false;
                if (kitchen_dlc_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_98");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_102");

                    utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 24);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch19(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Wise choice! That will be 5,000 credits.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Very well. [Purchase]
        if (response.equals("s_78"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_80");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch21(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: A swell choice! That will be 5,000 credits please.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Okie dokie! [Purchase]
        if (response.equals("s_86"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_88");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Sorry, that is too expensive!
        if (response.equals("s_90"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_92");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int kitchen_dlc_handleBranch24(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: A good choice for the regalist. That will be 5,000 credits please.

        //-- [RESPONSE NOTE]
        //-- PLAYER: Agreeable. [Purchase]
        if (response.equals("s_98"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: There you go!
                string_id message = new string_id(c_stringFile, "s_100");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: That's too expensive!
        if (response.equals("s_102"))
        {
            //-- [NOTE]
            if (kitchen_dlc_condition__defaultCondition(player, npc))
            {
                //-- NPC: If you change your mind, come find me!
                string_id message = new string_id(c_stringFile, "s_104");
                utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

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
            detachScript(self, "conversation.kitchen_dlc");
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
        detachScript(self, "conversation.kitchen_dlc");

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
        if (kitchen_dlc_condition__defaultCondition(player, self))
        {
            //-- NPC: Welcome to my appliance emporium! May I interest you in any of my products?
            string_id message = new string_id(c_stringFile, "s_4");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: I would like to purchase a sink!
            boolean hasResponse0 = false;
            if (kitchen_dlc_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: I am thinking a refrigerator.
            boolean hasResponse1 = false;
            if (kitchen_dlc_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse1 = true;
            }

            //-- PLAYER: I need a set of cabinets.
            boolean hasResponse2 = false;
            if (kitchen_dlc_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse2 = true;
            }

            //-- PLAYER: No thanks.
            boolean hasResponse3 = false;
            if (kitchen_dlc_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse3 = true;
            }

            if (hasResponse)
            {
                int responseIndex = 0;
                string_id responses[] = new string_id[numberOfResponses];

                if (hasResponse0)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_6");

                if (hasResponse1)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_16");

                if (hasResponse2)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_70");

                if (hasResponse3)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_106");

                utils.setScriptVar(player, "conversation.kitchen_dlc.branchId", 1);

                npcStartConversation(player, self, "kitchen_dlc", message, responses);
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
        if (!conversationId.equals("kitchen_dlc"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.kitchen_dlc.branchId");

        if (branchId == 1 && kitchen_dlc_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 2 && kitchen_dlc_handleBranch2(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 5 && kitchen_dlc_handleBranch5(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 6 && kitchen_dlc_handleBranch6(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 9 && kitchen_dlc_handleBranch9(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 12 && kitchen_dlc_handleBranch12(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 15 && kitchen_dlc_handleBranch15(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 18 && kitchen_dlc_handleBranch18(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 19 && kitchen_dlc_handleBranch19(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 21 && kitchen_dlc_handleBranch21(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 24 && kitchen_dlc_handleBranch24(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.kitchen_dlc.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}