package script.event;/*
@Origin: dsrc.script.event
@Author:  BubbaJoeX
@Purpose: Gives the cache
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 9/10/2024, at 3:25 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.*;

import java.util.ArrayList; // Import ArrayList

public class live_event_cache_giver extends base_script
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
        sendDirtyObjectMenuNotification(self);
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Cache"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (hasObjVar(player, "te24") && hasObjVar(self, "station_user." + getPlayerStationId(player)))
        {
            broadcast(player, "You already have claimed a cache on this account.");
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            String[] tokens = trial.HEROIC_TOKENS;
            ArrayList<String> validTokens = new ArrayList<>();
            ArrayList<String> convertedNames = new ArrayList<>();
            dictionary static_item_data;

            for (int i = 0; i < tokens.length; i++)
            {
                static_item_data = static_item.getMasterItemDictionary(tokens[i]);
                if (static_item_data != null)
                {
                    if (tokens[i].contains("marauder"))
                    {
                        continue;
                    }
                    validTokens.add(tokens[i]);
                    convertedNames.add(static_item_data.getString("string_name"));
                }
            }

            if (convertedNames.isEmpty())
            {
                broadcast(player, "No valid tokens available.");
                return SCRIPT_CONTINUE;
            }

            String title = "Token Selection";
            String prompt = "Select a token type to deposit 100 tokens into your inventory:";
            sui.listbox(self, player, prompt, sui.OK_CANCEL, title, convertedNames.toArray(new String[0]), "handleTokenSelection", true, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleTokenSelection(obj_id self, dictionary params) throws InterruptedException
    {
        int tokenCount = 175;
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }

        int pressed = sui.getIntButtonPressed(params);
        if (pressed == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the claim.");
            return SCRIPT_CONTINUE;
        }

        String[] tokens = trial.HEROIC_TOKENS;
        ArrayList<String> validTokens = new ArrayList<>();
        dictionary static_item_data;

        for (String token : tokens)
        {
            static_item_data = static_item.getMasterItemDictionary(token);
            if (static_item_data != null)
            {
                validTokens.add(token);
            }
        }

        if (idx >= validTokens.size())
        {
            return SCRIPT_CONTINUE;
        }

        String token = validTokens.get(idx);
        dictionary staticItem = static_item.getMasterItemDictionary(token);
        if (staticItem == null)
        {
            return SCRIPT_CONTINUE;
        }

        if (token.contains("heroic"))
        {
            tokenCount = 25;
        }
        if (token.contains("entertainer"))
        {
            tokenCount = 250;
        }
        if (token.contains("maurader"))
        {
            tokenCount = 1;
            //unused token too lazy to remove it.
        }
        if (token.contains("_pcg_"))
        {
            tokenCount = 500;
        }
        obj_id item = static_item.createNewItemFunction(token, utils.getInventoryContainer(player), tokenCount);
        if (isIdValid(item))
        {
            broadcast(player, "You have received " + tokenCount + " " + staticItem.getString("string_name"));
            broadcast(player, "You have received " + 2 + " Event Tokens");
            static_item.createNewItemFunction("item_event_token_01_01", utils.getInventoryContainer(player));
            static_item.createNewItemFunction("item_event_token_01_01", utils.getInventoryContainer(player));
            setObjVar(player, "te24", 1);
            setObjVar(self, "station_user." + getPlayerStationId(player), 1);
        }
        sendDirtyObjectMenuNotification(self);
        return SCRIPT_CONTINUE;
    }
}