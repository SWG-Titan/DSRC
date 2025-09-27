// ======================================================================
//
// jabba_curio_shop.java
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

public class jabba_curio_shop extends script.base_script
{
    public jabba_curio_shop()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/jabba_curio_shop";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean jabba_curio_shop_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

// ======================================================================
// Script Actions
// ======================================================================

    public void jabba_curio_shop_action_showWindow(obj_id player, obj_id npc) throws InterruptedException
    {
        dictionary d = new dictionary();
        d.put("player", player);
        messageTo(npc, "showInventorySUI", d, 0, false);
        return;
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

    int jabba_curio_shop_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Howdy spacer! Care to browse Jabba's vast assortment of trinkets and wares?

        //-- [RESPONSE NOTE]
        //-- PLAYER: I would like to that, yes.
        if (response.equals("s_4"))
        {
            doAnimationAction(player, "nod_head_multiple");

            //-- [NOTE] end
            if (jabba_curio_shop_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "expect_tip");

                jabba_curio_shop_action_showWindow(player, npc);

                //-- NPC: Take your time browsing as
                string_id message = new string_id(c_stringFile, "s_6");
                utils.removeScriptVar(player, "conversation.jabba_curio_shop.branchId");

                npcEndConversationWithMessage(player, message);

                return SCRIPT_CONTINUE;
            }

        }

        //-- [RESPONSE NOTE]
        //-- PLAYER: No, but thank you.
        if (response.equals("s_5"))
        {
            doAnimationAction(player, "wave2");

            //-- [NOTE]
            if (jabba_curio_shop_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "wave2");

                doAnimationAction(player, "wave2");

                //-- NPC: Very well. If you change your mind, come see me!
                string_id message = new string_id(c_stringFile, "s_7");
                utils.removeScriptVar(player, "conversation.jabba_curio_shop.branchId");

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
            detachScript(self, "conversation.jabba_curio_shop");
        }

        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "Avo (a Curio Vendor)");
        setDescriptionString(self, "Jabba's Curio Shopkeeper, Avo, owns a wide variety of trinkets. Some he might be willing to part ways with for the right price.");
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
        detachScript(self, "conversation.jabba_curio_shop");

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

        faceTo(self, player);
        faceTo(player, self);

        if (ai_lib.isInCombat(self) || ai_lib.isInCombat(player))
            return SCRIPT_OVERRIDE;

        //-- [NOTE]
        if (jabba_curio_shop_condition__defaultCondition(player, self))
        {
            doAnimationAction(player, "applause_excited");

            //-- NPC: Howdy spacer! Care to browse Jabba's vast assortment of trinkets and wares?
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: I would like to that, yes.
            boolean hasResponse0 = false;
            if (jabba_curio_shop_condition__defaultCondition(player, self))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }

            //-- PLAYER: No, but thank you.
            boolean hasResponse1 = false;
            if (jabba_curio_shop_condition__defaultCondition(player, self))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_5");

                utils.setScriptVar(player, "conversation.jabba_curio_shop.branchId", 1);

                npcStartConversation(player, self, "jabba_curio_shop", message, responses);
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
        if (!conversationId.equals("jabba_curio_shop"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.jabba_curio_shop.branchId");

        if (branchId == 1 && jabba_curio_shop_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.jabba_curio_shop.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

}