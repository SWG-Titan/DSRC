package script.content.npe2;/*
@Origin: dsrc.script.content.npe2
@Author:  BubbaJoeX
@Purpose: Spawns medical droid, tactical droid or entertainer based on objvar
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 2/22/2025, at 8:01 PM, 
@Copyright © SWG: New Beginnings 2025.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.ai.ai;
import script.library.ai_lib;
import script.library.create;
import script.obj_id;

public class utility_spawners extends script.base_script
{

    public int OnAttach(obj_id self) throws InterruptedException
    {
        doSpawn(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        doSpawn(self);
        return SCRIPT_CONTINUE;
    }

    public void doSpawn(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, "utility_type"))
        {
            setName(self, "BUSTED SPAWNER LOL GET REKT");
            detachScript(self, "content.npe2.utility_spawners");
            return;
        }
        int type = getIntObjVar(self, "utility_type");
        switch (type)
        {
            case 0:
                spawnMedicalDroid(self);
                break;
            case 1:
                spawnTacticalDroid(self);
                break;
            case 2:
                spawnEntertainer(self);
                break;
            case 3:
                spawnAbubb(self);
                break;
            default:
                break;
        }
    }

    public void spawnMedicalDroid(obj_id self) throws InterruptedException
    {
        obj_id healer = create.object("object/mobile/fx_7_droid.iff", getLocation(self));
        attachScript(healer, "developer.bubbajoe.doctor_droid");
        setName(healer, "FX-7 Medical Assistant Droid");
        setYaw(healer, getYaw(self));
        setInvulnerable(healer, true);
    }

    public void spawnTacticalDroid(obj_id self) throws InterruptedException
    {
        obj_id officer = create.object("object/mobile/asn_121.iff", getLocation(self));
        attachScript(officer, "bot.supply_droid");
        setName(officer, "Tactical Supply Probe");
        setInvulnerable(officer, true);
        setYaw(officer, getYaw(self));
    }

    public void spawnEntertainer(obj_id self) throws InterruptedException
    {
        String[] TEMPLATES = {
                "object/mobile/dressed_commoner_naboo_human_female_01.iff",
                "object/mobile/dressed_commoner_naboo_human_female_02.iff",
                "object/mobile/dressed_commoner_naboo_human_female_03.iff",
                "object/mobile/dressed_commoner_naboo_human_female_04.iff",
                "object/mobile/dressed_commoner_naboo_human_female_05.iff"
        };
        obj_id entertainer = createObject(TEMPLATES[rand(0, TEMPLATES.length - 1)], getLocation(self));
        setName(entertainer, "a Master Entertainer");
        setInvulnerable(entertainer, true);
        attachScript(entertainer, "ai.ai");
        ai_lib.setMood(entertainer, "themepark_oola");
        ai.stop(entertainer);
        setYaw(entertainer, getYaw(self));
        attachScript(entertainer, "bot.entertainer");
    }

    public void spawnAbubb(obj_id self) throws InterruptedException
    {
        obj_id crafter = create.object("object/mobile/dressed_commoner_naboo_moncal_male_01.iff", getLocation(self));
        attachScript(crafter, "content.tos_trader_buffer");
        setName(crafter, "Abubb (a Master Artisan)");
        setYaw(crafter, getYaw(self));
        setInvulnerable(crafter, true);
    }
}