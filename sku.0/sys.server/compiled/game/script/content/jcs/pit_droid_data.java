package script.content.jcs;

import java.util.HashSet;
import java.util.Set;

public class pit_droid_data extends script.base_script
{
    private static final Set<Integer> assignedNumbers = new HashSet<>();
    private static final int MAX_DROIDS = 30;

    public static int getUniqueNumber()
    {
        if (assignedNumbers.size() >= MAX_DROIDS)
        {
            return -1; // Stop after reaching the limit
        }

        int randomNameSuffix;
        do
        {
            randomNameSuffix = rand(1, MAX_DROIDS);
        } while (assignedNumbers.contains(randomNameSuffix));

        assignedNumbers.add(randomNameSuffix);
        return randomNameSuffix;
    }
}