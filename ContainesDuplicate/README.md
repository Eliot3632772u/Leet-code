# Contains Duplicate — HashSet Solution

## Problem

Given an integer array `nums`, return `true` if any value appears **at least twice**, and `false` if every element is distinct.

**Example:**
```
Input:  [1, 2, 3, 1]
Output: true
```

---

## Core Idea

The key insight is simple: as we walk through the array, we need to know whether the current number has been seen before. A **HashSet** is the perfect data structure for this — it stores elements with O(1) average-time lookup and insertion, so we can check and record each number in constant time.

The algorithm:
1. Start with an empty set.
2. For each number in the array, check if it's already in the set.
3. If yes → duplicate found, return `true` immediately.
4. If no → add it to the set and continue.
5. If we finish the loop without a hit → return `false`.

---

## Java Solution

```java
import java.util.*;

class Solution {
    public boolean containsDuplicate(int[] nums) {

        HashSet<Integer> set = new HashSet<>();

        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }
}
```

Java's built-in `HashSet<Integer>` handles all the hashing and collision resolution internally. The logic is clean and direct — the solution is essentially a transliteration of the algorithm into code.

---

## C Solution

The C solution manually implements a HashSet using an **array of linked lists** (a technique known as **separate chaining**).

```c
Node** newHashSet(int capacity) {
    Node **new = calloc(capacity, sizeof(Node*));
    return new;
}
```

The set is an array of `Node*` pointers, each slot initialized to `NULL` via `calloc`. Every slot is the head of a linked list that holds all values that hash to the same index.

### Hashing

```c
int hash(int key, int capacity) {
    if (key < 0) key = -key;
    return key % capacity;
}
```

The hash function maps a value to a bucket index using modulo. Negative numbers are negated first to keep the index in bounds.

### Insertion

```c
void insert(Node** set, Node* new, int key) {
    if (set[key] == NULL) {
        set[key] = new;
        return;
    }
    new->next = set[key];
    set[key] = new;
}
```

New nodes are prepended to the front of the linked list at `set[key]`. This is O(1) and avoids traversing the list.

### Lookup

```c
Node* get(Node** set, int key, int value) {
    if (set[key] == NULL) return NULL;

    Node *head = set[key];
    while (head) {
        if (head->value == value) return head;
        head = head->next;
    }
    return NULL;
}
```

To look up a value, we go to its bucket and walk the linked list comparing values. This handles **hash collisions** — multiple different values can share the same bucket index, and the chain lets us distinguish them.

### Putting It Together

```c
bool containsDuplicate(int* nums, int numsSize) {
    int capacity = 10000;
    Node **hashSet = newHashSet(capacity);

    for (int i = 0; i < numsSize; i++) {
        int key = hash(nums[i], capacity);
        Node *value = get(hashSet, key, nums[i]);
        if (value != NULL) return true;
        insert(hashSet, newNode(nums[i]), key);
    }
    return false;
}
```

For each element: compute its bucket → check if it's already there → if yes, return `true` → if no, insert it and move on.

---

## Complexity Analysis

| | Time | Space |
|---|---|---|
| **Best / Average** | O(n) | O(n) |
| **Worst case** | O(n²) | O(n) |

The worst case for time occurs if all elements hash to the same bucket, turning every lookup into a full list traversal. In practice, with a good hash function and reasonable capacity, lookups are O(1) on average.

---

## Why a HashSet and Not Sorting?

You could sort the array first (O(n log n)) and then check adjacent elements for duplicates — and that would use O(1) extra space. The HashSet approach trades space for speed, getting down to **O(n) time** at the cost of O(n) extra memory. For most practical inputs, the HashSet is the faster choice.