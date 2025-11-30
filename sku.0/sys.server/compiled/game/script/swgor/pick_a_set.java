package script.swgor;/*
@Origin: dsrc.script.swgor
@Author:  BubbaJoeX
@Purpose: Heroic jewlery selection
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 1/13/2025, at 5:08 PM, 
@Copyright © SWG: Titan 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.static_item;
import script.library.sui;
import script.library.utils;

import java.util.HashSet;

import static script.string_id.unlocalized;

public class pick_a_set extends welcome_pack
{

    public void sync(obj_id self)
    {
        setName(self, "Heroic Jewelry Cache");
        setDescriptionString(self, "You may open this cache to receive one free set of heroic jewelry.");
    }

    public static final String HANDLER_SET_TOOL_CLASS = "handleSetToolClass";
    public static final string_id MENU_USE = unlocalized("Redeem");
    public static final String[] SETS = {
            "Heroism Set",
            "Dire Fate Set (Bounty Hunter)",
            "Enforcer's Set (Bounty Hunter)",
            "Flawless Set (Bounty Hunter)",
            "Frontman Set (Commando)",
            "Grenadier Set (Commando)",
            "Juggernaut Set (Commando)",
            "Dark Fury Set (Jedi)",
            "Guardian's Set (Jedi)",
            "Lightsaber Duelist's Set (Jedi)",
            "Blackbar's Doom Set (Medic)",
            "First Responder's Set (Medic)",
            "Striker's Set (Medic)",
            "Dead Eye Set (Officer)",
            "General's Set (Officer)",
            "Hellstorm Set (Officer)",
            "Gambler's Set (Smuggler)",
            "Rogue Set (Smuggler)",
            "Scoundrel's Set (Smuggler)",
            "Assassins's Set (Spy)",
            "The Ghost Set (Spy)",
            "The Razor Cat Set (Spy)",
            "Tragedy Set (Entertainer)",
            "Tinkerers Set (Trader)"
    };

    public static obj_id[] grantBioSet(obj_id player, int num, obj_id destroy_me) throws InterruptedException
    {
        obj_id pInv = utils.getInventoryContainer(player);
        HashSet<obj_id> theSet = new HashSet<>();
        String root = "";
        if (num == 0)
        {
            root = "set_hero_01_01";
        }
        if (num == 1)
        {
            root = "set_bh_utility_b_01_01";
        }
        if (num == 2)
        {
            root = "set_bh_dps_01_01";
        }
        if (num == 3)
        {
            root = "set_bh_utility_a_01_01";
        }
        if (num == 4)
        {
            root = "set_commando_utility_a_01_01";
        }
        if (num == 5)
        {
            root = "set_commando_dps_01_01";
        }
        if (num == 6)
        {
            root = "set_commando_utility_b_01_01";
        }
        if (num == 7)
        {
            root = "set_jedi_utility_a_01_01";
        }
        if (num == 8)
        {
            root = "set_jedi_utility_b_01_01";
        }
        if (num == 9)
        {
            root = "set_jedi_dps_01_01";
        }
        if (num == 10)
        {
            root = "set_medic_utility_b_01_01";
        }
        if (num == 11)
        {
            root = "set_medic_utility_a_01_01";
        }
        if (num == 12)
        {
            root = "set_medic_dps_01_01";
        }
        if (num == 13)
        {
            root = "set_officer_dps_01_01";
        }
        if (num == 14)
        {
            root = "set_officer_utility_b_01_01";
        }
        if (num == 15)
        {
            root = "set_officer_utility_a_01_01";
        }
        if (num == 16)
        {
            root = "set_smuggler_utility_b_01_01";
        }
        if (num == 17)
        {
            root = "set_smuggler_utility_a_01_01";
        }
        if (num == 18)
        {
            root = "set_smuggler_dps_01_01";
        }
        if (num == 19)
        {
            root = "set_spy_dps_01_01";
        }
        if (num == 20)
        {
            root = "set_spy_utility_a_01_01";
        }
        if (num == 21)
        {
            root = "set_spy_utility_b_01_01";
        }
        if (num == 22)
        {
            root = "set_ent_01_01";
        }
        if (num == 23)
        {
            root = "set_trader_01_01";
        }
        if (num > 23)
        {
            sendSystemMessageTestingOnly(player, "Index Selected: " + num + ", Is out of bounds. Error. Try again.");
            return null;
        }
        theSet.add(static_item.createNewItemFunction("item_ring_" + root, pInv));
        theSet.add(static_item.createNewItemFunction("item_band_" + root, pInv));
        theSet.add(static_item.createNewItemFunction("item_necklace_" + root, pInv));
        theSet.add(static_item.createNewItemFunction("item_bracelet_r_" + root, pInv));
        theSet.add(static_item.createNewItemFunction("item_bracelet_l_" + root, pInv));
        obj_id[] items = new obj_id[theSet.size()];
        theSet.toArray(items);
        for (obj_id a : items)
        {
            //attachScript(a, "item.armor.biolink_item_non_faction");
            setObjVar(a, "noTrade", true);
            setObjVar(a, "noTradeShared", true);
        }
        showLootBox(player, items);
        destroyObject(destroy_me);
        return items;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        int menu = mi.addRootMenu(menu_info_types.ITEM_USE, MENU_USE);
        sendDirtyObjectMenuNotification(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (canManipulate(player, self, false, true, 3.0f, false))
            {
                if (utils.isNestedWithinAPlayer(self))
                {
                    startSetSelection(player);
                    sendDirtyObjectMenuNotification(self);
                    return SCRIPT_CONTINUE;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void startSetSelection(obj_id player) throws InterruptedException
    {
        obj_id self = getSelf();
        String prompt = "Please select your one free set of heroic jewelry.";
        String title = "Heroic Jewelry Set Redemption";
        int pid = sui.listbox(self, player, prompt, sui.OK_CANCEL, title, SETS, "handleOptionSelect", true, false);
        setWindowPid(player, pid);
    }

    public void setWindowPid(obj_id player, int pid) throws InterruptedException
    {
        if (pid > -1)
        {
            utils.setScriptVar(player, "character_builder.pid", pid);
        }
    }

    @Override
    public int OnInitialize(obj_id self)
    {
        setObjVar(self, "noTradeShared", true);
        return SCRIPT_CONTINUE;
    }

    public int handleOptionSelect(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        int idx = sui.getListboxSelectedRow(params);
        if (btn == sui.BP_CANCEL)
        {
            cleanScriptVars(player);
            return SCRIPT_CONTINUE;
        }
        if (idx == -1 || idx > SETS.length)
        {
            cleanScriptVars(player);
            return SCRIPT_CONTINUE;
        }
        grantBioSet(player, idx, self);
        closeOldWindow(player);
        return SCRIPT_CONTINUE;
    }

    public void cleanScriptVars(obj_id player) throws InterruptedException
    {
        obj_id self = getSelf();
        utils.removeScriptVarTree(player, "character_builder");
        utils.removeScriptVarTree(self, "character_builder");
    }
}
