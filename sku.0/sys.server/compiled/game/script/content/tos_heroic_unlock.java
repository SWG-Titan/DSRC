package script.content;/*
@Origin: dsrc.script.content
@Author:  BubbaJoeX
@Purpose: Unlocks heroics if players answer the questions correctly.
@Requirements: <no requirements>
@Notes: Answers must be exact, but ignores case.
@Created: Tuesday, 5/7/2024, at 6:54 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.instance;
import script.library.sui;

public class tos_heroic_unlock extends base_script
{
    String[] HEROIC_FLAGS = {
            "heroic_exar_kun",
            "heroic_star_destroyer",
            "heroic_tusken_army",
            "heroic_axkva_min",
            "heroic_ig88",
            "echo_base"
    };
    String[] QUESTIONS = {
            "Who is the creator of the IG-88 droids?",
            "What does Krix Swiftshadow throw inside the hangar?",
            "Who struck down Axkva Min?",
            "Who is the Imperial Officer that lead the Imperial AT-AT walkers on Hoth?",
            "What year did the Tuskens raid Mos Espa?",
            "Who was the infamous Sith that was entombed?"
    };
    String[] ANSWERS = {
            "Holowan Laboratories",
            "Thermal Detonator",
            "Gethzerion",
            "General Veers",
            "1 ABY",
            "Exar Kun"
    };
    String[] HINTS = {
            "Not 'Obi-' wan but...",
            "A type of grenade",
            "A prominent Nightsister",
            "\"Distance to the power generators?\"",
            "X amount of years after the Battle of Yavin (X ABY)",
            "Not Xaos, but..."
    };

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
        mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Test of Wisdom"));
        if (isGod(player))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Remove Heroics Flags"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            giveFirstQuestion(self, player);
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            for (String flag : HEROIC_FLAGS)
            {
                instance.removePlayerFlagForInstance(player, flag);
            }
            broadcast(player, "Heroic Flags have been removed.");
        }
        return SCRIPT_CONTINUE;
    }

    public int giveFirstQuestion(obj_id self, obj_id player) throws InterruptedException
    {
        String question = QUESTIONS[0];
        String hint = HINTS[0];
        String prompt = "Question: " + question + "\n\nHint: " + hint;
        String title = "Test of Wisdom";
        sui.inputbox(self, player, prompt, title, "handleFirstAnswer", 128, false, "");
        return SCRIPT_CONTINUE;
    }

    public int handleFirstAnswer(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the Test of Wisdom.");
            return SCRIPT_CONTINUE;
        }
        if (answer.equalsIgnoreCase(ANSWERS[0]))
        {
            String question = QUESTIONS[1];
            String hint = HINTS[1];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleSecondAnswer", 128, false, "");
        }
        else
        {
            broadcast(player, "Incorrect answer. Please try again.");
            String question = QUESTIONS[0];
            String hint = HINTS[0];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleFirstAnswer", 128, false, "");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleSecondAnswer(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the Test of Wisdom.");
            return SCRIPT_CONTINUE;
        }
        if (answer.equalsIgnoreCase(ANSWERS[1]))
        {
            String question = QUESTIONS[2];
            String hint = HINTS[2];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleThirdAnswer", 128, false, "");
        }
        else
        {
            broadcast(player, "Incorrect answer. Please try again.");
            String question = QUESTIONS[1];
            String hint = HINTS[1];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleSecondAnswer", 128, false, "");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleThirdAnswer(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the Test of Wisdom.");
            return SCRIPT_CONTINUE;
        }
        if (answer.equalsIgnoreCase(ANSWERS[2]))
        {
            String question = QUESTIONS[3];
            String hint = HINTS[3];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleFourthAnswer", 128, false, "");
        }
        else
        {
            broadcast(player, "Incorrect answer. Please try again.");
            String question = QUESTIONS[2];
            String hint = HINTS[2];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleThirdAnswer", 128, false, "");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleFourthAnswer(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the Test of Wisdom.");
            return SCRIPT_CONTINUE;
        }
        if (answer.equalsIgnoreCase(ANSWERS[3]))
        {
            String question = QUESTIONS[4];
            String hint = HINTS[4];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleFifthAnswer", 128, false, "");
        }
        else
        {
            broadcast(player, "Incorrect answer. Please try again.");
            String question = QUESTIONS[3];
            String hint = HINTS[3];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleFourthAnswer", 128, false, "");
        }
        return SCRIPT_CONTINUE;
    }

    public int handleFifthAnswer(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the Test of Wisdom.");
            return SCRIPT_CONTINUE;
        }
        if (answer.equalsIgnoreCase(ANSWERS[4]))
        {
            String question = QUESTIONS[5];
            String hint = HINTS[5];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "finish", 128, false, "");
        }
        else
        {
            broadcast(player, "Incorrect answer. Please try again.");
            String question = QUESTIONS[4];
            String hint = HINTS[4];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "handleFifthAnswer", 128, false, "");
        }
        return SCRIPT_CONTINUE;
    }

    public int finish(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String answer = sui.getInputBoxText(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have canceled the Test of Wisdom.");
            return SCRIPT_CONTINUE;
        }
        if (answer.equalsIgnoreCase(ANSWERS[5]))
        {
            instance.flagPlayerForInstance(player, "heroic_exar_kun");
            instance.flagPlayerForInstance(player, "heroic_star_destroyer");
            instance.flagPlayerForInstance(player, "heroic_tusken_army");
            instance.flagPlayerForInstance(player, "heroic_axkva_min");
            instance.flagPlayerForInstance(player, "heroic_ig88");
            instance.flagPlayerForInstance(player, "echo_base");
            sui.msgbox(self, player, "\\#DAA520Congratulations! You have been granted access to Heroic Instances.\\#.", sui.OK_ONLY, "Test of Wisdom", "finish");
            play2dNonLoopingSound(player, "sound/utinni.snd");
        }
        else
        {
            broadcast(player, "Incorrect answer. Please try again.");
            String question = QUESTIONS[5];
            String hint = HINTS[5];
            String prompt = "Question: " + question + "\n\nHint: " + hint;
            String title = "Test of Wisdom";
            sui.inputbox(self, player, prompt, title, "finish", 128, false, "");
        }
        return SCRIPT_CONTINUE;
    }

    public boolean isUnlocked(obj_id player) throws InterruptedException
    {
        int heroicFlags = 0;
        for (String flag : HEROIC_FLAGS)
        {
            instance.isFlaggedForInstance(player, flag);
            {
                heroicFlags++;
            }
        }
        return heroicFlags == HEROIC_FLAGS.length;
    }
}
