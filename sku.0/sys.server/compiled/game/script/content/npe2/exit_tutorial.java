package script.content.npe2;/*
@Origin: dsrc.script.content.npe2
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 2/7/2025, at 7:31 PM, 
@Copyright © SWG: Titan 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class exit_tutorial extends base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Refugee Camp Transit");
        setDescriptionString(self, "This terminal will allow refugees of the Galactic Civil War to relocate to one of three planets.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Refugee Camp Transit");
        setDescriptionString(self, "This terminal will allow refugees of the Galactic Civil War to relocate to one of three planets.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, false, false, 4f, false))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, string_id.unlocalized("Request Shuttle"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            String[] options = {"Coronet, Corellia", "Theed, Naboo", "Mos Eisley, Tatooine (\\#129924RECOMMENDED\\#.)"};
            sui.listbox(self, player, "Select a destination to being your relocation to:", sui.OK_CANCEL, "Refugee Transit: Available Locations", options, "handleExitSelection");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleExitSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (player == null)
        {
            return SCRIPT_CONTINUE;
        }
        int buttonPressed = sui.getIntButtonPressed(params);
        if (buttonPressed == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled your departure.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            int index = sui.getListboxSelectedRow(params);
            switch (index)
            {
                case 0:
                    warpPlayer(player, "corellia", -137.0f, 28.0f, -4723.0f, null, 0f, 0f, 0f, "noHandler", false);
                    break;
                case 1:
                    warpPlayer(player, "naboo", -4855.0f, 6.0f, 4167.0f, null, 0f, 0f, 0f, "noHandler", false);
                    break;
                case 2:
                    warpPlayer(player, "tatooine", 3627.0f, 5.0f, -4772.0f, null, 0f, 0f, 0f, "noHandler", false);
                    break;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
