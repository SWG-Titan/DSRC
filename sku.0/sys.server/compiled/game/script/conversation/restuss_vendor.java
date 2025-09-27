// ======================================================================
//
// restuss_vendor.java
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

public class restuss_vendor extends script.base_script
{
    public restuss_vendor()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/restuss_vendor";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean restuss_vendor_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
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


    public void openVendorWindow(obj_id self, obj_id player)
    {
        dictionary d = new dictionary();
        d.put("player", player);
        messageTo(self, "showInventorySUI", d, 1.5f, false);
    }

    int restuss_vendor_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Greetings, soldier! Are you here to browse the war supply?

        //-- [RESPONSE NOTE]
        //-- PLAYER: Sir, yes sir!
        if (response.equals("s_6"))
        {
            doAnimationAction(player, "salute2");

            //-- [NOTE]
            if (restuss_vendor_condition__defaultCondition(player, npc))
            {
                openVendorWindow(npc, player);
                doAnimationAction(player, "beckon");

                //-- NPC: Let me know if you find something.
                string_id message = new string_id(c_stringFile, "s_8");
                utils.removeScriptVar(player, "conversation.restuss_vendor.branchId");

                npcEndConversationWithMessage(player, message);

                conversation.echoToGroup(player, npc, player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: Not at the moment.
        if (response.equals("s_10"))
        {
            doAnimationAction(player, "shake_head_no");

            //-- [NOTE]
            if (restuss_vendor_condition__defaultCondition(player, npc))
            {
                doAnimationAction(player, "wave2");

                //-- NPC: Farewell!
                string_id message = new string_id(c_stringFile, "s_12");
                utils.removeScriptVar(player, "conversation.restuss_vendor.branchId");

                npcEndConversationWithMessage(player, message);

                conversation.echoToGroup(player, npc, player, message);

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
            detachScript(self, "conversation.restuss_vendor");
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
        detachScript(self, "conversation.restuss_vendor");

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
        if (restuss_vendor_condition__defaultCondition(player, npc))
        {
            doAnimationAction(player, "salute1");

            //-- NPC: Greetings, soldier! Are you here to browse the war supply?
            string_id message = new string_id(c_stringFile, "s_4");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Sir, yes sir!
            boolean hasResponse0 = false;
            if (restuss_vendor_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: Not at the moment.
            boolean hasResponse1 = false;
            if (restuss_vendor_condition__defaultCondition(player, npc))
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

                utils.setScriptVar(player, "conversation.restuss_vendor.branchId", 1);

                npcStartConversation(player, npc, "restuss_vendor", message, responses);
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
        if (!conversationId.equals("restuss_vendor"))
            return SCRIPT_CONTINUE;

        obj_id npc = self;

        int branchId = utils.getIntScriptVar(player, "conversation.restuss_vendor.branchId");

        if (branchId == 1 && restuss_vendor_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.restuss_vendor.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}