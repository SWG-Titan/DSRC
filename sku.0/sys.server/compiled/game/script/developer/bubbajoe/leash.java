package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Leash enemies from ground target loc based upon range, formation, speed.
@Created: Saturday, 3/30/2024, at 9:54 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.ai.ai;
import script.*;
import script.library.sui;
import script.library.utils;

import static script.library.ai_lib.*;

public class leash extends script.base_script
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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Leash"));
        int s_base = mi.addRootMenu(menu_info_types.SERVER_MENU10, new string_id("Setup"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU11, new string_id("Set Min Distance"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU12, new string_id("Set Max Distance"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU13, new string_id("Set Formation"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU14, new string_id("Set Speed"));
        mi.addSubMenu(s_base, menu_info_types.SERVER_MENU15, new string_id("Set Range"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU11)
            {
                sui.inputbox(self, player, "Enter the minimum follow range.", "OnUpdateMIn", "OnUpdateMIn", 350, false, "");
            }
            if (item == menu_info_types.SERVER_MENU12)
            {
                sui.inputbox(self, player, "Enter the maximum follow range.", "OnUpdateMax", "OnUpdateMax", 350, false, "");
            }
            if (item == menu_info_types.SERVER_MENU13)
            {
                sui.inputbox(self, player, "Enter the formation: \n1 = Column\n2 = Box\n3 = Line\n4 = Wedge\n5 = Circle\n6 = Star\n 7 = 'SWG'\n 8 = No Formation (Default Follow with min/max)", "OnUpdateFormation", "OnUpdateFormation", 350, false, "");
            }
            if (item == menu_info_types.SERVER_MENU14)
            {
                sui.inputbox(self, player, "Enter the speed for the mobs.", "OnUpdateSpeed", "OnUpdateSpeed", 350, false, "");
            }
            if (item == menu_info_types.SERVER_MENU15)
            {
                sui.inputbox(self, player, "Enter the range to leash mobs.", "OnUpdateRange", "OnUpdateRange", 350, false, "");
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
        location whereAmIGoing = getLocation(player);
        whereAmIGoing.x = x;
        whereAmIGoing.y = y;
        whereAmIGoing.z = z;
        obj_id[] mobs = getCreaturesInRange(whereAmIGoing, getFloatObjVar(self, "range"));
        int count = 0;
        for (obj_id creature : mobs)
        {
            if (isMob(creature) && !isPlayer(creature) && !isConversational(creature))
            {
                int formation = getIntObjVar(self, "formation");
                switch (formation)
                {
                    case 1:
                        followInColumnFormation(creature, player, count++);
                        break;
                    case 2:
                        followInBoxFormation(creature, player, count++);
                        break;
                    case 3:
                        followInLineFormation(creature, player, count++);
                        break;
                    case 4:
                        followInWedgeFormation(creature, player, count++);
                        break;
                    case 5:
                        followInCircleFormation(creature, player, count++, mobs.length);
                        break;
                    case 6:
                        followInStarFormation(creature, player, count++, mobs.length);
                        break;
                    case 7:
                        followInSWGFormation(creature, player, count++, mobs.length);
                        break;
                    case 8:
                        ai.follow(creature, player, getFloatObjVar(self, "min"), getFloatObjVar(self, "max"));
                        break;
                    default:
                        broadcast(player, "Invalid formation setup.");
                        break;
                }
                setMovementPercent(creature, getFloatObjVar(self, "speed"));
                setMovementWalk(creature);
            }
        }
        broadcast(player, "You have leashed " + mobs.length + " mobiles.");
        return SCRIPT_CONTINUE;
    }

    private boolean isConversational(obj_id creature)
    {
        String[] scripts = getScriptList(creature);
        for (String script : scripts)
        {
            if (script.contains("conversation."))
            {
                return true;
            }
        }
        return false;
    }

    public int OnUpdateMIn(obj_id self, dictionary params) throws InterruptedException
    {
        String min = sui.getInputBoxText(params);
        obj_id player = sui.getPlayerId(params);
        float actual = Float.parseFloat(min);
        setObjVar(self, "min", actual);
        broadcast(player, "Minimum follow range set to " + actual);
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateMax(obj_id self, dictionary params) throws InterruptedException
    {
        String max = sui.getInputBoxText(params);
        obj_id player = sui.getPlayerId(params);
        float actual = Float.parseFloat(max);
        setObjVar(self, "max", actual);
        broadcast(player, "Maximum follow range set to " + actual);
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateFormation(obj_id self, dictionary params) throws InterruptedException
    {
        String formation = sui.getInputBoxText(params);
        obj_id player = sui.getPlayerId(params);
        int actual = Integer.parseInt(formation);
        setObjVar(self, "formation", actual);
        broadcast(player, "Formation set to " + actual);
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, obj_id target, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = utils.packStringId(new string_id("Min Follow Range"));
        attribs[idx] = "" + getFloatObjVar(self, "min");
        idx++;
        names[idx] = utils.packStringId(new string_id("Max Follow Range"));
        attribs[idx] = "" + getFloatObjVar(self, "max");
        idx++;
        names[idx] = utils.packStringId(new string_id("Formation"));
        switch (getIntObjVar(self, "formation"))
        {
            case 1:
                attribs[idx] = "Column";
                break;
            case 2:
                attribs[idx] = "Box";
                break;
            case 3:
                attribs[idx] = "Line";
                break;
            case 4:
                attribs[idx] = "Wedge";
                break;
            case 5:
                attribs[idx] = "Circle";
                break;
            case 6:
                attribs[idx] = "Star";
                break;
            case 7:
                attribs[idx] = "SWG";
                break;
            case 8:
                attribs[idx] = "No Formation/Default Follow";
                break;
            default:
                attribs[idx] = "Invalid Formation";
                break;
        }
        idx++;
        names[idx] = utils.packStringId(new string_id("Speed"));
        attribs[idx] = "" + getFloatObjVar(self, "speed");
        idx++;
        names[idx] = utils.packStringId(new string_id("Range"));
        attribs[idx] = "" + getFloatObjVar(self, "range");
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateSpeed(obj_id self, dictionary params) throws InterruptedException
    {
        String speed = sui.getInputBoxText(params);
        obj_id player = sui.getPlayerId(params);
        float actual = Float.parseFloat(speed);
        setObjVar(self, "speed", actual);
        broadcast(player, "Speed set to " + actual);
        return SCRIPT_CONTINUE;
    }

    public int OnUpdateRange(obj_id self, dictionary params) throws InterruptedException
    {
        String range = sui.getInputBoxText(params);
        obj_id player = sui.getPlayerId(params);
        float actual = Float.parseFloat(range);
        setObjVar(self, "range", actual);
        broadcast(player, "Range set to " + actual);
        return SCRIPT_CONTINUE;
    }

}
