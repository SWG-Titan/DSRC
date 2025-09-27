package script.item.content.rewards;/*
@Origin: script.item.content.rewards.ace_grant
@Author: BubbaJoeX
@Purpose: Grants player an ace pilot. (Grants random sub-classes of the master pilot skill based on faction)
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.*/

import script.*;
import script.library.skill;
import script.library.sui;
import script.library.utils;

public class ace_grant extends base_script
{
    public static final String[] FACTIONS =
            {
                    "Rebel",
                    "Imperial",
                    "Neutral"
            };

    public ace_grant()
    {
    }

    public int OnInitialize(obj_id self)
    {
        setName(self, "Knowledge of the Wingman");
        setDescriptionStringId(self, new string_id("This item will grant the skills for Master Pilot based on your factional choice. \n\\#DD1234Note: You need to declare your faction before using this item.\\#."));
        return SCRIPT_CONTINUE;
    }

    public int OnAttach(obj_id self)
    {
        setName(self, "Knowledge of the Wingman");
        setDescriptionStringId(self, new string_id("This item will grant the skills for Master Pilot based on your factional choice. \n\\#DD1234Note: You need to declare your faction before using this item.\\#."));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (utils.isNestedWithinAPlayer(self))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, new string_id("Seek Piloting Knowledge"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (utils.isNestedWithinAPlayer(self))
            {
                int pid = sui.listbox(self, player, "Choose which Master Pilot faction you wish to learn. \n\\#DD1234Note: You must declare a faction or you will be assigned neutral.\\#.", sui.OK_CANCEL, "Knowledge of the Wingman", FACTIONS, "handleFactionSelect", true);
                setSUIProperty(pid, sui.LISTBOX_BTN_OK, sui.PROP_TEXT, "Select");
                sui.showSUIPage(pid);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleFactionSelect(obj_id self, dictionary params) throws InterruptedException
    {
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String faction = FACTIONS[idx];
        if (faction.equals("Rebel"))
        {
            grantRebelPilot(self, sui.getPlayerId(params));
            LOG("ethereal", "[Ace Grant]: " + utils.getStringName(sui.getPlayerId(params)) + " has learned all the skills of a Rebel Navy Pilot.");
        }
        else if (faction.equals("Imperial"))
        {
            grantImperialPilot(self, sui.getPlayerId(params));
            LOG("ethereal", "[Ace Grant]: " + utils.getStringName(sui.getPlayerId(params)) + " has learned all the skills of an Imperial Navy Pilot.");
        }
        else if (faction.equals("Neutral"))
        {
            grantNeutralPilot(self, sui.getPlayerId(params));
            LOG("ethereal", "[Ace Grant]: " + utils.getStringName(sui.getPlayerId(params)) + " has learned all the skills of a Freelance/Neutral Pilot.");
        }
        return SCRIPT_CONTINUE;
    }

    public void grantImperialPilot(obj_id self, obj_id player)
    {
        skill.grantSkill(player, "pilot_imperial_navy_novice");
        skill.grantSkill(player, "pilot_imperial_navy_starships_01");
        skill.grantSkill(player, "pilot_imperial_navy_starships_02");
        skill.grantSkill(player, "pilot_imperial_navy_starships_03");
        skill.grantSkill(player, "pilot_imperial_navy_starships_04");
        skill.grantSkill(player, "pilot_imperial_navy_weapons_01");
        skill.grantSkill(player, "pilot_imperial_navy_weapons_02");
        skill.grantSkill(player, "pilot_imperial_navy_weapons_03");
        skill.grantSkill(player, "pilot_imperial_navy_weapons_04");
        skill.grantSkill(player, "pilot_imperial_navy_procedures_01");
        skill.grantSkill(player, "pilot_imperial_navy_procedures_02");
        skill.grantSkill(player, "pilot_imperial_navy_procedures_03");
        skill.grantSkill(player, "pilot_imperial_navy_procedures_04");
        skill.grantSkill(player, "pilot_imperial_navy_droid_01");
        skill.grantSkill(player, "pilot_imperial_navy_droid_02");
        skill.grantSkill(player, "pilot_imperial_navy_droid_03");
        skill.grantSkill(player, "pilot_imperial_navy_droid_04");
        skill.grantSkill(player, "pilot_imperial_navy_master");
        broadcast(player, "You have learned all the skills of an Imperial Navy Pilot.");
        destroyObject(self);
    }

    public void grantRebelPilot(obj_id self, obj_id player)
    {
        skill.grantSkill(player, "pilot_rebel_navy_novice");
        skill.grantSkill(player, "pilot_rebel_navy_starships_01");
        skill.grantSkill(player, "pilot_rebel_navy_starships_02");
        skill.grantSkill(player, "pilot_rebel_navy_starships_03");
        skill.grantSkill(player, "pilot_rebel_navy_starships_04");
        skill.grantSkill(player, "pilot_rebel_navy_weapons_01");
        skill.grantSkill(player, "pilot_rebel_navy_weapons_02");
        skill.grantSkill(player, "pilot_rebel_navy_weapons_03");
        skill.grantSkill(player, "pilot_rebel_navy_weapons_04");
        skill.grantSkill(player, "pilot_rebel_navy_procedures_01");
        skill.grantSkill(player, "pilot_rebel_navy_procedures_02");
        skill.grantSkill(player, "pilot_rebel_navy_procedures_03");
        skill.grantSkill(player, "pilot_rebel_navy_procedures_04");
        skill.grantSkill(player, "pilot_rebel_navy_droid_01");
        skill.grantSkill(player, "pilot_rebel_navy_droid_02");
        skill.grantSkill(player, "pilot_rebel_navy_droid_03");
        skill.grantSkill(player, "pilot_rebel_navy_droid_04");
        skill.grantSkill(player, "pilot_rebel_navy_master");
        broadcast(player, "You have learned all the skills of a Rebel Navy Pilot.");
        destroyObject(self);
    }

    public void grantNeutralPilot(obj_id self, obj_id player)
    {
        skill.grantSkill(player, "pilot_neutral_novice");
        skill.grantSkill(player, "pilot_neutral_starships_01");
        skill.grantSkill(player, "pilot_neutral_starships_02");
        skill.grantSkill(player, "pilot_neutral_starships_03");
        skill.grantSkill(player, "pilot_neutral_starships_04");
        skill.grantSkill(player, "pilot_neutral_weapons_01");
        skill.grantSkill(player, "pilot_neutral_weapons_02");
        skill.grantSkill(player, "pilot_neutral_weapons_03");
        skill.grantSkill(player, "pilot_neutral_weapons_04");
        skill.grantSkill(player, "pilot_neutral_procedures_01");
        skill.grantSkill(player, "pilot_neutral_procedures_02");
        skill.grantSkill(player, "pilot_neutral_procedures_03");
        skill.grantSkill(player, "pilot_neutral_procedures_04");
        skill.grantSkill(player, "pilot_neutral_droid_01");
        skill.grantSkill(player, "pilot_neutral_droid_02");
        skill.grantSkill(player, "pilot_neutral_droid_03");
        skill.grantSkill(player, "pilot_neutral_droid_04");
        skill.grantSkill(player, "pilot_neutral_master");
        broadcast(player, "You have learned all the skills of a Freelance/Neutral Pilot.");
        destroyObject(self);
    }
}
