package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 8:36 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.money;
import script.library.sui;

public class tos_world_boss extends base_script
{

    public static final int COST = 25000;
    public final String[] WORLD_BOSS_HANDLERS = {
            "handleKraytMovement",
            "handlePekoMovement",
            "handleDonkDonkMovement",
            "handleRoliiMovement",
            "handleCrusaderMovement",
            "handleIG24Movement"
    };
    public String[] WORLD_BOSS_OPTIONS = {
            "Elder Ancient Krayt Dragon",
            "Mutated Peko-Peko Empress",
            "Donk-Donk Binks", "Darth Rolii",
            "The Crusader",
            "IG-24"
    };

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        int menu = menuInfo.addRootMenu(menu_info_types.ITEM_USE, new string_id("Bounty Hunter's Guild Transport"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            String title = "\\#DAA520Bounty Hunter's Guild Transport\\#.";
            String prompt = "\\#DAA520Payment Required:\\#.\t25,000 credits\n\nWould you like to pay the fee and travel to a world boss location?";
            sui.msgbox(self, player, prompt, sui.OK_CANCEL, title, "handlePayment");
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePayment(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have declined to pay the fee.");
            return SCRIPT_CONTINUE;
        }
        if (getTotalMoney(player) < COST)
        {
            broadcast(player, "You do not have enough credits to depart.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            money.requestPayment(player, self, COST, "no_handler", null, false);
        }
        String title = "\\#DAA520Bounty Hunter's Guild Transport\\#.";
        String prompt = "Please select a world boss from the list below to travel to.";
        sui.listbox(self, player, prompt, sui.OK_CANCEL, title, WORLD_BOSS_OPTIONS, "handleWorldBossSelection");
        return SCRIPT_CONTINUE;
    }

    public int handleWorldBossSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);

        if (idx == -1 || button == sui.BP_CANCEL)
        {
            if (button == sui.BP_CANCEL)
            {
                broadcast(player, "You have declined transit and have been refunded.");
                transferBankCreditsFromNamedAccount(money.ACCT_TRAVEL, player, COST, "no_handler", "no_handler", null);
            }
            return SCRIPT_CONTINUE;
        }

        if (idx >= 0 && idx < WORLD_BOSS_HANDLERS.length)
        {
            String selectedBoss = WORLD_BOSS_OPTIONS[idx];
            sui.msgbox(self, player, "\\#DAA520You have selected " + selectedBoss + "\nAre you sure you want to travel to " + selectedBoss + "'s last known location?", sui.YES_NO, WORLD_BOSS_HANDLERS[idx]);
        }

        return SCRIPT_CONTINUE;
    }

    public int handleKraytMovement(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(player) < 90)
        {
            broadcast(player, "You must be level 90 to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        location krayt = new location(-4777.877f, 56.07668f, -4313.15f, "tatooine", null);
        warpPlayerToLocation(player, krayt);
        return SCRIPT_CONTINUE;
    }

    public int handleRoliiMovement(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(player) < 90)
        {
            broadcast(player, "You must be level 90 to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        location rolii = new location(-5423.312f, 3.2507432f, 7287.3315f, "corellia", null);
        warpPlayerToLocation(player, rolii);
        return SCRIPT_CONTINUE;
    }

    public int handleDonkDonkMovement(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(player) < 90)
        {
            broadcast(player, "You must be level 90 to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        location rolii = new location(-2094.1987f, 75.7696f, 3264.3337f, "rori", null);
        warpPlayerToLocation(player, rolii);
        return SCRIPT_CONTINUE;
    }

    public int handleCrusaderMovement(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(player) < 90)
        {
            broadcast(player, "You must be level 90 to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        location crusader = new location(-4770.0386f, 16.24033f, 4369.7153f, "endor", null);
        warpPlayerToLocation(player, crusader);
        return SCRIPT_CONTINUE;
    }

    public int handlePekoMovement(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(player) < 90)
        {
            broadcast(self, "You must be level 90 to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        location peko = new location(-5648.4614f, -158.96f, -25.302708f, "naboo", null);
        warpPlayerToLocation(player, peko);
        return SCRIPT_CONTINUE;
    }

    public int handleIG24Movement(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (getLevel(player) < 90)
        {
            broadcast(self, "You must be level 90 to travel to this location.");
            return SCRIPT_CONTINUE;
        }
        location ig24 = new location(-5215f, 12f, -5093f, "lok", null);
        warpPlayerToLocation(player, ig24);
        return SCRIPT_CONTINUE;
    }

    public void warpPlayerToLocation(obj_id player, location loc) throws InterruptedException
    {
        warpPlayer(player, loc.area, loc.x, loc.y, loc.z, null, 0, 0, 0, "", true);
    }
}
