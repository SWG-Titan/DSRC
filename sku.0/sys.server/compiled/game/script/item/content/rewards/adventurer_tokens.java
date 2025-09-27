package script.item.content.rewards;

/*
@Origin: dsrc.script.item.content.rewards
@Author: BubbaJoeX
@Purpose: Token granting cache for Rebel and Imperial players.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Friday, 10/11/2024, at 10:59 AM
*/

import script.*;
import script.library.static_item;

public class adventurer_tokens extends base_script
{
    private final linked_array tokenData = new linked_array();

    public adventurer_tokens()
    {
        tokenData.addStringCombo("item_meatlump_lump_01_01", 50);
        tokenData.addStringCombo("item_heroic_token_axkva_01_01", 5);
        tokenData.addStringCombo("item_heroic_token_tusken_01_01", 5);
        tokenData.addStringCombo("item_heroic_token_ig88_01_01", 5);
        tokenData.addStringCombo("item_heroic_token_black_sun_01_01", 5);
        tokenData.addStringCombo("item_heroic_token_exar_01_01", 5);
        tokenData.addStringCombo("item_heroic_token_echo_base_01_01", 5);
        tokenData.addStringCombo("item_nova_orion_space_resource_01_01", 15);
        tokenData.addStringCombo("item_token_duty_space_01_01", 15);
        tokenData.addStringCombo("item_pgc_token_03", 5);
    }

    public int OnAttach(obj_id self)
    {
        setName(self, "Adventurer's Token Cache");
        setDescriptionString(self, "This cache contains a variety of tokens for the adventurous.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Adventurer's Token Cache");
        setDescriptionString(self, "This cache contains a variety of tokens for the adventurous.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15f, true))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Cache"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15f, true))
        {
            openBox(player, self);
        }
        return SCRIPT_CONTINUE;
    }

    public void openBox(obj_id player, obj_id self) throws InterruptedException
    {
        obj_id[] lootArray = new obj_id[tokenData.getSize("String")];
        int i = 0;
        for (int j = 0; j < tokenData.getSize("String"); j++)
        {
            String tokenString = tokenData.getString(j);
            Integer tokenAmount = tokenData.getInteger(j);

            if (tokenString != null && tokenAmount != null && tokenAmount > 0)
            {
                obj_id token = static_item.createNewItemFunction(tokenString, player, tokenAmount);
                if (isIdValid(token))
                {
                    lootArray[i] = token;
                    broadcast(player, "You have received " + tokenAmount + " " + tokenString + " tokens.");
                    i++;
                }
            }
        }

        if (i > 0)
        {
            obj_id[] finalLootArray = new obj_id[i];
            System.arraycopy(lootArray, 0, finalLootArray, 0, i);
            showLootBox(player, finalLootArray);
        }
        destroyObject(self);
    }
}
