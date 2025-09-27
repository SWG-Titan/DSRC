package script.event.pharple_day;

import script.*;

public class pharple_speed extends base_script
{
    public pharple_speed()
    {
    }

    public int OnAttach(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setup(self);
        return SCRIPT_CONTINUE;
    }

    public void setup(obj_id self)
    {
        setName(self, "Pharple Feed");
        setDescriptionString(self, "This pharple feed will give you the speed you need!");
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (canUse(player))
            {
                sendSpeedEffectMessageToPlayer(player);
            }
            else
            {
                broadcast(player, "You cannot reach for more of this delicious feed for at least 5 minutes.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Munch"));
        return SCRIPT_CONTINUE;
    }

    private void sendSpeedEffectMessageToPlayer(obj_id player)
    {
        if (!hasScript(player, "event.pharple_day.pharple_speed_player"))
        {
            attachScript(player, "event.pharple_day.pharple_speed_player");
        }
        dictionary params = new dictionary();
        params.put("speedMultiplier", 5.0f);
        params.put("duration", 30f);
        messageTo(player, "applyPharpleSpeedEffect", params, 0, true);
    }

    public boolean canUse(obj_id player)
    {
        int lastUsed = getIntObjVar(player, "pd.lastUsed");
        int currentTime = getCalendarTime();

        if (currentTime - lastUsed >= 300)
        {
            setObjVar(player, "pd.lastUsed", currentTime);
            return true;
        }
        return false;
    }
}
