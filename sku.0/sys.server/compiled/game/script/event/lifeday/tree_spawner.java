package script.event.lifeday;/*
@Origin: dsrc.script.event.lifeday
@Author: BubbaJoeX
@Purpose: Spawns a lifeday tree
@Created: Saturday, 11/18/2023, at 8:10 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.dictionary;

public class tree_spawner extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        spawnTrees(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        spawnTrees(self);
        return SCRIPT_CONTINUE;
    }

    public int spawnTrees(obj_id self)
    {
        String[] treeList = dataTableGetStringColumn("datatables/adhoc/tree_spawner.iff", "template");
        int treeCount = dataTableGetNumRows("datatables/adhoc/tree_spawner.iff");
        int i = 0;
        while (i < treeCount)
        {
            location loc = getLocation(self);
            loc.x += (float) dataTableGetInt("datatables/adhoc/tree_spawner.iff", i, "x");
            loc.z += (float) dataTableGetInt("datatables/adhoc/tree_spawner.iff", i, "z");
            loc.y = getHeightAtLocation(loc.x, loc.z);
            obj_id tree = createObject(treeList[i], loc);
            attachScript(tree, "event.lifeday.tree_chopping");
            i++;
        }
        return SCRIPT_CONTINUE;
    }
}
