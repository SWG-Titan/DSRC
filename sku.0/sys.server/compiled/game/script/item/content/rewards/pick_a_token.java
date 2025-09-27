package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author:  BubbaJoeX
@Purpose: Allows players to choose a token for a reward
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Monday, 9/9/2024, at 4:49 AM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.repeatables;
import script.library.static_item;
import script.library.sui;
import script.library.trial;

public class pick_a_token extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 5f, true) && !isIncapacitated(player) && !isDead(player))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Tokens"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            pickTokens(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int pickTokens(obj_id self, obj_id player) throws InterruptedException
    {
        String[] tokens = trial.HEROIC_TOKENS;
        String[] convertedNames = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++)
        {
            dictionary staticItem = static_item.getMasterItemDictionary(tokens[i]);
            convertedNames[i] = staticItem.getString("string_name");
        }
        String title = "Token Selection";
        String prompt = "Select a token type to fund content for:";
        sui.listbox(self, player, prompt, sui.OK_CANCEL, title, convertedNames, "handleTokenSelection", true, false);
        return SCRIPT_CONTINUE;
    }

    public int handleTokenSelection(obj_id self, dictionary params) throws InterruptedException
    {
        int count;
        obj_id player = sui.getPlayerId(params);
        String type = getStringObjVar(self, "type");
        if (type == null || type.isEmpty())
        {
            broadcast(player, "Invalid item type.");
            return SCRIPT_CONTINUE;
        }
        if (type.equals("weekly"))
        {
            count = repeatables.W_TOKEN_REWARD_AMOUNT;
        }
        else
        {
            count = repeatables.D_TOKEN_REWARD_AMOUNT;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (idx < 6)
        {
            idx = idx - 1;//account for the removed maurader token
        }
        String[] tokens = trial.HEROIC_TOKENS;
        if (idx >= tokens.length)
        {
            return SCRIPT_CONTINUE;
        }
        String token = tokens[idx];
        dictionary staticItem = static_item.getMasterItemDictionary(token);
        if (staticItem == null)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id item = static_item.createNewItemFunction(token, player, count);
        if (isIdValid(item))
        {
            broadcast(player, "You have recieved " + count + " " + staticItem.getString("string_name"));
        }
        return SCRIPT_CONTINUE;
    }
}
