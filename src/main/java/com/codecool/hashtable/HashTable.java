package com.codecool.hashtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HashTable<K, V> {

    // Number of all buckets - Do not modify!
    private final int bucketsSize = 16;

    private List<List<Entry<K, V>>> buckets;

    private List<Entry<K, V>> getNextBucket() {
        List<Entry<K, V>> entryList = null;
        if (buckets == null) {
            buckets = new ArrayList<>(16);
            buckets.add(new ArrayList<>());
            entryList = buckets.get(0);
        } else {
            int firstBucketSize = buckets.get(0).size();
            for (int i = 0; i < bucketsSize; i++) {
                try {
                    entryList = buckets.get(i);
                    if (entryList.size() < firstBucketSize) break;
                } catch (IndexOutOfBoundsException ex) {
                    buckets.add(new ArrayList<>());
                    entryList = buckets.get(i);
                    break;
                }
                if (i == bucketsSize - 1) entryList = buckets.get(0);
            }
        }
        return entryList;
    }

    private int getBucketIndexForKey(K key) {
        for (int i = 0; i < bucketsSize; i++) {
            try {
                if (findEntryInBucket(key, getBucketAtIndex(i)) != null) return i;
            } catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return -1;
    }

    private List<Entry<K, V>> getBucketAtIndex(int position) {
        return buckets.get(position);
    }

    private Entry<K, V> findEntryInBucket(K key, List<Entry<K, V>> bucket) {
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) return entry;
        }
        return null;
    }

    public V get(K key) {

        int bucketIndex = getBucketIndexForKey(key);
        if (bucketIndex != -1) {
            return findEntryInBucket(key, getBucketAtIndex(bucketIndex)).value;
        }
        return null;
    }

    public void put(K key, V value) {
        if (buckets == null) {
            getNextBucket().add(new Entry<>(key, value));
        } else {
            int bucketIndex = getBucketIndexForKey(key);
            if (bucketIndex != -1) {
                findEntryInBucket(key, getBucketAtIndex(bucketIndex)).value = value;
                return;
            } else {
                getNextBucket().add(new Entry<>(key, value));
            }
        }
    }

    public V remove(K key) {
        int bucketIndex = getBucketIndexForKey(key);
        if (bucketIndex != -1) {
            List<Entry<K, V>> entryList = getBucketAtIndex(bucketIndex);
            Entry<K, V> entry = findEntryInBucket(key, entryList);
            V value = entry.value;
            entryList.remove(entry);
            return value;
        }
        return null;
    }

    public void clear() {
        for (List<Entry<K, V>> entryList : buckets) {
            entryList.clear();
        }
    }
}

class Entry<K, V> {

    public K key;
    public V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

}
