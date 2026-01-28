package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_peko
@Author: BubbaJoeX
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    The Mutated Peko-Peko Empress is a mutated Peko-Peko world boss that spawns in the Chommell Sector on Tatooine.
    The Mutated Peko-Peko Empress has a variety of mechanics that make her a challenging fight.
        1. The Mutated Peko-Peko Empress will speak a spam message.
        2. The Mutated Peko-Peko Empress will disarm players of their weapons.
        3. The Mutated Peko-Peko Empress will knock back players, causing them to stagger.
        4. The Mutated Peko-Peko Empress will call upon her whelps to aid her in her final stand.
        5. The Mutated Peko-Peko Empress will say a final flavor message before death.
@Requirements: script.player.player_nb, script.library.nb_player
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.location;
import script.obj_id;

public class c_peko extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "peko";
    private static final String BOSS_NAME = "Mutated Peko-Peko Empress";
    private static final String TITLE_NAME = "Vanquisher of the Peko-Peko";
    private static final String TITLE_SKILL = "title_world_boss_peko";
    private static final String ADD_CREATURE = "peko_peko";
    private static final String ELITE_ADD_CREATURE = "peko_peko_albatross_high";

    private static final String[] SQUAWK_MSGS = {
            "<LOUD AVIAN NOISES>",
            "<ANGRY AVIAN NOISES>",
            "<UPSET AVIAN NOISES>",
            "<DISPLEASED AVIAN NOISES>",
            "<RIGHTEOUS AVIAN NOISES>",
            "<DISTURBING AVIAN NOISES>",
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setDescriptionString(self, "A powerful and mutated Peko-Peko.");
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_PEKO);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: Peko has been killed");
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

        // Initial chirp
        if (worldboss.shouldTriggerPhase(self, 100, "chirp"))
        {
            chat.chat(self, worldboss.getRandomMessage(SQUAWK_MSGS));
        }

        // Phase 1: 75% - Spawn adds
        if (worldboss.shouldTriggerPhase(self, 75, "hasSpawned"))
        {
            titan_player.createCircleSpawn(self, self, ADD_CREATURE, 12, 24);
            return SCRIPT_CONTINUE;
        }

        // Phase 2: 50% - Knock back players
        if (worldboss.shouldTriggerPhase(self, 50, "hasKnockedBack"))
        {
            chat.chat(self, worldboss.getRandomMessage(SQUAWK_MSGS));
            staggerPlayers(self, players);
        }

        // Phase 3: 25% - Disarm players
        if (worldboss.shouldTriggerPhase(self, 25, "hasDisarmed"))
        {
            chat.chat(self, worldboss.getRandomMessage(SQUAWK_MSGS));
            for (obj_id player : players)
            {
                obj_id heldWeapon = getCurrentWeapon(player);
                if (isIdValid(heldWeapon))
                {
                    broadcast(player, "The most recent attack from " + getFirstName(attacker) + " caused the Mutated Peko-Peko Empress to disarm you with a wind gust!");
                    putIn(heldWeapon, utils.getInventoryContainer(player));
                }
            }
        }

        // Phase 4: 10% - Last stand with elite adds
        if (worldboss.shouldTriggerPhase(self, 10, "hasLastStand"))
        {
            buff.removeAllBuffs(self);
            titan_player.createCircleSpawn(self, self, ELITE_ADD_CREATURE, 4, 24);
            staggerPlayers(self, players);
            worldboss.broadcastToPlayers(players, "The Mutated Peko-Peko Empress has called upon her fledglings to aid her in her final stand!");
        }

        return SCRIPT_CONTINUE;
    }

    private void staggerPlayers(obj_id self, obj_id[] targets) throws InterruptedException
    {
        float maxDistance = titan_player.WORLD_BOSS_CREDIT_RANGE + 12f; // bring them outside the credit range
        playClientEffectObj(targets, "clienteffect/cr_bodyfall_huge.cef", self, "");

        if (targets == null || targets.length == 0)
        {
            return;
        }

        for (obj_id target : targets)
        {
            if (!isIdValid(target))
            {
                continue;
            }

            int playerHealth = getHealth(target);
            int playerAction = getAction(target);
            int statDrain = playerHealth / 2;
            int actionDrain = playerAction / 2;
            setHealth(target, playerHealth - statDrain);
            setAction(target, playerAction - actionDrain);

            location stagger = getLocation(target);
            stagger.x = stagger.x + rand(-maxDistance, maxDistance);
            stagger.z = stagger.z + rand(-maxDistance, maxDistance);
            stagger.y = getHeightAtLocation(stagger.x, stagger.z);
            stagger.area = getCurrentSceneName();
            warpPlayer(target, stagger.area, stagger.x, stagger.y, stagger.z, null, 0, 0, 0);
            broadcast(target, "The wind from the Mutated Peko-Peko's wings have knocked you back!");

            // Remove position buffs that could prevent proper knockback
            if (buff.hasBuff(target, "co_position_secured"))
            {
                buff.removeBuff(target, "co_position_secured");
            }
            if (buff.hasBuff(target, "co_base_of_operations"))
            {
                buff.removeBuff(target, "co_base_of_operations");
            }

            sendConsoleCommand("/stopFollow", target);
            faceTo(target, self);
        }
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        worldboss.handleCorpseRewards(self, BOSS_KEY, TITLE_NAME, TITLE_SKILL, BOSS_NAME);
        return SCRIPT_CONTINUE;
    }
}
