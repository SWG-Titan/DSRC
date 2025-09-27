package script.hub.building;

import script.obj_id;

public class hub_main extends script.base_script
{

    public static String HUB_SPAWN_TABLE = "datatable/hub/spawns.iff";
    public static String HUB_WLC_MSG = "";
    public static int TERMINATE = 1;
    public static int MAX_PLAYERS = 300;
    public static int MIN_PLAYERS_PER_BUILDOUT = 10;
    public static String HUB_SCENE = "dungeon2";

    public hub_main()
    {

    }

    public static int OnAttach(obj_id self) throws InterruptedException
    {
        LOG("ethereal", "[Hub]: Hub script attached");
        return SCRIPT_CONTINUE;
    }

    public static int OnInitialize(obj_id self) throws InterruptedException
    {
        LOG("ethereal", "[Hub]: Hub script initialized");
        return SCRIPT_CONTINUE;
    }
}
