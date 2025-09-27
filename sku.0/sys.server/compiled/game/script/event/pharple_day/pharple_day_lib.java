package script.event.pharple_day;/*
@Origin: dsrc.script.event.pharple_day
@Author:  BubbaJoeX
@Purpose: Consts and methods for Pharple Day
@Requirements: <no requirements>
@Notes: Remove template and replace with pure collection system. No need to spawn the objects for them to just be deleted.
@Created: Thursday, 8/22/2024, at 9:12 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.library.colors;
import script.library.combat;
import script.obj_id;
import script.string_id;

public class pharple_day_lib extends script.base_script
{
    public static final String CREATURE_SCRIPT = "event.pharple_day.spawner_child";
    public static final String CREATURE_TEMPLATE = "pharple_day_pharple";
    public static final String FEATHER_TEMPLATE = "object/tangible/pharple_day/pharple_plucked_feather.iff";
    //------------------------------------------------------------------------------------------//
    public static final String TOTAL_FEATHER_VAR = "pd.total_feathers";
    public static final String FEATHER_COLLECTION_NAME = "pharple_day_pluck";
    public static final int MAX_FEATHERS = 24;
    public static final String FEATHER_DROP_MSG = "You pluck a feather from the pharple.";
    public static final String FEATHER_DROP_FAIL_MSG = "You failed to pluck a feather from the pharple and have enraged it!";
    public static final float FEATHER_DISTANCE_LOCKOUT = 15.0f;
    public static final float FEATHER_USE_DISTANCE = 3.0f;
    public static final int PHARPLE_DIFFICULTY = MODE.EASY.ordinal();
    public static final String PHARPLE_DAY_TITLE = "Notorious Plucker";
    public static float FEATHER_DROP_CHANCE = 0.10f;
    public float getFeatherRoll = rand(0.0f, 100.0f);

    public boolean isRollSuccess(float roll)
    {
        return roll <= FEATHER_DROP_CHANCE;
    }

    public boolean isCollectionFull(obj_id player)
    {
        return getCollectionSlotValue(player, FEATHER_COLLECTION_NAME) >= MAX_FEATHERS;
    }

    public boolean canPluck(obj_id player, obj_id pharple)
    {
        return (getDistance(player, pharple) <= FEATHER_USE_DISTANCE);
    }

    public void enrageCreature(obj_id plucker, obj_id pharple) throws InterruptedException
    {
        int[] buffList = buff.getAllBuffs(plucker);
        for (int buffCrc : buffList)
        {
            if (buffCrc != 0)
            {
                buff.applyBuff(pharple, buffCrc);
            }
        }
        setLevel(pharple, getLevel(plucker));
        setNpcDifficulty(pharple, 2);
        string_id message = new string_id("-ENRAGED-");
        showFlyText(pharple, message, 1.5f, colors.YELLOWGREEN);
        combat.startCombat(plucker, pharple);
    }

    public void handlePluck(obj_id who, obj_id creature, obj_id resultInventory) throws InterruptedException
    {
        if (!isIdValid(who) || !isIdValid(creature) || !isIdValid(resultInventory))
        {
            return;
        }
        if (isCollectionFull(who))
        {
            broadcast(who, "You have plucked the maximum number of feathers allowed.");
            return;
        }
        if (!canPluck(who, creature))
        {
            broadcast(who, "You are too far away to pluck a feather.");
            return;
        }
        if (isRollSuccess(getFeatherRoll))
        {
            obj_id feather = createObject(FEATHER_TEMPLATE, resultInventory, "");
            if (isIdValid(feather))
            {
                broadcast(who, "You have plucked a feather.");
                setObjVar(who, TOTAL_FEATHER_VAR, getIntObjVar(who, TOTAL_FEATHER_VAR) + 1);
            }
        }
        else
        {
            broadcast(who, FEATHER_DROP_FAIL_MSG);
            enrageCreature(who, creature);
        }
    }

    /*
        Variables
        CREATURE_SCRIPT: The script that will be attached to the creature object that will be spawned.
        CREATURE_TEMPLATE: The template of the creature object that will be spawned.
        FEATHER_TEMPLATE: The template of the feather object that will be created when a player plucks a feather.
        TOTAL_FEATHER_VAR: The objvar that will store the total number of feathers a player has collected.
        FEATHER_COLLECTION_NAME: The collection name that will be used to store the feathers.
        MAX_FEATHERS: The maximum number of feathers a player can collect.
        FEATHER_DROP_CHANCE: The chance that a feather will drop when a player plucks a feather. This should be really low, but not impossible.
        FEATHER_DROP_MSG: The message that will be displayed when a player successfully plucks a feather.
        FEATHER_DROP_MSG_BONUS: The message that will be displayed when a player successfully plucks a feather and gets a bonus feather.
        FEATHER_DROP_FAIL_MSG: The message that will be displayed when a player fails to pluck a feather.
        FEATHER_DISTANCE_LOCKOUT: The distance a player must be away from the last pluck location.
        FEATHER_USE_DISTANCE: The distance a player must be from the pharple to pluck a feather.
        PHARPLE_DIFFICULTY: The difficulty level of the pharple. (0 = normal, 1 = elite, 2 = boss)
        PHARPLE_DAY_TITLE: The title that will be awarded to players who collect all the feathers.

        Methods
        isRollSuccess: Checks if the roll was successful.
        isCollectionFull: Checks if the collection is full.
        canPluck: Checks if the player can pluck a feather.
        enrageCreature: Enrages the creature. Apply all buffs from player to pharple and match level.
        handlePluck: Handles the plucking of a feather.

     */
    //------------------------------------------------------------------------------------------//
    public enum MODE
    {
        EASY, NORMAL, HARD
    }
}
