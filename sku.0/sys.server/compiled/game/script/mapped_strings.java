package script;

import java.util.ArrayList;
import java.util.List;

/*
@Origin: dsrc.script
@Author:  BubbaJoeX
@Purpose: <no purpose>
@Requirements: Map Strings
@Notes: <no notes>
@Created: Tuesday, 11/12/2024, at 11:40 PM,
@Copyright © SWG: Titan 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/
public class mapped_strings
{
    private final List<String> primaryKey = new ArrayList<>();
    private final List<String> secondaryKey = new ArrayList<>();
    private final List<String> tertiaryKey = new ArrayList<>();

    public void addTriple(String str, String key1, String key2)
    {
        primaryKey.add(str);
        secondaryKey.add(key1);
        tertiaryKey.add(key2);
    }

    public void addDouble(String str, String key)
    {
        primaryKey.add(str);
        secondaryKey.add(key);
        tertiaryKey.add(""); // Placeholder to maintain order
    }

    public void add(String str, String key)
    {
        addDouble(str, key); // Maintain consistency by using addDouble
    }

    public void remove(int index)
    {
        if (index >= 0 && index < primaryKey.size())
        {
            primaryKey.remove(index);
            secondaryKey.remove(index);
            tertiaryKey.remove(index);
        }
    }

    public String getPrimaryKey(int index)
    {
        return (index >= 0 && index < primaryKey.size()) ? primaryKey.get(index) : null;
    }

    public String getSecondaryKey(int index)
    {
        return (index >= 0 && index < secondaryKey.size()) ? secondaryKey.get(index) : null;
    }

    public String getTertiaryKey(int index)
    {
        return (index >= 0 && index < tertiaryKey.size()) ? tertiaryKey.get(index) : null;
    }

    public int size()
    {
        return primaryKey.size();
    }

    public void printAll()
    {
        for (int i = 0; i < primaryKey.size(); i++)
        {
            System.out.println("Index " + i + ": " + primaryKey.get(i) + ", " + secondaryKey.get(i) + ", " + tertiaryKey.get(i));
        }
    }

    public boolean isEmpty()
    {
        return primaryKey.isEmpty();
    }

    public String[] getKeys()
    {
        return primaryKey.toArray(new String[0]);
    }

    public boolean containsKey(String content)
    {
        return primaryKey.contains(content);
    }

    public String getKey(String content)
    {
        int index = primaryKey.indexOf(content);
        return (index != -1) ? secondaryKey.get(index) : null;
    }

    public String getValue(String template, int which)
    {
        int index = primaryKey.indexOf(template);
        if (index != -1)
        {
            switch (which)
            {
                case 1:
                    return secondaryKey.get(index);
                case 2:
                    return tertiaryKey.get(index);
                default:
                    return null;
            }
        }
        return null;
    }

    public void put(String key, String value)
    {
        int index = primaryKey.indexOf(key);
        if (index != -1)
        {
            // Update existing entry
            secondaryKey.set(index, value);
        }
        else
        {
            // Add new entry with empty tertiary
            primaryKey.add(key);
            secondaryKey.add(value);
            tertiaryKey.add("");
        }
    }
}
