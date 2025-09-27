package script.systems.city;/*
@Origin: script.systems.city.city_hire
@Author: BubbaJoeX
@Purpose: Dragging this item onto a mobile will allow you to hire them.
@TODO: Add a check to see if mobile is the big 10. (original trilogy leads)
@Note: The mobile must be an AI and interactable, and non-conversational, or major celebrity.
@Requirements: <no requirements>
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.city;
import script.library.colors_hex;
import script.library.create;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class city_hire extends script.base_script
{
    public static String CITY_ACTOR_TOOL = "object/tangible/loot/tool/city_actor_bio_extractor.iff";
    public static String BASEMSG = "INFORMATION: \n" + "This extraction unit can be used to extract the bio-logical matter of a creature for decoration purposes inside the city you belong to.\n\n" + "USAGE:\n" + "Drag the extractor onto an interactable NPC (AI Only) to copy it's bio-logical data.\n" + "\n\nREQUIREMENTS:\n\n" + " You must be Mayor or Militia and have Politican Novice to use this item. You may trade these items to your Mayor or Militia member if you cannot place them yourself.";
    public static string_id DESC = new string_id(BASEMSG);

    public int OnAttach(obj_id self)
    {
        if (!hasObjVar(self, "tokenUsed"))
        {
            setName(self, "Bio-logical Extraction Unit");
        }
        setDescriptionStringId(self, DESC);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        if (!hasObjVar(self, "tokenUsed"))
        {
            setName(self, "Bio-logical Extraction Unit");
        }
        setDescriptionStringId(self, DESC);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulateToken(self, player))
        {
            if (hasObjVar(self, "tokenUsed"))
            {

                mi.addRootMenu(menu_info_types.SERVER_MENU50, new string_id("Place Actor"));
            }
            else
            {
                broadcast(player, "You must drag the Bio-logical Extraction Unit onto the creature you wish to capture.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isInWorldCell(player))
        {
            if (item == menu_info_types.SERVER_MENU50)
            {
                if (hasObjVar(self, "tokenUsed"))
                {
                    if (hasObjVar(self, "city_hire.mobile"))
                    {
                        String template = getStringObjVar(self, "city_hire.mobile");
                        obj_id actor = create.createObject(template, getLocation(player));
                        attachScript(actor, "systems.city.city_actor");
                        attachScript(actor, "systems.city.city_furniture");
                        int city_id = getCityAtLocation(getLocation(player), 0);
                        setObjVar(actor, "city_id", city_id);
                        city.addDecoration(city_id, player, self);
                        persistObject(actor);
                        broadcast(player, "You have placed " + utils.getStringName(actor) + " in this city.");
                        LOG("ethereal", "[City Actor]: " + utils.getStringName(actor) + " has been placed in city " + city_id + " by " + utils.getStringName(player) + ".");
                        destroyObject(self);
                    }
                }
                else
                {
                    broadcast(player, "You must drag the Bio-logical Extraction Unit onto the creature you wish to capture.");
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean canManipulateToken(obj_id self, obj_id player) throws InterruptedException
    {
        //@note: keep these in order of importance, with the most important last
        int city_id = getCityAtLocation(getLocation(player), 0);
        boolean isMayor = city.isTheCityMayor(player, city_id);
        if (hasObjVar(player, "city_decorator"))
        {
            return true;
        }
        if (city.isMilitiaOfCity(player, city_id))
        {
            return true;
        }
        if (isMayor)
        {
            return true;
        }
        return isGod(player);
    }
}

