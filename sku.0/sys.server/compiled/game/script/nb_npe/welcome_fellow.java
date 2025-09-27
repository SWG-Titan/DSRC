package script.nb_npe;/*
@Origin: dsrc.script.nb_npe
@Author: BubbaJoeX
@Purpose: Chats with new players,
@Created: Tuesday, 9/5/2023, at 11:13 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.chat;
import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.dictionary;


public class welcome_fellow extends script.base_script
{
    public welcome_fellow()
    {
    }

    public int OnAttach(obj_id self)
    {
        this.OnInitialize(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }
}
