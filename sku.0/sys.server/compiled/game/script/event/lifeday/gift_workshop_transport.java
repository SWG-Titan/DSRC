package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author:  BubbaJoeX
@Purpose: Takes players to or from the location.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 11/20/2024, at 3:04 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class gift_workshop_transport extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public void sync(obj_id self)
    {
        setName(self, "Saun Dann's Transport");
        setDescriptionString(self, "This object transports players to and from the gift shop.");
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU3)
            {
                if (hasObjVar(self, "workshop_transport"))
                {
                    broadcast(self, "Transport is set TO the workshop");
                    removeObjVar(self, "workshop_transport");
                }
                else
                {
                    setObjVar(self, "workshop_transport", 1);
                    broadcast(self, "Transport is set FROM the workshop");
                }
            }
        }

        if (item == menu_info_types.ITEM_USE)
        {
            if (hasObjVar(self, "workshop_transport"))
            {
                location loc = new location(25, 0, -32, "adventure3", null);
                warpPlayer(player, loc.area, loc.x, loc.y, loc.z, null, 0, 0, 0, "", isGod(player));
            }
            else
            {
                location loc = new location(3528, 0, -4804, "tatooine", null);
                warpPlayer(player, loc.area, loc.x, loc.y, loc.z, null, 0, 0, 0, "", isGod(player));
            }
        }

        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU3, new string_id("Toggle"));
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Travel"));
        return SCRIPT_CONTINUE;
    }
}
