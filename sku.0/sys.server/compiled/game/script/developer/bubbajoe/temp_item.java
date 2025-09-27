package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Delete the item after 60 minutes have passed from OnAttach
@Note: This might seem like reinventing the wheel, but it's a good example of how to use messageTo to delete an object after a certain amount of time.
@Created: Saturday, 3/30/2024, at 9:45 AM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.dictionary;
import script.library.utils;
import script.obj_id;
import script.string_id;

public class temp_item extends script.base_script
{
    private static final int EXPIRATION_TIME_MINUTES = 60; // Time to delete the item in minutes

    public int OnAttach(obj_id self)
    {
        // Delete self after 60 minutes using messageTo
        int timeNow = getCalendarTime();
        setObjVar(self, "timeNow", timeNow);
        dictionary params = new dictionary();
        params.put("timeNow", timeNow);

        // Convert minutes to seconds for messageTo
        float delayInSeconds = EXPIRATION_TIME_MINUTES * 60f;

        messageTo(self, "trash", params, delayInSeconds, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }

        // Calculate the remaining time until deletion in minutes
        int elapsedTime = (getCalendarTime() - getIntObjVar(self, "timeNow"));
        int remainingTime = EXPIRATION_TIME_MINUTES - (elapsedTime / 60); // Convert elapsed time to minutes

        names[idx] = utils.packStringId(new string_id("Deletion"));
        attribs[idx] = "This item is flagged to be deleted in " + remainingTime + " minute" + (remainingTime != 1 ? "s" : "") + ".";
        return SCRIPT_CONTINUE;
    }

    public int trash(obj_id self, dictionary params)
    {
        int timeNow = params.getInt("timeNow");
        destroyObject(self);
        LOG("ethereal", "[Temporary Item] " + getName(self) + " has been deleted as it has expired. Originally generated at " + getCalendarTimeStringLocal_YYYYMMDDHHMMSS(timeNow));
        return SCRIPT_CONTINUE;
    }
}
