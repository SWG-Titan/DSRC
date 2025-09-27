package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: Creates a custom string description field of objects.
@Note: Doesn't quite work the way it did on my previous project.
*/

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.obj_id;

public class sync extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        String DESC = getStringObjVar(self, "null_desc");
        if (DESC == null)
        {
            setDescriptionString(self, "An unknown object");
        }
        else
        {
            String descMem = getStringObjVar(self, "null_desc");
            setDescriptionString(self, descMem);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        String DESC = getStringObjVar(self, "null_desc");
        if (DESC == null)
        {
            setDescriptionString(self, "An unknown object");
        }
        else
        {
            String descMem = getStringObjVar(self, "null_desc");
            setDescriptionString(self, descMem);
        }
        return SCRIPT_CONTINUE;
    }
}
