package script.theme_park.stp;

/*
 * Copyright © SWG: Titan 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.obj_id;

/**
 * @author BubbaJoe
 */
public class stp_sign extends script.base_script
{
    public stp_sign()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        String sign_name = getStringObjVar(self, "stp.sign.name");
        if (sign_name.equals("city"))
        {
            setName(self, "a sign");
        }
        else
        {
            setName(self, sign_name);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        String sign_name = getStringObjVar(self, "stp.sign.name");
        if (sign_name.equals("city"))
        {
            setName(self, "a sign");
        }
        else
        {
            setName(self, sign_name);
        }
        return SCRIPT_CONTINUE;
    }
}
