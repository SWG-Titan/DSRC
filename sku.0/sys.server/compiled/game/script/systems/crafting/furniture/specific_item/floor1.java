package script.systems.crafting.furniture.specific_item;

/*
 * Copyright © SWG:Resurgence 2023.
 *
 * Unauthorized usage, viewing or sharing of this file is prohibited.
 */

import script.draft_schematic;
import script.library.craftinglib;
import script.obj_id;
import script.resource_weight;

public class floor1 extends script.systems.crafting.furniture.crafting_base_furniture
{
    public static final String VERSION = "v0.00.00";
    public static final String[] REQUIRED_SKILLS =
            {
                    "crafting_architect_novice"
            };
    public static final String[] ASSEMBLY_SKILL_MODS =
            {
                    "structure_assembly"
            };
    public static final String[] EXPERIMENT_SKILL_MODS =
            {
                    "structure_experimentation"
            };
    public static final String[] APPEARANCES =
            {
                    "object/tangible/tarkin_custom/decorative/shared_ljt_dojofloor.iff",
                    "object/tangible/tarkin_custom/decorative/shared_ljt_dojofloor02.iff",
                    "object/tangible/tarkin_custom/decorative/shared_intr_hotel_ceiling_a1.iff",
                    "object/tangible/tarkin_custom/decorative/shared_djt_floorstone_dirt_e.iff",
                    "object/tangible/tarkin_custom/decorative/shared_intr_medicalfloor_fallback.iff",
                    "object/tangible/tarkin_custom/decorative/shared_newbie_ceiling_c.iff",
                    "object/tangible/tarkin_custom/decorative/shared_newbie_ceiling_c_black.iff",
                    "object/tangible/tarkin_custom/decorative/shared_thm_yavn_masstemple_grandhallfloor.iff",
                    "object/tangible/tarkin_custom/decorative/shared_thed_marble_graylightslate.iff"
            };
    public static final resource_weight[] OBJ_ASSEMBLY_ATTRIBUTE_RESOURCES =
            {
                    new resource_weight("hitPoints", new resource_weight.weight[]
                            {
                                    new resource_weight.weight(craftinglib.RESOURCE_DECAY_RESIST, 1),
                                    new resource_weight.weight(craftinglib.RESOURCE_QUALITY, 1)
                            })
            };
    public static final resource_weight[] OBJ_MAX_ATTRIBUTE_RESOURCES =
            {
                    new resource_weight("hitPoints", new resource_weight.weight[]
                            {
                                    new resource_weight.weight(craftinglib.RESOURCE_DECAY_RESIST, 1),
                                    new resource_weight.weight(craftinglib.RESOURCE_QUALITY, 1)
                            })
            };

    public floor1()
    {
    }

    public String[] getRequiredSkills() throws InterruptedException
    {
        return REQUIRED_SKILLS;
    }

    public String[] getAssemblySkillMods() throws InterruptedException
    {
        return ASSEMBLY_SKILL_MODS;
    }

    public String[] getExperimentSkillMods() throws InterruptedException
    {
        return EXPERIMENT_SKILL_MODS;
    }

    public resource_weight[] getResourceMaxResourceWeights() throws InterruptedException
    {
        return OBJ_MAX_ATTRIBUTE_RESOURCES;
    }

    public resource_weight[] getAssemblyResourceWeights() throws InterruptedException
    {
        return OBJ_ASSEMBLY_ATTRIBUTE_RESOURCES;
    }

    public String[] getAppearances(obj_id player, draft_schematic.slot[] slots) throws InterruptedException
    {
        return APPEARANCES;
    }
}
