package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Pseudo ITV - Sends player to Mustafar at a specific location on a cooldown for 1 hour.
@Created: Wednesday, 11/1/2023, at 1:05 AM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;
import script.library.sui;

public class mustafar_heroic_beacon extends script.base_script
{
    public float OFFSET_X = 0.0f;
    public float OFFSET_Z = 0.0f;
    public int COOLDOWN_TIME = 1800;
    public String[] TRAVEL_OPTIONS = {
            "Kubaza Beetle Cavern",
            "Old Research Facility",
            "Droid Factory (Decrepit/Operational)",
            "Droid Army (Koseyet)",
            "HK-47",
            "Sher Kar"
    };

    public int OnAttach(obj_id self)
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        reInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public void reInitialize(obj_id self)
    {
        setName(self, "Mustafarian Transport Beacon");
        setDescriptionString(self, "This beacon will allow you to travel to any Mustafarian Instance entrance.");
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Request Travel"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (isIncapacitated(player))
            {
                return SCRIPT_CONTINUE;
            }
            if (combat.isInCombat(player))
            {
                return SCRIPT_CONTINUE;
            }
            if (hasObjVar(player, "mustafar.heroic.cooldown"))
            {
                int cooldown = getIntObjVar(player, "mustafar.heroic.cooldown");
                if (cooldown > getGameTime() && !isGod(player))
                {
                    broadcast(player, "You must wait " + ((cooldown - getGameTime()) / 60) + " minutes before you can travel to these locations again.");
                    return SCRIPT_CONTINUE;
                }
            }
            sui.listbox(self, player, "Select an option to travel to: ", sui.OK_CANCEL, "Mustafarian Transport Beacon", TRAVEL_OPTIONS, "handleBeacon", true);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleBeacon(obj_id self, dictionary params) throws InterruptedException
    {
        int bp = sui.getIntButtonPressed(params);
        obj_id player = sui.getPlayerId(params);
        if (bp == sui.BP_CANCEL)
        {
            broadcast(self, "You have canceled your departure.");
            return SCRIPT_CONTINUE;
        }
        int selection = sui.getListboxSelectedRow(params);
        if (selection == -1)
        {
            return SCRIPT_CONTINUE;
        }
        switch (selection)
        {
            case 0:
                //-722 157 509 mustafar
                warpPlayer(player, "mustafar", -724 + OFFSET_X, 157, 509 + OFFSET_Z, null, -724 + OFFSET_X, 157, 509 + OFFSET_Z, "", true);
                LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon and was sent to " + TRAVEL_OPTIONS[selection]);
                break;
            case 1:
                //2117 87 3097 mustafar
                warpPlayer(player, "mustafar", 2123 + OFFSET_X, 87, 3090 + OFFSET_Z, null, 2123 + OFFSET_X, 87, 3090 + OFFSET_Z, "", true);
                LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon and was sent to " + TRAVEL_OPTIONS[selection]);
                break;
            case 2:
                //518 65 1986 mustafar
                warpPlayer(player, "mustafar", 518 + OFFSET_X, 65, 1986 + OFFSET_Z, null, 518 + OFFSET_X, 65, 1986 + OFFSET_Z, "", true);
                LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon and was sent to " + TRAVEL_OPTIONS[selection]);
                break;
            case 3:
                //175, 0, -200 mustafar
                warpPlayer(player, "mustafar", 175 + OFFSET_X, 0, -200 + OFFSET_Z, null, 175 + OFFSET_X, 0, -200 + OFFSET_Z, "", true);
                LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon and was sent to " + TRAVEL_OPTIONS[selection]);
                break;
            case 4:
                //-2530, 0, 1650 mustafar
                warpPlayer(player, "mustafar", -2530 + OFFSET_X, 0, 1650 + OFFSET_Z, null, -2530 + OFFSET_X, 0, 1650 + OFFSET_Z, "", true);
                LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon and was sent to " + TRAVEL_OPTIONS[selection]);
                break;
            case 5:
                //-2000, 0, 4200 | -2047.5789 83.15722 4251.4863
                warpPlayer(player, "mustafar", -2000 + OFFSET_X, 0, 4200 + OFFSET_Z, null, -2000 + OFFSET_X, 0, 4200 + OFFSET_Z, "", true);
                LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon and was sent to " + TRAVEL_OPTIONS[selection]);
                break;
        }
        setObjVar(player, "mustafar.heroic.cooldown", getGameTime() + COOLDOWN_TIME);
        LOG("ethereal", " [Mustafar]: Player " + getName(player) + " has used a Mustafar Heroic Transport Beacon.");
        return SCRIPT_CONTINUE;
    }
}
