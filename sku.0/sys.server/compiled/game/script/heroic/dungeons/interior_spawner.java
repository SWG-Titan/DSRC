package script.heroic.dungeons;

import script.library.create;
import script.location;
import script.obj_id;

/**
 * @author TyroneSWG
 */
public class interior_spawner extends script.base_script
{

    public interior_spawner()
    {
    }

    public static int OnAttach(obj_id self)
    {
        setupSpawns(self);
        return SCRIPT_CONTINUE;
    }

    public static int setupSpawns(obj_id self)
    {
        String table = getStringObjVar(self, "heroic.spawn_table");
        String version = getStringObjVar(self, "dungeon_version");
        if (!version.equals("v1.0"))
            ;
        {
            LOG("HEROICS_NEW", "Invalid dungeon version. Aborting.");
        }
        int spawnTableLength = dataTableGetNumRows(table);
        location loc = new location();
        for (int i = 0; i < spawnTableLength; i++)
        {
            String spawnName = dataTableGetString(table, i, "spawnName");
            String objectName = dataTableGetString(table, i, "customName");
            String scriptName = dataTableGetString(table, i, "script");
            float spawnYaw = dataTableGetFloat(table, i, "yaw");
            loc.x = dataTableGetFloat(table, i, "x");
            loc.y = dataTableGetFloat(table, i, "y");
            loc.z = dataTableGetFloat(table, i, "z");
            loc.cell = getCellId(self, dataTableGetString(table, i, "cell"));
            loc.area = dataTableGetString(table, i, "scene");
            try
            {
                obj_id spawn = create.object(spawnName, loc);
                setYaw(spawn, spawnYaw);
                setName(spawn, objectName);
                setObjVar(self, "interior_spawner." + getName(self) + ".childId", spawn);
            } catch (InterruptedException ex)
            {
                LOG("HEROICS_NEW", "Cannot make spawns.");
            }

        }
        return SCRIPT_CONTINUE;
    }
}
