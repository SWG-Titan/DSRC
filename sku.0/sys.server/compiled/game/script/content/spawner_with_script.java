package script.content;

import script.*;

public class spawner_with_script extends base_script
{
    public final static String TEMPLATE_VAR = "content.spawner_template";
    public final static String SCRIPT_VAR = "content.spawner_script";
    public final static String RECYCLE_VAR = "content.spawner.should_recycle";
    public final static String RECYCLE_TIME_VAR = "content.spawner.recycle_time";
    public final static String SPAWNED_VAR = "content.spawner_spawned";

    public int OnAttach(obj_id self)
    {
        doSpawn(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        doSpawn(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            int spawning = mi.addRootMenu(menu_info_types.ITEM_USE, string_id.unlocalized("Adhoc Spawner"));//placeholder menu for organization
            mi.addSubMenu(spawning, menu_info_types.SERVER_MENU1, string_id.unlocalized("Refresh Spawner"));
            mi.addSubMenu(spawning, menu_info_types.SERVER_MENU2, string_id.unlocalized("Set Script"));
            mi.addSubMenu(spawning, menu_info_types.SERVER_MENU3, string_id.unlocalized("Set Template"));

        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player) && item == menu_info_types.ITEM_USE)
        {
            broadcast(player, "Use the sub-menus.");
        }

        if (isGod(player) && item == menu_info_types.SERVER_MENU1)
        {
            doSpawn(self);
        }

        if (isGod(player) && item == menu_info_types.SERVER_MENU2)
        {
            //setSpawnerScript(self);
        }

        if (isGod(player) && item == menu_info_types.SERVER_MENU3)
        {
            //setSpawnerTemplate(self);
        }
        return SCRIPT_CONTINUE;
    }

    public void doSpawn(obj_id self)
    {
        String template = getStringObjVar(self, TEMPLATE_VAR);
        String script = getStringObjVar(self, SCRIPT_VAR);
        boolean recycle = hasObjVar(self, RECYCLE_VAR) && getBooleanObjVar(self, RECYCLE_VAR);
        int recycleTime = hasObjVar(self, RECYCLE_TIME_VAR) ? getIntObjVar(self, RECYCLE_TIME_VAR) : 0;

        if (template == null || template.isEmpty() || script == null || script.isEmpty())
        {
            return;
        }

        if (recycle && recycleTime <= 0)
        {
            return;
        }

        obj_id spawned = createObject(template, getLocation(self));
        if (!isIdValid(spawned))
        {
            return;
        }

        setObjVar(self, SPAWNED_VAR, spawned);
        detachAllScripts(spawned);
        attachScript(spawned, script);

        if (recycle)
        {
            messageTo(self, "handleRecycle", null, recycleTime, false);
            LOG("spawner", "Recycling spawner in " + recycleTime + " seconds.");
        }
    }

    public int handleRecycle(obj_id self, dictionary params)
    {
        if (hasObjVar(self, SPAWNED_VAR))
        {
            obj_id spawned = getObjIdObjVar(self, SPAWNED_VAR);
            if (isIdValid(spawned))
            {
                destroyObject(spawned);
            }
            removeObjVar(self, SPAWNED_VAR);
        }
        return SCRIPT_CONTINUE;
    }
}
