package script.item.container;/*
@Origin: dsrc.script.item.container
@Author: BubbaJoeX
@Purpose: Salvation Army
@Created: Friday, 11/10/2023, at 6:06 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.chat;
import script.library.sui;
import script.library.utils;

public class dropbox_person extends base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info item) throws InterruptedException
    {
        if (isGod(player))
        {
            item.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Link Container"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            sui.inputbox(self, player, "Enter NetworkID of container", sui.OK_CANCEL, "LINK DROPBOX", sui.INPUT_NORMAL, null, "handleDropbox", null);
        }
        return SCRIPT_CONTINUE;
    }

    public String NO_TRADE_PHRASE = "I am sorry, but I cannot accept that item as a donation.";
    public String GOT_CONTAINER_PHRASE = "I am sorry, but that item is too bulky to donate.";

    public int OnGiveItem(obj_id self, obj_id item, obj_id player) throws InterruptedException
    {
        if (isNoTradeShared(item))
        {
            chat.chat(self, NO_TRADE_PHRASE);
            putIn(item, utils.getInventoryContainer(player));
            return SCRIPT_OVERRIDE;
        }
        if (hasObjVar(item, "noTrade"))
        {
            chat.chat(self, NO_TRADE_PHRASE);
            putIn(item, utils.getInventoryContainer(player));
            return SCRIPT_CONTINUE;
        }
        if (getGameObjectType(item) == GOT_misc_container || getGameObjectType(item) == GOT_misc_container_wearable || getGameObjectType(item) == GOT_misc_container_public)
        {
            chat.chat(self, GOT_CONTAINER_PHRASE);
            putIn(item, utils.getInventoryContainer(player));
            return SCRIPT_CONTINUE;
        }
        putIn(item, getObjIdObjVar(self, "container"));
        chat.chat(self, "Thank you for your donation!");
        return SCRIPT_CONTINUE;
    }

    public int handleDropbox(obj_id self, dictionary params) throws InterruptedException
    {
        String text = sui.getInputBoxText(params);
        obj_id container = utils.stringToObjId(text);
        setObjVar(self, "container", container);
        return SCRIPT_CONTINUE;
    }
}
