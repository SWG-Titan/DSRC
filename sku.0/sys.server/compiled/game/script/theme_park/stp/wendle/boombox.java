package script.theme_park.stp.wendle;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.obj_id;

/**
 * @author BubbaJoe
 */
public class boombox extends script.base_script
{
    public boombox()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        createTriggerVolume("boombox_cantina", 15.0f, false);
        setName(self, "a radio (inactive)");
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (isPlayer(breacher) && (volumeName.equals("boombox_cantina")))
        {
            String soundFile = "sound/mus_pirate_cantina.snd";
            String soundFile2 = "sound/mus_cantina_band_remix.snd";
            setName(self, getName(self) + " (active)");
            playMusic(breacher, soundFile);
            //playMusic(breacher, soundFile2);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
}