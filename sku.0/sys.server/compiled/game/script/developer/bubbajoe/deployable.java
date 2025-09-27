package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Generic script for deployables
@Notes:
   Spawnables are cleared on server restart.
   Spawnables are not saved to the database.
   Spawnables cannot be moved.
   Spawnables can be cleared by the GM.

@Created: Sunday, 3/10/2024, at 9:23 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class deployable extends script.base_script
{
    private static final float MAX_DISTANCE = 15.f;

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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Deploy"));
        if (isGod(player))
        {
            int s_base = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("[Setup]"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU10, new string_id("Set Template"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU11, new string_id("Set Associated Script"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU12, new string_id("Set Name"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU13, new string_id("Trigger Placement"));
        }
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
                promptForObject(self, player);
            }
            else if (item == menu_info_types.SERVER_MENU11)
            {
                promptForScript(self, player);
            }
            else if (item == menu_info_types.SERVER_MENU12)
            {
                promptForName(self, player);
            }
            else if (item == menu_info_types.SERVER_MENU13)
            {
                location here = getLocation(player);
                this.OnGroundTargetLoc(self, player, menu_info_types.ITEM_USE, here.x, here.y, here.z);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z) throws InterruptedException
    {
        if (menuItem != menu_info_types.ITEM_USE)
        {
            broadcast(player, "An error has occurred");
            return SCRIPT_CONTINUE;
        }
        String tangiblePath = "";
        String scriptPath = "";
        if (hasObjVar(self, "deployable.item"))
        {
            tangiblePath = getStringObjVar(self, "deployable.item");
        }
        else
        {
            broadcast(player, "Item is not setup. Please follow radial menus.");
            return SCRIPT_CONTINUE;
        }
        if (!tangiblePath.contains(".iff"))
        {
            broadcast(player, "Invalid template specified. Please follow radial menus.");
            return SCRIPT_CONTINUE;
        }
        //script can be empty 1/2
        if (hasObjVar(self, "deployable.script"))
        {
            scriptPath = getStringObjVar(self, "deployable.script");
        }
        if (!utils.isNestedWithin(self, player))
        {
            broadcast(self, "This must be used from inside your top-level inventory.");
            return SCRIPT_CONTINUE;
        }
        if (getState(player, STATE_SWIMMING) == 1)
        {
            broadcast(player, "You may not deploy that while in water.");
            return SCRIPT_CONTINUE;
        }
        location playerLoc = getLocation(player);
        location newLoc = new location(x, y, z, playerLoc.area, playerLoc.cell);
        if (getDistance(playerLoc, newLoc) <= MAX_DISTANCE)
        {
            if (tangiblePath.endsWith(".iff") && tangiblePath.startsWith("object/"))
            {
                obj_id summonable = createObject(tangiblePath, newLoc);
                //script can be empty 2/2
                if (hasObjVar(self, "deployable.script"))
                {
                    attachScript(summonable, scriptPath);
                }
                if (hasObjVar(self, "deployable.name"))
                {
                    setName(summonable, getStringObjVar(self, "deployable.name"));
                }
                setObjVar(summonable, "deployable.deleteme", true);
                setYaw(summonable, getYaw(player));
                if (shouldDecrement(self, player))
                {
                    decrementCount(self);
                }
                LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has placed " + tangiblePath + " at " + newLoc.toReadableFormat(true));
                return SCRIPT_CONTINUE;
            }
            else
            {
                broadcast(self, "Failed to create " + tangiblePath);
                return SCRIPT_CONTINUE;
            }
        }
        else
        {
            broadcast(player, "That location is too far away to deploy this object.");
            return SCRIPT_CONTINUE;
        }
    }

    public int promptForObject(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter a tangible object for this tool to summon.\n\n*Tangible Only*", "updateObject");
        return SCRIPT_CONTINUE;
    }

    public int promptForScript(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter a script for this tool to attach when summoning item. \n\n*This will not override template defined scripts*", "updateScript");
        return SCRIPT_CONTINUE;
    }

    public int promptForName(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter a name to set when summoning item. \n\n*\\#00FF00Keep appropriate\\#.*", "updateName");
        return SCRIPT_CONTINUE;
    }

    public int updateObject(obj_id self, dictionary params) throws InterruptedException
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
            setObjVar(self, "deployable.item", text);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable object to summon " + text);
        }
        return SCRIPT_CONTINUE;
    }

    public int updateScript(obj_id self, dictionary params) throws InterruptedException
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
            setObjVar(self, "deployable.script", text);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable object to attach " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public int updateName(obj_id self, dictionary params) throws InterruptedException
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
            setObjVar(self, "deployable.name", text);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable object to be named " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public boolean shouldDecrement(obj_id self, obj_id player)
    {
        return !isGod(player);
    }
}
