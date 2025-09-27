// ======================================================================
//
// splash_painting_vendor.java
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

public class splash_painting_vendor extends script.base_script
{
    public splash_painting_vendor()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/splash_painting_vendor";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean splash_painting_vendor_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
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

    int splash_painting_vendor_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Welcome to Splash's Painting Emporium! Jabba has graciously allowed me to sell artwork here in his Curio Shop. Would you care to browse my artwork?

        //-- [RESPONSE NOTE]
        //-- PLAYER: Yes please!
        if (response.equals("s_6"))
        {
            //-- [NOTE]
            if (splash_painting_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "expect_tip");

                doAnimationAction(player, "nod");

                //-- NPC: Let me know if you see something that catches your eye. Also, check back time-to-time, as I might have new artwork available!
                string_id message = new string_id(c_stringFile, "s_8");
                utils.removeScriptVar(player, "conversation.splash_painting_vendor.branchId");
                //Show inventory
                openVendorWindow(npc, player);
                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Not right now, thanks though.
        if (response.equals("s_10"))
        {
            doAnimationAction(player, "shake_head_no");

            //-- [NOTE]
            if (splash_painting_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "wave1");

                //-- NPC: See you around.
                string_id message = new string_id(c_stringFile, "s_12");
                utils.removeScriptVar(player, "conversation.splash_painting_vendor.branchId");

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
            detachScript(self, "conversation.splash_painting_vendor");
        }

        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "Splash (a Painting Vendor)");
        setDescriptionString(self, "A local artist from Wayfar, Splash has made his mark on Tatooine, so much so that he has been contracted by Jabba to sell his work (for a pretty fee).");
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
        detachScript(self, "conversation.splash_painting_vendor");

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
        faceTo(npc, player);
        faceTo(player, npc);
        if (ai_lib.isInCombat(npc) || ai_lib.isInCombat(player))
            return SCRIPT_OVERRIDE;

        //-- [NOTE]
        if (splash_painting_vendor_condition__defaultCondition(player, npc))
        {
            doAnimationAction(npc, "wave2");

            //-- NPC: Welcome to Splash's Painting Emporium! Jabba has graciously allowed me to sell artwork here in his Curio Shop. Would you care to browse my artwork?
            string_id message = new string_id(c_stringFile, "s_4");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Yes please!
            boolean hasResponse0 = false;
            if (splash_painting_vendor_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: Not right now, thanks though.
            boolean hasResponse1 = false;
            if (splash_painting_vendor_condition__defaultCondition(player, npc))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_6");

                if (hasResponse1)
                    responses[responseIndex++] = new string_id(c_stringFile, "s_10");

                utils.setScriptVar(player, "conversation.splash_painting_vendor.branchId", 1);

                npcStartConversation(player, npc, "splash_painting_vendor", message, responses);
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
        if (!conversationId.equals("splash_painting_vendor"))
            return SCRIPT_CONTINUE;

        obj_id npc = self;

        int branchId = utils.getIntScriptVar(player, "conversation.splash_painting_vendor.branchId");

        if (branchId == 1 && splash_painting_vendor_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.splash_painting_vendor.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}