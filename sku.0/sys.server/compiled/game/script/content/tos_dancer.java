package script.content;

import script.dictionary;
import script.library.chat;
import script.library.utils;
import script.obj_id;

import static script.library.utils.*;

public class tos_dancer extends script.base_script
{
    public void skillAnimate(obj_id self)
    {
        if (getTemplateName(self).contains("max"))
        {
            setAnimationMood(self, "themepark_music_2");
        }
        else if (hasObjVar(self, "dance"))
        {
            equip(createObject("object/tangible/instrument/bandfill.iff", self, ""), self);
            setAnimationMood(self, "groove_2");
        }
        else
        {
            setAnimationMood(self, "entertained");
        }
    }

    public int OnAttach(obj_id self)
    {
        skillAnimate(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        skillAnimate(self);
        return SCRIPT_CONTINUE;
    }

    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if ("Encore!".equals(text))
        {
            if (hasScriptVar(self, "cheeredFor"))
            {
                broadcast(speaker, "The band is in the middle of an intermission.");
                return SCRIPT_CONTINUE;
            }
            setScriptVar(self, "cheeredFor", true);
            skillAnimate(self);
            playClientEffectObj(self, "clienteffect/entertainer_dazzle_level_1.cef", self, "");
            if (getTemplateName(self).contains("max"))
            {
                messageTo(self, "handleBandMaster", null, 4.0f, false);
            }
            else
            {
                messageTo(self, "handleCheerleader", null, 1.0f, false);
            }

            messageTo(self, "clearEmoteVar", null, 120.0f, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleBandMaster(obj_id self, dictionary params) throws InterruptedException
    {
        playClientEffectObj(self, "clienteffect/entertainer_dazzle_level_3.cef", self, "");
        chat.chat(self, "Feel the blues!");
        return SCRIPT_CONTINUE;
    }

    public int handleCheerleader(obj_id self, dictionary params) throws InterruptedException
    {
        playClientEffectObj(self, "clienteffect/entertainer_dazzle_level_2.cef", self, "");
        chat.chat(self, "Dax, hit that track!");
        return SCRIPT_CONTINUE;
    }


    public int clearEmoteVar(obj_id self, dictionary params) throws InterruptedException
    {
        utils.removeScriptVar(self, "cheeredFor");
        playClientEffectObj(self, "clienteffect/entertainer_dazzle_level_2.cef", self, "");
        return SCRIPT_CONTINUE;
    }
}
