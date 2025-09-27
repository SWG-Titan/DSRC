package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Allows players to customize their clothing hue.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 8/20/2024, at 5:13 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.hue;
import script.library.static_item;
import script.library.sui;
import script.library.utils;

import java.util.Arrays;
import java.util.Vector;

public class tos_furniture_shop extends base_script
{
    public static final String VAR_PREFIX = "armor_colorize";
    public static final String PID_NAME = VAR_PREFIX + ".pid";
    public static final String ARMOR_OBJ_LIST = VAR_PREFIX + ".armor_obj_list";
    public static final String PLAYER_ID = VAR_PREFIX + ".player_oid";
    public static final String TOOL_ID = VAR_PREFIX + ".tool_oid";
    public static final String TITLE = "Nolah's Dye Dispenser";
    public static final String PROMPT = "Select which piece of furniture you wish to customize.";
    public static final int OBJECT_COLOR_MAX = 4;

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!isValidId(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        if (mid != null)
        {
            mid.setServerNotify(true);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!isValidId(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            beginArmorColorization(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public boolean beginArmorColorization(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(self) || !exists(self))
        {
            return false;
        }
        else if (!isValidId(player) || !exists(player))
        {
            return false;
        }
        closeOldWindow(player);
        Vector items = new Vector();
        obj_id[] invItems = getInventoryAndEquipment(player);
        if (invItems == null || invItems.length < 0)
        {
            return false;
        }
        if (invItems != null)
        {
            for (obj_id invItem : invItems)
            {
                if ((getTemplateName(invItem)).startsWith("object/tangible/furniture/"))
                {
                    items.addElement(invItem);
                }
            }
        }
        if (items.isEmpty())
        {
            broadcast(player, "You do not have any valid furniture items to customize.");
            return false;
        }
        Vector armor = new Vector();
        Vector armorNames = new Vector();
        if (!items.isEmpty())
        {
            for (Object item : items)
            {
                obj_id piece = (obj_id) item;
                if (static_item.isStaticItem(piece))
                {
                    continue;
                }
                armor.addElement(piece);
                String name = "";
                if (getEncodedName(piece) == null && getEncodedName(piece).isEmpty())
                {
                    name = "(Name Hidden)";
                }
                else
                {
                    name = getEncodedName(piece);
                }
                armorNames.addElement(name);
            }
        }
        int armorListSize = armor.size();
        int nameSize = armorNames.size();
        if (!armor.isEmpty() && !armorNames.isEmpty() && armorListSize == nameSize)
        {
            utils.setScriptVar(player, ARMOR_OBJ_LIST, armor);
            int pid = sui.listbox(self, player, PROMPT, sui.OK_CANCEL, TITLE, armorNames, "handleArmorSelection", true, false);
            dictionary params = new dictionary();
            setSUIAssociatedLocation(pid, self);
            setSUIMaxRangeToObject(pid, 8);
            params.put("callingPid", pid);
            sui.setPid(player, pid, PID_NAME);
            if (pid < 0)
            {
                removePlayerVars(player);
                return false;
            }
            return true;
        }
        return false;
    }

    public int handleArmorSelection(obj_id self, dictionary params) throws InterruptedException
    {
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx < 0)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        utils.setScriptVar(self, PLAYER_ID, player);
        utils.setScriptVar(player, TOOL_ID, self);
        obj_id[] armor = utils.getObjIdArrayScriptVar(player, ARMOR_OBJ_LIST);
        if (armor == null || armor.length == 0)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx >= armor.length)
        {
            return SCRIPT_CONTINUE;
        }
        ranged_int_custom_var[] palColors = hue.getPalcolorVars(armor[idx]);
        if ((palColors == null) || (palColors.length == 0))
        {
            broadcast(player, "No color customization available for this furniture piece.");
            return SCRIPT_CONTINUE;
        }
        int palColorsLength = palColors.length;
        String[] indexName = new String[OBJECT_COLOR_MAX];
        int loop = OBJECT_COLOR_MAX;
        if (palColorsLength < OBJECT_COLOR_MAX)
        {
            Arrays.fill(indexName, "");
            loop = palColorsLength;
        }
        for (int i = 0; i < loop; i++)
        {
            ranged_int_custom_var ri = palColors[i];
            if (ri != null)
            {
                String customizationVar = ri.getVarName();
                if (customizationVar.startsWith("/"))
                {
                    customizationVar = customizationVar.substring(1);
                }
                indexName[i] = customizationVar;
            }
        }
        if (indexName[0].isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        openCustomizationWindow(player, armor[idx], indexName[0], -1, -1, indexName[1], -1, -1, indexName[2], -1, -1, indexName[3], -1, -1);
        return SCRIPT_CONTINUE;
    }

    public int decrementTool(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = utils.getObjIdScriptVar(self, PLAYER_ID);
        removePlayerVars(player);
        return SCRIPT_CONTINUE;
    }

    public int cancelTool(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = utils.getObjIdScriptVar(self, PLAYER_ID);
        if (!isValidId(player) || !exists(player))
        {
            return SCRIPT_CONTINUE;
        }
        removePlayerVars(player);
        return SCRIPT_CONTINUE;
    }

    public void closeOldWindow(obj_id player) throws InterruptedException
    {
        int pid = sui.getPid(player, PID_NAME);
        if (pid > -1)
        {
            forceCloseSUIPage(pid);
            sui.removePid(player, PID_NAME);
        }
    }

    public void removePlayerVars(obj_id player) throws InterruptedException
    {
        obj_id self = getSelf();
        if (!isValidId(self) || !exists(self) || !isValidId(player) || !exists(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, VAR_PREFIX);
        utils.removeScriptVarTree(self, VAR_PREFIX);
        utils.removeObjVar(player, VAR_PREFIX);
    }

}
