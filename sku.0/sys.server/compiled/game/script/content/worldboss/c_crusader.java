package script.content.worldboss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_mandalorian_crusader
@Author: BubbaJoeX
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    The Mandalorian Crusader is a Mandalorian world boss that spawns near the Death Watch Bunker on Endor
    The Mandalorian Crusader has a variety of mechanics that make him a challenging fight.
        1. The Mandalorian Crusader will speak a spam message.
        2. The Mandalorian Crusader will disarm players of their weapons.
        3. The Mandalorian Crusader will bombard players, causing massive damage.
        4. The Mandalorian Crusader will increase his focus, causing players to enrage.
        5. The Mandalorian Crusader will enter his last stand, calling upon the full power of the Mandalorian Crusader.
        6. The Mandalorian Crusader will say a final flavor message before death
@Requirements: script.player.player_nb, script.library.nb_player
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;
import script.string_id;

public class c_crusader extends script.base_script
{
    // Boss Configuration
    private static final String BOSS_KEY = "pax";
    private static final String BOSS_NAME = "The Crusader";
    private static final String TITLE_NAME = "The Crusader's Bane";
    private static final String TITLE_SKILL = "title_world_boss_crusader";

    private static final String[] MAND_MSGS = {
            "Cowards!",
            "You will not escape me!",
            "I will not be defeated!",
            "I will not be stopped!",
            "I will not be denied!",
            "I will not be ignored!",
            "I will not be forgotten!",
            "I will not be defeated!",
            "I am Mand'alor!",
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, BOSS_NAME);
        setDescriptionStringId(self, string_id.unlocalized("A powerful rogue Mandalorian."));
        worldboss.markBossActive(BOSS_KEY);
        titan_utils.markAsEventSpawn(self);
        titan_player.doWorldBossAnnounce(self, titan_player.WORLD_BOSS_PAX);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        LOG("ethereal", "[World Boss System]: Crusader has been killed.");
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
            chat.chat(self, worldboss.getRandomMessage(MAND_MSGS));
        }

        // Phase 1: 75% - Remove buffs
        if (worldboss.shouldTriggerPhase(self, 75, "hasSpawned"))
        {
            chat.chat(self, "I will not be challenged in such uncivil ways!");
            worldboss.broadcastToPlayers(players, "The crusader has lost his enhancements. Strike now!");
            buff.removeAllBuffs(self);
            return SCRIPT_CONTINUE;
        }

        // Phase 2: 50% - Bombard
        if (worldboss.shouldTriggerPhase(self, 50, "hasBeenBombed"))
        {
            chat.chat(self, worldboss.getRandomMessage(MAND_MSGS));
            chat.chat(self, "Eat Durasteel!");
            worldboss.bombardPlayers(self, players, 3200, 5000);
        }

        // Phase 3: 20% - Enrage warning
        if (worldboss.shouldTriggerPhase(self, 20, "hasDisarmed"))
        {
            chat.chat(self, worldboss.getRandomMessage(MAND_MSGS));
            worldboss.broadcastToPlayers(players, "The most recent attack from " + getFirstName(attacker) + " has enraged the crusader, causing him to increase his focus.");
        }

        // Phase 4: 8% - Last stand
        if (worldboss.shouldTriggerPhase(self, 8, "hasLastStand"))
        {
            buff.removeAllBuffs(self);
            worldboss.broadcastToPlayers(players, "The crusader has entered his last stand!");
            chat.chat(self, "This Is The Way.");
            buff.applyBuff(self, "crystal_buff", 30, 10);
        }

        // Phase 5: 1% - Final message
        if (worldboss.shouldTriggerPhase(self, 1, "lastMandMsg"))
        {
            chat.chat(self, MAND_MSGS[MAND_MSGS.length - 1]);
        }

        return SCRIPT_CONTINUE;
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id landedDeathBlow = getObjIdObjVar(self, xp.VAR_LANDED_DEATHBLOW);
        obj_id[] players = getPlayerCreaturesInRange(self, titan_player.WORLD_BOSS_CREDIT_RANGE);

        // Process Jedi Elder quest and distribute rewards
        worldboss.processJediElderQuest(players);
        worldboss.distributeRewards(players, "mando", TITLE_NAME, TITLE_SKILL);
        worldboss.grantDeathblowBonus(landedDeathBlow, BOSS_NAME);

        // Special themed loot for random player
        if (players.length > 0)
        {
            obj_id randomPlayer = players[rand(0, players.length - 1)];
            if (isIdValid(randomPlayer))
            {
                obj_id inv = utils.getInventoryContainer(randomPlayer);
                broadcast(randomPlayer, "You have found some interesting items off of The Crusader...");
                create.createObject("object/tangible/loot/dungeon/death_watch_bunker/binary_liquid.iff", inv, "");
                create.createObject("object/tangible/loot/dungeon/death_watch_bunker/emulsion_protection.iff", inv, "");
            }
            else
            {
                LOG("ethereal", "[World Boss System]: (The Crusader) No valid ID for randomly selected player. Not handing out themed loot.");
            }
        }

        // Chance for bunker house drop
        int random = rand(1, 100);
        if (random >= 75)
        {
            static_item.createNewItemFunction("item_content_bunker_house_02", utils.getInventoryContainer(self));
            LOG("ethereal", "[World Boss System]: (The Crusader) has dropped a bunker house. Giving to the corpse.");
        }

        return SCRIPT_CONTINUE;
    }
}
