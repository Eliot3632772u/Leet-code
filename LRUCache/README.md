# LRU Cache — Solution Explained

## What Is an LRU Cache?

An **LRU (Least Recently Used) Cache** is a fixed-capacity data structure that evicts the **least recently used** entry when it is full and a new item needs to be inserted. It supports two operations:

- `get(key)` → return the value if it exists, or `-1` if it doesn't
- `put(key, value)` → insert or update a key-value pair; evict the LRU entry if at capacity

---

## The Core Insight: `LinkedHashMap`

The entire solution hinges on one data structure: `LinkedHashMap<Integer, Integer>`.

`LinkedHashMap` is a Java map that **preserves insertion order** — it maintains a doubly-linked list internally that tracks the order in which entries were inserted. This means:

- The **first** entry in iteration order = the **oldest** (least recently used) entry
- The **last** entry = the most recently used entry

By always moving accessed or updated entries to the **end** of the map, we ensure the front always holds the LRU candidate for eviction.

---

## Class Fields

```java
LinkedHashMap<Integer, Integer> map;  // stores key→value with insertion order
final int capacity;                   // maximum number of entries allowed
int size;                             // current number of entries
```

---

## Constructor

```java
public LRUCache(int capacity) {
    this.map = new LinkedHashMap<>();
    this.capacity = capacity;
    this.size = 0;
}
```

Initializes an empty `LinkedHashMap`, stores the capacity limit, and sets size to `0`.

---

## `get(int key)` — Step by Step

```java
public int get(int key) {
    if (!map.containsKey(key)) return -1;  // Step 1
    int value = map.remove(key);           // Step 2
    map.put(key, value);                   // Step 3
    return value;                          // Step 4
}
```

**Step 1 — Cache miss check:**
If the key doesn't exist in the map, return `-1` immediately.

**Step 2 — Remove the entry:**
Remove the key from its current position in the map. This extracts the value while also erasing the entry from its current position in the insertion-order list.

**Step 3 — Re-insert at the end:**
`map.put(key, value)` adds the entry back. Because `LinkedHashMap` preserves insertion order, the re-inserted entry goes to the **tail** — marking it as the most recently used.

**Step 4 — Return the value.**

**Why remove + re-insert?** `LinkedHashMap` doesn't provide a built-in "move to end" operation. The remove-then-put pattern is the idiomatic way to update an entry's position in the ordering.

---

## `put(int key, int value)` — Step by Step

```java
public void put(int key, int value) {

    // Case 1: Key already exists — update it
    if (map.containsKey(key)) {
        map.remove(key);
        map.put(key, value);
        return;
    }

    // Case 2: Cache is full — evict LRU entry
    if (size == capacity) {
        var it = map.entrySet().iterator();
        it.next();
        it.remove();
        size--;
    }

    // Insert the new entry
    map.put(key, value);
    size++;
}
```

### Case 1 — Key already exists (update)

```java
if (map.containsKey(key)) {
    map.remove(key);
    map.put(key, value);
    return;
}
```

Same remove + re-insert trick as in `get`. Updating an existing key should mark it as most recently used, so we remove it from its old position and re-insert it at the tail with the new value.

Notice that `size` is **not** changed here — we're replacing, not adding.

### Case 2 — Cache is full (evict LRU)

```java
if (size == capacity) {
    var it = map.entrySet().iterator();
    it.next();
    it.remove();
    size--;
}
```

**`map.entrySet().iterator()`** — gets an iterator over the entries in insertion order (oldest first).

**`it.next()`** — advances to the first entry, which is the **least recently used** item (it has been sitting at the head of the linked list the longest).

**`it.remove()`** — removes that entry directly through the iterator. This is safe and efficient; using the iterator's own `remove()` avoids a `ConcurrentModificationException`.

**`size--`** — decrements the count since one entry was evicted.

### Insert the new entry

```java
map.put(key, value);
size++;
```

After eviction (if needed), insert the new key-value pair at the tail of the map (most recently used position) and increment size.

---

## Full Walkthrough Example

Suppose `capacity = 2`.

| Operation | Map (head → tail) | size | Result |
|---|---|---|---|
| `put(1, 10)` | `{1=10}` | 1 | — |
| `put(2, 20)` | `{1=10, 2=20}` | 2 | — |
| `get(1)` | `{2=20, 1=10}` | 2 | `10` (1 moved to tail) |
| `put(3, 30)` | `{1=10, 3=30}` | 2 | key `2` evicted (it was at head = LRU) |
| `get(2)` | `{1=10, 3=30}` | 2 | `-1` (2 was evicted) |

---

## Time and Space Complexity

| Operation | Time Complexity |
|---|---|
| `get` | O(1) average |
| `put` | O(1) average |

**Space:** O(capacity) — at most `capacity` entries are ever stored.

`LinkedHashMap` provides O(1) average for `containsKey`, `remove`, and `put` (backed by a hash table), while the doubly-linked list maintains order with no extra traversal cost.

---

## Key Takeaways

1. **`LinkedHashMap` preserves insertion order**, giving us a free ordering structure on top of a hash map.
2. **"Move to end" = remove + re-insert.** Every access (read or write) ends with the entry at the tail.
3. **The head is always the LRU victim.** When capacity is exceeded, the iterator's first element is evicted in O(1).
4. **Manual `size` tracking** is used instead of `map.size()` — both work, but the explicit counter makes the capacity check intent crystal clear.