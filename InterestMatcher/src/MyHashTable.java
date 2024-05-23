public class MyHashTable<K, V> {

    static class Entry<K, V> {
        K key;
        V value;

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    static int INITIAL_CAPACITY = 10000;
    Entry<K, V>[] table;
    private int size;

    public static void setINITIAL_CAPACITY(int INITIAL_CAPACITY) {
        MyHashTable.INITIAL_CAPACITY = INITIAL_CAPACITY;
    }
    public Entry<K, V>[] getTable() {
        return table;
    }
    public int getSize() {
        return size;
    }

    public MyHashTable() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(K key, V value) {
        int index = hash(key);

        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            size++;
        } else {
            int startIndex = index;
            do {
                index = (index + 1) % table.length;
                if (table[index] == null || table[index].key.equals(key)) {
                    table[index] = new Entry<>(key, value);
                    size++;
                    return;
                }
            } while (index != startIndex);
            throw new IllegalStateException("Hashtable is full or hash function is not suitable.");
        }
    }

    public V get(K key) {
        int index = hash(key);

        if (table[index] != null && table[index].key.equals(key)) {
            return table[index].value;
        } else {
            int startIndex = index;
            do {
                index = (index + 1) % table.length;
                if (table[index] != null && table[index].key.equals(key)) {
                    return table[index].value;
                }
            } while (index != startIndex);
            return null;
        }
    }

    public void remove(K key) {
        int index = hash(key);

        if (table[index] != null && table[index].key.equals(key)) {
            table[index] = null;
            size--;
        } else {
            int startIndex = index;
            do {
                index = (index + 1) % table.length;
                if (table[index] != null && table[index].key.equals(key)) {
                    table[index] = null;
                    size--;
                    return;
                }
            } while (index != startIndex);
        }
    }
    
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }
   
    public Entry<K, V> findMaxEntry() {
        Entry<K, V> maxEntry = null;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Entry<K, V> currentEntry = table[i];
                if (maxEntry == null || compare(currentEntry.getValue(), maxEntry.getValue()) > 0) {
                    maxEntry = currentEntry;
                }
            }
        }
        return maxEntry;
    }

    private int compare(V v1, V v2) {
        if (v1 instanceof Comparable && v2 instanceof Comparable) {
            return ((Comparable<V>) v1).compareTo(v2);
        } else {
            throw new IllegalArgumentException("Values are not comparable.");
        }
    }
    
    public boolean containsKey(K key) {
        int index = hash(key);

        if (table[index] != null && table[index].key.equals(key)) {
            return true;
        } else {
            int startIndex = index;
            do {
                index = (index + 1) % table.length;
                if (table[index] != null && table[index].key.equals(key)) {
                    return true;
                }
            } while (index != startIndex);
            return false;
        }
    }
    
   public K[] getVertices() {
    int count = 0;
    for (Entry<K, V> entry : table) {
        if (entry != null) {
            count++;
        }
    }

    Object[] vertices = new Object[count];
    int index = 0;
    for (Entry<K, V> entry : table) {
        if (entry != null) {
            vertices[index++] = entry.key;
        }
    }
    return (K[]) vertices;
} 
}

