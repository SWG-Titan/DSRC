// ======================================================================
//
// vehicle_mechanic_vendor.java
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

public class vehicle_mechanic_vendor extends script.base_script
{
    public vehicle_mechanic_vendor()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/vehicle_mechanic_vendor";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean vehicle_mechanic_vendor_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
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

    int vehicle_mechanic_vendor_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Vroom vroom, fellow spacer! How can I help you?

        //-- [RESPONSE NOTE]
        //-- PLAYER: I would like to browse your goods.
        if (response.equals("s_4"))
        {
            doAnimationAction(player, "explain");

            //-- [NOTE]
            if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "whisper");

                //-- NPC: Give me a wink if you see something you like.
                string_id message = new string_id(c_stringFile, "s_5");
                utils.removeScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId");
                openVendorWindow(npc, player);
                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: How do I use the Mechanic's Toolkit?
        if (response.equals("s_6"))
        {
            doAnimationAction(player, "implore");

            //-- [NOTE]
            if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "explain");

                doAnimationAction(player, "nod");

                //-- NPC: Get on your vehicle and head to a garage. Then use the toolkit to increase whatever vehicle modifer the toolkit uses.
                string_id message = new string_id(c_stringFile, "s_7");
                int numberOfResponses = 0;

                boolean hasResponse = false;

                //-- PLAYER: What is the modifier limit?
                boolean hasResponse0 = false;
                if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
                {
                    ++numberOfResponses;
                    hasResponse = true;
                    hasResponse0 = true;
                }

                //-- PLAYER: Does this work on Multi-passenger mounts?
                boolean hasResponse1 = false;
                if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
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
                        responses[responseIndex++] = new string_id(c_stringFile, "s_8");

                    if (hasResponse1)
                        responses[responseIndex++] = new string_id(c_stringFile, "s_9");

                    utils.setScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId", 3);

                    npcSpeak(player, message);
                    npcSetConversationResponses(player, responses);
                }
                else
                {
                    utils.removeScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId");

                    npcEndConversationWithMessage(player, message);
                }

                return SCRIPT_CONTINUE;
            }

        }

        return SCRIPT_DEFAULT;
    }

// ----------------------------------------------------------------------

    int vehicle_mechanic_vendor_handleBranch3(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Get on your vehicle and head to a garage. Then use the toolkit to increase whatever vehicle modifer the toolkit uses.

        //-- [RESPONSE NOTE]
        //-- PLAYER: What is the modifier limit?
        if (response.equals("s_8"))
        {
            doAnimationAction(player, "beckon");

            //-- [NOTE]
            if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "explain");

                doAnimationAction(player, "nod");

                //-- NPC: The limit for all increases is 15 from the vehicle's base statistics.
                string_id message = new string_id(c_stringFile, "s_10");
                utils.removeScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Does this work on Multi-passenger mounts?
        if (response.equals("s_9"))
        {
            doAnimationAction(player, "beckon");

            //-- [NOTE]
            if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "hi5_tandem");

                doAnimationAction(player, "hi5_tandem");

                //-- NPC: Yes! Zoom along with your friends!
                string_id message = new string_id(c_stringFile, "s_11");
                utils.removeScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId");

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
            detachScript(self, "conversation.vehicle_mechanic_vendor");
        }

        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "Zesale (a Swoop Racer)");
        setDescriptionString(self, "Renowned for his swift wins, Zesale is a master of navigating on vehicles, and tuning them.");
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
        detachScript(self, "conversation.vehicle_mechanic_vendor");

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
        faceTo(self, player);
        faceTo(player, self);
        if (ai_lib.isInCombat(npc) || ai_lib.isInCombat(player))
            return SCRIPT_OVERRIDE;

        //-- [NOTE]
        if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
        {
            doAnimationAction(player, "2hot4u");

            //-- NPC: Vroom vroom, fellow spacer! How can I help you?
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: I would like to browse your goods.
            boolean hasResponse0 = false;
            if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: How do I use the Mechanic's Toolkit?
            boolean hasResponse1 = false;
            if (vehicle_mechanic_vendor_condition__defaultCondition(player, npc))
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

                utils.setScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId", 1);

                npcStartConversation(player, npc, "vehicle_mechanic_vendor", message, responses);
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
        if (!conversationId.equals("vehicle_mechanic_vendor"))
            return SCRIPT_CONTINUE;

        obj_id npc = self;

        int branchId = utils.getIntScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId");

        if (branchId == 1 && vehicle_mechanic_vendor_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        if (branchId == 3 && vehicle_mechanic_vendor_handleBranch3(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.vehicle_mechanic_vendor.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}