package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Orbital Station bind to send player to home.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/5/2024, at 4:09 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;
import script.library.sui;
import script.library.utils;

public class tos_bind_custom extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Transit Shuttle");
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Transit Shuttle");
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id(getStringObjVar(self, "strRadialMenu")));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (combat.isInCombat(player))
            {
                broadcast(player, "You cannot travel while in combat.");
                return SCRIPT_CONTINUE;
            }
            if (isIncapacitated(player))
            {
                broadcast(player, "You cannot travel while in incapacitated.");
                return SCRIPT_CONTINUE;
            }
            if (isDead(player))
            {
                broadcast(player, "You cannot travel while dead.");
                return SCRIPT_CONTINUE;
            }
            sui.msgbox(self, player, "\\#e3d005Are you sure you want to leave and travel to your home?\\#", sui.OK_CANCEL_ALL, "Transit", "handleBind");
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
            broadcast(self, "You have canceled your trip home.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            sendPlayerToHome(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int sendPlayerToHome(obj_id self, obj_id player) throws InterruptedException
    {
        if (player == null)
        {
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(player, "residenceHouseId"))
        {
            broadcast(player, "You do not have a bind point set.");
            return SCRIPT_CONTINUE;
        }
        obj_id home = getObjIdObjVar(player, "residenceHouseId");
        if (isIdValid(home))
        {
            dictionary dict = new dictionary();
            dict.put("requestingObject", self);
            dict.put("homeOwner", player);
            messageTo(home, "retrieveHouseCoords", dict, 0, false);
        }
        else
        {
            broadcast(player, "You do not have a valid bind point set.");
        }
        return SCRIPT_CONTINUE;
    }

    public int ownerResidenceLocationResponse(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            messageTo(self, "cleanupShip", null, 0, false);
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("homeOwner");
        location homeLocation = params.getLocation("residenceLocation");
        String homePlanet = params.getString("homePlanet");
        if (sui.hasPid(player, "home_itv_pid"))
        {
            int pid = sui.getPid(player, "home_itv_pid");
            forceCloseSUIPage(pid);
        }
        if (!isIdValid(player) || homeLocation == null || homePlanet == null || homePlanet.isEmpty())
        {
            broadcast(player, "There was an error retrieving your home location.");
        }
        else
        {
            utils.setScriptVar(self, "homeLoc", homeLocation);
            utils.setScriptVar(self, "destPlanet", homePlanet);
            int pid = sui.msgbox(self, player, "\\#e3d005Please confirm once more that you wish to depart.", sui.OK_CANCEL, "\\#e3d005Transit", sui.MSG_QUESTION, "sendPlayerToHomeLocation");
            sui.setPid(player, pid, "home_itv_pid");
        }
        return SCRIPT_CONTINUE;
    }

    public int sendPlayerToHomeLocation(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            messageTo(self, "cleanupShip", null, 0, false);
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (sui.hasPid(player, "home_itv_pid"))
        {
            int pid = sui.getPid(player, "home_itv_pid");
            forceCloseSUIPage(pid);
        }
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_CANCEL)
        {
            utils.removeScriptVar(self, "homeLoc");
            utils.removeScriptVar(self, "destPlanet");
            return SCRIPT_CONTINUE;
        }
        else
        {
            location destLoc = utils.getLocationScriptVar(self, "homeLoc");
            String destPlanet = utils.getStringScriptVar(self, "destPlanet");
            warpPlayer(player, destPlanet, destLoc.x, destLoc.y, destLoc.z, null, 0, 0, 0, "", false);
        }
        return SCRIPT_CONTINUE;
    }
}
