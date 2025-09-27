package script.planet;/*
@Origin: dsrc.script.planet
@Author: BubbaJoeX
@Purpose: Sunburn, fatigue and dehydration script.
@Requirements
    This script uses wrapped cherry-picked methods from SWG-Source/dsrc:3.1, modify accordingly. (3.1 is unstable. stabilize it yourselves.)
@Notes:
    This script runs per planet, not per player. It is attached to the planet object. This may cause issues with zoning, but we can remove that with stopListeningToMessage OnLogin.
    This script is basically a stomach revival script with a few tweaks. It includes sun damage, fatigue and dehydration.
    If a player is not near a building, they will take sun damage. If they are not eating or drinking, they will take fatigue and dehydration damage.
    If they have thermal paste, they will take half damage from sunburn.
    If they have a buff with food_dish or food_dessert buff group, they will not take fatigue damage, as they are not fatigued.
    If they have a buff with food_drink buff group, they will not take dehydration damage, as they are not dehydrated.
    If they have a buff with food_drink buff group, they will not take dehydration damage, as they are not dehydrated.
    If a player is level 80 or lower, they will take a small amount of sun damage. If they are above level 80, they will take 1/8th of their health in damage.
    If a player is fatigued or dehydrated, they will take 1/3rd of their health in damage.
    All three are checked every 15 minutes synchronously.
    If all three conditions are met, the player will take all three types of damage, resulting in a total of 6/8th of their health in damage.
-------------------------------------------------------------------------------------------------------------------------
@Created: Thursday, 4/4/2024, at 9:54 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
-------------------------------------------------------------------------------------------------------------------------
*/

import script.dictionary;
import script.library.buff;
import script.library.combat;
import script.library.utils;
import script.location;
import script.obj_id;

public class planet_uv extends script.planet.planet_base
{
    public static float PLANETSIDE = 8192f;
    public static float TIME_DELAY = 900;
    public static String CLIENTEFFECT_DIZZY = "clienteffect/combat_special_defender_dizzy.cef";
    public boolean enabled = false;

    public int OnAttach(obj_id self) throws InterruptedException
    {
        this.OnInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (!enabled)
        {
            return SCRIPT_CONTINUE;
        }
        messageTo(self, "handleStartUVDelay", null, TIME_DELAY, false);
        setObjVar(getPlanetByName("tatooine"), "planet_uv_controller." + getCurrentSceneName(), self);
        LOG("ethereal", "[Weather | UV]: UV script initialized on " + getCurrentSceneName() + ".");
        return SCRIPT_CONTINUE;
    }

    public int handleStartUVDelay(obj_id self, dictionary params) throws InterruptedException
    {
        if (!enabled)
        {
            return SCRIPT_CONTINUE;
        }
        LOG("ethereal", "[Weather | UV]: UV script started on " + getCurrentSceneName() + ".");
        startUV(self);
        return SCRIPT_CONTINUE;
    }

    public void startUV(obj_id self) throws InterruptedException
    {
        location origin = new location(0, 0, 0, getCurrentSceneName());
        obj_id[] players = getAllPlayers(origin, PLANETSIDE);
        LOG("ethereal", "[Weather | UV]: Found " + players.length + " players on " + getCurrentSceneName() + ".");
        for (int i = 0; i < players.length; i++)
        {
            if (isPlayer(players[i]))
            {
                if (isSunburned(players[i]))
                {
                    LOG("ethereal", "[Weather | UV]: Player " + players[i] + " is sunburned.");
                    handleSunburn(self, players[i]);
                }
                if (isFatigued(players[i]))
                {
                    LOG("ethereal", "[Weather | UV]: Player " + players[i] + " is fatigued.");
                    handleFatigue(self, players[i]);
                }
                if (isDehydrated(players[i]))
                {
                    LOG("ethereal", "[Weather | UV]: Player " + players[i] + " is dehydrated.");
                    handleDehydration(self, players[i]);
                }
            }
        }
    }

    public void handleSunburn(obj_id self, obj_id player) throws InterruptedException
    {
        LOG("ethereal", "[Weather | UV]: Player " + getPlayerFullName(player) + " | Entered handleSunburn.");
        int uvDamage = getHealth(player) / 3; //this is 1/8th of the player's health.
        if (!hasItem(player, "tube_paste", true))
        {
            uvDamage = uvDamage / 2; //if a player had 12000 health, they would take 1500 damage. If they have thermal paste, they would take 750 damage.
        }
        obj_id tubePaste = utils.getItemByTemplateInInventoryOrEquipped(player, "object/tangible/loot/generic_usable/tube_paste_generic.iff");
        if (hasScript(tubePaste, "planet.uv_sunscreen"))
        {
            broadcast(player, "You have applied sunscreen and are protected from the sun's harmful rays.");
            decrementCount(tubePaste);
        }
        else
        {
            //@note: total formula: uvDamage = getHealth(player) / 2/8th - (hasItem(player, "paste", true) ? uvDamage / 2 : 1);
            broadcast(player, "Your skin is burning from the sun! You need to find shade or sunscreen!");
            playClientEffectObj(player, CLIENTEFFECT_DIZZY, player, "head");
            damage(player, DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, uvDamage);
            broadcast(player, "You have taken " + uvDamage + " damage from a sunburn!");
        }
        dictionary params = new dictionary();
        params.put("type", 1);
        params.put("player", player);
        LOG("ethereal", "[Weather | UV]: Queued handleTimedRecheck (sunburn) for " + getPlayerFullName(player) + ".");
        messageTo(self, "handleTimedRecheck", params, TIME_DELAY, false);
    }

