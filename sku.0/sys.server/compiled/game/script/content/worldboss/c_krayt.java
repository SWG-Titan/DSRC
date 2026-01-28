package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_darth_rolii
@Author: BubbaJoeX
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    Elder Krayt Dragon Ancient is a powerful world boss that spawns in the Dune Sea on Tatooine.
    Elder Krayt Dragon Ancient has a variety of mechanics that make him a challenging fight.
        1. Elder Krayt Dragon Ancient will speak a spam message.
        2. Elder Krayt Dragon Ancient will poison players.
        3. Elder Krayt Dragon Ancient will rampage players.
        4. Elder Krayt Dragon Ancient will call for assistance from the sands below.
        5. Elder Krayt Dragon Ancient will spew acid.
        6. Elder Krayt Dragon Ancient will stomp the ground.
@Requirements: script.player.player_nb, script.library.nb_player
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;
import script.string_id;

public class c_krayt extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "krayt";
    private static final String BOSS_NAME = "Elder Krayt Dragon Ancient";
    private static final String TITLE_NAME = "Slayer of the Ancients";
    private static final String TITLE_SKILL = "title_world_boss_krayt";
    private static final String ADD_CREATURE = "krayt_dragon_adolescent";

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setDescriptionString(self, "A powerful and ancient Krayt Dragon.");
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_KRAYT);
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        if (isGod(killer))
        {
            return SCRIPT_CONTINUE;
        }
        LOG("ethereal", "[World Boss System]: Krayt has been killed by " + killer);
        titan_player.doWorldBossDeathMsg(self, killer);
        worldboss.markBossInactive(BOSS_KEY);
        LOG("ethereal", "[World Boss System]: System has set the world boss objvar to inactive on Tatooine");
        return SCRIPT_CONTINUE;
    }

    public int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id wpn, int[] damage) throws InterruptedException
    {
        if (attacker == self)
        {
            return SCRIPT_CONTINUE;
        }

        obj_id[] players = getCreaturesInRange(self, titan_player.WORLD_BOSS_CREDIT_RANGE);

        // Phase 1: 90% - Poison
        if (worldboss.shouldTriggerPhase(self, 90, "krayt_poison"))
        {
            showFlyText(self, new string_id("- POISON -"), 5.5f, colors.GREEN);
            for (obj_id target : players)
            {
                buff.applyBuff(target, "poison", 60.0f, 5.0f);
                broadcast(target, "The Elder Krayt Dragon Ancient has hit you with poison!");
                playClientEffectLoc(target, "clienteffect/combat_grenade_poison.cef", getLocation(target), 1.0f);
            }
        }

        // Phase 2: 70% - Rampage
        if (worldboss.shouldTriggerPhase(self, 70, "krayt_rampage"))
        {
            showFlyText(self, new string_id("- RAMPAGE -"), 5.5f, colors.RED);
            for (obj_id target : players)
            {
                int randDamage = rand(1688, 2694);
                buff.applyBuff(target, "poison", 60.0f, 5.0f);
                broadcast(target, "The Elder Krayt Dragon Ancient has gone on a rampage!");
                damage(target, DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, randDamage);
                LOG("ethereal", "[World Boss System]: Krayt has damaged " + getName(target) + " for a total of " + randDamage + " damage.");
            }
        }

        // Phase 3: 50% - Summon adds
        if (worldboss.shouldTriggerPhase(self, 50, "krayt_adds"))
        {
            showFlyText(self, new string_id("- DISTRESS -"), 5.5f, colors.ORANGE);
            worldboss.broadcastToPlayers(players, "The Elder Krayt Dragon Ancient has yelled for assistance from the sands below!");
            titan_player.createCircleSpawn(self, self, ADD_CREATURE, 6, 12);
        }

        // Phase 4: 30% - Acid spit
        if (worldboss.shouldTriggerPhase(self, 30, "krayt_devastation"))
        {
            showFlyText(self, new string_id("- GRGGRGRGRGRLGLGLRGLR -"), 5.5f, colors.GOLD);
            for (obj_id target : players)
            {
                int randDamage = rand(1800, 2000);
                broadcast(target, "The Elder Krayt Dragon Ancient has spewed acid!");
                damage(target, DAMAGE_ELEMENTAL_ACID, HIT_LOCATION_HEAD, randDamage);
                playClientEffectLoc(target, "clienteffect/combat_grenade_poison.cef", getLocation(target), 1.0f);
            }
        }

        // Phase 5: 10% - Stomp
        if (worldboss.shouldTriggerPhase(self, 10, "krayt_stomp"))
        {
            for (obj_id target : players)
            {
                playClientEffectObj(target, "clienteffect/int_camshake_heavy.cef", self, "root");
                playClientEffectObj(target, "clienteffect/int_camshake_medium.cef", self, "root");
                playClientEffectObj(target, "clienteffect/int_camshake_light.cef", self, "root");
                broadcast(target, "The Elder Krayt Dragon Ancient has stomped the ground!");
                sendConsoleCommand("/kneel", target);
                sendConsoleCommand("/prone", target);
            }
            chat.chat(self, "-EAR PIERCING ROAR-");
        }

        // Phase 6: 5% - Final weakening
        if (worldboss.shouldTriggerPhase(self, 5, "krayt_final"))
        {
            worldboss.broadcastToPlayers(players, "The Elder Krayt Dragon Ancient has been weakened!");
            setWeaponAttackSpeed(aiGetPrimaryWeapon(self), 0.1f);
            chat.chat(self, "-YELP-");
        }

        return SCRIPT_CONTINUE;
    }

    private void createStomachContents(obj_id self, obj_id playerInventory, int count) throws InterruptedException
    {
        String JUNK_TABLE = "datatables/crafting/reverse_engineering_junk.iff";
        String column = "note";
        int junkCount = count;
        for (int i = 0; i < junkCount; i++)
        {
            String junk = dataTableGetString(JUNK_TABLE, rand(1, dataTableGetNumRows(JUNK_TABLE)), column);
            obj_id junkItem = static_item.createNewItemFunction(junk, playerInventory);
            if (isIdValid(junkItem))
            {
                if (junk.contains("heroic_") || junk.contains("_heroic_") || junk.contains("meatlump"))
                {
                    --junkCount;
                    continue;
                }
                setCount(junkItem, rand(1, 3));
            }
        }
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id landedDeathBlow = getObjIdObjVar(self, xp.VAR_LANDED_DEATHBLOW);
        obj_id[] players = getPlayerCreaturesInRange(self, titan_player.WORLD_BOSS_CREDIT_RANGE);

        // Process Jedi Elder quest and distribute rewards
        worldboss.processJediElderQuest(players);
        worldboss.distributeRewards(players, BOSS_KEY, TITLE_NAME, TITLE_SKILL);
        worldboss.grantDeathblowBonus(landedDeathBlow, BOSS_NAME);

        // Chance for bunker house drop
        int random = rand(1, 100);
        if (random >= 75)
        {
            obj_id bunkerRewardee = players[rand(0, players.length - 1)];
            static_item.createNewItemFunction("item_content_bunker_house_02", utils.getInventoryContainer(bunkerRewardee));
            broadcast(bunkerRewardee, "You have found an interesting structure deed within this creature's stomach!");
            LOG("ethereal", "[World Boss System]: System has given " + getPlayerFullName(bunkerRewardee) + " a housing deed, selected at random.");
        }

        // Give stomach contents to top damagers
        obj_id[] topDamagers = getObjIdArrayObjVar(self, xp.VAR_TOP_DAMAGERS);
        if (topDamagers != null)
        {
            for (obj_id attacker : topDamagers)
            {
                if (isIdValid(attacker))
                {
                    broadcast(attacker, "You have found some strange items within this creature's stomach!");
                    createStomachContents(self, utils.getInventoryContainer(attacker), 5);
                    showFlyText(self, new string_id("+ REGURGITATION + "), 1.4f, colors.GREENYELLOW);
                    LOG("ethereal", "[World Boss System]: System has given " + getPlayerFullName(attacker) + " stomach contents for being on the attacker list");
                }
            }
        }

        // Pearl drop for death blower
        if (isIdValid(landedDeathBlow))
        {
            int chance = rand(0, 100);
            if (chance >= 75)
            {
                broadcast(landedDeathBlow, "You have found a flawless pearl within this creature's stomach!");
                static_item.createNewItemFunction("item_krayt_pearl_04_20", utils.getInventoryContainer(landedDeathBlow));
                LOG("ethereal", "[World Boss System]: System has given " + getPlayerFullName(landedDeathBlow) + " a flawless pearl for killing the Krayt.");
            }
            else if (chance <= 25)
            {
                broadcast(landedDeathBlow, "You have found a near flawless pearl within this creature's stomach!");
                static_item.createNewItemFunction("item_krayt_pearl_04_19", utils.getInventoryContainer(landedDeathBlow));
                LOG("ethereal", "[World Boss System]: System has given " + getPlayerFullName(landedDeathBlow) + " a near flawless pearl for killing Krayt,");
            }
        }

        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: Krayt has been killed [OnDeath]");
        return SCRIPT_CONTINUE;
    }
}
