package script.content.new_instance;/*
@Origin: dsrc.script.content.new_instance
@Author:  BubbaJoeX
@Purpose: Moves players to a certain instance.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 6/3/2024, at 4:46 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.group;
import script.library.sui;

public class instance_enter extends base_script
{
    public static String INSTANCE_SCENE = "adventure3";
    public float NULLIFY = 0.0f;
    public String[] INSTANCE_NAMES = {
            "Black Sun Outpost",
            "Skirmish on Endor",
            "Palace Raid",
            "Trandoshan Tumble"
    };
    //All locations use this format: new location(x, y, z, scene, cell), 0 is NULLIFY just for cosmetic purposes as well as reading the coordinates as we don't need to use height on a flat plane.
    public location BLACKSUN_OUTPOST_S01 = new location(-5000, NULLIFY, 5000, INSTANCE_SCENE, null);
    public location BLACKSUN_OUTPOST_S02 = new location(5000, NULLIFY, 5000, INSTANCE_SCENE, null);
    public location BLACKSUN_OUTPOST_S03 = new location(NULLIFY, NULLIFY, 5000, INSTANCE_SCENE, null);

    public int OnAttach(obj_id self)
    {
        this.OnSetup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnSetup(obj_id self)
    {
        setName(self, "Smuggler Transport");
        setDescriptionString(self, "This transport will take you to one of four instances: \n" + INSTANCE_NAMES[0] + "\n" + INSTANCE_NAMES[1] + "\n" + INSTANCE_NAMES[2] + "\n" + INSTANCE_NAMES[3]);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        this.OnSetup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Depart"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            requestMovement(player);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int requestMovement(obj_id player) throws InterruptedException
    {
        if (!group.isGrouped(player))
        {
            broadcast(player, "You must have four or more members in your group to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        if (group.getGroupSize(player) < 4)
        {
            broadcast(player, "You must have four or more members in your group to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        sui.listbox(player, "Choose an instance to travel to.", INSTANCE_NAMES, "handleInstanceSelection");
        return SCRIPT_CONTINUE;
    }

    public int handleInstanceSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled your travels.");
            return SCRIPT_CONTINUE;
        }
        if (idx == -1)
        {
            LOG("ethereal", "[New Instance]: Player " + player + " cannot select instance as INSTANCE_NAMES is -1.");
            return SCRIPT_CONTINUE;
        }
        switch (idx)
        {
            case 0://blacksun
                if (sendPlayer(player, BLACKSUN_OUTPOST_S01, "content.new_instance.player_instance_timer_new", true, INSTANCE_NAMES[0]) == 1) ;
            {
                LOG("ethereal", "[New Instance]: Player " + player + " has been sent to " + INSTANCE_NAMES[0] + ".");
            }
            break;
            case 1:
                if (sendPlayer(player, BLACKSUN_OUTPOST_S02, "content.new_instance.player_instance_timer_new", true, INSTANCE_NAMES[1]) == 1) ;
            {
                LOG("ethereal", "[New Instance]: Player " + player + " has been sent to " + INSTANCE_NAMES[1] + ".");
            }
            break;
            case 2:
                if (sendPlayer(player, BLACKSUN_OUTPOST_S03, "content.new_instance.player_instance_timer_new", true, INSTANCE_NAMES[2]) == 1) ;
            {
                LOG("ethereal", "[New Instance]: Player " + player + " has been sent to " + INSTANCE_NAMES[2] + ".");
            }
            break;
            case 3:
                broadcast(player, "This instance is not available at this time.");
                break;
        }
        return SCRIPT_CONTINUE;
    }

    public int sendPlayer(obj_id player, location loc, String scriptToAttach, boolean showLoading, String label) throws InterruptedException
    {
        warpPlayer(player, loc.area, loc.x, loc.y, loc.z, loc.cell, loc.x, loc.y, loc.z, null, showLoading);
        attachScript(player, scriptToAttach);
        LOG("ethereal", "[New Instance]: Player " + player + " has been moved to " + label + " (" + loc.toLogFormat() + ") and attached to " + scriptToAttach + ".");
        return SCRIPT_CONTINUE;
    }
}
