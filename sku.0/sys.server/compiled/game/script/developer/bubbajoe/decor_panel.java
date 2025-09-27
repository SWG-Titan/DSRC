package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: Defunct decoration panel for moving, renaming, and copying height of objects. See dsrc.script.developer.bubbajoe.player_developer for the current version.
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.dictionary;
import script.library.sui;
import script.location;
import script.obj_id;

@SuppressWarnings("unused")
public class decor_panel extends script.base_script
{
    public static final String DP_PAGE = "/Script.decorationPanel";
    public static final String DP_RENAME_FIELD = "renameInput.txtName.LocalText";
    public static final String DP_MOVEMENT_FIELD = "movementInput.txtAmt.LocalText";
    public String PID = "dp.pid";

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnSpeaking(obj_id self, String text)
    {
        if (text.equals("decoration_panel"))
        {
            openDecorationPanel(self);
        }
        return SCRIPT_CONTINUE;
    }

    public void openDecorationPanel(obj_id suiTarget)
    {
        int pid = sui.createSUIPage(DP_PAGE, suiTarget, suiTarget);
        setObjVar(suiTarget, "dp.pid", pid);
        setSUIProperty(pid, "bg.caption.lblTitle", "Text", "Decoration Panel");
        setSUIProperty(pid, "north", "Text", "North");
        setSUIProperty(pid, "east", "Text", "East");
        setSUIProperty(pid, "south", "Text", "South");
        setSUIProperty(pid, "west", "Text", "West");
        setSUIProperty(pid, "up", "Text", "Up");
        setSUIProperty(pid, "down", "Text", "Down");
        setSUIProperty(pid, "copyheight", "Text", "Copy Height");
        setSUIProperty(pid, "movetome", "Text", "Move To Me");
        setSUIProperty(pid, "rename", "Text", "Rename");
        setSUIProperty(pid, "north", "IsCancelButton", "false");
        setSUIProperty(pid, "east", "IsCancelButton", "false");
        setSUIProperty(pid, "south", "IsCancelButton", "false");
        setSUIProperty(pid, "west", "IsCancelButton", "false");
        setSUIProperty(pid, "up", "IsCancelButton", "false");
        setSUIProperty(pid, "down", "IsCancelButton", "false");
        setSUIProperty(pid, "copyheight", "IsCancelButton", "false");
        setSUIProperty(pid, "movetome", "IsCancelButton", "false");
        setSUIProperty(pid, "rename", "IsDefaultButton", "false");
        setSUIProperty(pid, "north", "IsDefaultButton", "false");
        setSUIProperty(pid, "east", "IsDefaultButton", "false");
        setSUIProperty(pid, "south", "IsDefaultButton", "false");
        setSUIProperty(pid, "west", "IsDefaultButton", "false");
        setSUIProperty(pid, "up", "IsDefaultButton", "false");
        setSUIProperty(pid, "down", "IsDefaultButton", "false");
        setSUIProperty(pid, "copyheight", "IsDefaultButton", "false");
        setSUIProperty(pid, "movetome", "IsDefaultButton", "false");
        setSUIProperty(pid, "rename", "IsDefaultButton", "false");
        setSUIProperty(pid, "lockin", "Visible", "false");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "north", "handleMoveNorth");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "east", "handleMoveEast");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "south", "handleMoveSouth");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "west", "handleMoveWest");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "up", "handleMoveUp");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "down", "handleMoveDown");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "copyheight", "handleCopyHeight");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "movetome", "handleMoveToMe");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "rename", "handleRename");
        modifySUIEventCallback(pid, sui_event_type.SET_onButton, "lockIn", "handleSetAmount");

        showSUIPage(pid);
        flushSUIPage(pid);
    }

    public void handleMoveNorth(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(target);
        loc.z += currentMovementAmount(params);
        setLocation(target, loc);
    }

    public void handleMoveEast(obj_id self, dictionary params)
    {
        obj_id target = getTarget(self);
        location loc = getLocation(target);
        loc.x += currentMovementAmount(params);
        setLocation(target, loc);
    }

    public void handleMoveSouth(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(target);
        loc.z -= currentMovementAmount(params);
        setLocation(target, loc);
    }

    public void handleMoveWest(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(target);
        loc.x -= currentMovementAmount(params);
        setLocation(target, loc);
    }

    public void handleMoveUp(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(target);
        loc.y += currentMovementAmount(params);
        setLocation(target, loc);
    }

    public void handleMoveDown(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(target);
        loc.y -= currentMovementAmount(params);
        setLocation(target, loc);
    }

    public void handleRename(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        String name = params.getString(DP_RENAME_FIELD);
        setName(target, name);
    }

    public void handleMoveToMe(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(self);
        setLocation(target, loc);
    }

    public void handleCopyHeight(obj_id self, dictionary params)
    {
        obj_id target = getIntendedTarget(self);
        location loc = getLocation(target);
        location myLoc = getLocation(self);
        loc.y = myLoc.y;
        setLocation(target, loc);
    }

    public float currentMovementAmount(dictionary params)
    {
        return params.getFloat(DP_MOVEMENT_FIELD);
    }
}
