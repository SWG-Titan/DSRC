package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Purpose: Defunct decoration panel for moving, renaming, and copying height of objects. See dsrc.script.developer.bubbajoe.player_developer for the current version.
@Created: Wednesday, 11/1/2023, at 4:40 PM, 
@Credit: SWG: Source, Elour, Aconite, BubbaJoe
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.sui;
import script.library.utils;
import script.location;
import script.obj_id;

public class decoration_panel extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text)
    {
        if (isGod(speaker))
        {
            if (text.equals("decorationPanel"))
            {
                startDecorationPanel(self);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int startDecorationPanel(obj_id self)
    {
        int page = createSUIPage("/Script.decorationPanel", self, self);
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnNorth", "onMoveNorth");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnSouth", "onMoveSouth");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnEast", "onMoveEast");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnWest", "onMoveWest");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnUp", "onMoveUp");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnDown", "onMoveDown");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnRotateLeft", "onRotateLeft");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnRotateRight", "onRotateRight");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnRotateRoll", "onRotateRoll");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnRotatePitch", "onRotatePitch");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnMoveLeft", "onMoveLeft");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnMoveRight", "onMoveRight");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnMoveForward", "onMoveForward");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnMoveBackward", "onMoveBackward");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnSummon", "onSummon");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnCopyYaw", "onCopyYaw");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnClone", "onCloneObject");
        subscribeToSUIEvent(page, sui_event_type.SET_onButton, "btnSetName", "onSetName");
        subscribeToSUIEvent(page, sui_event_type.SET_onTextbox, "txtInput", "onValueChanged");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnSetName", "txtName", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnSetIncrement", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnNorth", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnSouth", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnEast", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnWest", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnUp", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnDown", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnRotateLeft", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnRotateRight", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnRotateRoll", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnRotatePitch", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnMoveLeft", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnMoveRight", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnMoveForward", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnMoveBackward", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnSummon", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnCopyYaw", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnClone", "txtInput", "LocalText");
        subscribeToSUIPropertyForEvent(page, sui_event_type.SET_onButton, "btnSetName", "txtInput", "LocalText");
        setSUIAssociatedObject(page, self);
        boolean showResult = showSUIPage(page);
        if (!showResult)
        {
            broadcast(self, "Cannot display UI page '/Script.decorationPanel");
        }
        flushSUIPage(page);
        setObjVar(self, "decorationPanel.pid", page);
        return SCRIPT_CONTINUE;
    }

    public int onMoveNorth(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        location targetLoc = getLocation(target);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        targetLoc.z = targetLoc.z + increment;
        setLocation(target, targetLoc);
        LOG("ethereal", "Moving north by " + increment + " to " + targetLoc);
        return SCRIPT_CONTINUE;
    }

    public int onMoveSouth(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        location targetLoc = getLocation(target);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        targetLoc.z = targetLoc.z - increment;
        setLocation(target, targetLoc);
        LOG("ethereal", "Moving south by " + increment + " to " + targetLoc);
        return SCRIPT_CONTINUE;
    }

    public int onMoveEast(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        location targetLoc = getLocation(target);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        targetLoc.x = targetLoc.x + increment;
        setLocation(target, targetLoc);
        LOG("ethereal", "Moving east by " + increment + " to " + targetLoc);
        return SCRIPT_CONTINUE;
    }

    public int onMoveWest(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        location targetLoc = getLocation(target);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        targetLoc.x = targetLoc.x - increment;
        setLocation(target, targetLoc);
        LOG("ethereal", "Moving west by " + increment + " to " + targetLoc);
        return SCRIPT_CONTINUE;
    }

    public int onMoveUp(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        location targetLoc = getLocation(target);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        targetLoc.y = targetLoc.y + increment;
        setLocation(target, targetLoc);
        LOG("ethereal", "Moving up by " + increment + " to " + targetLoc);
        return SCRIPT_CONTINUE;
    }

    public int onMoveDown(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        location targetLoc = getLocation(target);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        targetLoc.y = targetLoc.y - increment;
        setLocation(target, targetLoc);
        LOG("ethereal", "Moving down by " + increment + " to " + targetLoc);
        LOG("ethereal", "params are: " + params);
        return SCRIPT_CONTINUE;
    }

    public int onRotateLeft(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        increment = increment * -1;
        float targetOrientation = getYaw(target);
        targetOrientation = targetOrientation + increment;
        setYaw(target, targetOrientation);
        LOG("ethereal", "Rotating left by " + increment + " to " + targetOrientation);
        return SCRIPT_CONTINUE;
    }

    public int onRotateRight(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        float increment = utils.stringToFloat(params.getString("txtInput.LocalText"));
        float targetOrientation = getYaw(target);
        targetOrientation = targetOrientation + increment;
        setYaw(target, targetOrientation);
        LOG("ethereal", "Rotating right by " + increment + " to " + targetOrientation);
        return SCRIPT_CONTINUE;
    }

    public int onCopyYaw(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        float targetOrientation = getYaw(self);
        setYaw(target, targetOrientation);
        LOG("ethereal", "Copying yaw of " + targetOrientation);
        return SCRIPT_CONTINUE;
    }

    public int onSetName(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        obj_id target = getIntendedTarget(player);
        String text = params.getString("txtName.LocalText");
        setName(target, text);
        LOG("ethereal", "Setting name to " + text);
        return SCRIPT_CONTINUE;
    }

    public int onMoveLeft(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        sendConsoleCommand("/moveFurniture left " + utils.stringToInt(params.getString("txtInput.LocalText")), self);
        return SCRIPT_CONTINUE;
    }

    public int onMoveRight(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        sendConsoleCommand("/moveFurniture right " + utils.stringToInt(params.getString("txtInput.LocalText")), self);
        return SCRIPT_CONTINUE;
    }

    public int onMoveForward(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        sendConsoleCommand("/moveFurniture forward " + utils.stringToInt(params.getString("txtInput.LocalText")), self);
        return SCRIPT_CONTINUE;
    }

    public int onMoveBackward(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        sendConsoleCommand("/moveFurniture back " + utils.stringToInt(params.getString("txtInput.LocalText")), self);
        return SCRIPT_CONTINUE;
    }

    public int onRotateRoll(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        sendConsoleCommand("/rotateFurniture roll " + utils.stringToInt(params.getString("txtInput.LocalText")), self);
        return SCRIPT_CONTINUE;
    }

    public int onRotatePitch(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        sendConsoleCommand("/rotateFurniture pitch " + utils.stringToInt(params.getString("txtInput.LocalText")), self);
        return SCRIPT_CONTINUE;
    }

    public int onCloneObject(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        String template = getTemplateName(getIntendedTarget(self));
        sendConsoleCommand("/spawn " + template + " 1 0 0", self);
        return SCRIPT_CONTINUE;
    }

    public int onSummon(obj_id self, dictionary params)
    {
        LOG("ethereal", "params are: " + params);
        location here = getLocation(self);
        setLocation(getIntendedTarget(self), here);
        LOG("ethereal", "Summoning target to " + here);
        return SCRIPT_CONTINUE;
    }

    public int onValueChanged(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "params are: " + params);
        obj_id player = sui.getPlayerId(params);
        debugConsoleMsg(player, "New movement value is " + sui.getInputBoxText(params));
        LOG("ethereal", "New movement value is " + sui.getInputBoxText(params));
        return SCRIPT_CONTINUE;
    }

    public int getIncrement(obj_id self)
    {
        return (int) getFloatObjVar(self, "decorationPanel.increment");
    }
}
