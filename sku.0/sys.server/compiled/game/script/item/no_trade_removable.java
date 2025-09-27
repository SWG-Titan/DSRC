package script.item;

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

/**
 * Allows items that previously had the "noTrade" flag to be made tradeable by
 * their original owner.
 * <p>
 * Automatically attached to objects in no_trade_removable.tab
 */
public class no_trade_removable extends script.base_script
{
    // Using one of the unused reserved entries.
    public static final int RADIAL_MENU_ENTRY = menu_info_types.SERVER_MENU47;
    public static final string_id SID_REMOVE_NO_TRADE = new string_id("Remove No-Trade");
    public static final string_id SID_ITEM_NOT_INVENTORY = new string_id("sarlacc_minigame", "item_not_inventory");
    public static final string_id SID_ITEM_NOT_OWNER = new string_id("base_player", "item_not_owner");
    public static final string_id SID_ITEM_MADE_TRADABLE = new string_id("system_msg", "item_made_tradable");
    public static final String SCRIPT_NAME = "item.special.no_trade_removable";
    public static final String NO_TRADE_SCRIPT_NAME = "item.special.nomove";

    public no_trade_removable()
    {
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if ((getOwner(self) == player || isGod(player)) && hasObjVar(self, "noTrade") && utils.getContainingPlayer(self) == player)
        {
            mi.addRootMenu(RADIAL_MENU_ENTRY, SID_REMOVE_NO_TRADE);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int selectedMenuItem) throws InterruptedException
    {
        if (selectedMenuItem == RADIAL_MENU_ENTRY)
        {
            if (getOwner(self) != player && !isGod(player))
            {
                broadcast(player, "You must be the owner of this item to remove the No-Trade flag.");
                return SCRIPT_CONTINUE;
            }

            if (utils.getContainingPlayer(self) != player)
            {
                broadcast(player, "You must have the item in your inventory to remove the No-Trade flag.");
                return SCRIPT_CONTINUE;
            }

            if (hasObjVar(self, "noTrade"))
            {
                removeObjVar(self, "noTrade");
                if (hasScript(self, NO_TRADE_SCRIPT_NAME))
                {
                    detachScript(self, NO_TRADE_SCRIPT_NAME);
                }
                broadcast(player, "This item is now tradeable.");
                setObjVar(self, "noTradeRemoved", 1);
                CustomerServiceLog("noTrade", getPlayerName(player) + " (" + player + ") removed the noTrade ObjVar from object " + getTemplateName(self) + " (" + self + ")");
                detachScript(self, SCRIPT_NAME);
            }
        }
        return SCRIPT_CONTINUE;
    }
}