package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Sunday, 5/12/2024, at 7:50 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.player_structure;
import script.library.prose;
import script.library.sui;

public class tos_pa_system extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Announce"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            sui.inputbox(self, player, "Enter the message you would like to announce station-wide.", sui.OK_CANCEL, "PA System", sui.INPUT_NORMAL, null, "handleBroadcastMessage", null);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleBroadcastMessage(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String message = sui.getInputBoxText(params);
        if (message == null || message.equals(""))
        {
            sendSystemMessage(self, "You must enter a message to broadcast", null);
            return SCRIPT_CONTINUE;
        }
        obj_id[] occupants = player_structure.getPlayersInBuilding(getTopMostContainer(self));
        for (obj_id occupant : occupants)
        {
            playClientEffectObj(occupant, "sound/item_fusioncutter_start.snd", occupant, "");
            prose_package pp = new prose_package();
            prose.setStringId(pp, new string_id("\\#DAA520" + getPlayerFullName(player) + " announces: \\#." + message));
            commPlayer(player, occupant, pp);
        }
        broadcast(player, "You have sent a station-wide message.");
        return SCRIPT_CONTINUE;
    }
}
