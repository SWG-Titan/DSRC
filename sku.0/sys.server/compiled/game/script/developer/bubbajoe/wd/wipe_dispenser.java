package script.developer.bubbajoe.wd;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Item Recovery Terminal - Player recovery version extending the blue frog
@Requirements: Updated terminal.terminal_character_builder
@Note: There should only be 1 terminal. Never have more than 1 terminal in the galaxy.
@Created: Monday, 1/1/2024, at 8:44 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.static_item;
import script.library.sui;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;
import script.terminal.terminal_character_builder;

import java.util.ArrayList;

public class wipe_dispenser extends terminal_character_builder
{
    public static final boolean allowExtras = true;

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        if (isGod(player))
        {
            menuInfo.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reset for Self"));
            menuInfo.addRootMenu(menu_info_types.SERVER_MENU2, new string_id("Start Character Builder [GM Only]"));
        }
        menuInfo.addRootMenu(menu_info_types.ITEM_USE, new string_id("Retrieve Equipment"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            removeObjVar(player, "wd.equipment_given");
            setObjVar(self, "wd." + getPlayerAccountUsername(player) + ".count", 0);
            broadcast(player, "reset wb.equipment_given check");
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU2)
        {
            startCharacterBuilder(player);
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (getVolumeFree(utils.getInventoryContainer(player)) <= 35)
            {
                broadcast(player, "You do not have enough inventory space to retrieve all items.");
                return SCRIPT_CONTINUE;
            }
            if (getIntObjVar(self, "wd." + getPlayerAccountUsername(player) + ".count") < 10)
            {
                if (hasObjVar(player, "wd.equipment_given"))
                {
                    broadcast(player, "You have already retrieved all items.");
                    return SCRIPT_CONTINUE;
                }
                utils.setScriptVar(player, "character_builder.armorLevel", 3);//needed to declare the armor level via creation
                int profession = utils.getPlayerProfession(player);
                obj_id pInv = utils.getInventoryContainer(player);
                int species = getSpecies(player);
                if (species == 4) //wookiee
                {
                    if (profession == utils.COMMANDO || profession == utils.BOUNTY_HUNTER)
                    {
                        issueAssaultArmorSet(player, ARMOR_SET_ASSAULT_WOOKIEE);
                    }
                    else if (profession == utils.SPY || profession == utils.OFFICER)
                    {
                        issueBattleArmorSet(player, ARMOR_SET_BATTLE_WOOKIEE);
                    }
                    else if (profession == utils.MEDIC || profession == utils.SMUGGLER)
                    {
                        issueReconArmorSet(player, ARMOR_SET_RECON_WOOKIEE);
                    }
                    else
                    {
                        issueBattleArmorSet(player, ARMOR_SET_BATTLE_WOOKIEE);
                    }
                }
                else if (species == 33) //itho
                {
                    if (profession == utils.COMMANDO || profession == utils.BOUNTY_HUNTER)
                    {
                        issueAssaultArmorSet(player, ARMOR_SET_ASSAULT_ITHORIAN);
                    }
                    else if (profession == utils.SPY || profession == utils.OFFICER)
                    {
                        issueBattleArmorSet(player, ARMOR_SET_BATTLE_ITHORIAN);
                    }
                    else if (profession == utils.MEDIC || profession == utils.SMUGGLER)
                    {
                        issueReconArmorSet(player, ARMOR_SET_RECON_ITHORIAN);
                    }
                    else
                    {
                        issueBattleArmorSet(player, ARMOR_SET_BATTLE_ITHORIAN);
                    }
                }
                else //all other species
                {
                    if (profession == utils.COMMANDO || profession == utils.BOUNTY_HUNTER)
                    {
                        issueAssaultArmorSet(player, ARMOR_SET_ASSAULT_1);
                    }
                    else if (profession == utils.SPY || profession == utils.OFFICER)
                    {
                        issueBattleArmorSet(player, ARMOR_SET_BATTLE_3);
                    }
                    else if (profession == utils.MEDIC || profession == utils.SMUGGLER)
                    {
                        issueReconArmorSet(player, ARMOR_SET_RECON_2);
                    }
                    else
                    {
                        issueBattleArmorSet(player, ARMOR_SET_BATTLE_3);
                    }
                }
                issueWeapons(player);
                if (allowExtras)
                {
                    issueCommods(player);
                    issueLevelItems(player);
                    issueBackpackItem(player);
                    if (profession == utils.TRADER)
                    {
                        issueTraderItems(player);
                    }
                    if (profession == utils.FORCE_SENSITIVE)
                    {
                        sui.msgbox(player, "\\#DD1234\\The force calls out to you.. perhaps it's time to explore?\\#.");
                        //utils.sendMail(new string_id("A disturbance in the force"), new string_id("It's as if hundreds of voices cried out at once. Seek out a Force Shrine to retrieve items for your profession, young padawan, and get ready for the fight to come."), player, "The Force");
                    }
                    issueHousingVoucher(player);
                    issueAceGrant(player);
                }
                if (!hasObjVar(self, "wd." + getPlayerAccountUsername(player) + ".count"))
                {
                    setObjVar(self, "wd." + getPlayerAccountUsername(player) + ".count", 1);
                    setObjVar(player, "wd.equipment_given", 1);
                }
                else
                {
                    setObjVar(self, "wd." + getPlayerAccountUsername(player) + ".count", getIntObjVar(self, "wd." + getPlayerAccountUsername(player) + ".count") + 1);
                    setObjVar(player, "wd.equipment_given", 1);
                }
                broadcast(player, "You have retrieved all items.");
                LOG("ethereal", "[Item Recovery]: " + getPlayerFullName(player) + " has retrieved all items available.");
            }
            else
            {
                debugConsoleMsg(player, "This terminal can only be used 10 times per account, once per character.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void issueAceGrant(obj_id player) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        obj_id deed = createObject("object/tangible/loot/npc_loot/datapad_flashy_generic.iff", pInv, "");
        attachScript(deed, "item.content.rewards.ace_grant");
        setNoTrade(deed);
        obj_id sat = createObject("object/tangible/wearables/backpack/backpack_s03.iff", pInv, "");
        putIn(deed, sat);
        obj_id borstel = makeCraftedItem("object/draft_schematic/space/weapon/wpn_borstel_experimental.iff", 100.0f, sat);
        obj_id borstel1 = makeCraftedItem("object/draft_schematic/space/weapon/wpn_borstel_experimental.iff", 100.0f, sat);
        obj_id borstel2 = makeCraftedItem("object/draft_schematic/space/weapon/wpn_borstel_experimental.iff", 100.0f, sat);
        ArrayList<obj_id> borstelList = new ArrayList<>();
        borstelList.add(borstel);
        borstelList.add(borstel1);
        borstelList.add(borstel2);
        for (obj_id borstelItem : borstelList)
        {
            putIn(borstelItem, sat);
            setNoTrade(borstelItem);
        }
        setName(sat, "Ace Pilot Pack");
        setNoTrade(deed);
    }

    public void issueHousingVoucher(obj_id player) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        obj_id deed = createObject("object/tangible/loot/npc_loot/datapad_flashy_generic.iff", pInv, "");
        setNoTrade(deed);
        attachScript(deed, "developer.bubbajoe.wd.wd_house_picker");
    }

    public void issueLevelItems(obj_id player) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        obj_id satchel = createObject("object/tangible/wearables/backpack/backpack_s03.iff", pInv, "");
        setName(satchel, "Adventurer Pack");
        obj_id speeder = static_item.createNewItemFunction("item_deed_light_bend_barc_06_01", satchel);
        obj_id speeder1 = static_item.createNewItemFunction("item_tcg_loot_reward_series5_air2_swoop_speeder", satchel);
        obj_id level_holo = static_item.createNewItemFunction("item_auto_level_90_buddy_conversion", satchel);
        makeCraftedItem("object/draft_schematic/armor/crafted_armor_recolor_kit.iff", 100.0f, satchel);
        makeCraftedItem("object/draft_schematic/armor/crafted_armor_recolor_kit.iff", 100.0f, satchel);
        setNoTrade(speeder);
        setNoTrade(speeder1);
        setNoTrade(level_holo);
    }

    public void issueBackpackItem(obj_id player) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        int profession = utils.getPlayerProfession(player);
        if (profession == utils.TRADER)
        {
            obj_id traderBag = static_item.createNewItemFunction("item_tcg_loot_reward_series7_embroidered_sash", pInv);
            setNoTrade(traderBag);
        }
        else
        {
            obj_id otherBag = static_item.createNewItemFunction("item_tcg_loot_reward_series7_recon_backpack", pInv);
            setNoTrade(otherBag);
        }
    }

