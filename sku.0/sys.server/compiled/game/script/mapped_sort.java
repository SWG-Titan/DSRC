package script;

import java.util.*;

/*
@Origin: dsrc.script
@Author:  BubbaJoeX
@Purpose: Provides a generic, type-safe mapping structure that allows sorting and filtering of key-value pairs based on key datatype.
@Requirements: <no requirements>
@Notes: <no notes>
@Created: Saturday, 2/8/2025, at 8:33 PM,
@Copyright © SWG: Titan 2025
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/
public class mapped_sort<K extends Comparable<K>, V>
{
    private final Map<K, V> map;

    // Default constructor
    public mapped_sort()
    {
        this.map = new LinkedHashMap<>();
    }

    // Constructor with existing map
    public mapped_sort(Map<K, V> inputMap)
    {
        this.map = new LinkedHashMap<>(inputMap);
    }

    // Basic map operations
    public V put(K key, V value)
    {
        return map.put(key, value);
    }

    public V get(K key)
    {
        return map.get(key);
    }

    public V remove(K key)
    {
        return map.remove(key);
    }

    public boolean containsKey(K key)
    {
        return map.containsKey(key);
    }

    public boolean containsValue(V value)
    {
        return map.containsValue(value);
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public void clear()
    {
        map.clear();
    }

    public Set<K> keySet()
    {
        return map.keySet();
    }

    public Collection<V> values()
    {
        return map.values();
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        return map.entrySet();
    }

    public Map<K, V> getSortedMap()
    {
        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entries)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Map<K, V> filterByType(Class<?> type)
    {
        Map<K, V> filteredMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet())
        {
            if (type.isInstance(entry.getKey()))
            {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    public V getValueFromSortedMap(K key)
    {
        return getSortedMap().get(key);
    }

    public void dumpAllValuesWithKey()
    {
        for (Map.Entry<K, V> entry : map.entrySet())
        {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }
}