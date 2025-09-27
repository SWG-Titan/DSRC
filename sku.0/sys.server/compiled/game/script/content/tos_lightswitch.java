package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 5/11/2024, at 2:52 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class tos_lightswitch extends base_script
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
            attribs[idx] = toUpper(getStringObjVar(self, "root_color"), 0);
            idx++;
        }
        if (hasObjVar(self, "color"))
        {
            names[idx] = utils.packStringId(new string_id("Color shade"));
            attribs[idx] = getStringObjVar(self, "description");
        }
        return SCRIPT_CONTINUE;
    }

    public int setup(obj_id self)
    {
        setName(self, "Lightswitch");
        setDescriptionStringId(self, new string_id("This lightswitch can be used to change the color of the room lighting, if the room contains any magic lights."));
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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Change Room Lighting"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            sui.listbox(self, player, "Select the base color for this lightsource.", sui.OK_CANCEL, "Room Lighting", MCOLOR_MAGIC_LIGHT, "handleMainColor", true, false);
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
        sui.listbox(self, sui.getPlayerId(params), "Select the sub color for this lightsource.", sui.OK_CANCEL, "Wim Magwit's Luminous Lamp", subcolorList, "handleSubColor", true, false);
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
        String template = "object/tangible/tarkin_custom/decorative/lights/" + color + "/" + subcolor + "_" + rangeSelection + ".iff";
        obj_id cell = getContainedBy(self);
        if (cell == null)
        {
            broadcast(self, "You must be inside a room to use this command.");
            return;
        }
        obj_id[] lights = getContents(cell);
        for (obj_id light : lights)
        {
            if (hasScript(light, "item.content.rewards.magic_light"))
            {
                location loc = getLocation(light);
                float[] rotation = getQuaternion(light);
                obj_id new_light = createObject(template, loc);
                if (isIdValid(new_light))
                {
                    if (!hasScript(new_light, "item.content.rewards.magic_light"))
                    {
                        attachScript(new_light, "item.content.rewards.magic_light");
                    }
                    setQuaternion(new_light, rotation[0], rotation[1], rotation[2], rotation[3]);
                    setObjVar(new_light, "claimedBy", getPlayerIdFromFirstName("bubbajoe"));
                    setLocation(new_light, loc);
                    destroyObject(light);
                    persistObject(new_light);
                    setName(new_light, " ");
                }
                else
                {
                    broadcast(self, "Failed to create new light, template is not valid");
                }
            }
        }
    }
}
