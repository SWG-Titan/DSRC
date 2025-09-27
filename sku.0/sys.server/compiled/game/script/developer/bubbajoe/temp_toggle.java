package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: Temporary toggle for Chronicles Loot and Enzyme
@Requirements: <no requirements>
@Notes: remove upon patch
@Created: Wednesday, 1/8/2025, at 3:46 PM, 
@Copyright © SWG-OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.chat;
import script.library.loot;

public class temp_toggle extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        setName(self, "Voe Pahs");
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        int parent = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Enable/Disable Loot Offerings"));
        mi.addSubMenu(parent, menu_info_types.SERVER_MENU1, new string_id("Toggle Chronicles Loot"));
        mi.addSubMenu(parent, menu_info_types.SERVER_MENU2, new string_id("Toggle Beast Mastery Loot"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (loot.hasToggledChroniclesLootOff(player))
            {
                loot.enableChroniclesLoot(player);
                broadcast(player, "You have enabled Chronicles Loot!");
                playClientEffectLoc(player, "clienteffect/jawa_chatter_01.cef", getLocation(self), 1.0f, "cl_enable");
            }
            else
            {
                broadcast(player, "You have disabled Chronicles Loot!");
                playClientEffectLoc(player, "clienteffect/jawa_chatter_02.cef", getLocation(self), 1.0f, "cl_disable");
                loot.disableChroniclesLoot(player);
            }
        }
        if (item == menu_info_types.SERVER_MENU2)
        {
            if (hasObjVar(player, "enzymeBlock"))
            {
                removeObjVar(player, "enzymeBlock");
                playClientEffectLoc(player, "clienteffect/jawa_chatter_03.cef", getLocation(self), 1.0f, "bm_enable");
                broadcast(player, "You have enabled Beast Mastery Loot!");
            }
            else
            {
                setObjVar(player, "enzymeBlock", 1);
                playClientEffectLoc(player, "clienteffect/jawa_chatter_bounty.cef", getLocation(self), 1.0f, "bm_disable");
                broadcast(player, "You have disabled Beast Mastery Loot!");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnStartNpcConversation(obj_id self, obj_id speaker) throws InterruptedException
    {
        chat.chat(self, "Utinni!");
        debugConsoleMsg(speaker, "\\#84e81aRadial this creature to enable or disable certain loot offerings within the galaxy.\\#\n");
        return SCRIPT_CONTINUE;
    }
}
