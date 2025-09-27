package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/5/2024, at 5:47 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.static_item;
import script.library.sui;
import script.library.utils;

import java.util.Objects;

public class tcg_voucher_vendor extends base_script
{
    public String[] TCG_CHOICES = {
            "item_tcg_decorational_bespin_house",
            "item_tcg_decorational_yoda_house",
            "item_tcg_loot_reward_series1_bas_relief",
            "item_tcg_loot_reward_series1_beru_whitesuns_cookbook",
            "item_tcg_loot_reward_series1_black_corset_dress",
            "item_tcg_loot_reward_series1_black_flightsuit",
            "item_tcg_loot_reward_series1_display_case_01",
            "item_tcg_loot_reward_series1_dooku_bust",
            "item_tcg_loot_reward_series1_glowing_blue_eyes",
            "item_tcg_loot_reward_series1_glowing_red_eyes",
            "item_tcg_loot_reward_series1_gorax_ear",
            "item_tcg_loot_reward_series1_greeter_bomarr_monk",
            "item_tcg_loot_reward_series1_greeter_meatlump",
            "item_tcg_loot_reward_series1_hans_hydrospanner",
            "item_tcg_loot_reward_series1_housecleaning_kit",
            "item_tcg_loot_reward_series1_indoor_fountain_01",
            "item_tcg_loot_reward_series1_indoor_fountain_02",
            "item_tcg_loot_reward_series1_indoor_garden_01",
            "item_tcg_loot_reward_series1_mechno_chair",
            "item_tcg_loot_reward_series1_morgukai_shadow_scroll",
            "item_tcg_loot_reward_series1_naboo_jacket",
            "item_tcg_loot_reward_series1_nuna_ball_advertisement",
            "item_tcg_loot_reward_series1_orange_flightsuit",
            "item_tcg_loot_reward_series1_organa_speeder",
            "item_tcg_loot_reward_series1_painting_jedi_crest",
            "item_tcg_loot_reward_series1_painting_trooper",
            "item_tcg_loot_reward_series1_podracer_gasgano",
            "item_tcg_loot_reward_series1_radtrooper_badge",
            "item_tcg_loot_reward_series1_sith_speeder",
            "item_tcg_loot_reward_series1_target_creature",
            "item_tcg_loot_reward_series1_tusken_talisman",
            "item_tcg_loot_reward_series1_vendor_ewok",
            "item_tcg_loot_reward_series1_vendor_gungan",
            "item_tcg_loot_reward_series1_vendor_jawa",
            "item_tcg_loot_reward_series2_arc170_flightsuit",
            "item_tcg_loot_reward_series2_barn",
            "item_tcg_loot_reward_series2_chon_bust",
            "item_tcg_loot_reward_series2_computer_console_02",
            "item_tcg_loot_reward_series2_darth_vader_statuette",
            "item_tcg_loot_reward_series2_diner",
            "item_tcg_loot_reward_series2_diner_booth",
            "item_tcg_loot_reward_series2_diner_counter_center",
            "item_tcg_loot_reward_series2_diner_counter_corner",
            "item_tcg_loot_reward_series2_diner_counter_corner_s02",
            "item_tcg_loot_reward_series2_diner_package",
            "item_tcg_loot_reward_series2_diner_table",
            "item_tcg_loot_reward_series2_display_case_02",
            "item_tcg_loot_reward_series2_drink_dispenser",
            "item_tcg_loot_reward_series2_greeter_ewok",
            "item_tcg_loot_reward_series2_greeter_jawa",
            "item_tcg_loot_reward_series2_greeter_serving_droid",
            "item_tcg_loot_reward_series2_indoor_garden_02",
            "item_tcg_loot_reward_series2_keelkana_tooth",
            "item_tcg_loot_reward_series2_mandalorian_strongbox",
            "item_tcg_loot_reward_series2_organizational_datapad",
            "item_tcg_loot_reward_series2_painting_alliance_propaganda",
            "item_tcg_loot_reward_series2_painting_darth_vader",
            "item_tcg_loot_reward_series2_podracer_mawhonic",
            "item_tcg_loot_reward_series2_princess_leia_statuette",
            "item_tcg_loot_reward_series2_sanyassan_skull",
            "item_tcg_loot_reward_series2_v_wing",
            "item_tcg_loot_reward_series2_vendor_bomarr_monk",
            "item_tcg_loot_reward_series2_vendor_meatlump",
            "item_tcg_loot_reward_series2_versafunction88_datapad",
            "item_tcg_loot_reward_series2_video_game_table",
            "item_tcg_loot_reward_series3_armored_bantha",
            "item_tcg_loot_reward_series3_battle_droid_vendor",
            "item_tcg_loot_reward_series3_boba_fett_statue",
            "item_tcg_loot_reward_series3_creature_pet_deed_massiff",
            "item_tcg_loot_reward_series3_darth_vader_obi_wan_diorama",
            "item_tcg_loot_reward_series3_empal_surecon_center_medical_table",
            "item_tcg_loot_reward_series3_emperor_palpatine_statuette",
            "item_tcg_loot_reward_series3_gargans_hands_of_seduction",
            "item_tcg_loot_reward_series3_general_grievous_gutsack",
            "item_tcg_loot_reward_series3_greeter_battle_droid",
            "item_tcg_loot_reward_series3_greeter_toydarian",
            "item_tcg_loot_reward_series3_greeter_tusken",
            "item_tcg_loot_reward_series3_guise_of_the_master",
            "item_tcg_loot_reward_series3_hh_15_torpedo_warhead",
            "item_tcg_loot_reward_series3_hoth_travel_advertisement",
            "item_tcg_loot_reward_series3_house_sign",
            "item_tcg_loot_reward_series3_jango_fett_memorial_statue",
            "item_tcg_loot_reward_series3_jedi_meditation_room_deed",
            "item_tcg_loot_reward_series3_koro2_exodrive_airspeeder",
            "item_tcg_loot_reward_series3_mandalorian_skull_banner",
            "item_tcg_loot_reward_series3_merr_sonn_jt12_jetpack_blueprints",
            "item_tcg_loot_reward_series3_mustafar_travel_advertisement",
            "item_tcg_loot_reward_series3_nightsister_vendor",
            "item_tcg_loot_reward_series3_podracer_longtail",
            "item_tcg_loot_reward_series3_sith_meditation_room_deed",
            "item_tcg_loot_reward_series3_swamp_speeder",
            "item_tcg_loot_reward_series3_swg_tcg_painting",
            "item_tcg_loot_reward_series3_target_dummy",
            "item_tcg_loot_reward_series3_wampa_skin_rug",
            "item_tcg_loot_reward_series3_wookiee_ceremonial_pipe",
            "item_tcg_loot_reward_series4_ball_of_peace_02_01",
            "item_tcg_loot_reward_series4_balta_podracer_02_01",
            "item_tcg_loot_reward_series4_chandrilan_dress_02_01",
            "item_tcg_loot_reward_series4_chewbacca_statuette_02_01",
            "item_tcg_loot_reward_series4_child_bed_02_01",
            "item_tcg_loot_reward_series4_creature_pet_deed_nuna_02_01",
            "item_tcg_loot_reward_series4_droid_oil_bath_02_01",
            "item_tcg_loot_reward_series4_falleens_fist_02_01",
            "item_tcg_loot_reward_series4_generic_rug_02_01",
            "item_tcg_loot_reward_series4_geonosian_speeder_02_01",
            "item_tcg_loot_reward_series4_greeter_nightsister",
            "item_tcg_loot_reward_series4_greeter_palowick",
            "item_tcg_loot_reward_series4_guise_of_vapaad_02_01",
            "item_tcg_loot_reward_series4_han_solo_statuette_02_01",
            "item_tcg_loot_reward_series4_home_itv_02_01",
            "item_tcg_loot_reward_series4_kashyyyk_travel_advertisement_02_01",
            "item_tcg_loot_reward_series4_leia_and_r2_diorama_02_01",
            "item_tcg_loot_reward_series4_location_itv_02_01",
            "item_tcg_loot_reward_series4_medical_table_02_01",
            "item_tcg_loot_reward_series4_peko_peko_mount_02_01",
            "item_tcg_loot_reward_series4_relaxation_pool_deed_02_01",
            "item_tcg_loot_reward_series4_senate_pod_02_01",
            "item_tcg_loot_reward_series4_stuffed_tauntaun_02_01",
            "item_tcg_loot_reward_series4_stuffed_wampa_02_01",
            "item_tcg_loot_reward_series4_t16_toy_02_01",
            "item_tcg_loot_reward_series4_tatooine_travel_advertisement_02_01",
            "item_tcg_loot_reward_series4_tauntaun_ride_02_01",
            "item_tcg_loot_reward_series4_vendor_kitonak_02_01",
            "item_tcg_loot_reward_series4_vendor_toydarian_02_01",
            "item_tcg_loot_reward_series4_video_game_table_02_01",
            "item_tcg_loot_reward_series5_air2_swoop_speeder",
            "item_tcg_loot_reward_series5_armored_varactyl_statue",
            "item_tcg_loot_reward_series5_atat_blueprint",
            "item_tcg_loot_reward_series5_atat_head_itv",
            "item_tcg_loot_reward_series5_atat_statuette",
            "item_tcg_loot_reward_series5_ceremonial_travel_headdress",
            "item_tcg_loot_reward_series5_creature_pet_deed_scurrier_02_01",
            "item_tcg_loot_reward_series5_cybernetic_rots_arm",
            "item_tcg_loot_reward_series5_deathstar_hologram",
            "item_tcg_loot_reward_series5_fg_8t8_podracer",
            "item_tcg_loot_reward_series5_galactic_hunters_poster",
            "item_tcg_loot_reward_series5_greeter_royal_guard",
            "item_tcg_loot_reward_series5_greeter_senate_guard",
            "item_tcg_loot_reward_series5_house_sign",
            "item_tcg_loot_reward_series5_jabbas_roasting_spit",
            "item_tcg_loot_reward_series5_klorri_clan_shield",
            "item_tcg_loot_reward_series5_lcd_screen",
            "item_tcg_loot_reward_series5_mustafar_diorama",
            "item_tcg_loot_reward_series5_nightsister_backpack",
            "item_tcg_loot_reward_series5_painting_jedi_techniques",
            "item_tcg_loot_reward_series5_player_house_atat",
            "item_tcg_loot_reward_series5_player_house_hangar",
            "item_tcg_loot_reward_series5_rain_storm",
            "item_tcg_loot_reward_series5_signal_unit",
            "item_tcg_loot_reward_series5_skywalker_statuette_02_01",
            "item_tcg_loot_reward_series5_theater_poster",
            "item_tcg_loot_reward_series5_tiefighter_chair",
            "item_tcg_loot_reward_series5_title",
            "item_tcg_loot_reward_series5_trench_run_diorama",
            "item_tcg_loot_reward_series5_vader_statuette",
            "item_tcg_loot_reward_series5_xj6_air_speeder",
            "item_tcg_loot_reward_series6_a1_deluxe_floater",
            "item_tcg_loot_reward_series6_auto_feeder",
            "item_tcg_loot_reward_series6_baby_colo_fish",
            "item_tcg_loot_reward_series6_baby_colo_fishtank",
            "item_tcg_loot_reward_series6_baby_colo_set",
            "item_tcg_loot_reward_series6_beast_muzzle",
            "item_tcg_loot_reward_series6_beast_poster_spined_rancor",
            "item_tcg_loot_reward_series6_beast_poster_winged_quenker",
            "item_tcg_loot_reward_series6_build01_hk47_statuette",
            "item_tcg_loot_reward_series6_build02_hk47_mustafar_diorama",
            "item_tcg_loot_reward_series6_build03_battle_droid_statuette",
            "item_tcg_loot_reward_series6_build04_hkdroid_series_poster",
            "item_tcg_loot_reward_series6_buildreward_hk47_jet_pack",
            "item_tcg_loot_reward_series6_cloud_car_itv",
            "item_tcg_loot_reward_series6_cloud_city_hologram",
            "item_tcg_loot_reward_series6_deed_emperor_spire",
            "item_tcg_loot_reward_series6_deed_rebel_spire",
            "item_tcg_loot_reward_series6_dewback_armor",
            "item_tcg_loot_reward_series6_greedo_outfit",
            "item_tcg_loot_reward_series6_gualaar_mount",
            "item_tcg_loot_reward_series6_guise_of_fire",
            "item_tcg_loot_reward_series6_guise_of_ice",
            "item_tcg_loot_reward_series6_han_greedo_diorama",
            "item_tcg_loot_reward_series6_house_lamp",
            "item_tcg_loot_reward_series6_jabba_bed",
            "item_tcg_loot_reward_series6_jedi_council_diorama",
            "item_tcg_loot_reward_series6_lando_statuette",
            "item_tcg_loot_reward_series6_nightsister_painting",
            "item_tcg_loot_reward_series6_ponda_baba_arm",
            "item_tcg_loot_reward_series6_ric_920_speeder",
            "item_tcg_loot_reward_series6_shock_collar",
            "item_tcg_loot_reward_series6_snow_jacket",
            "item_tcg_loot_reward_series6_travel_ad_coruscant",
            "item_tcg_loot_reward_series6_travel_ad_ord_mantell",
            "item_tcg_loot_reward_series7_armored_backpack",
            "item_tcg_loot_reward_series7_atpt_walker",
            "item_tcg_loot_reward_series7_atst_chair",
            "item_tcg_loot_reward_series7_battle_worn_armor_kit",
            "item_tcg_loot_reward_series7_build01_tie_canopy",
            "item_tcg_loot_reward_series7_build02_xwing_wing",
            "item_tcg_loot_reward_series7_build03_gunship_blueprint",
            "item_tcg_loot_reward_series7_build04_broken_ball_turret",
            "item_tcg_loot_reward_series7_build05_eweb_decor",
            "item_tcg_loot_reward_series7_buildreward_republic_gunship",
            "item_tcg_loot_reward_series7_camo_armor_kit",
            "item_tcg_loot_reward_series7_commando_painting",
            "item_tcg_loot_reward_series7_deed_commando_bunker",
            "item_tcg_loot_reward_series7_deed_vehicle_garage",
            "item_tcg_loot_reward_series7_deed_vip_bunker",
            "item_tcg_loot_reward_series7_embroidered_sash",
            "item_tcg_loot_reward_series7_figrin_dan_diorama",
            "item_tcg_loot_reward_series7_gold_cape",
            "item_tcg_loot_reward_series7_handmade_sash",
            "item_tcg_loot_reward_series7_hutt_fighter_familiar",
            "item_tcg_loot_reward_series7_imperial_graffiti_01",
            "item_tcg_loot_reward_series7_imperial_graffiti_02",
            "item_tcg_loot_reward_series7_imperial_graffiti_03",
            "item_tcg_loot_reward_series7_imperial_graffiti_set",
            "item_tcg_loot_reward_series7_lando_poster",
            "item_tcg_loot_reward_series7_max_rebo_diorama",
            "item_tcg_loot_reward_series7_military_transport",
            "item_tcg_loot_reward_series7_purple_cape",
            "item_tcg_loot_reward_series7_rebel_graffiti_01",
            "item_tcg_loot_reward_series7_rebel_graffiti_02",
            "item_tcg_loot_reward_series7_rebel_graffiti_03",
            "item_tcg_loot_reward_series7_rebel_graffiti_set",
            "item_tcg_loot_reward_series7_recon_backpack",
            "item_tcg_loot_reward_series7_rocket_launcher",
            "item_tcg_loot_reward_series7_tie_fighter_familiar",
            "item_tcg_loot_reward_series7_xwing_fighter_familiar",
            "item_tcg_loot_reward_series8_armored_tauntaun_statue",
            "item_tcg_loot_reward_series8_atat_attack",
            "item_tcg_loot_reward_series8_bespin_city",
            "item_tcg_loot_reward_series8_bespin_house_deed",
            "item_tcg_loot_reward_series8_bespin_lamp_off",
            "item_tcg_loot_reward_series8_bespin_lamp_on",
            "item_tcg_loot_reward_series8_bespin_sconce_off",
            "item_tcg_loot_reward_series8_bespin_sconce_on",
            "item_tcg_loot_reward_series8_bespin_shelves",
            "item_tcg_loot_reward_series8_c3po_backpack",
            "item_tcg_loot_reward_series8_cryo_room",
            "item_tcg_loot_reward_series8_dagobah_garden",
            "item_tcg_loot_reward_series8_deco_vader_pod",
            "item_tcg_loot_reward_series8_exogorth_crater",
            "item_tcg_loot_reward_series8_exogorth_gloves",
            "item_tcg_loot_reward_series8_glass_sculpture",
            "item_tcg_loot_reward_series8_glowly_sculpture",
            "item_tcg_loot_reward_series8_lando_cape",
            "item_tcg_loot_reward_series8_painting_hanleia",
            "item_tcg_loot_reward_series8_painting_victory",
            "item_tcg_loot_reward_series8_painting_vintage",
            "item_tcg_loot_reward_series8_painting_yoda",
            "item_tcg_loot_reward_series8_palpatine_hologram",
            "item_tcg_loot_reward_series8_r2d2_dagobah",
            "item_tcg_loot_reward_series8_shield_generator_blueprint",
            "item_tcg_loot_reward_series8_single_pod_airspeeder",
            "item_tcg_loot_reward_series8_slave_1_itv_01",
            "item_tcg_loot_reward_series8_snow_speeder_familiar",
            "item_tcg_loot_reward_series8_torture_table",
            "item_tcg_loot_reward_series8_vader_pod",
            "item_tcg_loot_reward_series8_wampa_arm",
            "item_tcg_loot_reward_series8_yoda_backpack",
            "item_tcg_loot_reward_series8_yoda_house_deed",
            "item_tcg_loot_reward_series8_yoda_xwing",
            "item_tcg_loot_reward_series8_yodas_soup",
            "item_tcg_loot_reward_series9_aoc",
            "item_tcg_loot_reward_series9_computer_console_01",
            "item_tcg_loot_reward_series9_cwm",
            "item_tcg_loot_reward_series9_cws",
            "item_tcg_loot_reward_series9_djj",
            "item_tcg_loot_reward_series9_ds2",
            "item_tcg_loot_reward_series9_ep9",
            "item_tcg_loot_reward_series9_ep9a",
            "item_tcg_loot_reward_series9_ep9b",
            "item_tcg_loot_reward_series9_ep9c",
            "item_tcg_loot_reward_series9_ep9d",
            "item_tcg_loot_reward_series9_ep9e",
            "item_tcg_loot_reward_series9_ew1",
            "item_tcg_loot_reward_series9_ew2",
            "item_tcg_loot_reward_series9_fett",
            "item_tcg_loot_reward_series9_fish_tank",
            "item_tcg_loot_reward_series9_greeter_gungan",
            "item_tcg_loot_reward_series9_jawa",
            "item_tcg_loot_reward_series9_jedi_library_bookshelf",
            "item_tcg_loot_reward_series9_lepese_dictionary",
            "item_tcg_loot_reward_series9_mando",
            "item_tcg_loot_reward_series9_painting_imperial_propaganda",
            "item_tcg_loot_reward_series9_rebels",
            "item_tcg_loot_reward_series9_ro",
            "item_tcg_loot_reward_series9_ros",
            "item_tcg_loot_reward_series9_solo",
            "item_tcg_loot_reward_series9_swr",
            "item_tcg_loot_reward_series9_tfa",
            "item_tcg_loot_reward_series9_tlj",
            "item_tcg_loot_reward_series9_tlj2",
            "item_tcg_loot_reward_series9_tpm",
            "item_tcg_loot_reward_series9_vc",
            "item_tcg_loot_reward_series9_vendor_serving_droid",
            "item_tcg_merr_sonn_jt12_jetpack_deed",
            "item_tcg_nuna_rotten_egg_02_01",
            "item_tcg_scurrier_final_palm_schematic",
            "item_tcg_scurrier_trash_item_02_01",
            "item_tcg_scurrier_trash_item_02_02",
            "item_tcg_scurrier_trash_item_02_03",
            "item_tcg_scurrier_trash_item_02_04",
            "item_tcg_series9_uniform_admiral",
            "item_tcg_series9_uniform_director",
            "item_tcg_series9_uniform_grand_admiral",
            "item_tcg_series9_uniform_grand_general"
    };

