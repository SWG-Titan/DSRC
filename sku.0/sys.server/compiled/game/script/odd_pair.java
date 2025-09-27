package script;

import java.util.Random;

/*
@Origin: dsrc.script
@Author:  BubbaJoeX
@Purpose: Pairing of randomly generated integers with freeze functionality
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Tuesday, 11/12/2024, at 11:33 PM,
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing, or sharing of this file is prohibited.
*/

public class odd_pair
{
    private int value1;
    private int value2;
    private boolean isFrozen;
    private final Random randomGenerator;

    public odd_pair()
    {
        randomGenerator = new Random();
        generateNewValues();
        isFrozen = false;
    }

    // Generates new random values for value1 and value2
    private void generateNewValues()
    {
        if (!isFrozen)
        {
            value1 = randomGenerator.nextInt(100); // Generate random number between 0-99
            value2 = randomGenerator.nextInt(100);
        }
    }

    // Returns the first value
    public int getValue1()
    {
        if (!isFrozen)
        {
            generateNewValues();
        }
        return value1;
    }

    // Returns the second value
    public int getValue2()
    {
        if (!isFrozen)
        {
            generateNewValues();
        }
        return value2;
    }

    // Freezes the current values, stopping further generation
    public void freeze()
    {
        isFrozen = true;
    }

    // Unfreezes the values to allow generation again
    public void unfreeze()
    {
        isFrozen = false;
    }

    public int first()
    {
        return value1;
    }

    public int second()
    {
        return value2;
    }

    @Override
    public String toString()
    {
        return "OddPair{" + "value1=" + value1 + ", value2=" + value2 + ", isFrozen=" + isFrozen + '}';
    }
}
