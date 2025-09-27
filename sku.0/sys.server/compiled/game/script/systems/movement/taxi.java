package script.systems.movement;/*
@Origin: dsrc.script.systems.movement
@Author: BubbaJoeX
@Purpose: Taxi system - Allows developer players to create a taxi that will transport players to a specific location. To create a token to load into the taxi, use the command /developer createTaxiToken <cost>. To create a taxi, use the command /developer createTaxi <index> <name>. To set the destination of a taxi, link the token to the taxi by typing in the token's OID.. To use the taxi, right-click the taxi and select "Traverse".
@Created: Tuesday, 9/26/2023, at 1:11 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.money;
import script.library.sui;
import script.library.utils;

public class taxi extends script.base_script
{
    public float TAXI_DELAY = 30.0f;

    public int OnAttach(obj_id self)
    {
        reInit(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        reInit(self);
        return SCRIPT_CONTINUE;
    }

    public int reInit(obj_id self)
    {
        if (!hasObjVar(self, "taxi.name"))
        {
            LOG("ethereal", "[Taxi]: " + self + " has no name. Returning..");
            return SCRIPT_CONTINUE;
        }
        else
        {
            String template = getTemplateName(self);
            if (template.contains("zonegate"))
            {
                LOG("ethereal", "[Taxi]: " + self + " is a zone gate. Setting name and description to reflect this.");
                setName(self, "Global Travel: " + getStringObjVar(self, "taxi.name"));
                setDescriptionString(self, "This transport will taxi you to a certain locations.");
                setCondition(self, CONDITION_INTERESTING);
                return SCRIPT_CONTINUE;
            }
            else
            {
                setName(self, "Taxi: " + getStringObjVar(self, "taxi.name"));
                setDescriptionString(self, "This transport will taxi you to a certain locations.");
                setCondition(self, CONDITION_INTERESTING);
            }

        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Traverse"));
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Link Token to Taxi")); //@Note: apparently you can't use OnGiveItem with GOT_terminal_misc.
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!hasObjVar(self, "taxi.location"))
            {
                broadcast(player, "This taxi has no destination set.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                if (utils.hasScriptVar(player, "usedTaxi"))
                {
                    broadcast(player, "You must wait before using this taxi again.");
                    return SCRIPT_CONTINUE;
                }
                int cost = getIntObjVar(self, "taxi.cost");
                if (cost > 0)
                {
                    if (getCashBalance(player) < cost)
                    {
                        broadcast(player, "You do not have enough credits to use this taxi.");
                        return SCRIPT_CONTINUE;
                    }
                    else
                    {
                        if (money.requestPayment(player, self, cost, "pass_fail", null, true))
                        {
                            String currentTemplate = getTemplateName(self);
                            if (!currentTemplate.contains("zonegate_")) //@NOTE:  play idle taxi sound for non-zone gates, otherwise play hologram sound
                            {
                                broadcast(player, "You have paid " + cost + " credits to use this taxi. It will depart in 30 seconds.");
                                play2dNonLoopingSound(player, "sound/veh_airtaxi_idle_lp.snd");
                            }
                            else
                            {
                                broadcast(player, "You have paid " + cost + " credits to use this Galactic Transit Kiosk. You  will depart in 30 seconds.");
                                play2dNonLoopingSound(player, "sound/item_holo.snd");
                            }
                            dictionary params = new dictionary();
                            params.put("player", player);
                            messageTo(self, "taxiDelay", params, TAXI_DELAY, false);
                            utils.setScriptVar(player, "usedTaxi", 1);
                        }
                        else
                        {
                            String currentTemplate = getTemplateName(self);
                            if (!currentTemplate.contains("zonegate_"))
                            {
                                broadcast(player, "You do not have enough credits to use this taxi.");
                            }
                            else
                            {
                                broadcast(player, "You do not have enough credits to use this Galactic Transit Kiosk.");
                            }
                            LOG("ethereal", "[Taxi]: Fallthrough on money.requestPayment() for player " + getPlayerFullName(player) + " on taxi " + self + " with cost of " + cost + ".");
                            return SCRIPT_CONTINUE;
                        }
                    }
                }
                return SCRIPT_CONTINUE;
            }
        }
        else if (item == menu_info_types.SERVER_MENU1)
        {
            if (!isGod(player))
            {
                return SCRIPT_CONTINUE;
            }
            else
            {
                String buffer = "Enter the Network ID of the taxi token you wish to link to this taxi.";
                String title = "Taxi Token Linker";
                sui.filteredInputbox(self, player, buffer, title, "handleLinkRequest", null);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int taxiDelay(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = params.getObjId("player");
        broadcast(player, "The taxi is now departing. Transporting you to your destination.");
        utils.removeScriptVar(player, "usedTaxi");
        handleTaxi(self, player);
        return SCRIPT_CONTINUE;
    }

    public int handleTaxi(obj_id self, obj_id player)
    {
        play2dNonLoopingSound(player, "sound/veh_airtaxi_rise.snd");
        location where = getLocationObjVar(self, "taxi.location");
        warpPlayer(player, where.area, where.x, where.y, where.z, where.cell, 0, 0, 0);
        LOG("ethereal", "[Taxi]: Player " + getPlayerFullName(player) + " used " + self + " to travel to " + where.toReadableFormat(true));
        return SCRIPT_CONTINUE;
    }

    public int handleLinkRequest(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String input = sui.getInputBoxText(params);
        if (input == null || input.equals(""))
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            obj_id token = utils.stringToObjId(input);
            if (!isIdValid(token))
            {
                return SCRIPT_CONTINUE;
            }
            else
            {
                if (!hasObjVar(token, "taxi.location_token"))
                {
                    return SCRIPT_CONTINUE;
                }
                else
                {
                    setObjVar(self, "taxi.location", getLocationObjVar(token, "taxi.location_token"));
                    setObjVar(self, "taxi.cost", getIntObjVar(token, "taxi.cost"));
                    LOG("ethereal", "[Taxi]: Player " + getPlayerFullName(player) + " linked " + self + " to " + token + " with location " + getLocationObjVar(token, "taxi.location_token").toReadableFormat(true) + " and credit cost of " + getIntObjVar(token, "taxi.cost"));
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
}
