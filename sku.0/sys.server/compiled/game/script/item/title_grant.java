package script.item;/*
@Origin: dsrc.script.item
@Author:  BubbaJoeX
@Purpose: Grants new titles. Not all titles are valid, it's based on what objvars are set on this object.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 5/22/2024, at 8:18 PM, 
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.sui;

public class title_grant extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public String[] TITLE_DONATOR_NAMES =
            {
                    "Galactic Philanthropist",
                    "HoloNet Influencer",
                    "Black Sun Benefactor",
                    "Credit Pincher"
            };

    public String[] TITLES_DONATOR_SKILLS =
            {
                    "title_donator_1",
                    "title_donator_2",
                    "title_donator_3",
                    "title_donator_4"
            };

    public String[] TITLE_EVENT_NAMES =
            {
                    "Community Veteran",
                    "Cantina Crawler"
            };

    public String[] TITLES_EVENT_SKILLS =
            {
                    "title_event_1",
                    "title_event_2"
            };

    public String[] TITLE_WORLDBOSS_NAMES =
            {
                    "The Crusader's Bane",
                    "Bombad General",
                    "Slayer of the Ancients",
                    "Vanquisher of the Peko-Peko",
                    "Apprentice of the Force",
                    "Droid Ripper"
            };

    public String[] TITLES_WORLDBOSS_SKILLS =
            {
                    "title_world_boss_crusader",
                    "title_world_boss_donkdonk",
                    "title_world_boss_krayt",
                    "title_world_boss_peko",
                    "title_world_boss_rolii",
                    "title_world_boss_ig24"
            };

    public String[] TITLE_IMP_NAMES =
            {
                    "Imperial Security Bureau",
                    "Vader's Fist"
            };

    public String[] TITLES_IMP_SKILLS =
            {
                    "title_imp_1",
                    "title_imp_2"
            };

    public String[] TITLE_REB_NAMES =
            {
                    "Voice of the Rebellion",
                    "Alliance Unifier"
            };

    public String[] TITLES_REB_SKILLS =
            {
                    "title_reb_1",
                    "title_reb_2"
            };
    public String[] TITLES_NEUTRAL_SKILLS =
            {
                    "title_neutral_1",
                    "title_neutral_2"
            };

    public String[] TITLE_NEUTRAL_NAMES =
            {
                    "Spacer",
                    "Stargazer"
            };

    public String TITLE_GJPUD = "title_gjpud_1 ";
    public String TITLE_GJPUD_NAME = "Scrap Collector";
    public String TITLE_TOS_NAME = "title_tos_1";
    public String TITLE_TOS = "Airlock Surfer";
    public String SUI_TITLE = "Title Redemption";
    public String SUI_PROMPT = "Please select a title to redeem.";


    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
    {
        if (isIdValid(player) && isIdValid(self))
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("Redeem Title"));
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (isIdValid(player) && isIdValid(self))
        {
            String mode = getStringObjVar(self, "title_mode");
            if (mode != null)
            {
                switch (mode)
                {
                    case "donator":
                        showDonationGrantSui(self, player);
                        break;
                    case "event":
                        showEventGrantSui(self, player);
                        break;
                    case "worldboss":
                        showWorldBossGrantSui(self, player);
                        break;
                    case "imp":
                        showImpGrantSui(self, player);
                        break;
                    case "reb":
                        showRebGrantSui(self, player);
                        break;
                    case "neutral":
                        showNeutralGrantSui(self, player);
                        break;
                    case "gjpud":
                        showGJPUDGrantSui(self, player);
                        break;
                    case "tos":
                        showTOSGrantSui(self, player);
                        break;
                    default:
                        broadcast(player, "This title redemption object is not configured properly. Please contact a GM.");
                        break;
                }
            }

        }
        return SCRIPT_CONTINUE;
    }

    public int showDonationGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, TITLE_DONATOR_NAMES, "handleDonationGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showEventGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, TITLE_EVENT_NAMES, "handleEventGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showWorldBossGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, TITLE_WORLDBOSS_NAMES, "handleWorldBossGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showImpGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, TITLE_IMP_NAMES, "handleImpGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showRebGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, TITLE_REB_NAMES, "handleRebGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showNeutralGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, TITLE_NEUTRAL_NAMES, "handleNeutralGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showGJPUDGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, new String[]{TITLE_GJPUD_NAME}, "handleGJPUDGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int showTOSGrantSui(obj_id self, obj_id player) throws InterruptedException
    {
        sui.listbox(self, player, SUI_TITLE, sui.OK_CANCEL, SUI_PROMPT, new String[]{TITLE_TOS}, "handleTOSGrantSui");
        return SCRIPT_CONTINUE;
    }

    public int handleDonationGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLES_DONATOR_SKILLS[idx];
                String titleName = TITLE_DONATOR_NAMES[idx];
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleEventGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLES_EVENT_SKILLS[idx];
                String titleName = TITLE_EVENT_NAMES[idx];
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleWorldBossGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLES_WORLDBOSS_SKILLS[idx];
                String titleName = TITLE_WORLDBOSS_NAMES[idx];
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleImpGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLES_IMP_SKILLS[idx];
                String titleName = TITLE_IMP_NAMES[idx];
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleRebGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLES_REB_SKILLS[idx];
                String titleName = TITLE_REB_NAMES[idx];
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleNeutralGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLES_NEUTRAL_SKILLS[idx];
                String titleName = TITLE_NEUTRAL_NAMES[idx];
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleGJPUDGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLE_GJPUD;
                String titleName = TITLE_GJPUD_NAME;
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int handleTOSGrantSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int idx = sui.getListboxSelectedRow(params);
        int button = sui.getIntButtonPressed(params);
        if (button == sui.BP_CANCEL)
        {
            broadcast(player, "You have decided not to redeem a title.");
            return SCRIPT_CONTINUE;
        }
        else
        {
            if (idx > -1)
            {
                String title = TITLE_TOS;
                String titleName = TITLE_TOS_NAME;
                notify(player, titleName);
                giveTitle(self, player, title);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int notify(obj_id player, String title)
    {
        broadcast(player, "Congratulations! You have redeemed the title: " + title);
        return SCRIPT_CONTINUE;
    }

    public int giveTitle(obj_id self, obj_id player, String title) throws InterruptedException
    {
        if (title != null && !title.equals(""))
        {
            if (!hasSkill(player, title))
            {
                grantSkill(player, title);
                destroyObject(self);
            }
            else
            {
                broadcast(player, "You already have this title.");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
