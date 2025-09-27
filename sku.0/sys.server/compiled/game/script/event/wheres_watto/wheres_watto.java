package script.event.wheres_watto;/*
@Origin: dsrc.script.event.wheres_watto.controller
@Author: BubbaJoeX
@Purpose: Handles the spawning and movement of Watto for the Where's Watto event.
@Notes;
    Once found, Watto will reward the player with a one-time grant and a repeatable reward.
    The one-time grant is a series of 7 items, and the repeatable reward is a series of 56 items.
    The repeatable reward is given to the player and all group members on the same planet.
    The one-time grant is given to the player only.
    Watto will move to a new location after being found, and broadcast his current quadrant.
@TODO:
    Make rewards a datatable to read from.
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.ai.ai;
import script.*;
import script.library.*;

@SuppressWarnings({"unused", "unchecked", "deprecation"})
public class wheres_watto extends base_script
{
    public static String c_stringFile = "conversation/wheres_watto";
    public String[] REPEATABLE_REWARDS = {
            "item_tcg_loot_reward_series9_jedi_library_bookshelf",
            "item_tcg_loot_reward_series9_fish_tank",
            "item_tcg_loot_reward_series8_yodas_soup",
            "item_tcg_loot_reward_series8_yoda_house_deed",
            "item_tcg_loot_reward_series8_yoda_backpack",
            "item_tcg_loot_reward_series8_vader_pod",
            "item_tcg_loot_reward_series8_exogorth_gloves",
            "item_tcg_loot_reward_series8_c3po_backpack",
            "item_tcg_loot_reward_series7_xwing_fighter_familiar",
            "item_tcg_loot_reward_series7_tie_fighter_familiar",
            "item_tcg_loot_reward_series7_hutt_fighter_familiar",
            "item_tcg_loot_reward_series7_deed_vehicle_garage",
            "item_tcg_loot_reward_series7_camo_armor_kit",
            "item_tcg_loot_reward_series7_build04_broken_ball_turret",
            "item_tcg_loot_reward_series7_build03_gunship_blueprint",
            "item_tcg_loot_reward_series7_build02_xwing_wing",
            "item_tcg_loot_reward_series7_build01_tie_canopy",
            "item_tcg_loot_reward_series6_snow_jacket",
            "item_tcg_loot_reward_series6_dewback_armor",
            "item_tcg_loot_reward_series6_deed_rebel_spire",
            "item_tcg_loot_reward_series6_deed_emperor_spire",
            "item_tcg_loot_reward_series6_beast_muzzle",
            "item_tcg_loot_reward_series6_baby_colo_set",
            "item_tcg_loot_reward_series6_auto_feeder",
            "item_tcg_loot_reward_series5_vader_statuette",
            "item_tcg_loot_reward_series5_trench_run_diorama",
            "item_tcg_loot_reward_series5_tiefighter_chair",
            "item_tcg_loot_reward_series5_painting_jedi_techniques",
            "item_tcg_loot_reward_series5_house_sign",
            "item_tcg_loot_reward_series5_galactic_hunters_poster",
            "item_tcg_loot_reward_series5_deathstar_hologram",
            "item_tcg_loot_reward_series5_ceremonial_travel_headdress",
            "item_tcg_loot_reward_series5_air2_swoop_speeder",
            "item_tcg_loot_reward_series4_video_game_table_02_01",
            "item_tcg_loot_reward_series4_tauntaun_ride_02_01",
            "item_tcg_loot_reward_series4_t16_toy_02_01",
            "item_tcg_loot_reward_series4_relaxation_pool_deed_02_01",
            "item_tcg_loot_reward_series4_peko_peko_mount_02_01",
            "item_tcg_loot_reward_series4_chandrilan_dress_02_01",
            "item_tcg_loot_reward_series3_wookiee_ceremonial_pipe",
            "item_tcg_loot_reward_series3_swamp_speeder",
            "item_tcg_loot_reward_series3_sith_meditation_room_deed",
            "item_tcg_loot_reward_series3_merr_sonn_jt12_jetpack_blueprints",
            "item_tcg_loot_reward_series3_mandalorian_skull_banner",
            "item_tcg_loot_reward_series3_koro2_exodrive_airspeeder",
            "item_tcg_loot_reward_series3_jedi_meditation_room_deed",
            "item_tcg_loot_reward_series3_jango_fett_memorial_statue",
            "item_tcg_loot_reward_series3_house_sign",
            "item_tcg_loot_reward_series3_emperor_palpatine_statuette",
            "item_tcg_loot_reward_series3_empal_surecon_center_center_medical_table",
            "item_tcg_loot_reward_series3_boba_fett_statue",
            "item_tcg_loot_reward_series3_armored_bantha",
            "item_tcg_loot_reward_series2_organizational_datapad",
            "item_tcg_loot_reward_series2_mandalorian_strongbox",
            "item_tcg_loot_reward_series2_display_case_02",
            "item_tcg_loot_reward_series2_diner",
            "item_tcg_loot_reward_series2_barn",
            "item_tcg_loot_reward_series1_sith_speeder",
            "item_tcg_loot_reward_series1_painting_jedi_crest",
            "item_tcg_loot_reward_series1_naboo_jacket",
            "item_tcg_loot_reward_series1_housecleaning_kit",
            "item_tcg_loot_reward_series1_display_case_01",
            "item_tcg_loot_reward_series1_black_corset_dress",
            "item_tcg_loot_reward_series1_beru_whitesuns_cookbook",
    };

    public boolean spawnNewWatto = true;

    public boolean wheres_watto_condition__defaultCondition(obj_id player, obj_id npc)
    {
        return true;
    }

    public boolean wheres_watto_condition__canConverse(obj_id player, obj_id npc)
    {
        return true;
    }

    int wheres_watto_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        //-- [RESPONSE NOTE]
        //-- PLAYER: Gotcha!
        if (response.equals("s_4"))
        {
            LOG("ethereal", "[Where's Watto] " + "Watto has been found at : " + getLocation(npc));
            //-- [NOTE] warp the creo to a different spot.
            if (wheres_watto_condition__defaultCondition(player, npc))
            {
                location oldLoc = getLocation(npc);
                string_id message = new string_id(c_stringFile, "s_5");
                utils.removeScriptVar(player, "conversation.wheres_watto.branchId");
                setObjVar(player, "wheres_watto.found", 1);
                location watto_loc = new location(0, 0, 0, getCurrentSceneName(), null);
                watto_loc.x = watto_loc.x + (rand(-7250.0f, 7250.0f));
                watto_loc.z = watto_loc.z + (rand(-7250.0f, 7250.0f));
                watto_loc.y = getHeightAtLocation(watto_loc.x, watto_loc.z);
                createReward(npc, player);

                if (isGod(player))
                {
                    broadcast(player, "God Mode: Not broadcasting Watto missions. This Watto was on " + getCurrentSceneName() + "(" + quadrantName(npc) + ")");
                }
                else
                {
                    sendSystemMessageGalaxyTestingOnly(colors_hex.HEADER + colors_hex.ORANGERED + "[Event] Watto has been found on " + toUpper(getCurrentSceneName(), 0) + " by " + getFirstName(player) + "!");
                }

                if (spawnNewWatto)
                {
                    obj_id newWatto = create.object("object/mobile/watto.iff", watto_loc, true);

                    if (!hasScript(newWatto, "ai.ai"))
                    {
                        attachScript(newWatto, "ai.ai");
                    }
                    ai.wander(newWatto, 64f, 256f);
                    setMovementRun(newWatto);
                    setMovementPercent(newWatto, 2.0f);
                    ai_lib.barkString(npc, "Aye! I'm flying here!");
                    attachScript(newWatto, "event.wheres_watto.wheres_watto");
                    setScale(newWatto, rand(1.5f, 3.5f));
                    persistObject(newWatto);
                    npcEndConversationWithMessage(player, message);
                    npcEndConversation(player);
                    destroyObject(npc);
                }
                return SCRIPT_CONTINUE;
            }
        }

        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if ((!isMob(self)) || (isPlayer(self)))
        {
            detachScript(self, "conversation.wheres_watto");
        }

        setCondition(self, CONDITION_CONVERSABLE);

        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        setName(self, "\\#e07b00Watto\\#.");
        if (!hasObjVar(self, "watto_tag"))
        {
            setObjVar(self, "watto_tag", 1);
        }
        startBark(self);
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

    public int createReward(obj_id self, obj_id player) throws InterruptedException
    {
        if (isGod(player))
        {
            broadcast(player, "debug: running createReward | You have found Watto!");
        }
        LOG("ethereal", "[Where's Watto]: " + getPlayerFullName(player) + " has found Watto at " + getLocation(self).toLogFormat());
        obj_id[] players = getAllPlayers(getLocation(self), 28.0f);
        for (obj_id single : players)
        {
            obj_id pInv = utils.getInventoryContainer(single);
            int rewardIndex = rand(0, REPEATABLE_REWARDS.length);
            static_item.createNewItemFunction(REPEATABLE_REWARDS[rewardIndex], pInv);
            if (isGod(single))
            {
                broadcast(single, "Reward index is " + rewardIndex);
            }
            broadcast(single, "You have been granted a reward for attempting to find Watto!");
            LOG("ethereal", "[Where's Watto]: " + getPlayerFullName(single) + " was rewarded \"" + REPEATABLE_REWARDS[rewardIndex] + "\" for finding Watto at " + getLocation(self).toLogFormat());
        }
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.wheres_watto");
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (isGod(speaker))
        {
            if (text.equals("watto"))
            {
                //@NOTE: This is solely for Event Staff to give hints. Do not abuse this.
                sendSystemMessageGalaxyTestingOnly(colors_hex.HEADER + colors_hex.ROSYBROWN + "[Event] Watto has been spotted on " + toUpper(getCurrentSceneName(), 0) + " (" + toUpper(quadrantName(self)) + ")");
            }
            else if (text.equals("restartWaypointQueue"))
            {
                //@NOTE: Don;'t use. This is  continueBark
                stopListeningToMessage(self, "continueBark");
                listenToMessage(self, "continueToBark");
                messageTo(self, "continueBark", null, 1f, false);
            }
            else if (text.equals("clearFlag"))
            {
                utils.removeScriptVar(self, "discussing.locked");
                utils.removeScriptVar(self, "discussing.target");
            }
        }
        return SCRIPT_CONTINUE;
    }

    boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses)
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }

    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        if (utils.hasScriptVar(self, "discussing.locked"))
        {
            broadcast(player, "Watto is currently speaking to " + utils.getStringScriptVar(self, "discussing.target"));
            return SCRIPT_CONTINUE;
        }
        faceTo(self, player);
        ai_lib.stop(self);
        if (ai_lib.isInCombat(self) || ai_lib.isInCombat(player))
            return SCRIPT_OVERRIDE;

        if (wheres_watto_condition__defaultCondition(player, self))
        {
            //-- NPC: Aye, you found me! I knew I should have hidden better.
            string_id message = new string_id(c_stringFile, "s_3");
            int numberOfResponses = 0;

            boolean hasResponse = false;

            //-- PLAYER: Gotcha!
            boolean hasResponse0 = false;
            if (wheres_watto_condition__defaultCondition(player, self))
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

                utils.setScriptVar(player, "conversation.wheres_watto.branchId", 1);

                npcStartConversation(player, self, "wheres_watto", message, responses);
            }
            else
            {
                chat.chat(self, player, message);
            }
            utils.setScriptVar(self, "discussing.locked", true);
            utils.setScriptVar(self, "discussing.target", getPlayerFullName(player));
            return SCRIPT_CONTINUE;
        }

        //chat.chat(npc, "Error:  All conditions for OnStartNpcConversation were false.");

        return SCRIPT_CONTINUE;
    }

    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        if (!conversationId.equals("wheres_watto"))
            return SCRIPT_CONTINUE;

        int branchId = utils.getIntScriptVar(player, "conversation.wheres_watto.branchId");

        if (branchId == 1 && wheres_watto_handleBranch1(player, self, response) == SCRIPT_CONTINUE)
            return SCRIPT_CONTINUE;

        //chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");
        utils.removeScriptVar(player, "conversation.wheres_watto.branchId");

        return SCRIPT_CONTINUE;
    }

    public void unHideMe(obj_id self)
    {
        hideFromClient(self, false);
    }

    public boolean isJanuary() throws InterruptedException
    {
        java.util.Date date = new java.util.Date();
        int month = date.getMonth();
        return month == 0;
    }

    public boolean isDecember() throws InterruptedException
    {
        java.util.Date date = new java.util.Date();
        int month = date.getMonth();
        return month == 11;
    }

    public String quadrantName(obj_id npc) throws InterruptedException
    {
        location here = getLocation(npc);
        String quadrant = "";
        if (here.x > 0)
        {
            if (here.z > 0)
            {
                quadrant = "NE";
            }
            else
            {
                quadrant = "SE";
            }
        }
        else
        {
            if (here.z > 0)
            {
                quadrant = "NW";
            }
            else
            {
                quadrant = "SW";
            }
        }
        return toUpper(quadrant);
    }

    private void startBark(obj_id self) throws InterruptedException
    {
        sendSystemMessageGalaxyTestingOnly(colors_hex.HEADER + colors_hex.ROSYBROWN + "[Event] Watto has been spotted on " + toUpper(getCurrentSceneName(), 0) + " (" + toUpper(quadrantName(self)) + ")");
        messageTo(self, "continueBark", null, 60f * 5f, false);
    }

    public int continueBark(obj_id self, dictionary params) throws InterruptedException
    {
        float PLANETSIDE = 8192f * 2f;
        location origin = new location(0, 0, 0, "tatooine", null);
        obj_id[] players = getAllPlayers(origin, PLANETSIDE);
        createApproximateWaypointsForAllOnPlanet(players, getLocation(self));
        doRandomFlyText(self);
        playClientEffectObj(self, "clienteffect/entertainer_dazzle_level_3.cef", self, "", null, "halloweenFog");
        messageTo(self, "continueBark", null, 60f * 5f, false);
        return SCRIPT_CONTINUE;
    }

    private void doRandomFlyText(obj_id self)
    {
        String[] FLYTEXT_STRINGS = {
                "I NEED TO GO HOME!",
                "I THINK I AM LOST...",
                "I need a ride!",
                "MY WINGS ARE SORE!"
        };
        showFlyText(self, new string_id(FLYTEXT_STRINGS[rand(0, FLYTEXT_STRINGS.length)]), 1.0f, colors.TAN);
    }

    public int createApproximateWaypointsForAllOnPlanet(obj_id[] playerSource, location waypointLocation)
    {
        waypointLocation.x = waypointLocation.x + (rand(-500, 500));
        waypointLocation.z = waypointLocation.z + (rand(-500, 500));
        for (obj_id player : playerSource)
        {
            obj_id waypoint = createWaypointInDatapad(player, waypointLocation);
            setWaypointName(waypoint, "Watto's Last Location (500m radius)");
            setWaypointVisible(waypoint, true);
            setWaypointActive(waypoint, true);
            setWaypointColor(waypoint, "orange");
            attachScript(waypoint, "event.wheres_watto.waypoint_destroyer");
            LOG("ethereal", "[Where's Watto]: Giving hint waypoint to " + getPlayerFullName(player));
            broadcast(player, "Watto's last approximate location has been placed in your datapad.");
        }
        return SCRIPT_CONTINUE;
    }
}