    public int OnAttach(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public void setup(obj_id self)
    {
        setName(self, "Event Token");
        setDescriptionString(self, "This token may be used to claim one (1) TCG item of your choice.");
        attachScript(self, "item.special.nomove");
        setObjVar(self, "noTrade", 1);
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        obj_id inventory = utils.getInventoryContainer(player);
        if (!isIdValid(inventory))
        {
            return SCRIPT_CONTINUE;
        }
        if (getContainedBy(self) != inventory)
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Redeem Token"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            redeemVoucher(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    private void redeemVoucher(obj_id self, obj_id player) throws InterruptedException
    {
        String title = "Event Token Redemption";
        String prompt = "Please select the item you would like to redeem.";
        //Convert TCG_CHOICES to a new array that shows teh item's string_name from master_item.tab
        sui.listbox(self, player, prompt, sui.OK_CANCEL, title, buildNameList(player, TCG_CHOICES), "handleSelection", true, false);
        LOG("ethereal", "[Event]: Redeeming voucher.");
    }

    public String[] buildNameList(obj_id who, String[] choices) throws InterruptedException
    {
        String[] names = new String[choices.length];
        for (int i = 0; i < choices.length; i++)
        {
            dictionary itemData = dataTableGetRow("datatables/item/master_item/master_item.iff", choices[i]);
            names[i] = Objects.requireNonNull(itemData).getString("string_name");
        }
        LOG("ethereal", "[Event]: Building name list.");
        return names;
    }

    public int handleSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        LOG("ethereal", "[Event]: Index is " + idx);
        int bp = sui.getIntButtonPressed(params);
        LOG("ethereal", "[Event]: Button pressed is " + bp);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx < 0)
        {
            return SCRIPT_CONTINUE;
        }
        String item = TCG_CHOICES[idx];
        obj_id playerInv = utils.getInventoryContainer(player);
        if (playerInv == null)
        {
            LOG("ethereal", "[Event]: Player inventory is null.");
            return SCRIPT_CONTINUE;
        }
        obj_id reward = static_item.createNewItemFunction(item, player);
        broadcast(player, "You have redeemed your token for " + getNameNoSpam(reward) + "!");
        LOG("ethereal", "[Event]: Created item " + item + " (" + getNameNoSpam(reward) + ") for player " + getPlayerFullName(player));
        if (getCount(self) > 1)
        {
            decrementCount(self);
        }
        else
        {
            destroyObject(self);
        }
        LOG("ethereal", "[Event]: Destroyed voucher.");
        LOG("ethereal", "[Event]: Voucher redeemed successfully for " + getPlayerFullName(player));
        return SCRIPT_CONTINUE;
    }
}
