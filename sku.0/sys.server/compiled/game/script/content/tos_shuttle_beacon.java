package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Allows players to warp to the TOS anywhere from the galaxy on cooldown.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 5/10/2024, at 12:14 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;
import script.library.prose;
import script.library.sui;
import script.library.utils;

import java.util.Random;

public class tos_shuttle_beacon extends base_script
{
    public static final float TIME_DELAY = 10.0f;
    public static final int REUSE_TIMER = 900;
    public static final String REUSE_VAR = "tos_shuttle_beacon.lastTime";

    private static String getCommString()
    {
        String[] variations = {
                "A shuttle is en route to your location. Please be patient.",
                "Your shuttle is on its way. Hold tight!",
                "A transport vehicle has been dispatched. Kindly wait...",
                "The shuttle is heading your way. Stand by!",
                "Your request is received, and a shuttle is arriving soon. Please wait..."
        };
        Random random = new Random();
        return variations[random.nextInt(variations.length)];
    }

    private static String getCommAppearance()
    {
        String[] variations = {
                "object/mobile/r2.iff",
                "object/mobile/r3.iff",
                "object/mobile/r4.iff",
                "object/mobile/r5.iff",
                "object/mobile/ra7_bug_droid.iff",
        };
        Random random = new Random();
        return variations[random.nextInt(variations.length)];
    }

    private static String getCommChatter()
    {
        String[] variations = {
                "sound/dro_r2_1_babble.snd",
                "sound/dro_r2_3_danger.snd",
                "sound/dro_astromech_converse_04.snd",
                "sound/dro_r2_4_repair.snd",
                "sound/dro_astromech_beep.snd",
                "sound/dro_astromech_yell.snd",
                "sound/dro_astromech_greeter_whistle.snd",
        };
        Random random = new Random();
        return variations[random.nextInt(variations.length)];
    }

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (combat.isInCombat(player))
        {
            broadcast(player, "You cannot call for a shuttle while in combat.");
            return SCRIPT_CONTINUE;
        }
        if (getContainedBy(self) == utils.getInventoryContainer(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel to Rally Point Nova"));
        }
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reset"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (combat.isInCombat(player))
        {
            broadcast(player, "You cannot call for a shuttle while in combat.");
            return SCRIPT_CONTINUE;
        }
        if (getContainedBy(self) != utils.getInventoryContainer(player))
        {
            broadcast(player, "This item must be in your inventory to use it.");
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (getCurrentSceneName().equals("dungeon_hub"))
            {
                broadcast(player, "You are already aboard Rally Point Nova!");
                return SCRIPT_CONTINUE;
            }
            if (getCalendarTime() < (getIntObjVar(player, REUSE_VAR) + REUSE_TIMER))
            {
                int timeLeft = REUSE_TIMER - (getCalendarTime() - getIntObjVar(player, REUSE_VAR));
                broadcast(player, "You cannot use this comlink for another " + timeLeft + " seconds!");
                return SCRIPT_CONTINUE;
            }
            else
            {
                sui.msgbox(self, player, "Are you sure you want to travel to Rally Point Nova?", sui.YES_NO, "handleConfirmation");
            }
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            removeObjVar(player, REUSE_VAR);
            broadcast(player, "[GM] You have reset your cooldown for Rally Point Nova travel.");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleConfirmation(obj_id self, dictionary params) throws InterruptedException
    {
        final String randomMessage = getCommString();
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_OK)
        {
            prose_package pp = new prose_package();
            prose.setStringId(pp, new string_id(randomMessage));
            dictionary d = new dictionary();
            d.put("who", player);
            if (isGod(player))
            {
                commPlayers(self, getCommAppearance(), getCommChatter(), 3.0f, player, pp);
                messageTo(self, "delayedWarp", d, 3.0f, false);
            }
            else
            {
                commPlayers(self, getCommAppearance(), getCommChatter(), TIME_DELAY, player, pp);
                messageTo(self, "delayedWarp", d, TIME_DELAY, false);
            }

        }
        else
        {
            broadcast(player, "You have chosen not to travel to Rally Point Nova.");
        }
        return SCRIPT_CONTINUE;
    }

    public int delayedWarp(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id who = params.getObjId("who");
        if (combat.isInCombat(who))
        {
            broadcast(who, "Due to you being in combat, your shuttle has departed.");
            return SCRIPT_CONTINUE;
        }
        if (isIncapacitated(who))
        {
            broadcast(who, "Due to you being incapacitated, your shuttle has departed.");
            return SCRIPT_CONTINUE;
        }
        if (isDead(who))
        {
            broadcast(who, "Due to you being dead, your shuttle has departed.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(who, "tos_shuttle_beacon.lastTime", getCalendarTime());
        warpPlayer(who, getLocationObjVar(getPlanetByName("tatooine"), "hub_marker"));
        return SCRIPT_CONTINUE;
    }

    public void warpPlayer(obj_id player, location loc) throws InterruptedException
    {
        warpPlayer(player, loc.area, loc.x, loc.y, loc.z, loc.cell, loc.x, loc.y, loc.z);
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        int lastUsed = getIntObjVar(player, REUSE_VAR);
        names[idx] = utils.packStringId(new string_id("Last used"));
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(player, REUSE_VAR));
        idx++;
        names[idx] = utils.packStringId(new string_id("Next use"));
        attribs[idx] = getCalendarTimeStringLocal_YYYYMMDDHHMMSS(getIntObjVar(player, REUSE_VAR) + REUSE_TIMER);
        idx++;
        String NO = "\\#DD1234" + "No" + "\\#FFFFFF";
        String YES = "\\#32CD32" + "Yes" + "\\#FFFFFF";
        if (getCalendarTime() < (lastUsed + REUSE_TIMER))
        {
            names[idx] = "ready";
            attribs[idx] = NO;
            idx++;
        }
        else
        {
            names[idx] = "ready";
            attribs[idx] = YES;
            idx++;
        }
        return SCRIPT_CONTINUE;
    }
}
