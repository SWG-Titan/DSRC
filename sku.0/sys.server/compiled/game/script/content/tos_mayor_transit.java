package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Orbital Station bind to send player to home.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/5/2024, at 4:09 AM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.city;
import script.library.sui;
import script.library.utils;

public class tos_mayor_transit extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Elevator: Mayoral Hangar");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Elevator: Mayoral Transit");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Depart to City"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.msgbox(self, player, "\\#e3d005Are you sure you want to leave the station and travel to your city?\\#", sui.OK_CANCEL_ALL, "Transit", "handleBind");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int handleBind(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (player == null)
        {
            return SCRIPT_CONTINUE;
        }
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(self, "You have canceled your trip to your city.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            sendPlayerToCity(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int sendPlayerToCity(obj_id self, obj_id player) throws InterruptedException
    {
        if (city.isAMayor(player))
        {
            int city_id = getCitizenOfCityId(player);
            if (cityExists(city_id))
            {
                obj_id city_hall = cityGetCityHall(city_id);
                if (isIdValid(city_hall))
                {
                    dictionary dict = new dictionary();
                    dict.put("requestingObject", self);
                    dict.put("homeOwner", player);
                    messageTo(city_hall, "retrieveHouseCoords", dict, 0.0f, false);
                }
                else
                {
                    broadcast(player, "This city hall is condemned.");
                    return SCRIPT_CONTINUE;
                }
            }
        }
        else
        {
            broadcast(player, "You are not a mayor.");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int ownerResidenceLocationResponse(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("homeOwner");
        location homeLocation = params.getLocation("residenceLocation");
        String homePlanet = params.getString("homePlanet");
        if (!isIdValid(player) || homeLocation == null || homePlanet == null || homePlanet.equals(""))
        {
            broadcast(player, "There was an error retrieving your home location.");
        }
        else
        {
            utils.setScriptVar(player, "homeLoc", homeLocation);
            utils.setScriptVar(player, "destPlanet", homePlanet);
            int pid = sui.msgbox(self, player, "\\#e3d005Please confirm once more that you wish to depart.", sui.OK_CANCEL, "\\#e3d005Transit", sui.MSG_QUESTION, "sendPlayerToHomeLocation");
        }
        return SCRIPT_CONTINUE;
    }

    public int sendPlayerToHomeLocation(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_CANCEL)
        {
            utils.removeScriptVar(player, "homeLoc");
            utils.removeScriptVar(player, "destPlanet");
            return SCRIPT_CONTINUE;
        }
        else
        {
            location destLoc = utils.getLocationScriptVar(player, "homeLoc");
            String destPlanet = utils.getStringScriptVar(player, "destPlanet");
            warpPlayer(player, destPlanet, destLoc.x, destLoc.y, destLoc.z, null, 0, 0, 0, "", false);
        }
        return SCRIPT_CONTINUE;
    }
}
