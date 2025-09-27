package script.event.gjpud;/*
@Origin: dsrc.script.event.gjpud
@Author:  BubbaJoeX
@Purpose: Dragging a scrap heap on this creature will increment your "stamp" count and once you hit "10" it awards an event token.
@Requirements: GJPUD V2
@Notes: Do not spawn adhoc, this is a template for the GJPUD V2 event.
@Created: Wednesday, 8/7/2024, at 5:08 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.chat;
import script.library.static_item;
import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

import java.util.Calendar;

public class trade_in extends script.base_script
{

    public static final int TOKEN_LIMIT = 2;
    public static final int TRADE_LIMIT = 9;//+1 = 10
    public String[] RESPONSES_YAY = new String[]{
            "Thanks, I love scrap heaps!",
            "This will get me one step closer to getting off this rock!",
            "I'm going to be rich!",
            "Yipee!",
            "Poodoo! My backpack is going to be heavy!",
            "Thanks!",
            "Wonderous!"
    };
    public String[] SORRY_MESSAGES = new String[]{
            "Sorry boss, I only accept scrap heaps.",
            "I'm only interested in scrap heaps.",
            "I can't take that, I only accept scrap heaps.",
            "I'm sorry, I only accept scrap heaps.",
            "This isn't something worth my time, I only accept scrap heaps.",
            "I'm not interested in that, sorry..."
    };

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

    public int setup(obj_id self)
    {
        String namePrefix = toUpper(generateRandomName("object/creature/player/rodian_male.iff"), 0);
        setName(self, namePrefix + " (a scrap peddler)");
        setDescriptionString(self, "This peddler seems very enthusiastic about scrap heaps. Try dragging a scrap heap onto them and see what happens!");
        setInvulnerable(self, true);
        setCondition(self, CONDITION_HOLIDAY_INTERESTING);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isGod(player))
        {
            if (item == menu_info_types.SERVER_MENU1)
            {
                removeObjVar(player, "gjpud.v2.total");
                removeObjVar(player, "gjpud.v2.tokens");
                broadcast(player, "Your scrap heap count has been reset.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reset Counters"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnGiveItem(obj_id self, obj_id item, obj_id player) throws InterruptedException
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int currentCollected = getIntObjVar(player, "gjpud.v2.total");
        int totalTokens = getIntObjVar(player, "gjpud.v2.tokens");

        // Restrict trade-ins between 8/9/2024 and 8/16/2024 (MST) unless Game Master.
        /*if (year == 2024 && month == Calendar.AUGUST && day >= 9 && day <= 16)
        {
            if (isGod(player))
            {
                broadcast(player, "[GM] Overriding time lockout.,.");
            }
            else
            {
                chat.chat(self, "Sorry, I'm not accepting any scrap heaps right now.");
                putIn(item, utils.getInventoryContainer(player));
                return SCRIPT_CONTINUE;
            }
        }*/

        if (totalTokens >= TOKEN_LIMIT)
        {
            broadcast(self, "You have reached the maximum amount of Event Tokens you can earn on this character (2).");
            putIn(item, utils.getInventoryContainer(player));
            return SCRIPT_CONTINUE;
        }

        if (static_item.isStaticItem(item))
        {
            if (!static_item.getStaticItemName(item).equals("item_gjpud_scrap_heap"))
            {
                chat.chat(self, SORRY_MESSAGES[rand(0, SORRY_MESSAGES.length - 1)]);
                putIn(item, utils.getInventoryContainer(player));
                return SCRIPT_CONTINUE;
            }
            else
            {
                chat.chat(self, RESPONSES_YAY[rand(0, RESPONSES_YAY.length - 1)]);
            }
            if (currentCollected < TRADE_LIMIT)
            {
                int newCollected = currentCollected + 1;
                setObjVar(player, "gjpud.v2.total", newCollected);
                broadcast(player, "You have turned in a total of " + newCollected + " scrap heaps.");
                if (getCount(item) > 1)
                {
                    decrementCount(item);
                    putIn(item, utils.getInventoryContainer(player));
                }
                else
                {
                    destroyObject(item);
                }
            }
            else
            {
                setObjVar(player, "gjpud.v2.total", 0);
                setObjVar(player, "gjpud.v2.tokens", totalTokens + 1);
                broadcast(player, "You have collected 10 scrap heaps and have been awarded an Event Token.");
                static_item.createNewItemFunction("item_event_token_01_01", utils.getInventoryContainer(player));
                if (getCount(item) > 1)
                {
                    decrementCount(item);
                    putIn(item, utils.getInventoryContainer(player));
                }
                else
                {
                    destroyObject(item);
                }
            }
        }
        else
        {
            chat.chat(self, SORRY_MESSAGES[rand(0, SORRY_MESSAGES.length - 1)]);
            putIn(item, utils.getInventoryContainer(player));
        }
        return SCRIPT_CONTINUE;
    }
}
