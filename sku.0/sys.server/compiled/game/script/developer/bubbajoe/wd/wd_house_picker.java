package script.developer.bubbajoe.wd;/*
@Origin: dsrc.script.developer.bubbajoe.wd
@Author:  BubbaJoeX
@Purpose: Lets players pick one small house between tatooine, naboo or corellia style 1
@Requirements: <no requirements>
@Notes: 10 x 1 = 10 so i mean if they want a city so be it, its 2024, LET THEM PLAY
@Created: Sunday, 4/21/2024, at 11:34 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;
import script.library.utils;

public class wd_house_picker extends script.base_script
{
    public final String[] houseOptions = {
            "Small Corellia House",
            "Small Generic House",
            "Small Naboo House",
            "Small Tatooine House",
    };

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
        setName(self, "Small Housing Voucher");
        setDescriptionString(self, "This voucher can be redeemed for a small house.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (canManipulate(player, self, true, true, 15, true))
            {
                sui.listbox(self, player, "Please select which small house you would like to receive: ", sui.OK_CANCEL, "Small Housing Voucher", houseOptions, "handleHouseSelection", true);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true))
        {
            int mainMenu = mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Claim House"));
        }
        return SCRIPT_CONTINUE;
    }

    public int handleHouseSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        switch (idx)
        {
            case 0:
                obj_id corHouse = makeCraftedItem("object/draft_schematic/structure/corellia_house_player_small.iff", 94.25f, utils.getInventoryContainer(player));
                broadcast(player, "You have been given a small Corellian house.");
                setOwner(corHouse, player);
                setCrafter(corHouse, player);
                break;
            case 1:
                obj_id genHouse = makeCraftedItem("object/draft_schematic/structure/generic_house_player_small.iff", 94.25f, utils.getInventoryContainer(player));
                broadcast(player, "You have been given a small generic house.");
                setOwner(genHouse, player);
                setCrafter(genHouse, player);
                break;
            case 2:
                obj_id nabHouse = makeCraftedItem("object/draft_schematic/structure/naboo_house_player_small.iff", 94.25f, utils.getInventoryContainer(player));
                broadcast(player, "You have been given a small Naboo house.");
                setOwner(nabHouse, player);
                setCrafter(nabHouse, player);
                break;
            case 3:
                obj_id tatHouse = makeCraftedItem("object/draft_schematic/structure/house_player_small.iff", 94.25f, utils.getInventoryContainer(player));
                broadcast(player, "You have been given a small Tatooine house.");
                setOwner(tatHouse, player);
                setCrafter(tatHouse, player);
                break;
        }
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }
}
