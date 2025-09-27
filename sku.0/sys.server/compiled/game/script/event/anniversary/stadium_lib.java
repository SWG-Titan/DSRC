package script.event.anniversary;/*
@Origin: dsrc.script.event.anniversary
@Author:  BubbaJoeX
@Purpose: Consts
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Wednesday, 8/14/2024, at 9:27 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;
import script.location;
import script.menu_info_types;
import script.menu_info_data;
import script.menu_info;
import script.dictionary;

public class stadium_lib extends script.base_script
{
    public static float TOOL_RANGE = 256.0f;
    public static final String WIN_MSG = "Congratulations! You have won the event!";
    public static final String LOSE_MSG = "You have lost the event.";
    public static final String MOB_MARKER_KEY_VAR = "anniversary.stadium.mob";
    public static final String MOB_MARKER_POINT_VAR = "anniversary.stadium.pointType.";
    public static final String MOB_MARKER_PREFIX = "anniversary.stadium.mob.";
    public static final String MOB_SPAWNER_PLACEHOLDER = "anniversary.stadium.mob_temp";
    public static final String TRASH_MOB_MARKER_VAR = "trashMob";
    public static final String TRASH_MOB_MARKER_TARGET_VAR = "trashMobTarget";
    public static final String ELITE_MOB_MARKER_VAR = "eliteMob";
    public static final String ELITE_MOB_MARKER_TARGET_VAR = "eliteMobTarget";
    public static final String BOSS_MOB_MARKER_VAR = "bossMob";
    public static final String BOSS_MOB_MARKER_TARGET_VAR = "bossMobTarget";
    public static boolean USE_AI = true;
    public static final String GIFT_VAR = "anni_24";
    public static final String GIFT_TEMPLATE = "object/tangible/item/target_dummy_publish_giftbox.iff";
    public static final String GIFT_SCRIPT = "event.anniversary.gift_object";
}
