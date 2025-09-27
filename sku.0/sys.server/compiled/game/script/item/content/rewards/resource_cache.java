package script.item.content.rewards;/*
@Origin: dsrc.script.item.content.rewards
@Author:  BubbaJoeX
@Purpose: World Boss Purchasable - Grants 5k best resources of a given attribute.
@Requirements: <no requirements>
@Notes: Destroy on use
@Created: Friday, 7/5/2024, at 4:03 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.craftinglib;
import script.library.sui;
import script.library.utils;

public class resource_cache extends script.terminal.terminal_character_builder
{

    public int RES_CACHE_AMT = 45000;

    public int setup(obj_id self)
    {
        setName(self, "Resource Cache");
        setDescriptionString(self, "This cache allows you to pick a resource for 45,000 units at it's respective classes best quality.");
        return SCRIPT_CONTINUE;
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

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true))
        {
            if (utils.isNestedWithinAPlayer(self))
            {
                mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Cache"));
            }
            if (isGod(player))
            {
                mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reset"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            handleCacheBestResourceOption(player);
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            setup(self);
        }
        return SCRIPT_CONTINUE;
    }

    public void handleCacheBestResourceOption(obj_id player) throws InterruptedException
    {
        obj_id self = getSelf();
        refreshMenu(player, "Select the desired resource category", "Resource Cache", BEST_RESOURCE_TYPES, "handleBestCategorySelection", false);
    }

    public int handleCacheBestCategorySelection(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        int idx = sui.getListboxSelectedRow(params);
        if (btn == sui.BP_REVERT)
        {
            return SCRIPT_CONTINUE;
        }
        if (btn == sui.BP_CANCEL)
        {
            cleanScriptVars(player);
            closeOldCacheWindow(player);
            return SCRIPT_CONTINUE;
        }
        if (idx == -1)
        {
            cleanScriptVars(player);
            return SCRIPT_CONTINUE;
        }
        if (idx > RESOURCE_BASE_TYPES.length - 1)
        {
            utils.setScriptVar(player, "cache_builder.specificFilter", -1);
            refreshMenu(player, "Select the desired resource category", "Resource Cache", RESOURCE_TYPES, "handleBestCategorySelection", false);
            return SCRIPT_CONTINUE;
        }
        location loc = getLocation(player);
        String planet = "current";
        String[] resourceList = getResourceChildClasses(RESOURCE_BASE_TYPES[idx]);
        int goodResources = 0;
        for (int i = 0; i < resourceList.length; ++i)
        {
            if (!hasResourceType(resourceList[i]))
            {
                resourceList[i] = null;
            }
            else
            {
                ++goodResources;
            }
        }
        String[] temp = new String[goodResources];
        goodResources = 0;
        for (String s : resourceList)
        {
            if (s != null)
            {
                temp[goodResources++] = s;
            }
        }
        resourceList = temp;
        temp = null;
        refreshMenu(player, "Select the desired resource category", "Resource Cache", resourceList, "handleCacheBestResourceSelection", false);
        utils.setScriptVar(player, "cache_builder.resourceList", resourceList);
        return SCRIPT_CONTINUE;
    }

    public int handleCacheResourceSelection(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        int idx = sui.getListboxSelectedRow(params);
        if (btn == sui.BP_REVERT)
        {
            return SCRIPT_CONTINUE;
        }
        if (btn == sui.BP_CANCEL)
        {
            cleanScriptVars(player);
            closeOldCacheWindow(player);
            return SCRIPT_CONTINUE;
        }
        if (idx == -1)
        {
            cleanScriptVars(player);
            return SCRIPT_CONTINUE;
        }
        String[] resourceList = utils.getStringArrayScriptVar(player, "cache_builder.resourceList");
        if (utils.hasScriptVar(player, "cache_builder.specificFilter"))
        {
            String[] attribs = craftinglib.getAttribNamesByResourceClass(resourceList[idx]);
            if (attribs == null)
            {
                debugSpeakMsg(player, "attribs null");
                return SCRIPT_CONTINUE;
            }
            utils.setScriptVar(player, "cache_builder.resourceIndex", idx);
            refreshMenu(player, "Select the desired attribute", "Resource Cache", attribs, "handleCacheBestResourceSelectionWithAttribute", false);
            return SCRIPT_CONTINUE;
        }
        craftinglib.makeBestResource(player, resourceList[idx], RES_CACHE_AMT);
        closeOldCacheWindow(player);
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }

    public int handleCacheBestResourceSelectionWithAttribute(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int btn = sui.getIntButtonPressed(params);
        int idx = sui.getListboxSelectedRow(params);
        if (btn == sui.BP_REVERT)
        {
            return SCRIPT_CONTINUE;
        }
        if (btn == sui.BP_CANCEL)
        {
            cleanScriptVars(player);
            closeOldCacheWindow(player);
            return SCRIPT_CONTINUE;
        }
        if (idx == -1)
        {
            cleanScriptVars(player);
            return SCRIPT_CONTINUE;
        }
        String[] resourceList = utils.getStringArrayScriptVar(player, "cache_builder.resourceList");
        int resourceListIndex = utils.getIntScriptVar(player, "cache_builder.resourceIndex");
        String[] attribs = craftinglib.getAttribNamesByResourceClass(resourceList[resourceListIndex]);
        utils.removeScriptVar(player, "cache_builder.specificFilter");
        utils.removeScriptVar(player, "cache_builder.resourceIndex");
        craftinglib.makeBestResourceByAttribute(player, resourceList[resourceListIndex], attribs[idx], RES_CACHE_AMT);
        closeOldCacheWindow(player);
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }

    public void closeOldCacheWindow(obj_id player) throws InterruptedException
    {
        String playerPath = "cache_builder.";
        if (utils.hasScriptVar(player, "cache_builder.pid"))
        {
            int oldpid = utils.getIntScriptVar(player, "cache_builder.pid");
            forceCloseSUIPage(oldpid);
            utils.removeScriptVar(player, "cache_builder.pid");
        }
    }
}
