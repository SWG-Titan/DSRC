package script.item.content.rewards;

import script.library.factions;
import script.library.static_item;
import script.*;

import java.util.ArrayList;

public class factional_tokens extends script.base_script
{
    private final linked_array rebelTokens = new linked_array();
    private final linked_array imperialTokens = new linked_array();

    public factional_tokens()
    {
        rebelTokens.addStringCombo("item_battlefield_rebel_token", 150);
        rebelTokens.addStringCombo("item_gcw_rebel_token", 500);
        rebelTokens.addStringCombo("item_rebel_station_token_01_01", 150);
        rebelTokens.addStringCombo("item_restuss_rebel_commendation_02_01", 250);

        imperialTokens.addStringCombo("item_battlefield_imperial_token", 150);
        imperialTokens.addStringCombo("item_gcw_imperial_token", 500);
        imperialTokens.addStringCombo("item_imperial_station_token_01_01", 150);
        imperialTokens.addStringCombo("item_restuss_imperial_commendation_02_01", 250);
    }

    public int OnAttach(obj_id self)
    {
        setName(self, "Factional Token Cache");
        setDescriptionString(self, "This cache contains a variety of tokens for the Rebel and Imperial factions.");
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Factional Token Cache");
        setDescriptionString(self, "This cache contains a variety of tokens for the Rebel and Imperial factions.");
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15f, true))
        {
            if (factions.isRebel(player) || factions.isImperial(player))
            {
                mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Claim Cache"));
            }
            else
            {
                broadcast(player, "You must be a Rebel or Imperial to use this cache.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15f, true))
        {
            if (factions.isRebel(player) || factions.isImperial(player))
            {
                if (item == menu_info_types.ITEM_USE)
                {
                    String faction = factions.getFaction(player);
                    if (faction == null)
                    {
                        broadcast(player, "You must be a Rebel or Imperial to use this cache.");
                        return SCRIPT_CONTINUE;
                    }
                    if (faction.equalsIgnoreCase("Rebel"))
                    {
                        openBox(player, self, rebelTokens);
                    }
                    else if (faction.equalsIgnoreCase("Imperial"))
                    {
                        openBox(player, self, imperialTokens);
                    }
                    else
                    {
                        return SCRIPT_CONTINUE;
                    }
                }
            }
            else
            {
                broadcast(player, "You must be a Rebel or Imperial to use this cache.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }

    public void openBox(obj_id player, obj_id self, linked_array tokenArray) throws InterruptedException
    {
        ArrayList<obj_id> lootList = new ArrayList<>();

        for (int i = 0; i < tokenArray.getSize("String"); i++)
        {
            String tokenString = tokenArray.getString(i);
            Integer tokenAmount = tokenArray.getInteger(i);

            // Ensure that tokenString and tokenAmount are valid
            if (tokenString != null && tokenAmount != null && tokenAmount > 0)
            {
                obj_id token = static_item.createNewItemFunction(tokenString, player, tokenAmount);
                if (isIdValid(token))
                {
                    lootList.add(token);
                    broadcast(player, "You have received " + tokenAmount + " " + tokenString + " tokens.");
                }
            }
        }

        obj_id[] lootArray = lootList.toArray(new obj_id[0]);
        if (lootArray.length > 0)
        {
            showLootBox(player, lootArray);
        }
        destroyObject(self);
    }
}
