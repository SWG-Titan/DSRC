package script.item.content.token;

import script.library.static_item;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class gold_to_silver extends script.base_script
{

    public static final String SILVER_TOKEN = "item_pgc_token_02";

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true))
        {
            if (getContainedBy(self) != utils.getInventoryContainer(player))
            {
                return SCRIPT_CONTINUE;
            }
            mi.addRootMenu(menu_info_types.SERVER_MENU20, new string_id("Convert to Silver"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU20)
        {
            if (canManipulate(player, self, true, true, 5, true))
            {
                if (getContainedBy(self) != utils.getInventoryContainer(player))
                {
                    broadcast(player, "You do not own this!");
                    return SCRIPT_CONTINUE;
                }
                if (getCount(self) == 1)
                {
                    static_item.createNewItemFunction(SILVER_TOKEN, utils.getInventoryContainer(player), 1000);
                    decrementCount(self);
                }
                else //@NOTE: THIS IS SPLIT FOR A REASON  :D
                {
                    grantSilverTokens(self, player);
                }
                broadcast(player, "Successfully converted 1 gold token to 1000 silver tokens.");
            }
            else
            {
                broadcast(player, "Failed to decrement gold token.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    private void grantSilverTokens(obj_id self, obj_id player) throws InterruptedException
    {
        static_item.createNewItemFunction(SILVER_TOKEN, utils.getInventoryContainer(player), 1000);
        decrementCount(self);
    }
}
