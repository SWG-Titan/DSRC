package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author:  BubbaJoeX
@Purpose: This script handles a "trick" pumpkin, where when clicked it will spawn another pumpkin 4m around it randomly and repeat until the incremental is at 5
@Requirements: GMF Active
@Notes: Standalonef rom the global pumpkin scripts
@Created: Tuesday, 10/7/2025, at 9:42 PM, 
@Copyright © SWG - OR 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.combat;
import script.library.static_item;
import script.library.utils;

public class trick_pumpkin extends script.base_script
{
    public static final String TEMPLATE = "object/tangible/halloween/pumpkins/variant_0";
    public static final String SET_REWARD = "item_event_token_01_01";


    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int sync(obj_id self)
    {
        setName(self, getRandomName());
        setDescriptionString(self, "A mischievous pumpkin that seems ever so inviting to collect...");
        return SCRIPT_CONTINUE;
    }

    public String getRandomName()
    {
        String[] names =
                {
                        "Gourd of Endor",
                        "Sith-O-Lantern",
                        "Spooky Gourd",
                        "Wompkin"
                };
        int index = rand(0, names.length - 1);
        return names[index];
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!canSmashPumpkin(player))
        {
            return SCRIPT_CONTINUE;
        }
        final string_id radial_text = string_id.unlocalized("Collect");
        mi.addRootMenu(menu_info_types.ITEM_USE, radial_text);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            boolean hasBonus = false;
            if (getPosture(player) == POSTURE_CROUCHED)
            {
                hasBonus = true;
            }
            int increment = getIntObjVar(self, "increment");
            if (increment < 5)
            {
                increment++;
                setObjVar(self, "increment", increment);
                int x = rand(-4, 4);
                int z = rand(-4, 4);
                location here = getLocation(self);
                location there = new location(here.x + x, here.y, here.z + z, here.area);
                int variant = rand(1, 4);
                obj_id pumpkin = createObject(TEMPLATE + variant + ".iff", there);
                setObjVar(pumpkin, "increment", increment);
                playClientEffectObj(player, "clienteffect/pumpkin_collect.cef", self, "");
                destroyObject(self);
            }
            else
            {
                playClientEffectObj(player, "clienteffect/pumpkin_collect.cef", self, "");
                destroyObject(self);
                broadcast(player, "The pumpkin shatters into pieces, absorbing into the ground.");

                // Track sets smashed
                int sets = getIntObjVar(player, "pumpkin_sets_smashed");
                sets++;
                setObjVar(player, "pumpkin_sets_smashed", sets);

                // Reward if 10 sets completed
                if (sets == 10)
                {
                    static_item.createNewItemFunction(SET_REWARD, utils.getInventoryContainer(player));
                    if (hasBonus)
                    {
                        broadcast(player, "So you would have gotten an additional reward for smashing while crouched, but Bubba didn't finish it..");
                    }
                    broadcast(player, "Congratulations! You have collected 10 sets and earned a reward!");
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public boolean canSmashPumpkin(obj_id player) throws InterruptedException
    {
        if (combat.isInCombat(player))
        {
            broadcast(player, "You cannot do that while in combat!");
            return false;
        }
        if (getLevel(player) < 10)
        {
            broadcast(player, "You must be at least level 10 to participate in this activity.");
            return false;
        }
        if (isIncapacitated(player))
        {
            broadcast(player, "You cannot do that while incapacitated!");
            return false;
        }
        if (isGod(player))
        {
            broadcast(player, "You cannot do that while in god mode!");
            return false;
        }
        return true;
    }
}