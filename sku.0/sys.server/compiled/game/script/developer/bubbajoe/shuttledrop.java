package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Drops a shuttle with loot.
@Note: Setup the tool before use. There are 2 scenarios: Rebel and Imperial. Specify the loot table and count. and click and use.
@Created: Saturday, 3/30/2024, at 3:29 AM,
@Copyright © SWG-OR 2024.
        Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.loot;
import script.library.sui;
import script.library.utils;

public class shuttledrop extends script.base_script
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
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Summon Shuttle"));
        int s_base = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Setup"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU10, new string_id("Set Shuttle Scenario"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU11, new string_id("Set Loot Table"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU12, new string_id("Set Loot Count"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU13, new string_id("Set Delay"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                return SCRIPT_OVERRIDE;
            }
            if (item == menu_info_types.SERVER_MENU10)
            {
                sui.inputbox(self, player, "Enter 0 for rebel, 1 for imperial", "OnUpdateScenario", "OnUpdateScenario", 1, false, "");
            }
            else if (item == menu_info_types.SERVER_MENU11)
            {
                sui.inputbox(self, player, "Enter the loot table you want to generate from.", "OnUpdateLootTable", "OnUpdateLootTable", 250, false, "");
            }
            else if (item == menu_info_types.SERVER_MENU12)
            {
                sui.inputbox(self, player, "Enter the amount of loot items to generate from the table specified..", "OnUpdateLootCount", "OnUpdateLootCount", 30, false, "4");
            }
            else if (item == menu_info_types.SERVER_MENU13)
            {
                sui.inputbox(self, player, "Enter the delay in seconds before the shuttle drops the loot.", "OnUpdateDelay", "OnUpdateDelay", 30, false, "5");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        doAnimationAction(player, "laugh");
        location whereAmIGoing = getLocation(self);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        if (!isInWorldCell(player))
        {
            broadcast(player, "You cannot use this tool indoors.");
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(self, "shuttle.faction"))
        {
            broadcast(player, "You must set the shuttle scenario before summoning.");
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(self, "shuttle.lootTable"))
        {
            broadcast(player, "You must set the loot table before summoning.");
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(self, "shuttle.lootCount"))
        {
            broadcast(player, "You must set the loot count before summoning.");
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(self, "shuttle.delay"))
        {
            broadcast(player, "You must set the delay before summoning.");
            return SCRIPT_CONTINUE;
        }
        boolean faction = getBooleanObjVar(self, "shuttle.faction");
        handleShuttleDropVisual(self, faction, whereAmIGoing, player);
        return SCRIPT_CONTINUE;
    }

    public int handleShuttleDropVisual(obj_id self, boolean faction, location where, obj_id who) throws InterruptedException
    {
        obj_id[] players = getAllPlayers(where, 124f);
        if (players == null || players.length == 0)
        {
            return SCRIPT_CONTINUE;
        }
        for (obj_id player : players)
        {
            if (!isIdValid(player))
            {
                continue;
            }
            if (faction)
            {
                String message = "Mayday! Mayday! Mayday! I have to drop my payload, " + getPlayerFullName(player) + "! Get to it before those pesky Imperials catch us!";
                prose_package commP = new prose_package();
                commP.stringId = new string_id(message);
                commPlayer(self, player, commP, "object/mobile/dressed_rebel_intel_officer_human_female_01.iff");
                playClientEffectLoc(player, "appearance/rebel_transport_touch_and_go.prt", getLocation(player), 2.0f);
                LOG("ethereal", "[Developer]: " + getName(self) + " started Cargo Drop on " + getName(player));
            }
            else
            {
                String message = "Prepare for your cargo delivery, " + getFirstName(player) + "! I don't want any hiccups.";
                prose_package commP = new prose_package();
                commP.stringId = new string_id(message);
                commPlayer(self, player, commP, "object/mobile/dressed_imperial_officer_m_2.iff");
                playClientEffectLoc(player, "appearance/imperial_transport_touch_and_go.prt", getLocation(player), 2.0f);
                LOG("ethereal", "[Developer]: " + getName(self) + " started Cargo Drop on " + getName(player));
            }
        }
        dictionary d = new dictionary();
        float delay = getFloatObjVar(self, "shuttle.delay");
        d.put("player", who);
        d.put("lootCount", getIntObjVar(self, "shuttle.lootCount"));
        d.put("where", where);
        d.put("lootTable", getStringObjVar(self, "shuttle.lootTable"));
        messageTo(self, "handleShuttleDropLoot", d, delay, false);
        return SCRIPT_CONTINUE;
    }

    public int handleShuttleDropLoot(obj_id self, dictionary params) throws InterruptedException
    {
        //create cargo container with loot table from params
        obj_id owner = params.getObjId("player");
        String lootTable = params.getString("lootTable");
        location where = params.getLocation("where");
        int amt = params.getInt("lootCount");
        if (lootTable == null || lootTable.equals(""))
        {
            broadcast(owner, "No loot table specified for shuttle drop. Aborting.");
            return SCRIPT_CONTINUE;
        }
        if (amt <= 0)
        {
            broadcast(owner, "No loot count specified for shuttle drop. Aborting.");
            return SCRIPT_CONTINUE;
        }
        if (where == null)
        {
            broadcast(owner, "No location specified for shuttle drop. Aborting.");
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(owner))
        {
            broadcast(owner, "No owner specified for shuttle drop. Aborting.");
            return SCRIPT_CONTINUE;
        }
        obj_id cargo = create.object("object/tangible/container/loot/large_container.iff", where);
        attachScript(cargo, "item.container.player_loot_crate_adhoc");
        setName(cargo, "\\#FFC0CBa cargo container\\#.");
        loot.makeLootInContainer(cargo, lootTable, amt, 300);
        broadcast(owner, "A cargo container was made with " + amt + " items from the loot table: " + lootTable);
        obj_id[] contents = getContents(cargo);
        {
            for (obj_id content : contents)
            {
                if (hasScript(content, "item.special.nomove"))
                {
                    detachScript(content, "item.special.nomove");
                    broadcast(owner, "Removing nomove script from " + content);
                }
                if (hasObjVar(content, "noTrade"))
                {
                    removeObjVar(self, "noTrade");
                    broadcast(owner, "Removing No-Trade from " + content);
                }
            }
        }
        modifyYaw(self, rand(-180, 180));
        modifyPitch(self, rand(-180, 180));
        modifyRoll(self, rand(-180, 180));
        where.y = getHeightAtLocation(where.x, where.z) + 2.0f;
        setLocation(self, where);
        obj_id[] players = getAllPlayers(where, 120f);
        if (players == null)
        {
            return SCRIPT_CONTINUE;
        }
        for (obj_id player : players)
        {
            if (!isIdValid(player))
            {
                continue;
            }
            obj_id waypoint = createWaypointInDatapad(player, where);
            if (isIdValid(waypoint))
            {
                setWaypointActive(waypoint, true);
                setWaypointName(waypoint, utils.packStringId(new string_id("Potential Ejected Cargo")));
            }
            broadcast(owner, "A waypoint was made for player " + getName(player) + " at the cargo container.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateScenario(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (!text.equals("0") && !text.equals("1"))
            {
                broadcast(player, "Invalid format. Proper format: \"0\" for Rebel, \"1\" for Imperial.");
            }
            setObjVar(self, "shuttle.faction", Integer.parseInt(text));
            LOG("ethereal", "[Shuttle Drop]: " + getPlayerFullName(player) + " has modified shuttle drop scenario to " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateLootTable(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            setObjVar(self, "shuttle.lootTable", text);
            LOG("ethereal", "[Shuttle Drop]: " + getPlayerFullName(player) + " has modified shuttle drop loot table to " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateLootCount(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            setObjVar(self, "shuttle.lootCount", Integer.parseInt(text));
            LOG("ethereal", "[Shuttle Drop]: " + getPlayerFullName(player) + " has modified shuttle drop loot count to " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateDelay(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String text = sui.getInputBoxText(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        else
        {
            setObjVar(self, "shuttle.delay", Float.parseFloat(text));
            LOG("ethereal", "[Shuttle Drop]: " + getPlayerFullName(player) + " has modified shuttle drop delay to " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }
}