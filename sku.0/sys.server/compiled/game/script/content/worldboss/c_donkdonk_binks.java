package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_donkdonk_binks
@Author: BubbaJoeX
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    Donk-Donk Binks is a Gungan world boss that spawns in the Chommell Sector on Tatooine.
    Donk-Donk Binks has a variety of mechanics that make him a challenging fight.
        1. Donk-Donk Binks will speak a spam message.
        2. Donk-Donk Binks will disarm players of their weapons.
        3. Donk-Donk Binks will drug players, causing them to fall asleep.
        4. Donk-Donk Binks will call for reinforcements. (Donk-Donk Binks Add)
        5. Donk-Donk Binks will throw a booma, causing massive damage to players.
        6. Donk-Donk Binks will say a final flavor message before death.
@Requirements: script.player.player_nb, script.library.nb_player
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;
import script.string_id;

public class c_donkdonk_binks extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "donkdonk_binks";
    private static final String BOSS_NAME = "Donk-Donk Binks";
    private static final String TITLE_NAME = "Bombad General";
    private static final String TITLE_SKILL = "title_world_boss_donkdonk";
    private static final String ADD_CREATURE = "world_boss_donkdonk_binks_add";

    private static final String[] GUNGAN_MSGS = {
            "Yous no escape me!",
            "Yous mula be moole!",
            "Yousa think yousa so bombad!",
            "Yous weapons will adden nicely to meesa collection!",
            "Me warned yousa!",
            "Ya-hoo yousa, stopen dat!",
            "Dat's it!",
            "No maken mesa usen a booma!",
            "Stopen da doo-doo, yousa cannot defeat mesa in combat!"
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setDescriptionStringId(self, string_id.unlocalized("A distant cousin to Jar-Jar Binks, this criminal Gungan is notorious for causing havoc in the Chommell Sector"));
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_DONKDONK);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: Donk-Donk has been defeated.");
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
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

        // Initial speak
        if (worldboss.shouldTriggerPhase(self, 100, "speaken"))
        {
            chat.chat(self, worldboss.getRandomMessage(GUNGAN_MSGS));
        }

        // Phase 1: 75% - Spawn adds per player
        if (worldboss.shouldTriggerPhase(self, 75, "hasSpawned"))
        {
            chat.chat(self, "Okie dey, you asken for it!");
            worldboss.broadcastToPlayers(players, "Donk-Donk Binks has called for reinforcements!");
            worldboss.spawnAddsPerPlayer(self, players, ADD_CREATURE);
            return SCRIPT_CONTINUE;
        }

        // Phase 1b: 74-72% - Chat spam
        if (worldboss.shouldTriggerPhaseInRange(self, 72, 74, "chatSpam"))
        {
            chat.chat(self, worldboss.getRandomMessage(GUNGAN_MSGS));
        }

        // Phase 1c: 65% or 55% - Random chat
        if (worldboss.shouldTriggerPhase(self, 65, "chatSpamRandom1"))
        {
            chat.chat(self, worldboss.getRandomMessage(GUNGAN_MSGS));
        }
        if (worldboss.shouldTriggerPhase(self, 55, "chatSpamRandom2"))
        {
            chat.chat(self, worldboss.getRandomMessage(GUNGAN_MSGS));
        }

        // Phase 2: 50% - Booma attack
        if (worldboss.shouldTriggerPhase(self, 50, "hasBeenBoomad"))
        {
            chat.chat(self, worldboss.getRandomMessage(GUNGAN_MSGS));
            chat.chat(self, "Whoopsies... Meesa slipped!");
            worldboss.cryobanBombard(self, players, 1900, 6000);
        }

        // Phase 3: 37% - Disarm and summon more adds
        if (worldboss.shouldTriggerPhase(self, 37, "hasBeenDisarmed"))
        {
            chat.chat(self, worldboss.getRandomMessage(GUNGAN_MSGS));
            chat.chat(self, "Disarmen timen! Binksen no liken yousa weapons!");
            worldboss.disarmPlayers(players, "Donk-Donk Binks has disarmed you of your weapon!");
            worldboss.summonCompanions(self, attacker, ADD_CREATURE, 6, 12.0f);
            worldboss.broadcastToPlayers(players, "Donk-Donk Binks has called for more reinforcements from the Swamp Village!");
        }

        // Phase 4: 20% - Drug players
        if (worldboss.shouldTriggerPhase(self, 20, "hasDruggedPlayers"))
        {
            chat.chat(self, "Meesa gonna make yousa feel verrry sleepy!");
            worldboss.sedatePlayers(self, players, "me_stasis_1", 10, 5, "You have been poisoned by Donk-Donk Binks!");
            chat.chat(self, "Yousa gonna sleep now!");
        }

        // Phase 5: 8% - Last booma
        if (worldboss.shouldTriggerPhase(self, 8, "hasLastBoomad"))
        {
            chat.chat(self, "Meesa gonna make yousa go * BOOM! *");
            worldboss.cryobanBombard(self, players, 1900, 6000);
        }

        // Phase 6: 2% - Final message
        if (worldboss.shouldTriggerPhase(self, 2, "lastMsg"))
        {
            chat.chat(self, "If only me didn't liven a life of bombad crime...");
        }

        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        worldboss.handleCorpseRewards(self, "donkdonk", TITLE_NAME, TITLE_SKILL, BOSS_NAME);
        return SCRIPT_CONTINUE;
    }
}
