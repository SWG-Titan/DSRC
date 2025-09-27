package script.theme_park.stp.hub;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.*;
import script.library.sui;

/**
 * @author BubbaJoe
 * @purpose Shuttle to get off the hub.
 *         Locations are preset and require a single obj_var to advance. If you add a location make sure the objvar name is simple.
 */
public class shuttle extends base_script
{
    public static String TITLE = "Confirm Departure";

    public shuttle()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "a shuttle hatch");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "a shuttle hatch");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenuOrServerNotify(menu_info_types.ITEM_USE, new string_id("Travel"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {

            if (getStringObjVar(self, "hub_travel_point").equals("default"))
            {
                broadcast(player, "This shuttle is currently out of service.");
                return SCRIPT_CONTINUE;
            }
            else if (getStringObjVar(self, "hub_travel_point").equals("tatooine"))
            {
                String prompt = "Do you wish to board a shuttle to Mos Eisley?";
                sui.msgbox(self, player, prompt, sui.OK_CANCEL, TITLE, "handleTatooine");
            }
            else if (getStringObjVar(self, "hub_travel_point").equals("taanab"))
            {
                String prompt = "Do you wish to board a shuttle to Pendath?";
                sui.msgbox(self, player, prompt, sui.OK_CANCEL, TITLE, "handleTaanab");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleTatooine(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_OK)
        {
            broadcast(player, "TODO: add in tatooine's coord.");
            warpPlayer(player, "tatooine", 5277.0f, 75.0f, -4198.0f, null, 0, 0, 0, "", true);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int handleTaanab(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_OK)
        {
            warpPlayer(player, "taanab", 5277.0f, 75.0f, -4198.0f, null, 0, 0, 0, "", true);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
}
