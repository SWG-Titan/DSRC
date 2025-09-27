package script.event.halloween;/*
@Origin: dsrc.script.event.halloween.trick_thief
@Author: BubbaJoeX
@Purpose: Handles the Treat Thief for the Galactic Moon Festival.
@Notes;
    This world boss has six mechanics.
        1. The Treat Thief will spawn minions in a ring around him.
        2. The Treat Thief will spawn a sidekick to assist him.
        3. The Treat Thief will steal credits from players.
        4. The Treat Thief will spawn a second sidekick to assist him.
        5. The Treat Thief will spawn a second ring of minions around him.
        6. The Treat Thief will apply a buff to himself.
    This script is meant to be used by GMs to spawn the Treat Thief for the event.
    To set the loot drop template, change the LOOT_DROP_TEMPLATE string. Or refactor to a static item.
@Created: Sunday, 2/25/2024, at 11:42 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.*;
import script.location;
import script.menu_info;
import script.obj_id;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class treat_thief extends script.base_script
{
    public static final String RING_EVENT_ONE = "event_halloween_minion_01";
    public static final String RING_EVENT_TWO = "event_halloween_minion_02";
    public static final int RING_EVENT_NUM_MOBS = 12;
    public static final float RING_EVENT_MOB_RANGE = 24.0f;
    public static final String SIDEKICK_TEMPLATE_ONE = "event_halloween_sidekick_01";
    public static final String SIDEKICK_TEMPLATE_TWO = "event_halloween_sidekick_02";
    public static final String SIDEKICK_BUFF = "event_halloween_sidekick_buff";
    public static final String FINAL_BUFF = "event_halloween_boss_last_chance";
    public static final int FINAL_BUFF_DURATION = 5;
    public static final int FINAL_BUFF_POWER = 100;
    public static final float RUBBERBAND_DISTANCE = 16.0f;
    public static final String LOOT_DROP_TEMPLATE = "item_event_token_01_01";

    public treat_thief()
    {
    }

    public static int createCircleSpawn(obj_id self, obj_id target, String creature, int amount, float distance) throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target))
        {
            return SCRIPT_CONTINUE;
        }
        location loc = getLocation(target);
        float x;
        float z;
        for (int i = 0; i < amount; i++)
        {
            float angle = (float) (i * (360 / amount));
            x = loc.x + (float) Math.cos(angle) * distance;
            z = loc.z + (float) Math.sin(angle) * distance;
            obj_id creatureObj = create.object(creature, new location(x, loc.y, z, loc.area));
            faceTo(self, creatureObj);
        }
        return SCRIPT_CONTINUE;
    }

    public static void pushPlayer(obj_id self, obj_id player, float distance) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return;
        }
        location loc = getLocation(player);
        float x;
        float z;
        float angle = rand(0.0f, 360.0f);
        x = loc.x + (float) Math.cos(angle) * distance;
        z = loc.z + (float) Math.sin(angle) * distance;
        setLocation(player, new location(x, loc.y, z, loc.area));
    }

    public static boolean isHalloween()
    {
        ZonedDateTime stamp = ZonedDateTime.now(ZoneId.of("America/Chicago"));
        LocalDate now = stamp.toLocalDate();
        return now.getMonthValue() == 10 && now.getDayOfMonth() == 31;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!isHalloween())
        {
            LOG("events", "GMF Frankenabubb - Warning: Not Halloween.");
            return SCRIPT_CONTINUE;
        }
        setInvulnerable(self, false);
        setName(self, "Galactic Treat Thief");
        setScale(self, 2.0f);
        startFog(self);
        LOG("events", "GMF Treat Thief - Attached and started fog.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        LOG("events", "GMF Treat Thief - Initialized.");
        return SCRIPT_CONTINUE;
    }

    public int OnEnteredCombat(obj_id self) throws InterruptedException
    {
        if (utils.hasScriptVar(self, "event.halloween_spam"))
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            obj_id[] attackers = getHateList(self);
            if (attackers == null || attackers.length == 0)
            {
                return SCRIPT_CONTINUE;
            }
            StringBuilder attackerNames = new StringBuilder();
            for (obj_id person : attackers)
            {
                attackerNames.append(getPlayerFullName(person)).append(", ");
            }
            LOG("events", "GMF Treat Thief - Engaged with player(s) " + attackerNames);
            utils.setScriptVar(self, "event.halloween_spam", 1);
            sendSystemMessageGalaxyTestingOnly("[World Event] The Treat Thief has been engaged on " + toUpper(getCurrentSceneName(), 0));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id weapon, int[] damage) throws InterruptedException
    {
        if (!utils.hasScriptVar(self, "event.halloween_group_checked"))
        {
            handleGroupHealthIncrease(self, attacker);
        }

        if (beast_lib.isBeast(attacker) || pet_lib.isPet(attacker))
        {
            attacker = getMaster(attacker);
        }

        if (!isPlayer(attacker))
        {
            return SCRIPT_CONTINUE;
        }

        handleHealthRatios(self, attacker);
        return SCRIPT_CONTINUE;
    }

    private void handleGroupHealthIncrease(obj_id self, obj_id attacker) throws InterruptedException
    {
        if (group.isGrouped(attacker))
        {
            obj_id[] groupMembers = getGroupMemberIds(attacker);
            if (groupMembers == null || groupMembers.length == 0)
            {
                return;
            }
            for (obj_id groupMember : groupMembers)
            {
                addToHealth(self, 12000);
                broadcast(attacker, "[World Boss System]: Being grouped has increased this enemy's statistics.");
            }
            utils.setScriptVar(self, "event.halloween_group_checked", 1);
        }
    }

    private void handleHealthRatios(obj_id self, obj_id attacker) throws InterruptedException
    {
        float max = getMaxHealth(self);
        float current = getHealth(self);
        float ratio = current / max;

        if (ratio <= 0.90f && !utils.hasScriptVar(self, "event.halloween_first_ratio"))
        {
            triggerFirstRatioEvent(self, attacker);
        }
        else if (ratio <= 0.75f && !utils.hasScriptVar(self, "event.halloween_second_ratio"))
        {
            triggerSecondRatioEvent(self, attacker);
        }
        else if (ratio <= 0.60f && !utils.hasScriptVar(self, "event.halloween_third_ratio"))
        {
            triggerThirdRatioEvent(self, attacker);
        }
        else if (ratio <= 0.45f && !utils.hasScriptVar(self, "event.halloween_fourth_ratio"))
        {
            triggerFourthRatioEvent(self, attacker);
        }
        else if (ratio <= 0.25f && !utils.hasScriptVar(self, "event.halloween_fifth_ratio"))
        {
            triggerFifthRatioEvent(self);
        }
        else if (ratio <= 0.10f && !utils.hasScriptVar(self, "event.halloween_final_ratio"))
        {
            triggerFinalRatioEvent(self);
        }
    }

    private void triggerFirstRatioEvent(obj_id self, obj_id attacker) throws InterruptedException
    {
        chat.chat(self, "You cannot have my goodies! Minions, where are you!!!");
        utils.setScriptVar(self, "event.halloween_first_ratio", 1);
        createCircleSpawn(self, self, RING_EVENT_ONE, RING_EVENT_NUM_MOBS, RING_EVENT_MOB_RANGE);
    }

    private void triggerSecondRatioEvent(obj_id self, obj_id attacker) throws InterruptedException
    {
        chat.chat(self, "My ledger seems a little light.. I'll be taking those credits, " + getName(attacker) + "!");
        broadcast(attacker, "The Treat Thief has stolen 1,000 credits from you!");
        if (money.hasFunds(attacker, money.MT_TOTAL, 1000))
        {
            money.withdraw(attacker, 1000);
        }
        pushPlayer(self, attacker, RUBBERBAND_DISTANCE);
        utils.setScriptVar(self, "event.halloween_second_ratio", 1);
    }

    private void triggerThirdRatioEvent(obj_id self, obj_id attacker) throws InterruptedException
    {
        chat.chat(self, "Those first minions were weak! Maybe these will be more of a challenge.");
        createCircleSpawn(self, attacker, RING_EVENT_TWO, RING_EVENT_NUM_MOBS, RING_EVENT_MOB_RANGE);
        utils.setScriptVar(self, "event.halloween_third_ratio", 1);
    }

    private void triggerFourthRatioEvent(obj_id self, obj_id attacker) throws InterruptedException
    {
        chat.chat(self, "Stay away! I have sweet treats to steal from innocent spacers!");
        spawnSidekick(self, SIDEKICK_TEMPLATE_ONE, SIDEKICK_BUFF, 1.0f, 2.5f);
        utils.setScriptVar(self, "event.halloween_fourth_ratio", 1);
    }

    private void triggerFifthRatioEvent(obj_id self) throws InterruptedException
    {
        spawnSidekick(self, SIDEKICK_TEMPLATE_TWO, SIDEKICK_BUFF, 1.0f, 40.0f);
        utils.setScriptVar(self, "event.halloween_fifth_ratio", 1);
    }

    private void triggerFinalRatioEvent(obj_id self) throws InterruptedException
    {
        if (!buff.hasBuff(self, FINAL_BUFF))
        {
            buff.applyBuff(self, FINAL_BUFF, FINAL_BUFF_DURATION, FINAL_BUFF_POWER);
            utils.setScriptVar(self, "event.halloween_final_ratio", 1);
        }
    }

    public void spawnSidekick(obj_id self, String creature, String buffName, float dur, float scale) throws InterruptedException
    {
        obj_id sidekick = create.object(creature, getLocation(self));
        setScale(sidekick, scale);
        buff.applyBuff(sidekick, buffName, dur);
        LOG("events", "GMF Treat Thief - Summoning sidekick!");
        chat.chat(sidekick, "Utinni!");
    }

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        int lootChanc = rand(1, 100);
        if (lootChanc <= 50)
        {
            obj_id loot = createObject(LOOT_DROP_TEMPLATE, utils.getInventoryContainer(self), "");//TODO: figure out halloween specific world boss drops
            setName(loot, "Halloween Treat");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    private void startFog(obj_id device) throws InterruptedException
    {
        stopClientEffectObjByLabel(device, "halloweenFog");
        playClientEffectObj(device, "clienteffect/halloween_fog_machine.cef", device, "", null, "halloweenFog");
        messageTo(device, "continueFog", null, 18.0f, false);
    }

    public void stopFog(obj_id device) throws InterruptedException
    {
        stopClientEffectObjByLabel(device, "halloweenFog");
    }

    public int continueFog(obj_id self, dictionary params) throws InterruptedException
    {
        if (hasObjVar(self, "fogOn"))
        {
            playClientEffectObj(self, "clienteffect/halloween_fog_machine.cef", self, "", null, "halloweenFog");
            messageTo(self, "continueFog", null, 18.0f, false);
        }
        return SCRIPT_CONTINUE;
    }
}