    public void issueWeapons(obj_id player) throws InterruptedException
    {
        int profession = utils.getPlayerProfession(player);
        obj_id pInv = utils.getInventoryContainer(player);
        obj_id satchel = createObject("object/tangible/wearables/backpack/backpack_s03.iff", pInv, "");
        if (profession == utils.FORCE_SENSITIVE)
        {
            makeCraftedItem("object/draft_schematic/weapon/lightsaber_one_handed_gen4_must.iff", 100.0f, satchel);
            int randomIndex = rand(0, 9);
            static_item.createNewItemFunction("item_color_crystal_02_0" + randomIndex, satchel);
            static_item.createNewItemFunction("item_color_crystal_02_0" + randomIndex, satchel);
            static_item.createNewItemFunction("item_color_crystal_04_20" + randomIndex, satchel);
            setName(satchel, "Lightsaber Pack");
        }
        else
        {
            static_item.createNewItemFunction("weapon_stun_baton_legendary", satchel);
            static_item.createNewItemFunction("weapon_rifle_legendary_t21", satchel);
            static_item.createNewItemFunction("weapon_pistol_fwg5_legendary", satchel);
            static_item.createNewItemFunction("weapon_energy_lance_legendary", satchel);
            static_item.createNewItemFunction("weapon_2h_sword_maul_legendary", satchel);
            static_item.createNewItemFunction("weapon_carbine_e5_legendary", satchel);
            setName(satchel, "Weapons Pack");
        }
    }

