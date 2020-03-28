package ru.itmo.java;

import java.util.Map;

import static java.lang.Math.abs;

public class HashTable {

    private class Entry {

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        Object key;
        Object value;
    }

    private Entry[] table;
    private double loadFactor;
    private int treshold;
    private int size;
    private int cap;

    public HashTable(int capacity) {
        this(capacity, 0.5f);
    }

    public HashTable(int capacity, float loadF) {
        table = new Entry[capacity];
        loadFactor = loadF;
        cap = capacity;
        size = 0;
        treshold = (int) (cap * loadFactor);
    }

    private boolean isBetween(int leftRange, int rightRange, int n) {
        return (leftRange > rightRange)
            ? (leftRange < n || n <= rightRange)
            : (leftRange < n && n <= rightRange);
    }

    private int hash(Object key) {
        return abs(key.hashCode() % cap);
    }

    private int removeByIndex(int index) {
        int pretenderForSubstitution = abs((index + 1) % cap);
        if (table[pretenderForSubstitution] == null) {
            table[index] = table[pretenderForSubstitution];
            return pretenderForSubstitution;
        }
        while (isBetween(index, pretenderForSubstitution, hash(table[pretenderForSubstitution].key))) {
            pretenderForSubstitution = abs((pretenderForSubstitution + 1) % cap);
            if (table[pretenderForSubstitution] == null) {
                break;
            }
        }
        table[index] = table[pretenderForSubstitution];
        return pretenderForSubstitution;
    }

    public Object put(Object key, Object value) {
        Entry newElement = new Entry(key, value);
        int i = hash(key);
        Object ans;
        while (table[i] != null && !table[i].key.equals(key)) {
            i = abs((i + 1) % cap);
        }

        if (table[i] == null) {
            size++;
            ans = null;
        } else {
            ans = table[i].value;
        }

        table[i] = newElement;

        if (size == treshold) {
            Entry[] bufferTable = table;
            table = new Entry[2 * cap];
            cap = 2 * cap;
            treshold = (int) (cap * loadFactor);
            size = 0;
            for (Entry entry : bufferTable) {
                if (entry != null) {
                    put(entry.key, entry.value);
                }
            }
        }
        return ans;
    }

    public Object get(Object key) {
        int i = hash(key);
        while (table[i] != null && !table[i].key.equals(key)) {
            i = abs((i + 1) % cap);
        }
        return (table[i] == null) ? null : table[i].value;
    }

    public Object remove(Object key) {
        int i = hash(key.hashCode());
        while (table[i] != null) {
            if (table[i].key.equals(key)) {
                break;
            }
            i = abs((i + 1) % cap);
        }
        if (table[i] == null) {
            return null;
        }
        Object ans = table[i].value;
        do {
            i = removeByIndex(i);
        } while (table[i] != null);
        size--;
        return ans;
    }

    public int size() {
        return size;
    }

}
