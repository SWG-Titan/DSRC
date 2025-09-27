package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 9/2/2024, at 11:29 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class tos_ent_system_switch extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Toggle Entertainer System"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.ITEM_USE)
            {
                //check for cantina cell
                String cellName = getCellName(self);
                if (cellName == null || (!cellName.contains("cantina")))
                {
                    setName(self, "***IMPROPER SETUP***");
                }
                else
                {
                    obj_id currentCell = getCellId(self, cellName);
                    if (currentCell == null)
                    {
                        setName(self, "***IMPROPER SETUP (CELL)***");
                        return SCRIPT_CONTINUE;
                    }
                    if (hasScript(currentCell, "content.tos_ent_pulse_controller"))
                    {
                        detachScript(currentCell, "content.tos_ent_pulse_controller");
                        //get all players inside the cantina and remove the entertainer system
                        obj_id[] players = getContents(currentCell);
                        if (players != null && players.length > 0)
                        {
                            for (int i = 0; i < players.length; i++)
                            {
                                if (hasScript(players[i], "content.tos_ent_pulse") && isPlayer(players[i]))
                                {
                                    detachScript(players[i], "content.tos_ent_pulse");
                                    removeObjVar(players[i], "entertainer_system");
                                    cancelRecurringMessageTo(players[i], "token_pulse");
                                    if (hasMessageTo(players[i], "token_pulse"))
                                    {
                                        stopListeningToMessage(players[i], "token_pulse");
                                    }
                                }
                            }
                        }
                        broadcast(currentCell, "Entertainer Token System Offline.");
                        setName(self, "Cantina Controller (Entertainer System Disabled)");
                    }
                    else
                    {
                        broadcast(currentCell, "Entertainer Token System Online.");
                        attachScript(currentCell, "content.tos_ent_pulse_controller");
                        setName(self, "Cantina Controller (Entertainer System Enabled)");
                    }
                }
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
