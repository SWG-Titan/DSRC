package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author:  BubbaJoeX
@Purpose: Player side script for lifeday.
@Requirements: Life Day
@Notes: 2024 lifeday
@Created: Monday, 11/25/2024, at 7:22 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.obj_id;
import script.location;

public class player_lifeday extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnSpeaking(obj_id self, String text) throws InterruptedException
    {
        if (text.contains("Happy  Life") || text.contains("happy life"))
        {
            doVisuals(self, SPEAKING_VISUALS[(rand(0, SPEAKING_VISUALS.length - 1))]);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (text.contains("Happy  Life") || text.contains("happy life") && (speaker != self))
        {
            doVisuals(self, HEARING_VISUALS[(rand(0, HEARING_VISUALS.length - 1))]);
        }
        return SCRIPT_CONTINUE;
    }

    private void makeNewYearsGjft(obj_id self)
    {
        String GIFT_TEMPLATE = "object/tangible/item/target_dummy_publish_giftbox.iff";
        obj_id gift = createObject(GIFT_TEMPLATE, utils.getInventoryContainer(self), "");
        attachScript(gift, "event.lifeday.new_years_gift");
        setObjVar(self, "ny_25", true);
        broadcast(self, "You have received a present! Happy New Years from the SWG-OR Staff!");
        LOG("ethereal", "[New Years]: " + getPlayerFullName(self) + " has received their anniversary gift.");
    }

    public static String[] HEARING_VISUALS = {
            "appearance/pt_steam.prt",
            "clienteffect/bacta_bomb.cef"
    };

    public static String[] SPEAKING_VISUALS = {
            "appearance/pt_steam.prt",
            "clienteffect/bacta_bomb.cef"
    };

    public void doVisuals(obj_id self, String visualFile) throws InterruptedException
    {
        location loc = getLocation(self);
        playClientEffectLoc(getAllPlayers(loc, 15f), visualFile, loc, 1.0f);
    }
}
