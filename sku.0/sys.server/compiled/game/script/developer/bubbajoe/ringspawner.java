package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Spawns a ring of mobs around the ground target location.
@Note: As of 4/5/2024,  this script is a work in progress and is not yet functional.
@Created: Sunday, 3/10/2024, at 9:23 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.create;
import script.library.sui;
import script.library.utils;

public class ringspawner extends script.base_script
{
    private static final float MAX_DISTANCE = 15.f;

    public static int createCircleSpawn(obj_id self, obj_id player, location target, String creature, int amount, float distance, String script) throws InterruptedException
    {
        //obj_id[] players = getAllPlayers(target, distance);
        var x = 0.0f;
        var z = 0.0f;
        LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " is attempting to spawn a ring encounter...");
        for (int i = 0; i < amount; i++)
        {
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " processing mob " + i + " of " + amount);
            float angle = (float) (i * (360 / amount));
            x = target.x + (float) Math.cos(angle) * distance;
            z = target.z + (float) Math.sin(angle) * distance;
            obj_id creatureObj = create.object(creature, new location(x, getHeightAtLocation(x, z), z, target.area));
            if (!script.equals("N/A"))
            {
                attachScript(creatureObj, script);
            }
            faceTo(creatureObj, self);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " setup mob " + i + " of " + amount);
        }
        return SCRIPT_CONTINUE;
    }

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
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Create Ring Encounter"));
            int s_base = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("[Setup]"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU10, new string_id("Set Mob"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU11, new string_id("Set Script"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU12, new string_id("Set Name"));
            mi.addSubMenu(s_base, menu_info_types.SERVER_MENU13, new string_id("Set Radius and Amount"));
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
                promptForRatio(self, player);
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
        if (hasObjVar(self, "deployable.mob"))
        {
            tangiblePath = getStringObjVar(self, "deployable.mob");
        }
        else
        {
            broadcast(player, "Item is not setup. Please follow radial menus.");
            return SCRIPT_CONTINUE;
        }
        if (tangiblePath.contains(".iff"))
        {
            broadcast(player, "Invalid mob. Raw templates are not allow for this tool.");
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
        if (isIdValid(playerLoc.cell))
        {
            newLoc = playerLoc;
        }
        String mob = getStringObjVar(self, "deployable.mob");
        int amount = getIntObjVar(self, "deployable.amount");
        float radius = getFloatObjVar(self, "deployable.radius");
        if (!hasObjVar(self, "deployable.script"))
        {
            scriptPath = "N/A";
        }
        else
        {
            scriptPath = getStringObjVar(self, "deployable.script");
        }
        createCircleSpawn(self, player, newLoc, mob, amount, radius, scriptPath);
        LOG("ethereal", "[Deployable]: Location was " + newLoc.toReadableFormat(true));
        return SCRIPT_CONTINUE;
    }

    public int promptForObject(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter the mob string for this tool to summon.\n\n*Attackable only!*", "updateObject");
        return SCRIPT_CONTINUE;
    }

    public int promptForScript(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter a script for this tool to attach when summoning the mobs. \n\n*This will not override creature table defined scripts*", "updateScript");
        return SCRIPT_CONTINUE;
    }

    public int promptForName(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter a name to set when summoning the mobs. \n\n*\\#00FF00Keep appropriate\\#.*", "updateName");
        return SCRIPT_CONTINUE;
    }

    public int promptForRatio(obj_id self, obj_id player) throws InterruptedException
    {
        sui.inputbox(self, player, "Enter a numberfor the radius and amount of mobs to spawn in this format: \"1:1\" \n\n*\\#00FF00Keep under 32:32\\#.*", "updateRatio");
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
            setObjVar(self, "deployable.mob", text);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable ring mob to summon " + text);
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
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable ring mob to attach " + text + " upon summon.");
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
            setObjVar(self, "deployable.mob", text);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable ring mob to be named " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public int updateRatio(obj_id self, dictionary params) throws InterruptedException
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
            if (!text.contains(":"))
            {
                broadcast(player, "Invalid format. Proper format: \"1:1\" (NumberColonNumber)");
            }
            String[] rawRatio = text.split(":");
            setObjVar(self, "deployable.radius", Float.parseFloat(rawRatio[0]));
            setObjVar(self, "deployable.amount", rawRatio[1]);
            LOG("ethereal", "[Deployable]: " + getPlayerFullName(player) + " has modified deployable ring mob spawning ratio to " + text + " upon summon.");
        }
        return SCRIPT_CONTINUE;
    }

    public boolean shouldDecrement(obj_id self, obj_id player)
    {
        return !isGod(player);
    }
}
