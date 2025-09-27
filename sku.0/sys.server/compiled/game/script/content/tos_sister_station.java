package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Allows movement to sister stations
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 9:41 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.npe;
import script.library.sui;
import script.library.transition;

public class tos_sister_station extends base_script
{
    public String[] STATION_OPTIONS = {
            "Tansarii Point Station",
            "Nova Orion Station"
    };

    public int OnAttach(obj_id self)
    {
        setObjVar(self, "zoneLine", "station_nova_orion");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo)
    {
        int menu = menuInfo.addRootMenu(menu_info_types.ITEM_USE, new string_id("Traverse Wild Space"));
        menu_info_data menuInfoData = menuInfo.getMenuItemById(menu);
        menuInfoData.setServerNotify(true);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            String title = "Station Transport";
            String prompt = "\\#DAA520Please select which sister station you would like to travel to:\\#.";
            sui.listbox(self, player, prompt, sui.OK_CANCEL, title, STATION_OPTIONS, "handleStationChange");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleStationChange(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx == 0)
        {
            sui.msgbox(self, player, "\\#DAA520You have selected " + STATION_OPTIONS[0] + "\nAre you sure you want to travel to this sister station?\n Note: If you did not complete the tutorial, you will be required to finish certain elements before leaving.", sui.YES_NO, "handleStationMovement1");
        }
        if (idx == 1)
        {
            sui.msgbox(self, player, "\\#DAA520You have selected " + STATION_OPTIONS[1] + "\nAre you sure you want to travel to this sister station?", sui.YES_NO, "handleStationMovement2");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleStationMovement1(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (bp == sui.BP_OK)
        {
            npe.movePlayerFromFalconToSharedStation(player);
            removeObjVar(player, "npe");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleStationMovement2(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (bp == sui.BP_OK)
        {
            transition.zonePlayer(self, player);
        }
        return SCRIPT_CONTINUE;
    }
}
