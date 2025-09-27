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

public class panel7 extends script.systems.crafting.furniture.crafting_base_furniture
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
                    "object/tangible/borrie/wall/shared_asteroid_hutt_wall04.iff",
                    "object/tangible/borrie/wall/shared_asteroid_techwall03.iff",
                    "object/tangible/borrie/wall/shared_imperial_gunboat_int_wall_a_phong.iff",
                    "object/tangible/borrie/wall/shared_impl_int_wall_b_blank_grunge.iff",
                    "object/tangible/borrie/wall/shared_impl_int_wall_b_grunge.iff",
                    "object/tangible/borrie/wall/shared_impl_int_wall_d_grunge_phong.iff",
                    "object/tangible/borrie/wall/shared_impl_int_wall_e_grunge_phong.iff",
                    "object/tangible/borrie/wall/shared_impl_int_wall_f_grunge_phong.iff"
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

    public panel7()
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