    public void handleFatigue(obj_id self, obj_id player)
    {
        LOG("ethereal", "[Weather | UV]: Player " + getPlayerFullName(player) + " | Entered handleFatigue.");
        int fatigueDamage = getHealth(player) / 3;
        playClientEffectObj(player, CLIENTEFFECT_DIZZY, player, "head");
        damage(player, DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, fatigueDamage);
        broadcast(player, "You are fatigued and need to find proper sustenance.");
        dictionary params = new dictionary();
        params.put("type", 2);
        params.put("player", player);
        LOG("ethereal", "[Weather | UV]: Queued handleTimedRecheck (fatigue) for " + getPlayerFullName(player) + ".");
        messageTo(self, "handleTimedRecheck", params, TIME_DELAY, false);
    }

    public void handleDehydration(obj_id self, obj_id player)
    {
        LOG("ethereal", "[Weather | UV]: Player " + getPlayerFullName(player) + " | Entered handleDehydration.");
        int dehydrationDamage = getHealth(player) / 3;
        playClientEffectObj(player, CLIENTEFFECT_DIZZY, player, "head");
        damage(player, DAMAGE_ELEMENTAL_HEAT, HIT_LOCATION_BODY, dehydrationDamage);
        broadcast(player, "You are dehydrated and need to find something to drink.");
        dictionary params = new dictionary();
        params.put("type", 3);
        params.put("player", player);
        LOG("ethereal", "[Weather | UV]: Queued handleTimedRecheck (dehydration) for " + getPlayerFullName(player) + ".");
        messageTo(self, "handleTimedRecheck", params, TIME_DELAY, false);
    }

    public boolean isSunburned(obj_id player)
    {
        //@Note: Check to see if the player is near a building or in a building. If they are not, they can get sunburned. Since we can't use shadows for calculations, we are assuming that if a player is near a building they are "shaded".
        if (!isInWorldCell(player))
        {
            return false;
        }
        return !isNearShade(player);
    }

    public boolean isNearShade(obj_id player)
    {
        obj_id[] buildings = getObjectsInRange(getLocation(player), (rand(16f, 18f))); //@Note: account for a building's space between origin and collision extents
        for (obj_id building : buildings)
        {
            if (getTemplateName(building).contains("object/building/player") //@Note: better way to check for buildings? GOT perhaps instead of each planet's subfolder. GOT_structure_civic?
                    || (getTemplateName(building).contains("object/building/corellia"))
                    || (getTemplateName(building).contains("object/building/dantooine"))
                    || (getTemplateName(building).contains("object/building/naboo"))
                    || (getTemplateName(building).contains("object/building/rori"))
                    || (getTemplateName(building).contains("object/building/tatooine"))
                    || (getTemplateName(building).contains("object/building/lok"))
                    || (getTemplateName(building).contains("object/building/talus"))
                    || (getTemplateName(building).contains("object/building/yavin4")))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isFatigued(obj_id player) throws InterruptedException
    {
        //check to see if player has has a buff with a group of "food_dish or food_dessert";
        int fatigueCheckDish = buff.getBuffOnTargetFromGroup(player, "food_dish");
        int fatigueCheckDessert = buff.getBuffOnTargetFromGroup(player, "food_dessert");
        return fatigueCheckDish == 0 && fatigueCheckDessert == 0; //if both are 0, player is fatigued, else they are not.
    }

    public boolean isDehydrated(obj_id player) throws InterruptedException
    {
        //check to see if player has a buff with a group of "food_drink";
        int anyThirstBuff = buff.getBuffOnTargetFromGroup(player, "food_drink");
        return anyThirstBuff == 0;
    }

    public boolean isUVPlanet(String planet)
    {
        String[] uvPlanets = {"corellia", "dantooine", "lok", "naboo", "rori", "tatooine", "talus", "yavin4"};
        for (int i = 0; i < uvPlanets.length; i++)
        {
            if (planet.equals(uvPlanets[i]))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(obj_id player, String template, boolean useContainsOverEquals) throws InterruptedException
    {
        obj_id source = utils.getInventoryContainer(player);
        obj_id[] contents = getContents(source);
        if (contents == null)
        {
            return false;
        }
        for (obj_id item : contents)
        {
            if (useContainsOverEquals)
            {
                if (getTemplateName(item).contains(template))
                {
                    return true;
                }
            }
            else
            {
                if (getTemplateName(item).equals(template))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public int handleTimedRecheck(obj_id self, dictionary params) throws InterruptedException
    {
        //@lint: ignoreUnused(params);
        //@lint: suppressLoops(this);
        int type = params.getInt("type");
        obj_id player = params.getObjId("player");
        if (type == 1)
        {
            if (isSunburned(player))
            {
                handleSunburn(self, player);
            }
        }
        else if (type == 2)
        {
            if (isFatigued(player))
            {
                broadcast(player, "You are fatigued and need to find proper sustenance.");
                handleFatigue(self, player);
            }
        }
        else if (type == 3)
        {
            if (isDehydrated(player))
            {
                broadcast(player, "You are dehydrated and need to find proper sustenance.");
                handleDehydration(self, player);
            }
        }
        else
        {
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
}
