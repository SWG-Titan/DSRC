package script.event.lifeday;

import script.library.badge;
import script.library.static_item;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class lifeday_tree extends script.base_script
{
    public lifeday_tree()
    {
    }
    private static final string_id TREE_USE = new string_id("spam", "tree_use");
    private static final string_id NOT_OLD_ENOUGH = new string_id("spam", "not_old_enough");
    private static final string_id GIFT_GRANTED = new string_id("spam", "gift_granted");
    private static final String GIFT_SELF = "item_lifeday_gift_self_01_0";
    private static final String GIFT_OTHER = "item_lifeday_gift_other_01_0";
    private static final String LIFEDAY_BADGE = "lifeday_badge_11";
    private static final String LIFEDAY_BADGE_08 = "lifeday_badge_08";
    private static final String LIFEDAY_BADGE_09 = "lifeday_badge_09";
    private static final String LIFEDAY_BADGE_10 = "lifeday_badge_10";
    private static final String LIFEDAY_BADGE_11 = "lifeday_badge_11";
    private static final string_id TREE_BADGE = new string_id("spam", "tree_badge");

    private String currentYearObjVar() throws InterruptedException
    {
        return utils.XMAS_RECEIVED_IX_01;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        String config = getConfigSetting("GameServer", "grantGift");//@TODO: make sure this is set in the conf
        if (config != null)
        {
            if (config.equals("false"))
            {
                return SCRIPT_CONTINUE;
            }
        }
        if (!hasObjVar(player, currentYearObjVar()))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, TREE_USE);
        }
        if (!hasCompletedCollectionSlot(player, LIFEDAY_BADGE_08))//check for lifeday_08, no one should have this one so it will grant all previous.
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU2, TREE_BADGE);
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!isGod(player))
            {
                if ((getCurrentBirthDate() - getPlayerBirthDate(player)) < 5)
                {
                    sendSystemMessage(player, NOT_OLD_ENOUGH);
                    return SCRIPT_CONTINUE;
                }
            }
            if (!hasObjVar(player, currentYearObjVar()))
            {
                grantGift(player);
                LOG("events", "Life Day: Player " + player + " has used ITEM_USE on the Lifeday tree.");
            }
        }
        if (item == menu_info_types.SERVER_MENU2)
        {
            badge.grantBadge(player, LIFEDAY_BADGE_08);
            badge.grantBadge(player, LIFEDAY_BADGE_09);
            badge.grantBadge(player, LIFEDAY_BADGE_10);
            badge.grantBadge(player, LIFEDAY_BADGE_11);
            LOG("events", "Life Day: Player " + player + " has been granted all Lifeday badges.");
        }
        return SCRIPT_CONTINUE;
    }
    private boolean grantGift(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        obj_id inv = utils.getInventoryContainer(player);
        if (!isIdValid(inv))
        {
            return false;
        }

        // we need 12 inv slots for the gifts
        if (getVolumeFree(inv) < 12)
        {
            broadcast(player, "You do not have enough inventory space to receive your gifts.");
            return false;
        }
        for (int year = 1; year <= 6; year++)
        {
            LOG("events", "[Life Day]: Granting gift for year " + year + " to player " + player);
            static_item.createNewItemFunction(GIFT_SELF + year, inv);
            static_item.createNewItemFunction(GIFT_OTHER + year, inv);
        }

        return true;
    }
}
