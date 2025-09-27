package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Controls the movement of a door, or any other object, up and down.
@Requirements: <no requirements>
@Notes: Usage for Event Rooms #1 and #2 on Rally Point Nova
@Created: Tuesday, 5/7/2024, at 11:51 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class tos_event_door extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Gate Controller");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Gate Controller");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isGod(player))
        {
            int doorMenu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Gate Control"));
            mi.addSubMenu(doorMenu, menu_info_types.SERVER_MENU3, new string_id("Link Object"));
            mi.addSubMenu(doorMenu, menu_info_types.SERVER_MENU4, new string_id("Set Door Movement"));
            if (!hasObjVar(self, "up"))
            {
                mi.addSubMenu(doorMenu, menu_info_types.SERVER_MENU1, new string_id("Open Door"));
            }
            else
            {
                mi.addSubMenu(doorMenu, menu_info_types.SERVER_MENU2, new string_id("Close Door"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            moveDoorUp(self);
            playClientEffectObj(self, "clienteffect/elevator_rise.cef", self, "");
        }
        else if (item == menu_info_types.SERVER_MENU2)
        {
            moveDoorDown(self);
            playClientEffectObj(self, "clienteffect/elevator_rise.cef", self, "");
        }
        else if (item == menu_info_types.SERVER_MENU3)
        {
            sui.inputbox(self, player, "Enter the Network ID of the object you wish to make into a gate. \n\nNote: You can use static items, as long as you have allowTargetAnything enabled in your config.", "Gate Control", "handleObjectLink", "");
        }
        else if (item == menu_info_types.SERVER_MENU4)
        {
            sui.inputbox(self, player, "Enter the amount to move UP/DOWN. \n\nExample: 5.", "Gate Control", "handleMovementChange", "5");
        }
        return SCRIPT_CONTINUE;
    }

    public int moveDoorUp(obj_id self) throws InterruptedException
    {
        setObjVar(self, "up", 1);
        float movement = getFloatObjVar(self, "movement");
        obj_id sourceLoc = getObjIdObjVar(self, "gateTarget");
        location currentLoc = getLocation(sourceLoc);
        location newLoc = new location(currentLoc.x, currentLoc.y + movement, currentLoc.z, currentLoc.area, currentLoc.cell);
        if (hasObjVar(self, "gateTarget"))
        {
            setLocation(getObjIdObjVar(self, "gateTarget"), newLoc);
        }
        return SCRIPT_CONTINUE;
    }

    public int moveDoorDown(obj_id self) throws InterruptedException
    {
        removeObjVar(self, "up");
        float movement = getFloatObjVar(self, "movement");
        obj_id sourceLoc = getObjIdObjVar(self, "gateTarget");
        location currentLoc = getLocation(sourceLoc);
        location newLoc = new location(currentLoc.x, currentLoc.y - movement, currentLoc.z, currentLoc.area, currentLoc.cell);
        if (hasObjVar(self, "gateTarget"))
        {
            setLocation(getObjIdObjVar(self, "gateTarget"), newLoc);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleObjectLink(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String targetString = sui.getInputBoxText(params);
        if (targetString == null || targetString.equals(""))
        {
            broadcast(player, "Invalid string to convert to Object ID.");
            return SCRIPT_CONTINUE;
        }
        obj_id target = utils.stringToObjId(targetString);
        if (target == null)
        {
            broadcast(player, "Invalid Object ID.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "gateTarget", target);
        return SCRIPT_CONTINUE;
    }

    public int handleMovementChange(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String movementString = sui.getInputBoxText(params);
        if (movementString == null || movementString.equals(""))
        {
            broadcast(player, "Invalid movement amount.");
            return SCRIPT_CONTINUE;
        }
        float movement = utils.stringToFloat(movementString);
        if (movement == 0.0f || movement < 0.0f || movement > 100.0f)
        {
            broadcast(player, "Movement must be between 1 and 100. Units are not divided by 10 as they are in housing.");
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "movement", movement);
        return SCRIPT_CONTINUE;
    }
}
