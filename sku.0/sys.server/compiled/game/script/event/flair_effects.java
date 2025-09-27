package script.event;

import script.*;
import script.library.sui;
import script.library.utils;

import java.util.ArrayList;

public class flair_effects extends base_script
{
    public static final String EFFECT_TABLE = "datatables/adhoc/flair_effects.iff";
    public static final String TEMPLATE = "object/tangible/collection/datapad_10.iff";

    public flair_effects()
    {
    }

    public void setNameAndDescription(obj_id self)
    {
        setName(self, "Wim Magwitt's Flair Effect Controller");
        setDescriptionString(self, "This object allows you to select a flair effect to play near you.");
    }

    public void handleFlairEffect(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        int idx = sui.getListboxSelectedRow(params);
        String[] effects = dataTableGetStringColumnNoDefaults(EFFECT_TABLE, "effect_name");

        if (btn == sui.BP_CANCEL || idx < 0 || idx >= effects.length)
        {
            return;
        }

        String filename = dataTableGetString(EFFECT_TABLE, idx, "effect_file");
        String playerFacingName = dataTableGetString(EFFECT_TABLE, idx, "effect_name");
        String hardpointForEffect = dataTableGetString(EFFECT_TABLE, idx, "hardpoint");
        int cooldown = dataTableGetInt(EFFECT_TABLE, idx, "cooldown");

        // Set the effect and cooldown
        setObjVar(self, "flair_effect_cooldown", cooldown);
        setObjVar(self, "flair_effect_file", filename);
        setObjVar(self, "flair_effect_last_played", 0);
        setObjVar(self, "flair_effect_name", playerFacingName);
        setObjVar(self, "flair_effect_hardpoint", hardpointForEffect);

        LOG("ethereal", "[Flair Effects]: " + player + " has loaded " + filename + " to the data buffer with a cooldown of " + cooldown + " seconds.");
    }

    public int setupEffect(obj_id self, obj_id player) throws InterruptedException
    {
        String[] effects = dataTableGetStringColumnNoDefaults(EFFECT_TABLE, "effect_name");

        String prompt = "Select a flair effect to load to the data buffer.";
        sui.listbox(self, player, prompt, sui.OK_CANCEL, "Select Effect", effects, "handleFlairEffect", true);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        int main = mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Play Flair Effect"));
        mi.addSubMenu(main, menu_info_types.SERVER_MENU1, new string_id("Select Flair Effect"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            playEffect(self, player, getCurrentEffect(self));
        }
        else if (item == menu_info_types.SERVER_MENU1)
        {
            setupEffect(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int playEffect(obj_id self, obj_id player, String currentEffect)
    {
        if (hasObjVar(self, "flair_effect_file"))
        {
            String effectCef = getStringObjVar(self, "flair_effect_file");
            if (effectCef == null || effectCef.isEmpty())
            {
                effectCef = "clienteffect/bacta_bomb.cef";
            }
            int effectCooldown = getIntObjVar(self, "flair_effect_cooldown");
            if (effectCooldown > 0)
            {
                int lastPlayed = getIntObjVar(self, "flair_effect_last_played");
                if (lastPlayed > 0)
                {
                    LOG("ethereal", "[Flair Effects]: " + player + " has requested to play " + effectCef + " with a cooldown of " + effectCooldown + " seconds.");
                    int timeSince = getGameTime() - lastPlayed;
                    if (timeSince < effectCooldown)
                    {
                        int timeLeft = effectCooldown - timeSince;
                        broadcast(player, "You must wait " + timeLeft + " seconds before you can play your currently loaded flair effect.");
                        LOG("ethereal", "[Flair Effects]: " + player + " has been denied playing " + effectCef + " due to a cooldown of " + effectCooldown + " seconds.");
                        return SCRIPT_CONTINUE;
                    }
                    else
                    {
                        playClientEffectObj(player, effectCef, player, getStringObjVar(self, "flair_effect_hardpoint"));
                        LOG("ethereal", "[Flair Effects]: " + player + " has played " + effectCef + " with a cooldown of " + effectCooldown + " seconds near " + getLocation(player).toReadableFormat(true) + ".");
                        setObjVar(self, "flair_effect_last_played", getGameTime());
                        return SCRIPT_CONTINUE;
                    }
                }
                else
                {
                    playClientEffectObj(player, effectCef, player, getStringObjVar(self, "flair_effect_hardpoint"));
                    LOG("ethereal", "[Flair Effects]: " + player + " has played " + effectCef + " with a cooldown of " + effectCooldown + " seconds near " + getLocation(player).toReadableFormat(true) + ".");
                    setObjVar(self, "flair_effect_last_played", getGameTime());
                    return SCRIPT_CONTINUE;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }

    public String getCurrentEffect(obj_id self)
    {
        String effect = getStringObjVar(self, "flair_effect_file");
        if (effect == null || effect.isEmpty())
        {
            return "clienteffect/bacta_bomb.cef";
        }
        LOG("ethereal", "[Flair Effects]: " + self + "'s current effect is " + effect + ".");
        return effect;
    }

    public int OnAttach(obj_id self)
    {
        setNameAndDescription(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setNameAndDescription(self);
        return SCRIPT_CONTINUE;
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, "flair_effect_name"))
        {
            names[idx] = utils.packStringId(new string_id("Effect"));
            attribs[idx] = "\\#DAA520" + getStringObjVar(self, "flair_effect_name") + "\\#.";
            idx++;
        }
        if (hasObjVar(self, "flair_effect_cooldown"))
        {
            names[idx] = utils.packStringId(new string_id("Cooldown"));
            attribs[idx] = "\\#DAA520" + getIntObjVar(self, "flair_effect_cooldown") + " seconds\\#.";
            idx++;
        }
        if (hasObjVar(self, "flair_effect_last_played"))
        {
            names[idx] = utils.packStringId(new string_id("Last used"));
            attribs[idx] = "\\#DAA520" + getGameTime() + "\\#.";
            idx++;
        }
        return SCRIPT_CONTINUE;
    }
}
