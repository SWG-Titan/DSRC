package script.item.loot;

import script.dictionary;
import script.library.static_item;
import script.library.utils;
import script.obj_id;

public class hetts_isolation extends script.base_script
{
    public hetts_isolation()
    {
    }

    private static final int HETTS_ISOLATION_LOOT_CHANCE = 1;

    public int aiCorpsePrepared(obj_id self, dictionary params) throws InterruptedException
    {
        if (rand(1, 100) > HETTS_ISOLATION_LOOT_CHANCE)
        {
            return SCRIPT_CONTINUE;
        }
        static_item.createNewItemFunction("item_color_crystal_02_35", utils.getInventoryContainer(self));
        CustomerServiceLog("loot", "Hett's Isolation dropped from " + self + " at " + getLocation(self) + " by " + getName(self) + " (" + self + ")");
        return SCRIPT_CONTINUE;
    }
}
