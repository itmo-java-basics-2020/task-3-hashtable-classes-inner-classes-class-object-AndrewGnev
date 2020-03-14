package ru.itmo.java;

import java.util.Map;

import static java.lang.Math.abs;

public class HashTable {

    private class Entry {

        public Entry(Object k, Object v) {
            key = k;
            value = v;
            deleted = false;
        }

        Object key;
        Object value;
        boolean deleted;
    }

    private Entry[] table;
    private double loadFactor;
    private int treshold;
    private int size;
    private int cap;

    public HashTable(int capacity) {
        table = new Entry[capacity];
        loadFactor = 0.5;
        cap = capacity;
        size = 0;
        treshold = (int) (cap * loadFactor);
    }

    public HashTable(int capacity, float loadF) {
        table = new Entry[capacity];
        loadFactor = loadF;
        cap = capacity;
        size = 0;
        treshold = (int) (cap * loadFactor);
    }

    private int FindFreePlace(Entry obj, Entry[] table) {
        if (obj == null || obj.key == null) return -1;
        int hash = abs(obj.key.hashCode() % cap);
        int idx = -1;
        for (int i = hash; i < cap + hash; ++i) {
            if (table[i % cap] == null || table[i % cap].deleted || obj.key.equals(table[i % cap].key)) {
                if (idx == -1) {
                    idx = i % cap;
                }
                if (table[i % cap] != null && obj.key.equals(table[i % cap].key)) {
                    idx = i % cap;
                    break;
                }
            }
        }
        return idx;
    }

    Object put(Object key, Object value) {
        Entry newElement = new Entry(key, value);

        int idx = FindFreePlace(newElement, table);
        if (idx == -1) return null;
        Entry ans = table[idx];

        if (table[idx] == null || table[idx].deleted) {
            size++;
            if (size >= treshold) {
                Entry[] buffTable = new Entry[cap * 2];
                for (int i = 0; i < cap; ++i) {
                    if (table[i] != null) {
                        int index = FindFreePlace(table[i], buffTable);
                        buffTable[index] = table[i];
                    }
                }
                table = buffTable;
                cap = table.length;
                treshold = (int) (cap * loadFactor);
            }
        }

        table[idx] = newElement;
        return (ans == null || ans.deleted) ? null : ans.value;
    }

    Object get(Object key) {
        int hash = abs(key.hashCode() % cap);
        for (int i = hash; i < cap + hash; ++i) {
            if (table[i % cap] != null && table[i % cap].key.equals(key) && !table[i % cap].deleted) {
                return table[i % cap].value;
            }
        }
        return null;
    }

    Object remove(Object key) {
        int hash = abs(key.hashCode() % cap);
        for (int i = hash; i < cap + hash; ++i) {
            if (table[i % cap] != null && table[i % cap].key.equals(key) && !table[i % cap].deleted) {
                table[i % cap].deleted = true;
                size--;
                return table[i % cap].value;
            }
        }
        return null;
    }

    int size() {
        return size;
    }

}
