package script;

import java.util.ArrayList;
import java.util.List;

public class linked_array
{
    private final List<obj_id> objIds = new ArrayList<>();
    private final List<Float> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();
    private final List<Integer> integers = new ArrayList<>();
    private final List<Boolean> booleans = new ArrayList<>();
    private final List<location> locations = new ArrayList<>();
    private final List<obj_var> obj_vars = new ArrayList<>();
    private final List<string_id> string_ids = new ArrayList<>();

    // Add methods for each datatype, allowing nullable values
    public void addObjId(obj_id obj)
    {
        objIds.add(obj);
    }

    public void addFloat(Float f)
    {
        floats.add(f);
    }

    public void addString(String str)
    {
        strings.add(str);
    }

    public void addInteger(Integer amount)
    {
        integers.add(amount);
    }

    public void addBoolean(Boolean flag)
    {
        booleans.add(flag);
    }

    // New method to add both String and <datatype> in one swoop
    public void addStringCombo(String str, Integer amount)
    {
        strings.add(str);
        integers.add(amount);
    }

    public void addStringCombo(String str, Float amount)
    {
        strings.add(str);
        floats.add(amount);
    }

    public void addStringCombo(String str, obj_id amount)
    {
        strings.add(str);
        objIds.add(amount);
    }

    public void addStringCombo(String str, Boolean flag)
    {
        strings.add(str);
        booleans.add(flag);
    }


    // General add method
    public void add(obj_id obj, float f, String str, int amount, boolean flag)
    {
        objIds.add(obj);
        floats.add(f);
        strings.add(str);
        integers.add(amount);
        booleans.add(flag);
    }

    public obj_id getObjId(int index)
    {
        return objIds.get(index);
    }

    public Float getFloat(int index)
    {
        return floats.get(index);
    }

    public String getString(int index)
    {
        return strings.get(index);
    }

    public Integer getInteger(int index)
    {
        return integers.get(index);
    }

    public Boolean getBoolean(int index)
    {
        return booleans.get(index);
    }

    public void remove(int index)
    {
        objIds.remove(index);
        floats.remove(index);
        strings.remove(index);
        integers.remove(index);
        booleans.remove(index);
    }

    public int getSize(Object datatype)
    {
        return 1;
    }

    public int getTotalSize()
    {
        return objIds.size() + floats.size() + strings.size() + integers.size() + booleans.size();
    }

    public void printAll()
    {
        for (int i = 0; i < strings.size(); i++)
        {
            System.out.println("Index " + i + ": " + objIds.get(i) + ", " + floats.get(i) + ", " + strings.get(i) + ", " + integers.get(i) + ", " + booleans.get(i));
        }
    }
}
