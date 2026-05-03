# HashMap in Java — Deep Dive + TwoSum Solution

> A comprehensive guide to understanding Java's `HashMap` internally, including hashing, collision handling, resizing, and a practical LeetCode TwoSum walkthrough.

---

## Table of Contents

1. [What is a HashMap?](#what-is-a-hashmap)
2. [Core Idea: Hashing](#core-idea-hashing)
3. [Internal Structure](#internal-structure)
4. [put(key, value) — Step by Step](#putkey-value--step-by-step)
5. [get(key) — Step by Step](#getkey--step-by-step)
6. [Collision Handling](#collision-handling)
7. [Resizing (Rehashing)](#resizing-rehashing)
8. [Load Factor](#load-factor)
9. [Important Behaviors](#important-behaviors)
10. [Complexity](#complexity)
11. [Real Collision Simulation](#real-collision-simulation)
12. [TwoSum — Java Solution Explained](#twosum--java-solution-explained)

---

## What is a HashMap?

Think of a `HashMap` like a set of **labeled boxes**:

- Each **key** → determines which box to use
- Each **value** → is stored inside that box
- A **hash function** decides where to store or find data

```java
HashMap<String, Integer> map = new HashMap<>();
map.put("apple", 10);
map.put("banana", 20);
System.out.println(map.get("apple")); // 10
```

---

## Core Idea: Hashing

Every key goes through a hash function:

```java
int hash = key.hashCode();
```

Then Java converts it into a **bucket index** in an array:

```java
index = hash % capacity;
```

> That index is called a **bucket**

---

## Internal Structure

Internally, `HashMap` is basically an **array of buckets**.

Each bucket can contain:
- A **linked list** (Java 7)
- A **linked list OR a Red-Black Tree** (Java 8+)

```java
Node<K,V>[] table;
```

Each node looks like:

```java
class Node<K,V> {
    int hash;
    K key;
    V value;
    Node<K,V> next;
}
```

---

## ➕ `put(key, value)` — Step by Step

Let's simulate `map.put("apple", 10)`:

**Step 1: Compute hash**
```java
int hash = "apple".hashCode();
```

**Step 2: Find bucket index** (optimized version of `%`)
```java
index = hash & (capacity - 1);
```

**Step 3: Check the bucket**

| Scenario | Action |
|----------|--------|
| Bucket is empty | Insert directly |
| Bucket has items | Handle collision |

### Case A: No Collision
```
table[3] → null
After insert:
table[3] → ("apple", 10)
```

### Case B: Collision (Chaining)
```
table[3] → ("banana", 20)
After insert:
table[3] → ("banana", 20) → ("apple", 10)
```

> This technique is called **chaining** (linked list)

---

## `get(key)` — Step by Step

```java
map.get("apple");
```

1. Compute hash
2. Find bucket index
3. Traverse the chain:

```java
for (Node node = table[index]; node != null; node = node.next) {
    if (node.key.equals("apple")) {
        return node.value;
    }
}
```

---

## Collision Handling

Collisions occur when two different keys produce the **same bucket index**.

### Java 7 — Linked List only
```
bucket → [node1] → [node2] → [node3]
```

### Java 8+ — Adaptive Structure
```
If bucket size > 8:
    Linked List → Red-Black Tree
```

This improves worst-case lookup from **O(n) → O(log n)**.

---

## Resizing (Rehashing)

HashMap **automatically grows** when it gets too full.

**Trigger condition:**
```
size > capacity × loadFactor
```

**Defaults:**
| Property | Value |
|----------|-------|
| Initial capacity | 16 |
| Load factor | 0.75 |
| Threshold | 12 |

When threshold is exceeded → capacity **doubles** (16 → 32) and **all elements are rehashed**.

### Example:
```java
HashMap<Integer, String> map = new HashMap<>(4, 0.75f);
// Threshold = 4 × 0.75 = 3
// Inserting the 4th element triggers a resize!
```

---

## ⚖️ Load Factor

| Load Factor | Lookup Speed | Memory Usage |
|-------------|-------------|--------------|
| Low (0.5) | ✅ Faster | ❌ More memory |
| High (0.9) | ❌ Slower | ✅ Less memory |
| Default (0.75) | ✅ Balanced | ✅ Balanced |

> Default `0.75` is the best general-purpose tradeoff.

---

## Important Behaviors

### Allows:
- One `null` key
- Multiple `null` values

```java
map.put(null, 100); // valid
```

### Not Thread-Safe

For concurrent use:

```java
// Option 1 (basic)
Map m = Collections.synchronizedMap(new HashMap<>());

// Option 2 (recommended)
ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
```

---

## Complexity

| Operation | Average | Worst Case |
|-----------|---------|------------|
| `put` | O(1) | O(log n) |
| `get` | O(1) | O(log n) |
| `remove` | O(1) | O(log n) |

> Worst case applies when all keys hash to the same bucket (Java 8+ uses tree → O(log n)).

---

## Real Collision Simulation

Forcing all keys into the same bucket by overriding `hashCode()`:

```java
class Key {
    int id;
    Key(int id) { this.id = id; }

    @Override
    public int hashCode() {
        return 1; // Forces ALL keys into bucket 1
    }

    @Override
    public boolean equals(Object o) {
        return ((Key) o).id == this.id;
    }
}
```

```java
HashMap<Key, String> map = new HashMap<>();
map.put(new Key(1), "A");
map.put(new Key(2), "B");
map.put(new Key(3), "C");
```

**Internal state:**
```
table[1] → (Key1, "A") → (Key2, "B") → (Key3, "C")
```

> All 3 entries land in the same bucket — this is a worst-case chain.

---

## TwoSum — Java Solution Explained

### Problem
Given an array `nums` and a target `t`, return the indices of the two numbers that add up to `t`.

### Solution Code

```java
class Solution {
    public int[] twoSum(int[] nums, int t) {
        HashMap<Integer, Integer> map = new HashMap<>();

        int i = 0;
        int j = nums.length - 1;

        while (i <= j) {
            Integer c1 = map.get(t - nums[i]);
            if (c1 != null) return new int[]{c1, i};
            else map.put(nums[i], i);

            Integer c2 = map.get(t - nums[j]);
            if (c2 != null) return new int[]{c2, j};
            else map.put(nums[j], j);

            i++;
            j--;
        }

        return nums;
    }
}
```

---

### How It Works — Step by Step

This solution uses a **two-pointer + HashMap** hybrid approach.

#### Core Insight:
> If `nums[i] + X = target`, then `X = target - nums[i]`.
> We store each number in the map and check if its **complement** has already been seen.

#### Walkthrough

| Pointer | Action | Map State |
|---------|--------|-----------|
| `i` (left) | Look up `target - nums[i]` in map. Found? → return. Not found? → store `nums[i] → i`. | grows left-to-right |
| `j` (right) | Look up `target - nums[j]` in map. Found? → return. Not found? → store `nums[j] → j`. | grows right-to-left |
| Both | Move inward: `i++`, `j--` | until `i > j` |

#### Example
```
nums = [2, 7, 11, 15], target = 9
```

**Iteration 1:**
- `i = 0`, `nums[i] = 2` → look up `9 - 2 = 7` → not found → store `{2: 0}`
- `j = 3`, `nums[j] = 15` → look up `9 - 15 = -6` → not found → store `{15: 3}`
- Move: `i = 1`, `j = 2`

**Iteration 2:**
- `i = 1`, `nums[i] = 7` → look up `9 - 7 = 2` → **FOUND at index 0** → return `[0, 1]` 

---

### Important Notes

**Why `Integer` (not `int`) for `c1` / `c2`?**

```java
Integer c1 = map.get(t - nums[i]);
```

`map.get()` returns `null` when the key doesn't exist. Using `Integer` (wrapper) allows null-checking. A primitive `int` would throw a `NullPointerException`.

**Is this approach always correct?**

The two-pointer movement here is **not the classic sorted two-pointer**. It's a meet-in-the-middle scan with a HashMap acting as memory. It works for unsorted arrays because the map stores the index along with the value, and complements are checked across the entire scanned range.

**Complexity:**

| | Value |
|--|--|
| Time | O(n) |
| Space | O(n) — the HashMap stores at most n entries |

---

## Summary

```
HashMap = Hash Function + Array of Buckets + Chaining / Tree

put  → hash → find bucket → insert or chain
get  → hash → find bucket → traverse to match key
resize → when load factor exceeded → double capacity + rehash all

TwoSum → store complement in HashMap → O(n) lookup instead of O(n²)
```

---