    public void issueCommods(obj_id player) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        obj_id satchel = createObject("object/tangible/wearables/backpack/backpack_s03.iff", pInv, "");
        static_item.createNewItemFunction("item_heroic_token_black_sun_01_01", satchel, 8);
        static_item.createNewItemFunction("item_heroic_token_exar_01_01", satchel, 8);
        static_item.createNewItemFunction("item_heroic_token_ig88_01_01", satchel, 8);
        static_item.createNewItemFunction("item_heroic_token_tusken_01_01", satchel, 8);
        static_item.createNewItemFunction("item_heroic_token_axkva_01_01", satchel, 8);
        static_item.createNewItemFunction("item_heroic_token_mustafar_01_01", satchel, 20);
        int profession = utils.getPlayerProfession(player);
        if (profession == utils.ENTERTAINER)
        {
            static_item.createNewItemFunction("item_entertainer_token_01_01", satchel, 350);
        }
        setName(satchel, "Token Pack");
    }

    public void issueTraderItems(obj_id player) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        obj_id satchel = createObject("object/tangible/wearables/backpack/backpack_s03.iff", pInv, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/resource.iff", satchel, "");
        createObject("object/tangible/veteran_reward/harvester_elite.iff", satchel, "");
        createObject("object/tangible/veteran_reward/harvester_elite.iff", satchel, "");
        createObject("object/tangible/veteran_reward/harvester_elite.iff", satchel, "");
        setName(satchel, "Trader Pack");
    }

    public void setNoTrade(obj_id item)
    {
        setObjVar(item, "noTrade", 1);
        attachScript(item, "item.special.nomove");
    }
}
