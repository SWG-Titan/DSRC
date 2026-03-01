package script.library;

import script.*;
import script.library.*;

public class lightswitch extends script.base_script
{
    public static final String TOOL_TEMPLATE = "object/tangible/loot/npc_loot/datapad_generic.iff";
    public static final String TOOL_SCRIPT = "item.special.light_controller";
    public static final int TOOL_LIFESPAN = 120;
    public static final String TOOL_NAME = "Remote Light Controller";

    public static void grantLightController(obj_id terminal, obj_id player, obj_id structure) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(structure))
            return;

        if (hasLightController(player))
        {
            sendSystemMessage(player, "You already have a Remote Light Controller.", null);
            return;
        }

        obj_id tool = createObjectInInventoryAllowOverload(TOOL_TEMPLATE, player);
        if (!isIdValid(tool))
        {
            sendSystemMessage(player, "Failed to create the light controller. Your inventory may be full.", null);
            return;
        }

        setName(tool, TOOL_NAME);
        setObjVar(tool, "lightswitch.structure", structure);
        setObjVar(tool, "item.lifespan", TOOL_LIFESPAN);
        attachScript(tool, TOOL_SCRIPT);

        sendSystemMessage(player, "You receive a Remote Light Controller. Walk into any room and use it to change the lights. It will expire in 2 minutes.", null);
    }

    public static boolean hasLightController(obj_id player) throws InterruptedException
    {
        obj_id inventory = utils.getInventoryContainer(player);
        if (!isIdValid(inventory))
            return false;

        obj_id[] contents = getContents(inventory);
        if (contents == null)
            return false;

        for (int i = 0; i < contents.length; i++)
        {
            if (isIdValid(contents[i]) && hasScript(contents[i], TOOL_SCRIPT))
                return true;
        }
        return false;
    }
}
