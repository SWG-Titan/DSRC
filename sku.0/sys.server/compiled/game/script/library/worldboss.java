package script.library;
/*
@Origin: dsrc.script.library
@Author: Refactored from world boss scripts
@Purpose: Common library functions for World Boss encounters.
@Notes:
    This file contains shared functions used across all world boss scripts.
    - Health phase checking and triggering
    - Common combat mechanics (bombard, stun, disarm, etc.)
    - Reward distribution
    - Utility functions
@Created: Tuesday, 1/28/2026
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class worldboss extends script.base_script
{
    // =====================================
    // Constants
    // =====================================
    public static final float DEFAULT_COMBAT_RANGE = 64.0f;
    public static final String TOKEN_ITEM = "item_world_boss_token_01_01";
    public static final int BASE_TOKEN_AMOUNT = 5;
    public static final int DEATHBLOW_BONUS_TOKENS = 5;
    public static final int TITLE_KILL_REQUIREMENT = 10;

    // Jedi Elder Quest Constants
    public static final String JEDI_ELDER_SIGNAL = "trial_of_the_elders_wb";
    public static final String JEDI_ELDER_QUEST = "quest/trial_of_the_elder";
    public static final String JEDI_ELDER_TASK = "slayer_world_boss";

    // =====================================
    // Health Phase System
    // =====================================

    /**
     * Gets the current health percentage of a creature.
     * @param self The creature to check
     * @return The health percentage (0-100)
     */
    public static int getHealthPercent(obj_id self) throws InterruptedException
    {
        int health = getHealth(self);
        int maxHealth = getMaxHealth(self);
        return (health * 100) / maxHealth;
    }

    /**
     * Checks if a health phase should trigger. Uses script vars to ensure single execution.
     * @param self The world boss
     * @param phasePercent The health percentage threshold
     * @param phaseName Unique name for this phase (used as script var)
     * @return true if this phase should execute (first time reaching this threshold)
     */
    public static boolean shouldTriggerPhase(obj_id self, int phasePercent, String phaseName) throws InterruptedException
    {
        int currentPercent = getHealthPercent(self);
        if (currentPercent <= phasePercent && !utils.hasScriptVar(self, phaseName))
        {
            utils.setScriptVar(self, phaseName, 1);
            return true;
        }
        return false;
    }

    /**
     * Checks if a health phase should trigger within a range.
     * @param self The world boss
     * @param minPercent The minimum health percentage
     * @param maxPercent The maximum health percentage
     * @param phaseName Unique name for this phase
     * @return true if this phase should execute
     */
    public static boolean shouldTriggerPhaseInRange(obj_id self, int minPercent, int maxPercent, String phaseName) throws InterruptedException
    {
        int currentPercent = getHealthPercent(self);
        if (currentPercent <= maxPercent && currentPercent >= minPercent && !utils.hasScriptVar(self, phaseName))
        {
            utils.setScriptVar(self, phaseName, 1);
            return true;
        }
        return false;
    }

    // =====================================
    // Combat Mechanics
    // =====================================

    /**
     * Bombards players with damage and effects.
     * @param self The attacker
     * @param targets Array of target players
     * @param minDamage Minimum damage to deal
     * @param maxDamage Maximum damage to deal
     * @param effect Client effect to play
     */
    public static void bombardPlayers(obj_id self, obj_id[] targets, int minDamage, int maxDamage, String effect) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                playClientEffectObj(target, effect, target, "");
                reduceHealth(target, rand(minDamage, maxDamage));
                reduceAction(target, rand(minDamage, maxDamage));
            }
        }
    }

    /**
     * Bombards players with the default explosion effect.
     */
    public static void bombardPlayers(obj_id self, obj_id[] targets, int minDamage, int maxDamage) throws InterruptedException
    {
        bombardPlayers(self, targets, minDamage, maxDamage, "clienteffect/avatar_explosion_02.cef");
    }

    /**
     * Applies a cryoban grenade effect to players.
     */
    public static void cryobanBombard(obj_id self, obj_id[] targets, int minDamage, int maxDamage) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                playClientEffectObj(target, "clienteffect/combat_grenade_cryoban.cef", target, "head");
                playClientEffectObj(target, "clienteffect/combat_grenade_cryoban.cef", target, "root");
                playClientEffectObj(target, "clienteffect/combat_grenade_cryoban.cef", target, "hand_r");
                reduceHealth(target, rand(minDamage, maxDamage));
                reduceAction(target, rand(minDamage, maxDamage));
            }
        }
    }

    /**
     * Stuns players, bringing them to their knees and facing the boss.
     */
    public static void stunPlayers(obj_id self, obj_id[] targets, String message) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        playClientEffectObj(targets, "clienteffect/cr_bodyfall_huge.cef", self, "");
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                broadcast(target, message);
                faceTo(target, self);
            }
        }
    }

    /**
     * Stuns players and forces them to say something.
     */
    public static void stunPlayersWithReaction(obj_id self, obj_id[] targets, String message, String[] reactions) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        playClientEffectObj(targets, "clienteffect/cr_bodyfall_huge.cef", self, "");
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                sendConsoleCommand("/kneel", target);
                sendConsoleCommand("/say " + reactions[rand(0, reactions.length - 1)], target);
                broadcast(target, message);
                faceTo(target, self);
            }
        }
    }

    /**
     * Sedates players, making them prone and applying a debuff.
     */
    public static void sedatePlayers(obj_id self, obj_id[] targets, String buffName, float duration, float stackCount, String message) throws InterruptedException
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
                broadcast(target, message);
                buff.applyBuff(target, buffName, duration, stackCount);
                faceTo(target, self);
            }
        }
    }

    /**
     * Disarms all players of their current weapon.
     */
    public static void disarmPlayers(obj_id[] targets, String message) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        String[] defaultReactions = {"Blast it!", "Dank farrik!", "Oh dear!", "Son of a mud scuffer!", "Help!"};
        disarmPlayersWithReaction(targets, message, defaultReactions);
    }

    /**
     * Disarms players and forces them to say a reaction.
     */
    public static void disarmPlayersWithReaction(obj_id[] targets, String message, String[] reactions) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return;
        }
        for (obj_id target : targets)
        {
            if (isIdValid(target))
            {
                chat.chat(target, reactions[rand(0, reactions.length - 1)]);
                obj_id heldWeapon = getCurrentWeapon(target);
                if (isIdValid(heldWeapon))
                {
                    putIn(heldWeapon, utils.getInventoryContainer(target));
                    debugConsoleMsg(target, "\\#DD1234" + message + "\\#.");
                }
            }
        }
    }

    // =====================================
    // Spawn Helpers
    // =====================================

    /**
     * Summons companion creatures around the boss using circle spawn.
     */
    public static void summonCompanions(obj_id self, obj_id target, String creatureName, int quantity, float radius) throws InterruptedException
    {
        titan_player.createCircleSpawn(self, target, creatureName, quantity, radius);
        LOG("ethereal", "[World Boss System]: " + getName(self) + " has summoned " + quantity + " " + creatureName + " to aid in the fight.");
    }

    /**
     * Spawns one add per player in range.
     */
    public static void spawnAddsPerPlayer(obj_id self, obj_id[] players, String creatureName) throws InterruptedException
    {
        if (players == null || players.length == 0)
        {
            return;
        }
        for (obj_id player : players)
        {
            if (isIdValid(player))
            {
                create.object(creatureName, getLocation(player));
            }
        }
    }

    // =====================================
    // Stat Manipulation
    // =====================================

    /**
     * Reduces a player's health by a specified amount.
     */
    public static boolean reduceHealth(obj_id player, int amount) throws InterruptedException
    {
        return setAttrib(player, HEALTH, getMaxAttrib(player, HEALTH) - amount);
    }

    /**
     * Reduces a player's action by a specified amount.
     */
    public static boolean reduceAction(obj_id player, int amount) throws InterruptedException
    {
        return setAttrib(player, ACTION, getMaxAttrib(player, ACTION) - amount);
    }

    /**
     * Deals split energy/kinetic damage to a player.
     */
    public static void dealSplitDamage(obj_id player, int amount) throws InterruptedException
    {
        damage(player, DAMAGE_ENERGY, HIT_LOCATION_BODY, amount / 2);
        damage(player, DAMAGE_KINETIC, HIT_LOCATION_BODY, amount / 2);
    }

    // =====================================
    // World Boss State Management
    // =====================================

    /**
     * Sets the world boss status objvar on tatooine (used for tracking).
     */
    public static void setWorldBossStatus(String bossKey, String status) throws InterruptedException
    {
        obj_id tatooine = getPlanetByName("tatooine");
        String objvarPath = "dungeon_finder.world_boss." + bossKey;
        if (hasObjVar(tatooine, objvarPath))
        {
            removeObjVar(tatooine, objvarPath);
        }
        setObjVar(tatooine, objvarPath, status);
    }

    /**
     * Marks a world boss as active.
     */
    public static void markBossActive(String bossKey) throws InterruptedException
    {
        setWorldBossStatus(bossKey, "Active");
    }

    /**
     * Marks a world boss as inactive.
     */
    public static void markBossInactive(String bossKey) throws InterruptedException
    {
        setWorldBossStatus(bossKey, "Inactive");
    }

    // =====================================
    // Reward Distribution
    // =====================================

    /**
     * Processes Jedi Elder quest signals for eligible players.
     */
    public static void processJediElderQuest(obj_id[] players) throws InterruptedException
    {
        for (obj_id player : players)
        {
            if (isIdValid(player))
            {
                if (utils.getPlayerProfession(player) == utils.FORCE_SENSITIVE)
                {
                    if (groundquests.isQuestActive(player, JEDI_ELDER_QUEST) && groundquests.isTaskActive(player, JEDI_ELDER_QUEST, JEDI_ELDER_TASK))
                    {
                        groundquests.sendSignal(player, JEDI_ELDER_SIGNAL);
                    }
                }
            }
        }
    }

    /**
     * Distributes world boss tokens to all eligible players.
     * @param players Array of players in range
     * @param bossKey The objvar key for tracking kills (e.g., "mando", "rolii")
     * @param titleName The display name of the title
     * @param titleSkill The skill to grant for the title
     */
    public static void distributeRewards(obj_id[] players, String bossKey, String titleName, String titleSkill) throws InterruptedException
    {
        obj_id[] finalList = station_lib.processPlayerListAndRemoveDuplicates(players);
        for (obj_id player : finalList)
        {
            if (isIdValid(player))
            {
                obj_id token = static_item.createNewItemFunction(TOKEN_ITEM, player);
                if (isIdValid(token))
                {
                    int multiplier = utils.getIntBonusValue("wb");
                    int newCount = BASE_TOKEN_AMOUNT * multiplier;
                    setCount(token, newCount);
                    sendSystemMessage(player, "You have received " + colorText("DAA520", String.valueOf(newCount)) + " World Boss Tokens.", null);
                }
                showLootBox(player, new obj_id[]{token});
                processKillCount(player, bossKey, titleName, titleSkill);
            }
        }
    }
    /**
     * Processes kill count and grants title if eligible.
     */
    public static void processKillCount(obj_id player, String bossKey, String titleName, String titleSkill) throws InterruptedException
    {
        String killObjvar = "wb." + bossKey;
        String titleObjvar = "wb." + bossKey + "_title";

        if (!hasObjVar(player, killObjvar))
        {
            setObjVar(player, killObjvar, 1);
        }
        else
        {
            int count = getIntObjVar(player, killObjvar);
            if (count >= TITLE_KILL_REQUIREMENT && !hasObjVar(player, titleObjvar))
            {
                broadcast(player, "You have received the title " + colorText("DAA520", titleName) + " for killing this world boss " + TITLE_KILL_REQUIREMENT + " times!");
                grantSkill(player, titleSkill);
                setObjVar(player, titleObjvar, 1);
            }
            else
            {
                setObjVar(player, killObjvar, count + 1);
            }
        }
    }

    /**
     * Grants bonus tokens to the player who landed the death blow.
     */
    public static void grantDeathblowBonus(obj_id landedDeathBlow, String bossName) throws InterruptedException
    {
        if (isIdValid(landedDeathBlow))
        {
            obj_id victorInv = utils.getInventoryContainer(landedDeathBlow);
            static_item.createNewItemFunction(TOKEN_ITEM, victorInv, DEATHBLOW_BONUS_TOKENS);
            broadcast(landedDeathBlow, "You have received " + colorText("DAA520", String.valueOf(DEATHBLOW_BONUS_TOKENS)) + " World Boss Tokens for landing the final blow!");
        }
        else
        {
            LOG("ethereal", "[World Boss System]: (" + bossName + ") No valid ID for deathblower. Not handing out bonus tokens.");
        }
    }

    /**
     * Handles the complete reward distribution for a world boss death.
     */
    public static void handleCorpseRewards(obj_id self, String bossKey, String titleName, String titleSkill, String bossLogName) throws InterruptedException
    {
        obj_id landedDeathBlow = getObjIdObjVar(self, xp.VAR_LANDED_DEATHBLOW);
        obj_id[] players = getPlayerCreaturesInRange(self, titan_player.WORLD_BOSS_CREDIT_RANGE);

        processJediElderQuest(players);
        distributeRewards(players, bossKey, titleName, titleSkill);
        grantDeathblowBonus(landedDeathBlow, bossLogName);
    }

    // =====================================
    // Utility Functions
    // =====================================

    /**
     * Creates colored text for system messages.
     */
    public static String colorText(String color, String text)
    {
        return "\\#" + color + text + "\\#.";
    }

    /**
     * Gets a random message from an array.
     */
    public static String getRandomMessage(String[] messages)
    {
        return messages[rand(0, messages.length - 1)];
    }

    /**
     * Broadcasts a message to all players in an array.
     */
    public static void broadcastToPlayers(obj_id[] players, String message) throws InterruptedException
    {
        if (players == null || players.length == 0)
        {
            return;
        }
        for (obj_id player : players)
        {
            if (isIdValid(player))
            {
                broadcast(player, message);
            }
        }
    }

    /**
     * Applies buffs to the boss when entering combat.
     */
    public static void applyBossBuffs(obj_id self, String[] buffNames, float duration) throws InterruptedException
    {
        for (String buffName : buffNames)
        {
            buff.applyBuff(self, buffName, duration);
        }
    }
}
