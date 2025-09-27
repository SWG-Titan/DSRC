package script.developer.bubbajoe;/*
@Origin: dsrc.script.developer.bubbajoe
@Author: BubbaJoeX
@Purpose: Generates heatmaps of various objects for use with external tool.
@Requirements: dsrc.script.developer.bubbajoe.player_developer
@Created: Friday, 9/22/2023, at 10:11 PM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

import java.util.Arrays;
import java.util.List;

public class heatmaps extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public void generateHeatmap(obj_id self, obj_id[] targets, String filename)
    {
        broadcast(self, "Attempting to capture " + targets.length + " objects.");
        String[] simpleArray = new String[targets.length];
        for (int i = 0; i < targets.length; i++)
        {
            simpleArray[i] = getLocation(targets[i]).x + "," + getLocation(targets[i]).z;
        }
        List<String> list = Arrays.asList(simpleArray);
        String[] unique = list.stream().distinct().toArray(String[]::new);
        String[] counts = new String[unique.length];
        saveTextOnClient(self, filename, Arrays.toString(simpleArray));
        broadcast(self, "Heatmap captured.");
    }
}
