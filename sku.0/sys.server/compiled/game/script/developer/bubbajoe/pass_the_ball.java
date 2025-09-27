package script.developer.bubbajoe;/*
@Origin: script.developer.bubbajoe.
@Author: BubbaJoeX
@Purpose: Hot potato like game where you pass the ball to another player.
@Notes:
    This script is a work in progress and is not yet functional.
    Currently you can pass the ball to any creature object.
@TODO: Fix above.
@Created: Saturday, 4/5/2024, at 9:45 AM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.utils;
import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class pass_the_ball extends script.base_script
{
    public int OnAttach(obj_id self) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Throw Ball"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!isIdValid(player))
            {
                return SCRIPT_CONTINUE;
            }
            if (getState(player, STATE_RIDING_MOUNT) == 1)
            {
                broadcast(player, "You can't pass the ball while mounted.");
                return SCRIPT_CONTINUE;
            }
            obj_id myTarget = getIntendedTarget(player);
            if (myTarget == null || !isIdValid(myTarget))
            {
                sendSystemMessage(player, new string_id("spam", "need_intended_target"));
                return SCRIPT_CONTINUE;
            }
            float travelDistance = getDistance(myTarget, player);
            if (travelDistance > 30.0f)
            {
                sendSystemMessage(player, new string_id("spam", "out_of_snowball_range"));
                return SCRIPT_CONTINUE;
            }
            if (travelDistance <= 30.0f && travelDistance > 20.0f)
            {
                attacker_results cbtAttackerResults = makeDummyAttackerResults(player);
                defender_results[] cbtDefenderResults = makeDummyDefenderResults(myTarget);
                doCombatResults("throw_snow_ball_long", cbtAttackerResults, cbtDefenderResults);
            }
            if (travelDistance <= 20.0f && travelDistance > 10.0f)
            {
                attacker_results cbtAttackerResults = makeDummyAttackerResults(player);
                defender_results[] cbtDefenderResults = makeDummyDefenderResults(myTarget);
                doCombatResults("throw_snow_ball_medium", cbtAttackerResults, cbtDefenderResults);
            }
            if (travelDistance <= 10.0f && travelDistance > 0.0f)
            {
                attacker_results cbtAttackerResults = makeDummyAttackerResults(player);
                defender_results[] cbtDefenderResults = makeDummyDefenderResults(myTarget);
                doCombatResults("throw_snow_ball_short", cbtAttackerResults, cbtDefenderResults);
            }
            broadcast(player, "You have passed the ball to " + toUpper(getName(myTarget), 0));
            putIn(self, utils.getInventoryContainer(myTarget));
            obj_id[] witnesses = getAllPlayers(getLocation(self), 10.0f);
            for (obj_id witness : witnesses)
            {
                playClientEffectLoc(player, "clienteffect/entertainer_dazzle_level_three.cef", getLocation(witness), 1.0f);
            }
            broadcast(getIntendedTarget(player), "You have been passed the ball from " + toUpper(getName(player) + "!", 0));
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public attacker_results makeDummyAttackerResults(obj_id objAttacker) throws InterruptedException
    {
        attacker_results cbtAttackerResults = new attacker_results();
        cbtAttackerResults.id = objAttacker;
        cbtAttackerResults.weapon = utils.getStaticItemInInventory(objAttacker, "item_content_mustafar_toy_ball");
        cbtAttackerResults.endPosture = getPosture(objAttacker);
        return cbtAttackerResults;
    }

    public defender_results[] makeDummyDefenderResults(obj_id objDefender) throws InterruptedException
    {
        defender_results[] cbtDefenderResults = new defender_results[1];
        cbtDefenderResults[0] = new defender_results();
        cbtDefenderResults[0].id = objDefender;
        cbtDefenderResults[0].endPosture = 0;
        cbtDefenderResults[0].result = COMBAT_RESULT_HIT;
        cbtDefenderResults[0].damageAmount = 0;
        cbtDefenderResults[0].hitLocation = 0;
        return cbtDefenderResults;
    }
}
