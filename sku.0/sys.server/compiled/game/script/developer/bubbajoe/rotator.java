package script.developer.bubbajoe;

import script.*;
import script.library.sui;
import script.library.utils;

public class rotator extends script.base_script
{
    public static void startRotation(obj_id self, float yaw, float pitch, float roll, float interval, int duration)
    {
        dictionary params = new dictionary();
        params.put("yaw", yaw);
        params.put("pitch", pitch);
        params.put("roll", roll);
        params.put("interval", interval);
        params.put("timeLeft", duration);

        messageTo(self, "handleRotation", params, interval, false);
    }

    public int handleRotation(obj_id self, dictionary params)
    {
        float yaw = params.getFloat("yaw");
        float pitch = params.getFloat("pitch");
        float roll = params.getFloat("roll");
        int interval = params.getInt("interval");
        int timeLeft = params.getInt("timeLeft");

        modifyYaw(self, yaw);
        modifyPitch(self, pitch);
        modifyRoll(self, roll);

        timeLeft -= interval;
        if (timeLeft > 0)
        {
            params.put("timeLeft", timeLeft);
            messageTo(self, "handleRotation", params, interval, false);
        }

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        int superdaddy = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Rotator"));
        mi.addRootMenu(menu_info_types.SERVER_MENU2, new string_id("Start/Stop"));
        mi.addSubMenu(superdaddy, menu_info_types.SERVER_MENU3, new string_id("Set Yaw"));
        mi.addSubMenu(superdaddy, menu_info_types.SERVER_MENU4, new string_id("Set Pitch"));
        mi.addSubMenu(superdaddy, menu_info_types.SERVER_MENU5, new string_id("Set Roll"));
        mi.addSubMenu(superdaddy, menu_info_types.SERVER_MENU6, new string_id("Set Interval"));
        mi.addSubMenu(superdaddy, menu_info_types.SERVER_MENU7, new string_id("Set Duration"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU2)
        {
            if (!hasObjVar(self, "rotating"))
            {
                setObjVar(self, "rotating", true);
                broadcast(player, "Rotating object");
                startRotation(self, getFloatObjVar(self, "rotation_yaw"), getFloatObjVar(self, "rotation_pitch"), getFloatObjVar(self, "rotation_roll"), getIntObjVar(self, "rotation_interval"), getIntObjVar(self, "rotation_duration"));
            }
            else
            {
                stopListeningToMessage(self, "handleRotation");
                removeObjVar(self, "rotating");
                broadcast(player, "Done rotating object");
            }
        }
        if (item == menu_info_types.SERVER_MENU3)
        {
            broadcast(player, "Set the yaw rotation.");
            sui.inputbox(self, player, "Enter yaw degree (-180 to 180):", sui.OK_CANCEL, "Rotator", sui.INPUT_NORMAL, null, "handleYawInput", null);
        }
        else if (item == menu_info_types.SERVER_MENU4)
        {
            broadcast(player, "Set the pitch rotation.");
            sui.inputbox(self, player, "Enter pitch degree (-180 to 180):", sui.OK_CANCEL, "Rotator", sui.INPUT_NORMAL, null, "handlePitchInput", null);
        }
        else if (item == menu_info_types.SERVER_MENU5)
        {
            broadcast(player, "Set the roll rotation.");
            sui.inputbox(self, player, "Enter roll degree (-180 to 180):", sui.OK_CANCEL, "Rotator", sui.INPUT_NORMAL, null, "handleRollInput", null);
        }
        else if (item == menu_info_types.SERVER_MENU6)
        {
            broadcast(player, "Set the interval of rotation.");
            sui.inputbox(self, player, "Enter rotation interval (seconds [float]):", sui.OK_CANCEL, "Rotator", sui.INPUT_NORMAL, null, "handleIntervalInput", null);
        }
        else if (item == menu_info_types.SERVER_MENU7)
        {
            broadcast(player, "Set the duration of rotation.");
            sui.inputbox(self, player, "Enter rotation duration:", sui.OK_CANCEL, "Rotator", sui.INPUT_NORMAL, null, "handleDurationalInput", null);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleYawInput(obj_id self, dictionary params) throws InterruptedException
    {
        String input = sui.getInputBoxText(params);
        setObjVar(self, "rotation_yaw", utils.stringToFloat(input));
        return SCRIPT_CONTINUE;
    }

    public int handlePitchInput(obj_id self, dictionary params) throws InterruptedException
    {
        String input = sui.getInputBoxText(params);
        setObjVar(self, "rotation_pitch", utils.stringToFloat(input));
        return SCRIPT_CONTINUE;
    }

    public int handleRollInput(obj_id self, dictionary params) throws InterruptedException
    {
        String input = sui.getInputBoxText(params);
        setObjVar(self, "rotation_roll", utils.stringToFloat(input));
        return SCRIPT_CONTINUE;
    }

    public int handleIntervalInput(obj_id self, dictionary params) throws InterruptedException
    {
        String input = sui.getInputBoxText(params);
        setObjVar(self, "rotation_interval", utils.stringToFloat(input));
        return SCRIPT_CONTINUE;
    }

    public int handleDurationalInput(obj_id self, dictionary params) throws InterruptedException
    {
        String input = sui.getInputBoxText(params);
        setObjVar(self, "rotation_duration", utils.stringToInt(input));
        return SCRIPT_CONTINUE;
    }
}
