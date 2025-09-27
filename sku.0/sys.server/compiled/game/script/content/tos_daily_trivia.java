package script.content;

/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Allows players to answer trivia
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 5/14/2024, at 5:33 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.money;
import script.library.sui;
import script.library.utils;

import java.util.HashMap;
import java.util.Map;

public class tos_daily_trivia extends base_script
{
    private final Map<String, String> triviaMap = new HashMap<>();

    public tos_daily_trivia()
    {
        triviaMap.put("What is the name of Han Solo's ship?", "Millennium Falcon");
        triviaMap.put("Who trained Luke Skywalker in the ways of the Force?", "Obi-Wan Kenobi");
        triviaMap.put("What species is Yoda?", "Unknown");
        triviaMap.put("What is the Sith home planet?", "Moraband");
        triviaMap.put("What is Princess Leia’s home planet?", "Alderaan");
        triviaMap.put("Who built C-3PO?", "Anakin Skywalker");
        triviaMap.put("What is the color of Mace Windu's lightsaber?", "Purple");
        triviaMap.put("What bounty hunter captured Han Solo in carbonite?", "Boba Fett");
        triviaMap.put("Who was the Supreme Chancellor before Chancellor Palpatine?", "Valorum");
        triviaMap.put("What is the name of the Wookiee homeworld?", "Kashyyyk");
        triviaMap.put("Which hand (left or right) did Luke Skywalker lose during his duel with Darth Vader?", "Right");
        triviaMap.put("What is the capital city of the Galactic Republic?", "Coruscant");
        triviaMap.put("Who killed Jabba the Hutt?", "Leia Organa");
        triviaMap.put("What was the name of Anakin's childhood friend?", "Kitster");
        triviaMap.put("What ancient Sith Lord created the Rule of Two?", "Darth Bane");
        triviaMap.put("What is the name of the creature that attacked Luke on Hoth?", "Wampa");
        triviaMap.put("Who was the first Jedi to fall to Order 66 on screen?", "Plo Koon");
        triviaMap.put("What material is used to power lightsabers?", "Kyber Crystal");
        triviaMap.put("What was Grand Admiral Thrawn’s species?", "Chiss");
        triviaMap.put("Which bounty hunter disintegrated on Vader’s orders in Episode V?", "Zuckuss");
        triviaMap.put("What is the name of the Clone Wars-era Mandalorian leader killed by Maul?", "Pre Vizsla");
        triviaMap.put("What Sith Lord mastered the technique to cheat death and create life?", "Darth Plagueis");
        triviaMap.put("What is the name of the prison in Rogue One where Jyn Erso is held?", "Wobani Labor Camp");
        triviaMap.put("Who was the designer of the first Death Star’s superweapon?", "Galen Erso");


    }

    public boolean canDoTrivia(obj_id self, obj_id player)
    {
        int stationid = getPlayerStationId(player);
        if (hasObjVar(self, "tos_daily_trivia_s2." + stationid))
        {
            int last = getIntObjVar(self, "tos_daily_trivia_s2." + stationid);
            int now = getCalendarTime();
            if (now - last > 86400)
            {
                return true;
            }
            else
            {
                broadcast(player, "You have already answered a daily trivia question today. Try again tomorrow!");
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Daily Trivia"));
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Reset"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            removeObjVar(player, "tos_daily_trivia");
            if (hasObjVar(self, "tos_daily_trivia"))
            {
                removeObjVar(self, "tos_daily_trivia");
            }
            if (canDoTrivia(self, player))
            {
                String[] questions = triviaMap.keySet().toArray(new String[0]);
                int questionIndex = rand(0, questions.length - 1);
                String questionText = questions[questionIndex];
                String answer = triviaMap.get(questionText);
                setObjVar(player, "tos_daily_trivia_s2.answer", answer);
                sui.inputbox(self, player, "Answer the following question (Case Sensitive): \n\n\\#DAA520" + questionText, sui.OK_CANCEL, "Trivia", sui.INPUT_NORMAL, null, "handleTrivia");
            }
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            removeObjVar(self, "tos_daily_trivia_s2." + getPlayerStationId(player));
            removeObjVar(player, "tos_daily_trivia_s2." + getPlayerStationId(player));
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }

    public int handleTrivia(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        String correctAnswer = getStringObjVar(player, "tos_daily_trivia_s2.answer");
        int correct = 0;
        if (answer.contains(correctAnswer))
        {
            if (hasObjVar(player, "tos_daily_trivia_s2.correct"))
            {
                if (getIntObjVar(player, "tos_daily_trivia_s2.correct") >= 15)
                {
                    broadcast(player, "You have completed this season's trivia. Play again next update.");
                    return SCRIPT_CONTINUE;
                }
                if (getIntObjVar(player, "tos_daily_trivia_s2.correct") == 14)
                {
                    broadcast(player, "You have answered 14 questions correctly. You have been awarded a prize.");
                    awardLargePrize(player);
                    setObjVar(player, "tos_daily_trivia_s2.correct", 15);
                    return SCRIPT_CONTINUE;
                }
                correct = getIntObjVar(player, "tos_daily_trivia_s2.correct");
                correct++;
                debugConsoleMsg(player, "Total Correct Questions: " + correct);
                setObjVar(player, "tos_daily_trivia_s2.correct", correct);
            }
            else
            {
                debugConsoleMsg(player, "Total Correct Questions: " + 1);
                setObjVar(player, "tos_daily_trivia_s2.correct", 1);
            }
            awardSmallPrize(player);
            broadcast(player, "Correct! You have been awarded a small amount of credits.");
            setObjVar(self, "tos_daily_trivia_s2." + getPlayerStationId(player), getCalendarTime());
            return SCRIPT_CONTINUE;
        }
        else
        {
            broadcast(player, "Incorrect! Try again!");
            removeObjVar(self, "tos_daily_trivia_s2." + getPlayerStationId(player));
            return SCRIPT_CONTINUE;
        }
    }

    private void awardLargePrize(obj_id player) throws InterruptedException
    {
        obj_id token = createObject("object/tangible/loot/misc/marauder_token.iff", utils.getInventoryContainer(player), "");
        attachScript(token, "content.tcg_voucher_vendor");
    }

    private void awardSmallPrize(obj_id player) throws InterruptedException
    {
        money.deposit(player, 15000);
    }
}
