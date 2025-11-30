package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Marks the Hub cell id for transport
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 1/25/2025, at 10:12 AM, 
@Copyright © SWG: Titan 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;

public class tos_marker extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        updateMarker(self, null);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("[DEV] Override Marker"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            updateMarker(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public void updateMarker(obj_id self, obj_id nullablePlayer)
    {
        obj_id tatooine = getPlanetByName("tatooine");
        location whereAmI = getLocation(self);
        setObjVar(tatooine, "hub_marker", whereAmI);
        if (nullablePlayer != null)
        {
            if (isIdValid(nullablePlayer))
            {
                broadcast(nullablePlayer, "Updated Hub Marker: " + whereAmI.toLogFormat());
            }
        }
    }
}
