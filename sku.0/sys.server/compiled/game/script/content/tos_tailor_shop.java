package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Allows players to customize their clothing hue.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 8/20/2024, at 5:13 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.hue;
import script.library.static_item;
import script.library.sui;
import script.library.utils;

import java.util.Arrays;
import java.util.Vector;

public class tos_tailor_shop extends base_script
{
    private static final boolean BLOGGING_ON = false;
    private static final String BLOGGING_CATEGORY = "armor_recolor";
    private static final String VAR_PREFIX = "armor_colorize";
    private static final String PID_NAME = VAR_PREFIX + ".pid";
    private static final String ARMOR_OBJ_LIST = VAR_PREFIX + ".armor_obj_list";
    private static final String ARMOR_NAME_LIST = VAR_PREFIX + ".armor_name_list";
    private static final String PLAYER_ID = VAR_PREFIX + ".player_oid";
    private static final String TOOL_ID = VAR_PREFIX + ".tool_oid";
    private static final String CUSTOMER_SVC_CATEGORY = "crafted_armor_color_kit";
    private static final string_id NO_ARMOR = new string_id("tool/customizer", "no_armor");
    private static final String TITLE = "Nolah's Closet";
    private static final String PROMPT = "Select which piece of clothing or armor you wish to customize.";
    private static final int OBJECT_COLOR_MAX = 4;

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        blog("OnObjectMenuRequest - functions");
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
            CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "A player " + player + " is using crafted armor customization kit " + self + ". [ OnObjectMenuSelect() ]");
            blog("OnObjectMenuSelect - player attempting to use kit not in inventory");
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
        blog("beginArmorColorizationh: init");
        Vector items = new Vector();
        obj_id[] invItems = getInventoryAndEquipment(player);
        if (invItems == null || invItems.length < 0)
        {
            return false;
        }
        blog("invItems.length: " + invItems.length);
        if (invItems != null)
        {
            for (obj_id invItem : invItems)
            {
                if ((getTemplateName(invItem)).startsWith("object/tangible/wearables/"))
                {
                    items.addElement(invItem);
                }
            }
        }
        CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "A player " + player + " has " + items.size() + " valid armor items for colorization using crafted armor customization kit " + self + ". [ beginArmorColorization() ]");
        blog("beginArmorColorization - player has " + items.size() + " armor items in inventory");
        if (items.isEmpty())
        {
            sendSystemMessage(player, NO_ARMOR);
            CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "The player " + player + " DID NOT HAVE ANY valid armor items for colorization using crafted armor customization kit " + self + ". [ beginArmorColorization() ]");
            blog("beginArmorColorization - player DID NOT HAVE ANY armor items in inventory to start color session.");
            return false;
        }
        CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "The player " + player + " has enough valid armor items for colorization using crafted armor customization kit " + self + ". [ beginArmorColorization() ]");
        blog("beginArmorColorization - player has enough armor items in inventory to start color session.");
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
                String name = getEncodedName(piece);
                armorNames.addElement(name);
            }
        }
        if (!armor.isEmpty())
        {
            CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "Armor OID list is " + armor.size() + " long for player " + player + " that is using colorization using crafted armor customization kit " + self + ". [ beginArmorColorization() ]");
            blog("beginArmorColorization - Consolidating armor items.");
        }
        else if (!armorNames.isEmpty())
        {
            CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "Armor Name list is " + armorNames.size() + " long for player " + player + " that is using colorization using crafted armor customization kit " + self + ". [ beginArmorColorization() ]");
            blog("beginArmorColorization - Consolidating armor items.");
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
        CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "ERROR: Armor and Name lists may not have matched in value. Armor list: " + armorListSize + " Name List: " + nameSize + " or both lists were empty for player " + player + " using colorization using crafted armor customization kit " + self + ". [ beginArmorColorization() ]");
        blog("beginArmorColorization - Error. Armor and Name lists may not have matched in value.");
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
            broadcast(player, "No color customization available for selected wearable");
            return SCRIPT_CONTINUE;
        }
        int palColorsLength = palColors.length;
        blog("handleArmorSelection - palColors.length: " + palColorsLength);
        String[] indexName = new String[OBJECT_COLOR_MAX];
        blog("handleArmorSelection - indexName: " + indexName.length);
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
                blog("handleArmorSelection - indexName[" + i + "]: " + indexName[i]);
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
            CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "Coloring Instantiating Color Customization UI on Object: " + armor[idx] + " for player " + player + " has failed because the 1st color was null. Kit OID: " + self + ". [ handleArmorSelection() ]");
            return SCRIPT_CONTINUE;
        }
        CustomerServiceLog(CUSTOMER_SVC_CATEGORY, "Coloring Instantiating Color Customization UI on Object: " + armor[idx] + " for player " + player + " using colorization using crafted armor customization kit " + self + ". [ handleArmorSelection() ]");
        blog("handleArmorSelection - Armor Piece getting color UI: " + armor[idx]);
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
        blog("closeOldWindow - init");
        int pid = sui.getPid(player, PID_NAME);
        blog("closeOldWindow - pid: " + pid);
        if (pid > -1)
        {
            blog("closeOldWindow - force closing: " + pid);
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

    public boolean blog(String msg) throws InterruptedException
    {
        if (!BLOGGING_ON)
        {
            return false;
        }
        else if (msg == null || msg.isEmpty())
        {
            return false;
        }
        LOG(BLOGGING_CATEGORY, msg);
        return true;
    }

}
