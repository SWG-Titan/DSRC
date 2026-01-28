package script.content.worldboss;
/*
@Origin: dsrc.script.content.worldboss.c_dhanak
@Author: AI Generated
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    Dhanak is a ruthless space freighter captain from Kessel, now hiding out on Mustafar.
    After years of smuggling spice and running illegal cargo through the Kessel Run,
    Dhanak has amassed a small army of loyal mercenaries and modified droids.
    Now cornered on the volcanic world of Mustafar, Dhanak fights with desperation
    and cunning, using every dirty trick learned from a lifetime of crime.

    Mechanics:
        1. Dhanak will taunt players with smuggler bravado.
        2. Dhanak will throw thermal detonators, causing area damage.
        3. Dhanak will call in mercenary reinforcements from his crew.
        4. Dhanak will deploy combat droids to defend himself.
        5. Dhanak will use a personal shield generator at low health.
        6. Dhanak will attempt to bribe players, causing confusion.
        7. Dhanak will overload his ship's reactor as a last resort.
@Requirements: script.library.worldboss, script.library.titan_player
@Created: Tuesday, 1/28/2026
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;
import script.string_id;

public class c_dhanak extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "dhanak";
    private static final String BOSS_NAME = "Dhanak the Kessel Runner";
    private static final String TITLE_NAME = "Scourge of the Spice Lanes";
    private static final String TITLE_SKILL = "title_world_boss_dhanak";
    private static final String MERC_ADD = "npc_smuggler_thug";
    private static final String DROID_ADD = "droid_ig_assassin";

    private static final String[] SMUGGLER_TAUNTS = {
            "You think you can take me? I've outrun Imperial blockades!",
            "I made the Kessel Run in record time. You're nothing!",
            "Credits or carbon freeze - your choice!",
            "The Empire couldn't catch me. What chance do you have?",
            "I've got a bad feeling about this... for YOU!",
            "You want some of this? Come get it, sleemo!",
            "I didn't survive Kessel's mines to die here!",
            "My cargo is worth more than your entire life!",
            "Back off or I'll blast you into the next system!",
            "You're making a big mistake, friend.",
    };

    private static final String[] BRIBE_LINES = {
            "Wait! I can pay you! Name your price!",
            "Credits! Lots of credits! Just let me go!",
            "I've got spice! Pure Kessel spice! It's yours!",
            "Don't be a fool - I can make you rich!",
    };

    private static final String[] DESPERATION_LINES = {
            "No no no! This can't be happening!",
            "I won't go back to the mines!",
            "You'll never take me alive!",
            "If I'm going down, I'm taking you with me!",
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setDescriptionStringId(self, string_id.unlocalized("A notorious spice smuggler from Kessel, Dhanak has a massive bounty on his head from both the Empire and the Hutts. Cornered on Mustafar, he fights with the desperation of a man with nothing left to lose."));
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_DHANAK);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: Dhanak has been killed.");
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        if (isGod(killer))
        {
            return SCRIPT_CONTINUE;
        }
        worldboss.markBossInactive(BOSS_KEY);
        titan_player.doWorldBossDeathMsg(self, killer);
        return SCRIPT_CONTINUE;
    }

    public int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id wpn, int[] damage) throws InterruptedException
    {
        if (attacker == self)
        {
            return SCRIPT_CONTINUE;
        }

        obj_id[] players = getPlayerCreaturesInRange(self, worldboss.DEFAULT_COMBAT_RANGE);

        // Initial taunt
        if (worldboss.shouldTriggerPhase(self, 100, "initialTaunt"))
        {
            chat.chat(self, worldboss.getRandomMessage(SMUGGLER_TAUNTS));
        }

        // Phase 1: 85% - First thermal detonator
        if (worldboss.shouldTriggerPhase(self, 85, "firstDetonator"))
        {
            chat.chat(self, "Eat thermal detonator!");
            thermalDetonator(self, players);
            chat.chat(self, worldboss.getRandomMessage(SMUGGLER_TAUNTS));
        }

        // Phase 2: 75% - Call mercenary reinforcements
        if (worldboss.shouldTriggerPhase(self, 75, "mercReinforcements"))
        {
            chat.chat(self, "Boys! Get in here and earn your pay!");
            worldboss.broadcastToPlayers(players, "Dhanak has called in his mercenary crew!");
            worldboss.summonCompanions(self, self, MERC_ADD, 4, 8.0f);
        }

        // Phase 3: 65% - Another taunt
        if (worldboss.shouldTriggerPhase(self, 65, "midTaunt"))
        {
            chat.chat(self, worldboss.getRandomMessage(SMUGGLER_TAUNTS));
        }

        // Phase 4: 55% - Deploy combat droids
        if (worldboss.shouldTriggerPhase(self, 55, "droidDeployment"))
        {
            chat.chat(self, "Droids! Defensive protocol alpha!");
            worldboss.broadcastToPlayers(players, "Dhanak has activated his combat droids!");
            worldboss.summonCompanions(self, self, DROID_ADD, 2, 6.0f);
            showFlyText(self, new string_id("- DROID DEPLOYMENT -"), 2.5f, colors.CYAN);
        }

        // Phase 5: 45% - Bribe attempt (confuses players)
        if (worldboss.shouldTriggerPhase(self, 45, "bribeAttempt"))
        {
            chat.chat(self, worldboss.getRandomMessage(BRIBE_LINES));
            bribePlayers(self, players);
        }

        // Phase 6: 35% - Second wave of mercs
        if (worldboss.shouldTriggerPhase(self, 35, "secondMercWave"))
        {
            chat.chat(self, "Reinforcements! Where are my reinforcements?!");
            worldboss.broadcastToPlayers(players, "More of Dhanak's crew have arrived!");
            worldboss.summonCompanions(self, attacker, MERC_ADD, 3, 10.0f);
            chat.chat(self, worldboss.getRandomMessage(DESPERATION_LINES));
        }

        // Phase 7: 25% - Activate personal shield
        if (worldboss.shouldTriggerPhase(self, 25, "personalShield"))
        {
            chat.chat(self, "You want me? Come through my shields first!");
            worldboss.broadcastToPlayers(players, "Dhanak has activated a personal energy shield! Find a way to disable it!");
            buff.applyBuff(self, "me_buff_shield_1", 45);
            buff.applyBuff(self, "me_buff_defense_1", 45);
            playClientEffectObj(players, "clienteffect/ui_shield_generator_activate.cef", self, "");
            showFlyText(self, new string_id("- SHIELD ACTIVE -"), 3.0f, colors.YELLOW);
        }

        // Phase 8: 15% - Desperate thermal barrage
        if (worldboss.shouldTriggerPhase(self, 15, "desperateThermals"))
        {
            chat.chat(self, worldboss.getRandomMessage(DESPERATION_LINES));
            chat.chat(self, "Take them ALL!");
            worldboss.broadcastToPlayers(players, "Dhanak is throwing thermal detonators wildly!");
            thermalDetonator(self, players);
            thermalDetonator(self, players);
        }

        // Phase 9: 8% - Reactor overload warning
        if (worldboss.shouldTriggerPhase(self, 8, "reactorWarning"))
        {
            chat.chat(self, "If I can't escape... NEITHER CAN YOU!");
            worldboss.broadcastToPlayers(players, "WARNING: Dhanak is attempting to overload his ship's portable reactor! You have 15 seconds to get clear!");
            showFlyText(self, new string_id("- REACTOR OVERLOAD -"), 4.0f, colors.RED);
            messageTo(self, "doReactorExplosion", null, 15.0f, false);
        }

        // Phase 10: 3% - Final words
        if (worldboss.shouldTriggerPhase(self, 3, "finalWords"))
        {
            chat.chat(self, "Should have... taken the money...");
            buff.removeAllBuffs(self);
        }

        return SCRIPT_CONTINUE;
    }

    private void thermalDetonator(obj_id self, obj_id[] targets) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                playClientEffectObj(target, "clienteffect/combat_grenade_thermal.cef", target, "");
                playClientEffectLoc(target, "clienteffect/lair_med_damage_smoke.cef", getLocation(target), 1.0f);
                worldboss.reduceHealth(target, rand(2500, 4500));
                worldboss.reduceAction(target, rand(1500, 2500));
            }
        }
    }

    private void bribePlayers(obj_id self, obj_id[] targets) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        playClientEffectObj(targets, "clienteffect/holoemote_money.cef", self, "");
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                // Random chance to "confuse" the player (apply a short debuff)
                if (rand(1, 100) <= 40)
                {
                    broadcast(target, "The glint of credits distracts you momentarily!");
                    buff.applyBuff(target, "blind", 8);
                    sendConsoleCommand("/stand", target);
                }
                else
                {
                    broadcast(target, "You resist Dhanak's bribe attempt!");
                }
            }
        }
    }

    public int doReactorExplosion(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id[] players = getAllPlayers(getLocation(self), 20.0f);
        playClientEffectObj(players, "clienteffect/restuss_event_big_explosion.cef", self, "");
        playClientEffectLoc(players, "clienteffect/lair_large_destroyed.cef", getLocation(self), 2.0f);

        for (obj_id player : players)
        {
            if (isIdValid(player))
            {
                int explosionDamage = rand(8000, 12000);
                broadcast(player, "You were caught in the reactor explosion!");
                damage(player, DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, explosionDamage);
                buff.applyBuff(player, "onFire", 15);
            }
        }

        // Also damage Dhanak himself
        int selfDamage = getMaxHealth(self) / 4;
        damage(self, DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, selfDamage);
        chat.chat(self, "*cough* That... didn't go as planned...");

        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id landedDeathBlow = getObjIdObjVar(self, xp.VAR_LANDED_DEATHBLOW);
        obj_id[] players = getPlayerCreaturesInRange(self, titan_player.WORLD_BOSS_CREDIT_RANGE);

        // Process standard rewards
        worldboss.processJediElderQuest(players);
        worldboss.distributeRewards(players, BOSS_KEY, TITLE_NAME, TITLE_SKILL);
        worldboss.grantDeathblowBonus(landedDeathBlow, BOSS_NAME);

        // Special smuggler loot - spice and contraband for random players
        if (players.length > 0)
        {
            // Give spice to a random player
            obj_id spiceWinner = players[rand(0, players.length - 1)];
            if (isIdValid(spiceWinner))
            {
                obj_id inv = utils.getInventoryContainer(spiceWinner);
                broadcast(spiceWinner, "You found some of Dhanak's hidden spice stash!");
                static_item.createNewItemFunction("item_spice_shadowpaw", inv);
                LOG("ethereal", "[World Boss System]: (Dhanak) Gave spice to " + getPlayerFullName(spiceWinner));
            }

            // Give smuggler gear to another random player
            obj_id gearWinner = players[rand(0, players.length - 1)];
            if (isIdValid(gearWinner))
            {
                obj_id inv = utils.getInventoryContainer(gearWinner);
                broadcast(gearWinner, "You salvaged some of Dhanak's smuggling equipment!");
                // Give some thematic loot
                static_item.createNewItemFunction("item_smuggler_crate_01_01", inv);
                LOG("ethereal", "[World Boss System]: (Dhanak) Gave smuggler gear to " + getPlayerFullName(gearWinner));
            }
        }

        // Chance to drop a rare freighter schematic
        int schematicChance = rand(1, 100);
        if (schematicChance >= 90 && isIdValid(landedDeathBlow))
        {
            obj_id victorInv = utils.getInventoryContainer(landedDeathBlow);
            broadcast(landedDeathBlow, "You found Dhanak's personal freighter modification schematics!");
            static_item.createNewItemFunction("item_schematic_yt1300_mod", victorInv);
            LOG("ethereal", "[World Boss System]: (Dhanak) Rare schematic dropped for " + getPlayerFullName(landedDeathBlow));
        }

        return SCRIPT_CONTINUE;
    }
}
