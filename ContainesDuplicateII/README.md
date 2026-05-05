# Contains Nearby Duplicate — HashMap Solution

## Problem

Given an integer array `nums` and an integer `k`, return `true` if there exist two **distinct indices** `i` and `j` such that:

- `nums[i] == nums[j]`, and
- `abs(i - j) <= k`

Return `false` otherwise.

**Example:**
```
Input:  nums = [1, 2, 3, 1], k = 3
Output: true   (nums[0] == nums[3], and |0 - 3| = 3 <= k)

Input:  nums = [1, 2, 3, 1, 2, 3], k = 2
Output: false  (duplicates exist but none are within k positions of each other)
```

---

## Core Idea

This problem extends the classic "contains duplicate" problem by adding a **distance constraint**. We don't just need to know *if* a duplicate exists — we need to know *where* the previous occurrence was.

A **HashMap** is the right tool here. Instead of storing only whether a value has been seen, we store the **most recent index** at which each value appeared. Then, when we encounter a value again, we can instantly check whether the gap between the current index and the stored index satisfies `abs(i - j) <= k`.

Crucially, when a value is seen again but is **too far away**, we **update** its stored index to the current one. This is important: we always want to keep the most recent index, because a closer past occurrence gives us the best chance of satisfying the constraint for future elements.

The algorithm:
1. Start with an empty map.
2. For each index `i`, look up `nums[i]` in the map.
3. If found and `i - storedIndex <= k` → return `true`.
4. If found but too far → update the stored index to `i`.
5. If not found → add `nums[i] → i` to the map.
6. If the loop ends without a hit → return `false`.

---

## Java Solution

```java
public class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {

            Integer oldIndex = map.get(nums[i]);
            if (oldIndex != null) {
                if (i - oldIndex <= k) return true;
                map.replace(nums[i], i);
            }
            map.put(nums[i], i);
        }

        return false;
    }
}
```

Java's `HashMap<Integer, Integer>` maps each number to its most recent index. `map.get()` returns `null` if the key doesn't exist, which serves as the "not seen yet" sentinel. When a duplicate is found that's out of range, `map.replace()` updates the index so future lookups use the closer position.

---

## C Solution

The C solution manually implements a HashMap using an **array of linked lists** (separate chaining), where each node stores both the original value (as a key) and the most recent index (as a value).

### The Node

```c
typedef struct Node {
    int key;      // the actual number from nums[]
    int hashKey;  // its bucket index
    int value;    // the most recent index i where this number appeared
    struct Node* next;
} Node;
```

Each node holds three pieces of information: the number itself (`key`), its computed bucket (`hashKey`), and the index at which it last appeared (`value`). The `next` pointer links nodes within the same bucket for collision handling.

### Hashing

```c
int hash(int key, int capacity) {
    if (key < 0) key = -key;
    return key % capacity;
}
```

Maps any integer (including negatives) to a valid bucket index via modulo.

### Insertion

```c
void insert(Node **hashMap, int key, Node *new) {
    if (hashMap[key] == NULL) {
        hashMap[key] = new;
        return;
    }
    new->next = hashMap[key];
    hashMap[key] = new;
}
```

New nodes are prepended to the front of the bucket's linked list — O(1) and avoids full traversal.

### Lookup

```c
Node* get(Node **hashMap, int hashKey, int key) {
    if (hashMap[hashKey] == NULL) return NULL;

    Node *head = hashMap[hashKey];
    while (head) {
        if (head->key == key) return head;
        head = head->next;
    }
    return NULL;
}
```

Goes to the correct bucket, then walks the chain comparing the original `key` (not the hash) to resolve collisions — two different numbers can share a bucket, so the hash alone isn't enough to confirm a match.

### Putting It Together

```c
bool containsNearbyDuplicate(int* nums, int numsSize, int k) {
    int capacity = 10000;
    Node **map = newHashMap(capacity);

    for (int i = 0; i < numsSize; i++) {
        int key = hash(nums[i], capacity);
        Node* oldIndex = get(map, key, nums[i]);

        if (oldIndex != NULL && i - oldIndex->value <= k) return true;
        else if (oldIndex != NULL) oldIndex->value = i;  // update index in-place
        else insert(map, key, newNode(nums[i], key, i));
    }

    return false;
}
```

When a duplicate is found but out of range, `oldIndex->value = i` updates the stored index **in-place** on the existing node — no removal or reallocation needed. This is a key advantage of storing a mutable pointer: we can mutate the node directly rather than replacing it.

---

## Complexity Analysis

| | Time | Space |
|---|---|---|
| **Best / Average** | O(n) | O(n) |
| **Worst case** | O(n²) | O(n) |

Each element triggers one lookup and at most one insert or in-place update — both O(1) on average. The worst case for time arises if all elements hash to the same bucket, degrading every lookup to a full list traversal. With a reasonable capacity and well-distributed inputs this doesn't happen in practice.

---

## Why HashMap and Not a Sliding Window Set?

An alternative approach uses a **sliding window HashSet** of size `k`: add each element to the set, and if it's already present you've found a nearby duplicate. When the window exceeds size `k`, evict the oldest element. This is O(n) time and O(k) space — better space-wise when `k` is small.

The HashMap approach here uses O(n) space regardless of `k`, but it's simpler to reason about: no explicit window management, just a direct lookup of "where did I last see this number?"