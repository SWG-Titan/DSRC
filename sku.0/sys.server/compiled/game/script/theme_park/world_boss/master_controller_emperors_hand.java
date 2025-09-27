package script.theme_park.world_boss;/*
@Origin: dsrc.script.theme_park.world_boss.master_controller_emperors_hand
@Author:
    BubbaJoeX
    Talisa
@Purpose: Handles the world boss upon spawning, combat ratios and death.
@Notes;
    This world boss is not used.
@Requirements: <no requirements>
@Created: Sunday, 2/01/2023, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.obj_id;

public class master_controller_emperors_hand extends script.base_script
{
    public static final String VOLUME_NAME = "aggressive_area";
    public String[] EMPERORS_HAND_MSGS = {
            "I am Aralina Silk, The Hand of his Royal Majesty, The Emperor!",
            "You do not know the Power of the Darkside!",
            "I must obey my Master - and he want's your Head!",
            "The Emperor has decreed your destruction!",
            "The Sith will rule the Galaxy!",
            "Long live the Empire!",
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        obj_id tatooine = getPlanetByName("tatooine");
        if (hasObjVar(tatooine, "dungeon_finder.world_boss.emperors_hand"))
        {
            removeObjVar(tatooine, "dungeon_finder.world_boss.emperors_hand");
        }
        setObjVar(tatooine, "dungeon_finder.world_boss.emperors_hand", "Active");
        or_utils.markAsEventSpawn(self);
        or_player.doWorldBossAnnounce(self, or_player.WORLD_BOSS_EMPERORS_HAND);
        return SCRIPT_CONTINUE;
    }

    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        obj_id[] allPlayersNearby = getAllPlayers(getLocation(self), 128.0f);
        if (allPlayersNearby != null)
        {
            for (obj_id nearby : allPlayersNearby)
            {
                groundquests.sendSignal(nearby, "completedWorldBossNabooEmperorsHand");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        if (isGod(killer))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id tatooine = getPlanetByName("tatooine");
        if (hasObjVar(tatooine, "dungeon_finder.world_boss.emperors_hand"))
        {
            removeObjVar(tatooine, "dungeon_finder.world_boss.emperors_hand");
        }
        setObjVar(tatooine, "dungeon_finder.world_boss.emperors_hand", "Inactive");
        or_player.doWorldBossDeathMsg(self, killer);
        return SCRIPT_CONTINUE;
    }

    public int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id wpn, int[] damage) throws InterruptedException
    {
        obj_id[] players = getPlayerCreaturesInRange(self, 64.0f);
        int health = getHealth(self);
        int maxHealth = getMaxHealth(self);
        int percentHealth = (health * 100) / maxHealth;
        if (attacker == self)
        {
            return SCRIPT_CONTINUE;
        }
        if (!utils.hasScriptVar(self, "chirp"))
        {
            chat.chat(self, EMPERORS_HAND_MSGS[rand(0, EMPERORS_HAND_MSGS.length - 1)]);
            utils.setScriptVar(self, "chrip", 1);
        }
        if (percentHealth <= 100)
        {
            if (!utils.hasScriptVar(self, "handHasSpawned"))
            {
                chat.chat(self, "I am Aralina Silk, The Hand of his Royal Majesty, The Emperor!");
            }
        }
        if (percentHealth <= 90)
        {
            if (!utils.hasScriptVar(self, "handMedBuffs"))
            {
                buff.removeAllBuffs(self);
                for (obj_id who : players)
                {
                    broadcast(who, "Aralina Silk has summoned the use of Medical Buffs through the Darkside of the Force!");
                }
                chat.chat(self, "You will fail!");
                buff.applyBuff((self), "me_buff_health_2", 900);
                buff.applyBuff((self), "me_buff_action_3", 900);
                buff.applyBuff((self), "me_buff_strength_3", 900);
                buff.applyBuff((self), "me_buff_agility_3", 900);
                buff.applyBuff((self), "me_buff_precision_3", 900);
                buff.applyBuff((self), "me_buff_melee_gb_1", 900);
                buff.applyBuff((self), "me_buff_ranged_gb_1", 900);
                utils.setScriptVar(self, "handMedBuffs", 1);
                return SCRIPT_CONTINUE;
            }
        }
        if (percentHealth <= 75)
        {
            if (!utils.hasScriptVar(self, "stormtroopersHasSpawned"))
            {
                chat.chat(self, "You will suffer for your lack of vision!");
                for (obj_id who : players)
                {
                    broadcast(who, "The Emperors Hand has summoned reinforcements from the 501st Stormtrooper Detachment!");
                }
                or_player.createCircleSpawn(self, self, "emperors_hand_stormtroopers", 8, 5);
                utils.setScriptVar(self, "StormtroopersHasSpawned", 1);
                return SCRIPT_CONTINUE;
            }
        }
        if (percentHealth <= 50)
        {
            if (!utils.hasScriptVar(self, "agentsHasSpawned"))
            {
                chat.chat(self, EMPERORS_HAND_MSGS[rand(0, EMPERORS_HAND_MSGS.length - 1)]);
                for (obj_id who : players)
                {
                    broadcast(who, "The Emperors Hand has summoned reinforcements from the Imperial Security Bureau!");
                }
                or_player.createCircleSpawn(self, self, "emperors_hand_isb_guards", 4, 5);
                utils.setScriptVar(self, "agentsHasSpawned", 1);
                return SCRIPT_CONTINUE;
            }
        }
        if (percentHealth <= 25)
        {
            if (!utils.hasScriptVar(self, "inquisitorsHasSpawned"))
            {
                chat.chat(self, EMPERORS_HAND_MSGS[rand(0, EMPERORS_HAND_MSGS.length - 1)]);
                for (obj_id who : players)
                {
                    broadcast(who, "The Emperors Hand has summoned reinforcements from the Imperial Inquisitors!");
                }
                or_player.createCircleSpawn(self, self, "emperors_hand_inquisitors", 2, 5);
                utils.setScriptVar(self, "inquisitorsHasSpawned", 1);
                return SCRIPT_CONTINUE;
            }
        }
        if (percentHealth <= 15)
        {
            if (!utils.hasScriptVar(self, "ig88Buff"))
            {
                chat.chat(self, EMPERORS_HAND_MSGS[rand(0, EMPERORS_HAND_MSGS.length - 1)]);
                for (obj_id who : players)
                {
                    broadcast(who, "The Emperors Hand has summoned additional defenses through the Darkside of the Force!");
                }
                buff.applyBuff(self, "ig88_shield", 1800, 100);
                utils.setScriptVar(self, "ig88Buff", 1);
                return SCRIPT_CONTINUE;
            }
        }
        if (percentHealth <= 8)
        {
            if (!utils.hasScriptVar(self, "hasLastStand"))
            {
                for (obj_id who : players)
                {
                    broadcast(who, "The Emperor's Hand goes into her Last Stand, triggering her last defenses!");
                }
                chat.chat(self, "This Is The Way.");
                buff.applyBuff(self, "crystal_buff", 60, 20);
                utils.setScriptVar(self, "hasLastStand", 1);
            }
        }
        if (percentHealth <= 2)
        {
            if (!utils.hasScriptVar(self, "hasBeenBombed"))
            {
                chat.chat(self, EMPERORS_HAND_MSGS[rand(0, EMPERORS_HAND_MSGS.length - 1)]);
                for (obj_id who : players)
                {
                    broadcast(who, "The most recent attack from \" + getFirstName(attacker) + \" has enraged The Emperor's Hand, causing her to increase and call down an Imperial Bombing Run on her own position!");
                }
                bombard(self, players);
                utils.setScriptVar(self, "hasBeenBombed", 1);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void bombard(obj_id self, obj_id[] targets) throws InterruptedException
    {
        if (targets == null)
        {
            return;
        }
        for (obj_id iTarget : targets)
        {
            playClientEffectObj(iTarget, "clienteffect/avatar_explosion_02.cef", iTarget, "");
            reduceHealth(iTarget, rand(5000, 7500));
            reduceAction(iTarget, rand(2500, 5000));
        }
    }

    public boolean reduceHealth(obj_id player, int amt)
    {
        return setAttrib(player, HEALTH, getMaxAttrib(player, HEALTH) - amt);
    }

    public boolean reduceAction(obj_id player, int amt)
    {
        return setAttrib(player, ACTION, getMaxAttrib(player, ACTION) - amt);
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        //award players token
        obj_id[] players = getPlayerCreaturesInRange(self, or_player.WORLD_BOSS_CREDIT_RANGE);
        for (obj_id player : players)
        {
            if (isIdValid(player))
            {
                obj_id token = static_item.createNewItemFunction("item_world_boss_token_01_01", player);
                if (isIdValid(token))
                {
                    int multiplier = getIntObjVar(getPlanetByName("tatooine"), "bonus.wb");
                    int newCount = 5 * multiplier;
                    setCount(token, newCount);
                    sendSystemMessage(player, "You have received " + color("DAA520", String.valueOf(newCount)) + " World Boss Tokens.", null);
                }
                showLootBox(player, new obj_id[]{token});
            }
        }
        return SCRIPT_CONTINUE;
    }

    public String color(String color, String text) throws InterruptedException
    {
        return "\\#" + color + text + "\\#.";
    }
}