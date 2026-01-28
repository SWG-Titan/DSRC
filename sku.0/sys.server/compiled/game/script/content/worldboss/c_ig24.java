package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_ig24
@Author: BubbaJoe
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes; This boss should be placed on Lok, roaming in the NE quadrant.
@Requirements: script.player.player_nb, script.library.nb_player
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;

public class c_ig24 extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "ig24";
    private static final String BOSS_NAME = "IG-24";
    private static final String TITLE_NAME = "Droid Ripper";
    private static final String TITLE_SKILL = "title_world_boss_ig24";
    private static final String ADD_CREATURE = "world_boss_ig24_add";

    private static final String[] IG24_MSGS = {
            "Your demise is imminent. Resistance is futile.",
            "Target acquired. Preparing to terminate.",
            "You are merely organic. I am perfection.",
            "Your inferior biological form stands no chance.",
            "I calculate a 100% probability of your defeat.",
            "Fleeing is useless. I will find you.",
            "You cannot escape the precision of a droid.",
            "I was built to destroy. You are but a malfunction.",
            "Your efforts are inefficient and irrelevant.",
            "The galaxy has no place for weakness like yours.",
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setInvulnerable(self, false);
        setHealth(self, getMaxHealth(self));
        setDescriptionString(self, "A notorious spy, this IG droid was manufactured from IG-88's Droid Factory and has been programmed to retrieve the most egregious intel.");
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_IG24);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: IG-24 has been defeated.");
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
        if (worldboss.shouldTriggerPhase(self, 100, "speak"))
        {
            chat.chat(self, worldboss.getRandomMessage(IG24_MSGS));
        }

        // Phase 1: 75% - Spawn adds
        if (worldboss.shouldTriggerPhase(self, 75, "hasSpawned"))
        {
            chat.chat(self, "Your attempts to defeat me are most futile.");
            worldboss.broadcastToPlayers(players, "IG-24 has summoned reinforcements!");
            titan_player.createCircleSpawn(self, self, ADD_CREATURE, 2, 5);
            return SCRIPT_CONTINUE;
        }

        // Phase 1b: 74% - Chatter
        if (worldboss.shouldTriggerPhase(self, 74, "chatterSpeechAlpha"))
        {
            chat.chat(self, worldboss.getRandomMessage(IG24_MSGS));
        }

        // Phase 1c: 69% - Chatter
        if (worldboss.shouldTriggerPhase(self, 69, "chatterSpeechBeta"))
        {
            chat.chat(self, worldboss.getRandomMessage(IG24_MSGS));
        }

        // Phase 2: 65% - More spawns
        if (worldboss.shouldTriggerPhase(self, 65, "chatterSpeechCharlieAndSpawns"))
        {
            chat.chat(self, worldboss.getRandomMessage(IG24_MSGS));
            worldboss.broadcastToPlayers(players, "IG-24 has summoned more droids to his defense!");
            titan_player.createCircleSpawn(self, self, ADD_CREATURE, 2, 5);
        }

        // Phase 3: 50% - Bomb
        if (worldboss.shouldTriggerPhase(self, 50, "wasBombed"))
        {
            chat.chat(self, worldboss.getRandomMessage(IG24_MSGS));
            chat.chat(self, "The probability of survival is miniscule. Surrender yourself.");
            worldboss.cryobanBombard(self, players, 2400, 6000);
            worldboss.broadcastToPlayers(players, "You have been stuck with a sticky grenade!");
        }

        // Phase 4: 37% - Final adds
        if (worldboss.shouldTriggerPhase(self, 37, "addsSpawned"))
        {
            chat.chat(self, worldboss.getRandomMessage(IG24_MSGS));
            chat.chat(self, "Reinforcement Protocol Engaged.");
            worldboss.summonCompanions(self, attacker, "world_boss_ig24_adds", 2, 5f);
            worldboss.broadcastToPlayers(players, "IG-24 has summoned the last of his bodyguards.");
        }

        // Phase 5: 20% - Sedate players
        if (worldboss.shouldTriggerPhase(self, 20, "usedSedative"))
        {
            chat.chat(self, "Administering sedative. Now initiating incapacitation protocol.");
            sedatePlayers(self, players);
            chat.chat(self, "Incapacitation protocol rescinded. Termination protocol now in full effect.");
        }

        // Phase 6: 8% - Sticky grenade
        if (worldboss.shouldTriggerPhase(self, 8, "usedStickyGrenade"))
        {
            chat.chat(self, "Adhering explosive. Prepare for termination.");
            worldboss.cryobanBombard(self, players, 2400, 6000);
            worldboss.broadcastToPlayers(players, "You have been stuck with a sticky grenade!");
        }

        // Phase 7: 5% - Self destruct
        if (worldboss.shouldTriggerPhase(self, 5, "usedSelfDestruct"))
        {
            doSelfDestruct(self, players);
        }

        return SCRIPT_CONTINUE;
    }

    private void sedatePlayers(obj_id self, obj_id[] targets) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                sendConsoleCommand("/prone", target);
                broadcast(target, "You have been hit with a tranq-dart!");
                playClientEffectLoc(target, "clienteffect/int_camshake_heavy.cef", getLocation(self), 1.0f);
                buff.applyBuff(target, "poison", 30, 100);
                faceTo(target, self);
            }
        }
    }

    private int doSelfDestruct(obj_id self, obj_id[] targets) throws InterruptedException
    {
        chat.chat(self, "Manufacturer's protocol dictates I cannot be captured. I must be destroyed.");
        worldboss.broadcastToPlayers(targets, "IG-24 has attempted to initiated their self-destruct sequence. You have 10 seconds to get 16 meters away and maintain that distance!");
        messageTo(self, "doFinalBomb", null, 10.0f, false);
        return SCRIPT_CONTINUE;
    }

    public int doFinalBomb(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id[] players = getAllPlayers(getLocation(self), 14.0f); // subtract 2m in case the server hiccups
        playClientEffectObj(players, "clienteffect/restuss_event_big_explosion.cef", self, "");
        for (obj_id player : players)
        {
            broadcast(player, "You were caught in IG-24's self destruct and have died.");
            kill(player);
        }
        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        worldboss.handleCorpseRewards(self, BOSS_KEY, TITLE_NAME, TITLE_SKILL, BOSS_NAME);
        return SCRIPT_CONTINUE;
    }
}
