package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.library.factions;
import script.library.utils;
import script.obj_id;

/**
 * @author BubbaJoe
 */

public class chapter_house extends script.base_script
{
    public static String JEDI = "object/building/player/player_house_jedi_meditation_room.iff";
    public static String SITH = "object/building/player/player_house_sith_meditation_room.iff";

    public chapter_house()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (utils.getTemplateName(self).equals(JEDI)) ;
        {
            setName(self, "Jedi Chapter House");
            factions.setFaction(self, "Rebel");
        }
        if (utils.getTemplateName(self).equals(SITH)) ;
        {
            setName(self, "Sith Chapter House");
            factions.setFaction(self, "Imperial");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToReceiveItem(obj_id self, obj_id destinationCell, obj_id transferrer, obj_id item) throws InterruptedException
    {
        if (!isPlayer(item))
        {
            return SCRIPT_CONTINUE;
        }
        if (!hasSkill(item, "class_forcesensitive_phase1_novice"))
        {
            broadcast(item, "The Force has blocked your entry.");
            return SCRIPT_OVERRIDE;
        }
        else
        {
            broadcast(item, "The Force has guided you in.");
            return SCRIPT_CONTINUE;
        }
    }
}
