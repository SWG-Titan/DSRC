package script.event.pharple_day;

import script.color;
import script.dictionary;
import script.library.chat;
import script.obj_id;
import script.string_id;

public class pharple_speed_player extends script.base_script
{
    private static final String PHARPLE_APPEARANCE = "object/mobile/shared_pharple.iff";

    public pharple_speed_player()
    {
    }

    public int applyPharpleSpeedEffect(obj_id self, dictionary params) throws InterruptedException
    {
        float speedMultiplier = params.getFloat("speedMultiplier");
        float duration = params.getFloat("duration");
        makePlayerPharple(self);
        applySpeedEffect(self, speedMultiplier);
        messageTo(self, "resetPharpleSpeedEffect", params, duration, true);

        return SCRIPT_CONTINUE;
    }

    private void makePlayerPharple(obj_id player) throws InterruptedException
    {
        setObjectAppearance(player, PHARPLE_APPEARANCE);
        setMovementRun(player);
        chat.chat(player, "BAWK-BAWK!");
        showFlyText(player, new string_id("***"), 1.0f, color.GOLDENROD);
    }

    private void applySpeedEffect(obj_id player, float multiplier)
    {
        float baseSpeed = getBaseRunSpeed(player);
        setMovementPercent(player, (baseSpeed * multiplier));
        setBaseRunSpeed(player, baseSpeed * multiplier);
    }

    public int resetPharpleSpeedEffect(obj_id self, dictionary params) throws InterruptedException
    {
        resetPlayerAppearance(self);
        resetSpeed(self);
        return SCRIPT_CONTINUE;
    }

    private void resetPlayerAppearance(obj_id player)
    {
        revertObjectAppearance(player);
    }

    private void resetSpeed(obj_id player)
    {
        float baseSpeed = getBaseRunSpeed(player);
        setMovementPercent(player, baseSpeed);
        setBaseRunSpeed(player, baseSpeed);
    }
}
