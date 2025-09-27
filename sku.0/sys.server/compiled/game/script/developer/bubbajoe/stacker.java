package script.developer.bubbajoe;

import script.location;
import script.obj_id;

public class stacker extends script.base_script
{
    public int OnAttach(obj_id self)
    {
        int howMany = getIntObjVar(self, "stacker.amount");
        float howFar = getIntObjVar(self, "stacker.distance");
        String whichDirection = getStringObjVar(self, "stacker.direction");
        if (howMany == 0)
        {
            howMany = 1;
        }
        stack(self, howMany, howFar, whichDirection);
        return SCRIPT_CONTINUE;
    }

    public void stack(obj_id self, int count, float howFar, String direction)
    {
        String sourceObject = getTemplateName(self);
        location originalObject = getLocation(self);
        location newLocation = new location();
        newLocation.area = originalObject.area;
        newLocation.x = originalObject.x;
        newLocation.y = originalObject.y;
        newLocation.cell = originalObject.cell;

        // Determine the direction of stacking
        if (direction.equals("north"))
        {
            newLocation.y = originalObject.y + howFar;
        }
        else if (direction.equals("south"))
        {
            newLocation.y = originalObject.y - howFar;
        }
        else if (direction.equals("east"))
        {
            newLocation.x = originalObject.x + howFar;
        }
        else if (direction.equals("west"))
        {
            newLocation.x = originalObject.x - howFar;
        }

        // Stack the objects vertically along Z-axis for height
        for (int i = 0; i < count; i++)
        {
            createObject(sourceObject, newLocation);
            // Update the Z value for the next stacked object
            newLocation.z = originalObject.z + (i + 1) * howFar;
        }
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }
}
