package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Allows GM's to manage cell permissions, (public or private) and lock/unlock accordingly with a message upon entering the cell.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/7/2024, at 12:45 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.trial;
import script.library.utils;

public class tos_gating extends base_script
{
    public String[] CELL_OPTIONS = {
            "Lock Cell",
            "Unlock Cell",
            "Gate Faction",
            "Gate Faction (AI)",
            "Gate Skill",
            "Gate Level",
            "Gate Command"
    };

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
        if (isGod(player))
        {
            int gateMenu = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Room Controller"));
            mi.addSubMenu(gateMenu, menu_info_types.SERVER_MENU1, new string_id("Manage Cells"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            obj_id building = getTopMostContainer(self);
            String[] cellNames = getCellNames(building);
            if (cellNames != null && cellNames.length > 1)
            {
                LOG("ethereal", "[Cell Locker]: Cells are greater than 1, proceeding...");
                sui.listbox(self, player, "Select the cell you wish to gate.", sui.OK_CANCEL, "Select Cell", cellNames, "gateCell", true);
            }
            else
            {
                LOG("ethereal", "[Cell Locker]: Cell count is 1, aborting...");
                broadcast(player, "You cannot lock a building with only one cell.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int gateCell(obj_id self, dictionary params) throws InterruptedException
    {
        String[] cellNames = getCellNames(getTopMostContainer(self));
        LOG("ethereal", "[Cell Locker]: Entered gateCell...");
        obj_id player = sui.getPlayerId(params);
        LOG("ethereal", "[Cell Locker]: Player is " + player + "...");
        obj_id building = getTopMostContainer(self);
        LOG("ethereal", "[Cell Locker]: Building is " + building + "...");
        int cellName = sui.getListboxSelectedRow(params);
        LOG("ethereal", "[Cell Locker]: Cell name is " + cellNames[cellName] + "...");
        if (cellNames[cellName] != null)
        {
            LOG("ethereal", "[Cell Locker]: Cell name is valid, proceeding...");
            if (hasCell(building, cellNames[cellName]))
            {
                LOG("ethereal", "[Cell Locker]: Cell " + cellNames[cellName] + " exists...");
                broadcast(self, "Cell " + cellNames[cellName] + " exists.");
                setObjVar(self, "cellToManage", cellNames[cellName]);
                LOG("ethereal", "[Cell Locker]: Showing cell options...");
                sui.listbox(self, player, "Select the action you wish to take.", sui.OK_CANCEL, "Select Action", CELL_OPTIONS, "handleCellAction", true);
            }
            else
            {
                LOG("ethereal", "[Cell Locker]: Cell " + cellNames[cellName] + " does not exist...");
                broadcast(self, "Cell " + cellNames[cellName] + " does not exist.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleCellAction(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "[Cell Locker]: Entered handleCellAction...");
        obj_id player = sui.getPlayerId(params);
        int cellAction = sui.getListboxSelectedRow(params);
        String cellToManage = getStringObjVar(self, "cellToManage");
        if (cellAction == 0)
        {
            LOG("ethereal", "[Cell Locker]: Locking cell " + cellToManage + "...");
            broadcast(player, "Locking cell " + cellToManage + "...");
            sui.inputbox(self, player, "Enter the message you wish to display when a player enters this cell.", "Room Controller", "handleCellMessage", "");
            trial.makeCellPrivate(getTopMostContainer(self), cellToManage);

        }
        else if (cellAction == 1)
        {
            LOG("ethereal", "[Cell Locker]: Unlocking cell " + cellToManage + "...");
            broadcast(player, "Unlocking cell " + cellToManage + "...");
            obj_id targetCell = getCellId(getTopMostContainer(self), cellToManage);
            String[] scriptsToDetach = {
                    "structure.gating.gate_faction",
                    "structure.gating.gate_faction_ai",
                    "structure.gating.gating_skill",
                    "structure.gating.gating_level",
                    "structure.gating.gating_command",
                    "content.tos_gating_msg"
            };
            for (String script : scriptsToDetach)
            {
                if (hasScript(targetCell, script))
                {
                    detachScript(targetCell, script);
                }
            }
            trial.makeCellPublic(getTopMostContainer(self), cellToManage);
        }
        else if (cellAction == 2)
        {
            LOG("ethereal", "[Cell Locker]: Gating cell " + cellToManage + " to faction...");
            broadcast(player, "Gating cell " + cellToManage + " to faction...");
            obj_id targetCell = getCellId(getTopMostContainer(self), cellToManage);
            if (hasScript(targetCell, "content.tos_gating_msg"))
            {
                detachScript(targetCell, "content.tos_gating_msg");
            }
            attachScript(targetCell, "structure.gating.gate_faction");
            sui.inputbox(self, player, "Enter the faction you wish to gate with.", "Room Controller", "handleFactionGating", "");
        }
        else if (cellAction == 3)
        {
            LOG("ethereal", "[Cell Locker]: Gating cell " + cellToManage + " to faction AI...");
            broadcast(player, "Gating cell " + cellToManage + " to faction AI...");
            obj_id targetCell = getCellId(getTopMostContainer(self), cellToManage);
            if (hasScript(targetCell, "content.tos_gating_msg"))
            {
                detachScript(targetCell, "content.tos_gating_msg");
            }
            attachScript(targetCell, "structure.gating.gate_faction_ai");
            sui.inputbox(self, player, "Enter the faction AI (ex: jabba, jawa, tusken, etc) you wish to gate with.", "Room Controller", "handleFactionAIGating", "");
        }
        else if (cellAction == 4)
        {
            LOG("ethereal", "[Cell Locker]: Gating cell " + cellToManage + " to skill...");
            broadcast(player, "Gating cell " + cellToManage + " to skill...");
            obj_id targetCell = getCellId(getTopMostContainer(self), cellToManage);
            if (hasScript(targetCell, "content.tos_gating_msg"))
            {
                detachScript(targetCell, "content.tos_gating_msg");
            }
            attachScript(targetCell, "structure.gating.gating_skill");
            sui.inputbox(self, player, "Enter the skill you wish to gate with.", "Room Controller", "handleGatingSkill", "");
        }
        else if (cellAction == 5)
        {
            LOG("ethereal", "[Cell Locker]: Gating cell " + cellToManage + " to level...");
            broadcast(player, "Gating cell " + cellToManage + " to level...");
            obj_id targetCell = getCellId(getTopMostContainer(self), cellToManage);
            if (hasScript(targetCell, "content.tos_gating_msg"))
            {
                detachScript(targetCell, "content.tos_gating_msg");
            }
            attachScript(targetCell, "structure.gating.gating_level");
            sui.inputbox(self, player, "Enter the level you wish to gate with.", "Room Controller", "handleGatingLevel", "");
        }
        else if (cellAction == 6)
        {
            LOG("ethereal", "[Cell Locker]: Gating cell " + cellToManage + " to command...");
            broadcast(player, "Gating cell " + cellToManage + " to command...");
            obj_id targetCell = getCellId(getTopMostContainer(self), cellToManage);
            if (hasScript(targetCell, "content.tos_gating_msg"))
            {
                detachScript(targetCell, "content.tos_gating_msg");
            }
            attachScript(targetCell, "structure.gating.gating_command");
            sui.inputbox(self, player, "Enter the command you wish to gate with.", "Room Controller", "handleGatingCommand", "");
        }
        else
        {
            LOG("ethereal", "[Cell Locker]: CELL_OPTIONS index is invalid...");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleCellMessage(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("ethereal", "[Cell Locker]: Entered handleCellMessage...");
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        String cellMessage = sui.getInputBoxText(params);
        if (cellMessage == null || cellMessage.equals(""))
        {
            LOG("ethereal", "[Cell Locker]: Cell message is null, using default.");
            setObjVar(targetCell, "cellLockMessage", "This room is locked. You do not have permission to enter.");
            attachScript(targetCell, "content.tos_gating_msg");
            return SCRIPT_CONTINUE;
        }
        else
        {
            LOG("ethereal", "[Cell Locker]: Cell message is " + cellMessage + "...");
            broadcast(player, "Cell message is " + cellMessage + "...");
            LOG("ethereal", "[Cell Locker]: Cell ID is " + targetCell + ", applying script and message...");
            setObjVar(targetCell, "cellLockMessage", cellMessage);
            attachScript(targetCell, "content.tos_gating_msg");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleFactionGating(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        String faction = sui.getInputBoxText(params);
        if (faction == null || faction.equals(""))
        {
            broadcast(player, "Faction is null, aborting...");
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Faction is " + faction + "...");
            setObjVar(targetCell, "gating.faction", faction);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleFactionAIGating(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        String faction = sui.getInputBoxText(params);
        if (faction == null || faction.equals(""))
        {
            broadcast(player, "Faction is null, aborting...");
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Faction is " + faction + "...");
            setObjVar(targetCell, "gating.faction", faction);
        }
        sui.inputbox(self, player, "Enter the faction AI (ex: jabba, jawa, tusken, etc) you wish to gate with.", "Room Controller", "handleFactionAIGatingValue", "");
        return SCRIPT_CONTINUE;
    }

    public int handleFactionAIGatingValue(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        int factionStanding = utils.stringToInt(sui.getInputBoxText(params));
        if (factionStanding == 0)
        {
            broadcast(player, "Faction standing is 0, aborting...");
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Faction standing is " + factionStanding + "...");
            setObjVar(targetCell, "gating.faction_standing", factionStanding);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleGatingSkill(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        String skill = sui.getInputBoxText(params);
        if (skill == null || skill.equals(""))
        {
            broadcast(player, "Skill is null, aborting...");
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Skill is " + skill + "...");
            setObjVar(targetCell, "gating.skill", skill);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleGatingLevel(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        int level = utils.stringToInt(sui.getInputBoxText(params));
        if (level == 0)
        {
            broadcast(player, "Level is 0, aborting...");
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Level is " + level + "...");
            setObjVar(targetCell, "gating.level", level);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleGatingCommand(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id targetCell = getCellId(getTopMostContainer(self), getStringObjVar(self, "cellToManage"));
        String command = sui.getInputBoxText(params);
        if (command == null || command.equals(""))
        {
            broadcast(player, "Command is null, aborting...");
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Command is " + command + "...");
            setObjVar(targetCell, "gating.command", command);
        }
        return SCRIPT_CONTINUE;
    }

}
