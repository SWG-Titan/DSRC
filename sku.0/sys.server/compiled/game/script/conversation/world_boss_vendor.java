package script.conversation;

/*
 * Copyright © SWG-OR 2024.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.*;
import script.library.*;

public class world_boss_vendor extends base_script
{
    public static final String c_stringFile = "conversation/world_boss_vendor";

    public boolean world_boss_vendor_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }

    public void world_boss_vendor_action_showTokenVendorUI(obj_id player, obj_id npc) throws InterruptedException
    {
        dictionary d = new dictionary();
        d.put("player", player);
        messageTo(npc, "showInventorySUI", d, 0, false);
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        recoup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        recoup(self);
        return SCRIPT_CONTINUE;
    }

    public int recoup(obj_id self)
    {
        if (!isTangible(self) || isPlayer(self))
        {
            detachScript(self, "conversation.world_boss_vendor");
        }
        setCondition(self, CONDITION_CONVERSABLE);
        setCondition(self, CONDITION_INTERESTING);
        if (getTemplateName(self).contains("som/must"))
        {
            setName(self, "Mustafar Token Vendor");
            setDescriptionString(self, "This vendor will exchange your Mustafar Tokens for hot items.");
        }
        else if (getTemplateName(self).contains("mako_"))
        {
            setName(self, "Kitchen Vendor");
            setDescriptionString(self, "This vendor will allow you to purchase various appliances for your home.");
        }
        else
        {
            setName(self, "World Boss Token Vendor");
            setDescriptionString(self, "This vendor will exchange your World Boss Tokens for new items.");
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        int menu = menuInfo.addRootMenu(menu_info_types.CONVERSE_START, null);
        menu_info_data menuInfoData = menuInfo.getMenuItemById(menu);
        menuInfoData.setServerNotify(false);
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.world_boss_vendor");
        return SCRIPT_CONTINUE;
    }

    public int OnDetach(obj_id self) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }

    public boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses) throws InterruptedException
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }

    public int world_boss_vendor_handleBranch1(obj_id self, obj_id player, string_id response) throws InterruptedException
    {
        if (response.equals("s_2"))
        {
            world_boss_vendor_action_showTokenVendorUI(player, self);
            chat.chat(self, player, new string_id(c_stringFile, "s_3"));
        }
        else if (response.equals("s_4"))
        {
            chat.chat(self, player, new string_id(c_stringFile, "s_5"));
        }
        npcEndConversation(player);
        return SCRIPT_CONTINUE;
    }

    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        if (ai_lib.isInCombat(self) || ai_lib.isInCombat(player))
        {
            return SCRIPT_OVERRIDE;
        }
        if (world_boss_vendor_condition__defaultCondition(player, self))
        {
            prose_package pp = new prose_package();
            pp = prose.setStringId(pp, new string_id(c_stringFile, "s_1"));
            pp.target.set(player);
            npcStartConversation(player, self, "world_boss_vendor", null, pp, new string_id[]{new string_id(c_stringFile, "s_2"), new string_id(c_stringFile, "s_4")});
            return SCRIPT_CONTINUE;
        }
        chat.chat(self, "Error:  All conditions for OnStartNpcConversation were false.");
        return SCRIPT_CONTINUE;
    }

    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        if (!conversationId.equals("world_boss_vendor"))
        {
            return SCRIPT_CONTINUE;
        }
        if (world_boss_vendor_handleBranch1(self, player, response) == SCRIPT_CONTINUE)
        {
            return SCRIPT_CONTINUE;
        }
        chat.chat(self, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");
        utils.removeScriptVar(player, "conversation.world_boss_vendor.branchId");
        return SCRIPT_CONTINUE;
    }
}
