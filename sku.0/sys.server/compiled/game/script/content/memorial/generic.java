package script.content.memorial;/*
@Origin: dsrc.script.content.nb_quest
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Thursday, 5/16/2024, at 9:00 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.buff;
import script.library.utils;

public class generic extends base_script
{

    public static final float MEMORIAL_BUFF_DURATION = 14400.0f; // 4 hours
    public static String MEMORIAL_BUFF = "event_buff_gm"; //@TODO: change this to the correct buff name
    public static int MEMORIAL_BUFF_AMOUNT = 75; // 25% buff

    public int OnAttach(obj_id self)
    {
        setupMemorial(self);
        return SCRIPT_CONTINUE;
    }

    public void setupMemorial(obj_id self)
    {
        String name = getStringObjVar(self, "celebrity");
        if (name == null || name.isEmpty())
        {
            setName(self, "Memorial");
            setDescriptionString(self, "This is a memorial to a fallen hero. Radial it to pay your respects.");
        }
        setName(self, "Memorial: " + getStringObjVar(self, "celebrity"));
        setDescriptionString(self, "This is a memorial to " + getStringObjVar(self, "celebrity") + ". Radial it to pay your respects.");
    }

    public int OnInitialize(obj_id self)
    {
        setupMemorial(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Pay Respects"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (getPosture(player) != POSTURE_CROUCHED)
            {
                broadcast(player, "To pay your respects, you must do so respectfully. Please kneel before the memorial.");
                return SCRIPT_CONTINUE;
            }
            buff.applyBuff(player, MEMORIAL_BUFF, MEMORIAL_BUFF_DURATION, MEMORIAL_BUFF_AMOUNT);
            broadcast(player, "You have paid your respects to the memorial. You have been granted a temporary buff.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = "Remembering";
        attribs[idx] = getStringObjVar(self, "celebrity");
        idx++;
        names[idx] = "Years";
        attribs[idx] = getStringObjVar(self, "years");
        return SCRIPT_CONTINUE;
    }
}
