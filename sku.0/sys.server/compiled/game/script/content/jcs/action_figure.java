package script.content.jcs;/*
@Origin: dsrc.script.content.jcs.action_figure
@Author:  BubbaJoeX
@Purpose: Applys a low buff based on theme of toy
@Requirements: mapped_strings & 3.1
@Notes: Only one toy buff can be active at a time.
@Created: Monday, 2/24/2025, at 3:06 PM, 
@Copyright © SWG: New Beginnings 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.library.buff;
import script.library.utils;
import script.*;

public class action_figure extends base_script
{
    public static final int USAGE_BEFORE_CLEANING = 5;
    public static final int COOLDOWN_TIME = 3600;
    public static final mapped_strings templateData;

    static
    {
        templateData = new mapped_strings();
        templateData.addTriple("object/tangible/usable/toy_3po.iff", "Action Figure: C-3P0", "action_figure_toy_3po");
        templateData.addTriple("object/tangible/usable/toy_awing.iff", "Action Figure: A-Wing", "action_figure_toy_awing");
        templateData.addTriple("object/tangible/usable/toy_b1.iff", "Action Figure: B-1 Battle Droid", "action_figure_toy_b1");
        templateData.addTriple("object/tangible/usable/toy_boba.iff", "Action Figure: Boba Fett", "action_figure_toy_boba");
        templateData.addTriple("object/tangible/usable/toy_bwing.iff", "Action Figure: B-Wing", "action_figure_toy_bwing");
        templateData.addTriple("object/tangible/usable/toy_chewbacca.iff", "Action Figure: Chewbacca", "action_figure_toy_chewbacca");
        templateData.addTriple("object/tangible/usable/toy_han.iff", "Action Figure: Han", "action_figure_toy_han");
        templateData.addTriple("object/tangible/usable/toy_hk47.iff", "Action Figure: HK-47", "action_figure_toy_hk47");
        templateData.addTriple("object/tangible/usable/toy_ig88.iff", "Action Figure: IG-88", "action_figure_toy_ig88");
        templateData.addTriple("object/tangible/usable/toy_jabba.iff", "Action Figure: Jabba The Hutt", "action_figure_toy_jabba");
        templateData.addTriple("object/tangible/usable/toy_jango.iff", "Action Figure: Jango Fett", "action_figure_toy_jango");
        templateData.addTriple("object/tangible/usable/toy_lambda_shuttle.iff", "Action Figure: Lambda Shuttle", "action_figure_toy_lambda_shuttle");
        templateData.addTriple("object/tangible/usable/toy_lando.iff", "Action Figure: Lando", "action_figure_toy_lando");
        templateData.addTriple("object/tangible/usable/toy_leia.iff", "Action Figure: Leia", "action_figure_toy_leia");
        templateData.addTriple("object/tangible/usable/toy_luke.iff", "Action Figure: Luke", "action_figure_toy_luke");
        templateData.addTriple("object/tangible/usable/toy_maul.iff", "Action Figure: Darth Maul", "action_figure_toy_maul");
        templateData.addTriple("object/tangible/usable/toy_r2.iff", "Action Figure: R2-D2", "action_figure_toy_r2");
        templateData.addTriple("object/tangible/usable/toy_stormtrooper.iff", "Action Figure: Stormtrooper", "action_figure_toy_stormtrooper");
        templateData.addTriple("object/tangible/usable/toy_tie_bomber.iff", "Action Figure: TIE Bomber", "action_figure_toy_tie_bomber");
        templateData.addTriple("object/tangible/usable/toy_tie_fighter.iff", "Action Figure: TIE Fighter", "action_figure_toy_tie_fighter");
        templateData.addTriple("object/tangible/usable/toy_tie_interceptor.iff", "Action Figure: TIE Interceptor", "action_figure_toy_tie_interceptor");
        templateData.addTriple("object/tangible/usable/toy_vader.iff", "Action Figure: Vader", "action_figure_toy_vader");
        templateData.addTriple("object/tangible/usable/toy_vt49.iff", "Action Figure: VT-49", "action_figure_toy_vt49");
        templateData.addTriple("object/tangible/usable/toy_xwing.iff", "Action Figure: X-Wing", "action_figure_toy_xwing");
        templateData.addTriple("object/tangible/usable/toy_ykl37r.iff", "Action Figure: YKL37R", "action_figure_toy_ykl37r");
        templateData.addTriple("object/tangible/usable/toy_yt1300.iff", "Action Figure: YT-1300", "action_figure_toy_yt1300");
        templateData.addTriple("object/tangible/usable/toy_yt2400.iff", "Action Figure: YT-2400", "action_figure_toy_yt2400");
        templateData.addTriple("object/tangible/usable/toy_ywing.iff", "Action Figure: Y-Wing", "action_figure_toy_ywing");
    }

    public int OnAttach(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        sync(self);
        return SCRIPT_CONTINUE;
    }

    public void sync(obj_id self)
    {
        setName(self, getToyName(self));
        setObjVar(self, "noTrade", 1);
        setObjVar(self, "noTradeShared", 1);
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, "lastUsed"))
        {
            int lastUsed = getIntObjVar(self, "lastUsed");
            int nextUseTime = lastUsed + COOLDOWN_TIME; // 1-hour cooldown
            int remainingTime = nextUseTime - getCalendarTime();
            String formattedTime = utils.formatTime(remainingTime);
            names[idx] = utils.packStringId(new string_id("Next use"));
            attribs[idx] = formattedTime;
            idx++;
        }
        names[idx] = utils.packStringId(new string_id("Effect"));
        attribs[idx] = "@ui_buff:" + getToyBuff(self);
        idx++;
        names[idx] = utils.packStringId(new string_id("Cleanliness"));
        attribs[idx] = getIntObjVar(self, "usageCount") < USAGE_BEFORE_CLEANING ? "Sparkly Clean" : "Really Dirty";
        idx++;
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 4.0f, false))
        {
            if (!utils.isNestedWithinAPlayer(self, true))
            {
                LOG("ethereal", "[Action Figure]: Not nested within " + getPlayerFullName(player) + "'s inventory. Returning.");
                return SCRIPT_CONTINUE;
            }
            else
            {
                mi.addRootMenu(menu_info_types.ITEM_USE, string_id.unlocalized("Play with Action Figure"));
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (canFiddleWithToyForBuff(self, player))
            {
                int usageCount = getIntObjVar(self, "usageCount");
                if (usageCount >= USAGE_BEFORE_CLEANING)
                {
                    if (hasDustingCloth(player))
                    {
                        removeDustingCloth(player);
                        setObjVar(self, "usageCount", 0);
                        broadcast(player, "You have cleaned the toy with the Action Figure Dusting Cloth.");
                    }
                    else
                    {
                        broadcast(player, "You need an Action Figure Dusting Cloth to clean the toy before using it again.");
                        return SCRIPT_CONTINUE;
                    }
                }
                String buffName = getToyBuff(self);
                if (!buffName.isEmpty())
                {
                    buff.applyBuff(player, buffName);
                    LOG("ethereal", "[Action Figure]: Applying " + buffName + " to " + getPlayerFullName(player));
                    setObjVar(self, "lastUsed", getCalendarTime());
                    setObjVar(self, "usageCount", usageCount + 1);
                    broadcast(player, "You now feel nostalgic after playing with this action figure.");
                }
                else
                {
                    broadcast(player, "Something went wrong.");
                    LOG("ethereal", "[Action Figure]: No buff set but still tried to use. Returning.");
                }
            }
            else
            {
                broadcast(player, "You must wait before using this toy again.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    private String getToyBuff(obj_id self)
    {
        return getToyAttribute(self);
    }

    private String getToyAttribute(obj_id self)
    {
        String template = getTemplateName(self);
        if (templateData.containsKey(template))
        {
            String value = templateData.getValue(template, 2);
            return value != null ? value : "Unknown Toy";
        }
        return "Unknown Toy";
    }

    private String getToyName(obj_id self)
    {
        String template = getTemplateName(self);
        if (templateData.containsKey(template))
        {
            String value = templateData.getValue(template, 1);
            return value != null ? value : "Unknown Toy";
        }
        return "Unknown Toy";
    }

    private boolean canFiddleWithToyForBuff(obj_id self, obj_id player)
    {
        int lastUsed = getIntObjVar(self, "lastUsed");
        return (getCalendarTime() - lastUsed) >= COOLDOWN_TIME;
    }

    private boolean hasDustingCloth(obj_id player) throws InterruptedException
    {
        return utils.playerHasStaticItemInBankOrInventory(player, "action_figure_cloth");
    }

    private void removeDustingCloth(obj_id player)
    {
        try
        {
            obj_id cloth = utils.getStaticItemInInventory(player, "action_figure_cloth");
            if (isValidId(cloth) && exists(cloth))
            {
                LOG("ethereal", "[Action Figure]: Decrementing " + getPlayerFullName(player) + "'s Action Figure Cloth.");
                decrementCount(cloth);
            }
        } catch (Exception e)
        {
            LOG("ethereal", "[Action Figure]: " + e);
        }
    }
}
