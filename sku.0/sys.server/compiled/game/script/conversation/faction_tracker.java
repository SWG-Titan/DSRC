// ======================================================================
//
// faction_tracker.java
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

import static script.library.factions.*;

public class faction_tracker extends script.base_script
{
    public faction_tracker()

    {

    }

// ======================================================================
// Script Constants
// ======================================================================

    public static String c_stringFile = "conversation/faction_tracker";

// ======================================================================
// Script Conditions
// ======================================================================

    public boolean faction_tracker_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
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

    int faction_tracker_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [BRANCH NOTE]
        //-- NPC: Greetings.

        //-- [RESPONSE NOTE]
        //-- PLAYER: I would like to check my current faction standing throughout the galaxy.
        if (response.equals("s_4"))
        {
            doAnimationAction(player, "explain");

            //-- [NOTE] Open faction export
            if (faction_tracker_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "nod");

                //-- NPC: Copy that. Loading it into your datapad now.
                string_id message = new string_id(c_stringFile, "s_5");
                utils.removeScriptVar(player, "conversation.faction_tracker.branchId");
                showFactionStandingTable(npc, player);
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
            detachScript(self, "conversation.faction_tracker");
        }
        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setName(self, "a Factional Record Keeper");
        setDescriptionString(self, "This record keeper will keep track of your factions, statuses and bonuses.");
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
        detachScript(self, "conversation.faction_tracker");

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
        if (faction_tracker_condition__defaultCondition(player, npc))
        {
            doAnimationAction(npc, "wave2");

            doAnimationAction(player, "wave1");

            //-- NPC: Greetings.
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: I would like to check my current faction standing throughout the galaxy.
            boolean hasResponse0 = false;
            if (faction_tracker_condition__defaultCondition(player, npc))
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
                    responses[responseIndex++] = new string_id(c_stringFile, "s_4");

                utils.setScriptVar(player, "conversation.faction_tracker.branchId", 1);

                npcStartConversation(player, npc, "faction_tracker", message, responses);
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
        if (!conversationId.equals("faction_tracker"))
            return SCRIPT_CONTINUE;

        obj_id npc = self;

        int branchId = utils.getIntScriptVar(player, "conversation.faction_tracker.branchId");

        if (branchId == 1 && faction_tracker_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");

        utils.removeScriptVar(player, "conversation.faction_tracker.branchId");

        return SCRIPT_CONTINUE;
    }

// ======================================================================

    public static void showFactionStandingTable(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return;
        }
        mapped_strings factionStandings = factions.getAllFactionStanding(player);
        if (factionStandings.isEmpty())
        {
            return;
        }

        String[] factions = factionStandings.getKeys();
        String[][] tableData = new String[factions.length][6];
        String[] tableTitles = {"Faction", "Current Standing", "Rank", "Vendor Bonus", "Vendor", "Remaining Points To Cap"};
        String[] tableTypes = {"text", "text", "text", "text", "text", "text"};

        for (int i = 0; i < factions.length; i++)
        {
            float factionLitmus = getFactionStanding(player, factions[i]);
            tableData[i][0] = localize(new string_id("faction/faction_names", factions[i]));
            tableData[i][1] = String.valueOf(factionLitmus);
            tableData[i][2] = getFactionStatusRoleplay(player, factions[i]);
            tableData[i][3] = getFactionalBonus(player, factions[i]);
            tableData[i][4] = getFactionalVendors(player, factions[i]);
            float requiredGain = 5000 - factionLitmus;
            tableData[i][5] = String.valueOf(requiredGain);
        }

        String title = "Faction Standings - " + getPlayerFullName(player);
        int pid = sui.tableRowMajor(self, player, sui.OK_CANCEL, title, "onFactionStandingResponse", null, tableTitles, tableTypes, tableData);
        flushSUIPage(pid);
        showSUIPage(pid);
    }


}