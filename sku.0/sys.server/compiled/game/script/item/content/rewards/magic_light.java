package script.item.content.rewards;/*
@Origin: script.item.
@Author: BubbaJoeX
@Purpose: Wim Magwitt's Luminous Lamp
*/

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.*;
import script.library.sui;
import script.library.utils;

public class magic_light extends script.base_script
{
    public static final String SCRIPT_MAGIC_LIGHT = "item.content.rewards.magic_light";
    public static final String DATATABLE_MAGIC_LIGHT_PREFIX = "datatables/furniture/";
    public static final String DATATABLE_MAGIC_LIGHT_SUFFIX = ".iff";
    public static final String DATATABLE_MAIN_COLOR_COL = "root_color";
    public static final String DATATABLE_SUB_COLOR_COL = "color";
    public static final String DATATABLE_SUB_COLOR_DETAIL = "description";
    public static final String OBJVAR_CLAIMED_BY = "claimedBy";
    public static String[] RANGES_MAGIC_LIGHT = {
            "2m",
            "4m",
            "8m",
            "16m",
    };
    public static String[] MCOLOR_MAGIC_LIGHT = {
            "blue",
            "cyan",
            "gray",
            "green",
            "orange",
            "pink",
            "purple",
            "red",
            "white",
            "yellow",
    };

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (hasObjVar(self, OBJVAR_CLAIMED_BY))
        {
            names[idx] = utils.packStringId(new string_id("Owned by"));
            attribs[idx] = getPlayerFullName(getObjIdObjVar(self, OBJVAR_CLAIMED_BY));
            idx++;
        }
        if (hasObjVar(self, "range"))
        {
            names[idx] = utils.packStringId(new string_id("Light range"));
            attribs[idx] = getStringObjVar(self, "range");
            idx++;
        }
        if (hasObjVar(self, "root_color"))
        {
            names[idx] = utils.packStringId(new string_id("Color"));
            attribs[idx] = getStringObjVar(self, "root_color");
            idx++;
        }
        if (hasObjVar(self, "color"))
        {
            names[idx] = utils.packStringId(new string_id("Color shade"));
            attribs[idx] = getStringObjVar(self, "color");
            idx++;
        }
        return SCRIPT_CONTINUE;
    }

    public int setup(obj_id self)
    {
        setName(self, "\\#");
        setDescriptionStringId(self, new string_id("This lamp can be configured to emit a variety of colors at 4 static ranges."));
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        int parent = mi.addRootMenu(menu_info_types.SERVER_MENU13, new string_id("Lamp Options"));
        if (!hasObjVar(self, OBJVAR_CLAIMED_BY))
        {
            mi.addSubMenu(parent, menu_info_types.SERVER_MENU10, new string_id("Claim"));
        }
        else
        {
            if (player == getObjIdObjVar(self, OBJVAR_CLAIMED_BY))
            {
                mi.addSubMenu(parent, menu_info_types.SERVER_MENU11, new string_id("Modify Color"));
                mi.addSubMenu(parent, menu_info_types.SERVER_MENU12, new string_id("Unclaim"));
                mi.addSubMenu(parent, menu_info_types.SERVER_MENU14, new string_id("Hide Name"));
            }
            else
            {
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU10)
        {
            if (!hasObjVar(self, OBJVAR_CLAIMED_BY))
            {
                setObjVar(self, OBJVAR_CLAIMED_BY, player);
                broadcast(player, "You have claimed this light source, only you can modify it now.");
                playClientEffectObj(player, "sound/wep_landmine_on.snd", self, "");
                LOG("ethereal", "[Luminous Lamp]: [" + self + "] " + utils.getStringName(player) + " has claimed " + utils.getStringName(self) + ". This item is now bound to " + utils.getStringName(player) + ".");
            }
        }
        if (item == menu_info_types.SERVER_MENU11)
        {
            if (player == getObjIdObjVar(self, OBJVAR_CLAIMED_BY))
            {
                sui.listbox(self, player, "Select the base color for this light source.", sui.OK_CANCEL, "Wim Magwit's Luminous Lamp", MCOLOR_MAGIC_LIGHT, "handleMainColor", true, false);
            }
        }
        if (item == menu_info_types.SERVER_MENU12)
        {
            if (player == getObjIdObjVar(self, OBJVAR_CLAIMED_BY))
            {
                removeObjVar(self, OBJVAR_CLAIMED_BY);
                broadcast(player, "You have unclaimed this light source.");
                playClientEffectObj(player, "sound/wep_landmine_off.snd", self, "");
                LOG("ethereal", "[Luminous Lamp]: [" + self + "] " + utils.getStringName(player) + " has unclaimed " + utils.getStringName(self) + ".");
            }
        }
        if (item == menu_info_types.SERVER_MENU14)
        {
            if (player == getObjIdObjVar(self, OBJVAR_CLAIMED_BY))
            {
                setName(self, "");
                broadcast(player, "You have hidden the name of this light source.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void handleMainColor(obj_id self, dictionary params) throws InterruptedException
    {
        int idx = sui.getListboxSelectedRow(params);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return;
        }
        String mainColor = MCOLOR_MAGIC_LIGHT[idx];
        setObjVar(self, DATATABLE_MAIN_COLOR_COL, mainColor);
        String subcolorTable = DATATABLE_MAGIC_LIGHT_PREFIX + MCOLOR_MAGIC_LIGHT[idx] + DATATABLE_MAGIC_LIGHT_SUFFIX;
        String[] subcolorList = dataTableGetStringColumn(subcolorTable, "description");
        sui.listbox(self, sui.getPlayerId(params), "Select the sub color for this light source.", sui.OK_CANCEL, "Wim Magwit's Luminous Lamp", subcolorList, "handleSubColor", true, false);
    }

    public void handleSubColor(obj_id self, dictionary params) throws InterruptedException
    {
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx < 0)
        {
            idx = 0;
        }
        String subColor = dataTableGetStringColumn(DATATABLE_MAGIC_LIGHT_PREFIX + getStringObjVar(self, DATATABLE_MAIN_COLOR_COL) + DATATABLE_MAGIC_LIGHT_SUFFIX, DATATABLE_SUB_COLOR_COL)[idx];
        String subColorString = dataTableGetStringColumn(DATATABLE_MAGIC_LIGHT_PREFIX + getStringObjVar(self, DATATABLE_MAIN_COLOR_COL) + DATATABLE_MAGIC_LIGHT_SUFFIX, "description")[idx];
        setObjVar(self, DATATABLE_SUB_COLOR_DETAIL, subColorString);
        setObjVar(self, DATATABLE_SUB_COLOR_COL, subColor);
        sui.listbox(self, sui.getPlayerId(params), "Select the range for this light.", sui.OK_CANCEL, "Wim Magwit's Luminous Lamp", RANGES_MAGIC_LIGHT, "handleColorRange", true, false);
    }

    public void handleColorRange(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return;
        }
        int idx = sui.getListboxSelectedRow(params);
        String rangeSelection = RANGES_MAGIC_LIGHT[idx];
        switchTemplate(self, getStringObjVar(self, DATATABLE_MAIN_COLOR_COL), getStringObjVar(self, DATATABLE_SUB_COLOR_COL), rangeSelection, player);
        setObjVar(self, "range", rangeSelection);
    }

    public void switchTemplate(obj_id self, String color, String subcolor, String rangeSelection, obj_id player) throws InterruptedException
    {
        location loc = getLocation(self);
        float yaw = getYaw(self);
        float[] rotation = getQuaternion(self);
        String template = "object/tangible/tarkin_custom/decorative/lights/" + color + "/" + subcolor + "_" + rangeSelection + ".iff";
        if (!utils.isNestedWithinAPlayer(self, true))//inside a house
        {
            obj_id newLight = createObject(template, loc);
            attachScript(newLight, SCRIPT_MAGIC_LIGHT);
            setYaw(newLight, yaw);
            setQuaternion(newLight, rotation[0], rotation[1], rotation[2], rotation[3]);
            setObjVar(newLight, "claimedBy", getObjIdObjVar(self, OBJVAR_CLAIMED_BY));
            setLocation(newLight, loc);
            persistObject(newLight);
            persistObject(utils.getTopMostContainer(self));
            destroyObject(self);
            LOG("ethereal", "[Luminous Lamp]: [" + self + "] " + template + " created in " + utils.getStringName(utils.getTopMostContainer(self)) + " by " + utils.getStringName(player) + ".");
        }
        else //inside inventory
        {
            obj_id newLight = createObjectInInventoryAllowOverload(template, utils.getInventoryContainer(player));
            attachScript(newLight, SCRIPT_MAGIC_LIGHT);
            playClientEffectObj(newLight, "sound/item_switch_on.snd", newLight, "");
            destroyObject(self);
            LOG("ethereal", "[Luminous Lamp]: [" + self + "] " + template + " created in inventory of " + utils.getStringName(player) + ".");
        }
    }
}
