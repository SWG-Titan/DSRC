package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_darth_rolii
@Author: BubbaJoeX
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    Darth Rolii is a Sith world boss that spawns in the Chommell Sector on Tatooine.
    Darth Rolii has a variety of mechanics that make him a challenging fight.
        1. Darth Rolii will speak a spam message.
        2. Darth Rolii will disarm players of their weapons.
        3. Darth Rolii will bombard players, causing massive damage.
        4. Darth Rolii will enrage players, causing them to add hate.
        5. Darth Rolii will enter his last stand, calling upon the full power of the Dark Side of the Force.
        6. Darth Rolii will say a final flavor message before death.
@Requirements: script.player.player_nb, script.library.nb_player
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;

public class c_darth_rolii extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "gizmo";
    private static final String BOSS_NAME = "Darth Rolii";
    private static final String TITLE_NAME = "Apprentice of the Force";
    private static final String TITLE_SKILL = "title_world_boss_rolii";

    private static final String[] SITH_BATTLE_CHANTS = {
            "I am your new master...",
            "Feel the power of the dark side!",
            "Embrace your anger and unleash it!",
            "Sith shall conquer all in our path!",
            "Submit to me, you whelp!",
            "Crush the weak, let hatred fuel your strength!",
            "With passion and fury, we shall prevail!",
            "The Sith rise, the galaxy trembles!",
            "No mercy, no compassion, only power!",
    };

    private static final String[] STUN_REACTIONS = {"Ahhh!", "Ouch!", "Ow!", "Ugh!", "Yikes!", "Mercy!!!"};

    private static final String[] INITIAL_BUFFS = {
            "me_buff_strength_3",
            "me_buff_agility_3",
            "me_buff_precision_3",
            "me_buff_melee_gb_1"
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setDescriptionString(self, "A powerful rogue Sith apprentice.");
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_GIZMO);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: Rolii has been defeated.");
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

        // Initial chirp and buff application
        if (worldboss.shouldTriggerPhase(self, 100, "chirp"))
        {
            worldboss.applyBossBuffs(self, INITIAL_BUFFS, 600);
            chat.chat(self, worldboss.getRandomMessage(SITH_BATTLE_CHANTS));
        }

        // Phase 1: 75% - Remove buffs
        if (worldboss.shouldTriggerPhase(self, 75, "hasSpawned"))
        {
            chat.chat(self, "You will perish!");
            worldboss.broadcastToPlayers(players, "Darth Rolii has lost his enhancements.  The Force says the time to strike is now!");
            buff.removeAllBuffs(self);
            return SCRIPT_CONTINUE;
        }

        // Phase 2: 50% - Bombard
        if (worldboss.shouldTriggerPhase(self, 50, "hasBeenBombed"))
        {
            chat.chat(self, worldboss.getRandomMessage(SITH_BATTLE_CHANTS));
            worldboss.bombardPlayers(self, players, 1200, 3000);
        }

        // Phase 3: 25% - Stun players
        if (worldboss.shouldTriggerPhase(self, 25, "hasDisarmed"))
        {
            chat.chat(self, worldboss.getRandomMessage(SITH_BATTLE_CHANTS));
            worldboss.broadcastToPlayers(players, "The most recent attack by " + getFirstName(attacker) + " has enraged Darth Roli, causing him to stifle all players!");
            worldboss.stunPlayersWithReaction(self, players, "The Dark Side of the Force brings you to your knees!", STUN_REACTIONS);
        }

        // Phase 4: 10% - Last stand
        if (worldboss.shouldTriggerPhase(self, 10, "hasLastStand"))
        {
            buff.removeAllBuffs(self);
            for (obj_id player : players)
            {
                buff.applyBuff(player, "event_combat", 60, 150);
                buff.removeAllDebuffs(player);
                broadcast(player, "Darth Rolii has entered his last stand, calling upon the full power of the Dark Side of the Force!");
                debugConsoleMsg(player, "\\#DAA520" + "ATTACK NOW!" + "\\#.");
            }
            buff.applyBuff(self, "event_buff_dev", 30, 150);
        }

        // Phase 5: 1% - Final message
        if (worldboss.shouldTriggerPhase(self, 1, "hasLastStandMsg"))
        {
            chat.chat(self, worldboss.getRandomMessage(SITH_BATTLE_CHANTS));
        }

        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        worldboss.handleCorpseRewards(self, "rolii", TITLE_NAME, TITLE_SKILL, BOSS_NAME);
        return SCRIPT_CONTINUE;
    }
}

