# TwoSum in C — Custom HashMap from Scratch

> A deep dive into solving the classic TwoSum problem in C by building a **HashMap manually** using structs, linked list chaining, and a hash function — no standard library containers.

---

## Table of Contents

1. [Overview](#overview)
2. [Data Structure: the `map` Node](#data-structure-the-map-node)
3. [Building the HashMap](#building-the-hashmap)
4. [The Hash Function](#the-hash-function)
5. [Inserting into the HashMap](#inserting-into-the-hashmap)
6. [Looking Up a Value](#looking-up-a-value)
7. [TwoSum — Full Walkthrough](#twosum--full-walkthrough)
8. [Memory Layout Visualization](#memory-layout-visualization)
9. [Complexity](#complexity)
10. [Full Code](#full-code)

---

## Overview

In C there is no built-in `HashMap`. This solution builds one from scratch using:

- A **fixed-size array of pointers** as the bucket table
- A **linked list per bucket** to handle collisions (chaining)
- A **modulo hash function** to map keys to buckets

The TwoSum logic itself stays O(n) — exactly like the Java version — but all the infrastructure must be wired up manually.

---

## Data Structure: the `map` Node

```c
typedef struct map {
    int key;        // the number from the array (e.g. 2, 7, 11)
    int val;        // the index of that number in nums[]
    struct map* next; // pointer to next node in the same bucket (chaining)
} map;
```

Each node stores:

| Field | Meaning |
|-------|---------|
| `key` | The actual number (used for equality check) |
| `val` | The array index of that number |
| `next` | Next node in the collision chain |

### Allocating a new node

```c
map* newMap(int key, int value) {
    map* res = malloc(sizeof(map));
    res->key = key;
    res->val = value;
    res->next = NULL;
    return res;
}
```

`malloc` allocates memory on the heap. Each node lives independently and is linked via `next`.

---

## Building the HashMap

```c
map** newHashMap(int size) {
    map** res = calloc(size, sizeof(map*));
    return res;
}
```

This creates an **array of `size` pointers**, each pointing to the head of a linked list (a bucket).

- `calloc` is used instead of `malloc` → it **zero-initializes** all pointers to `NULL`
- A `NULL` bucket means it is empty

```
hashMap (array of pointers):
[ NULL ][ NULL ][ NULL ] ... [ NULL ]
  [0]     [1]     [2]          [99999]
```

---

## The Hash Function

```c
int hash(int key, int mapSize) {
    if (key < 0)
        key = -key;
    return key % mapSize;
}
```

Two things happen here:

1. **Negative keys** are made positive with `-key` (since array indices must be non-negative)
2. **Modulo** maps any integer into `[0, mapSize - 1]`

### Example

```
mapSize = 100000

hash(2,  100000) = 2
hash(7,  100000) = 7
hash(-3, 100000) = 3   ← negated first
hash(100002, 100000) = 2  ← collision with key 2 → handled by chaining
```

---

## Inserting into the HashMap

### `pushFront` — prepend a node to a bucket's list

```c
void pushFront(map **old, map* new) {
    new->next = *old;  // new node points to current head
    *old = new;        // head is now the new node
}
```

Why insert at the front? It is O(1) — no need to traverse the whole list.

### `insertToHashMap`

```c
void insertToHashMap(map **hashMap, int hashKey, int key, int value) {
    map *new = newMap(key, value);

    if (&hashMap[hashKey] != NULL) {
        pushFront(&hashMap[hashKey], new);
        return;
    }

    hashMap[hashKey] = new;
}
```

---

## Looking Up a Value

```c
map* getValue(map **hashMap, int key, int number) {
    if (hashMap[key] == NULL) return NULL;

    map *head = hashMap[key];

    while (head) {
        if (head->key == number) return head;
        head = head->next;
    }

    return NULL;
}
```

Steps:
1. Go to bucket `key` (the pre-computed hash index)
2. If bucket is empty → return `NULL`
3. Walk the linked list, comparing `head->key == number`
4. Return the matching node, or `NULL` if not found

---

## TwoSum — Full Walkthrough

```c
int* twoSum(int* nums, int numsSize, int target, int* returnSize) {

    int mapSize = 100000;
    map **hashMap = newHashMap(mapSize);
    int *res = malloc(sizeof(int) * 2);
    *returnSize = 2;

    int i = 0;
    while (i < numsSize) {

        int key = hash(target - nums[i], mapSize);
        map *complement = getValue(hashMap, key, target - nums[i]);

        if (complement != NULL) {
            res[0] = i;
            res[1] = complement->val;
            return res;
        }

        insertToHashMap(hashMap, hash(nums[i], mapSize), nums[i], i);
        i++;
    }

    return nums;
}
```

### Core Insight

> For each `nums[i]`, the solution asks:
> *"Has the complement `target - nums[i]` already been stored?"*
> If yes → we found the pair. If no → store `nums[i]` and move on.

### Step-by-Step with `nums = [2, 7, 11, 15]`, `target = 9`

---

**i = 0, nums[i] = 2**

```
complement needed  = 9 - 2 = 7
hash(7, 100000)    = 7
getValue(hashMap, 7, 7) → NULL  (map is empty)

→ Not found. Store nums[0] = 2:
  insertToHashMap(hashMap, hash(2)=2, key=2, val=0)

hashMap[2] → Node{key=2, val=0, next=NULL}
```

---

**i = 1, nums[i] = 7**

```
complement needed  = 9 - 7 = 2
hash(2, 100000)    = 2
getValue(hashMap, 2, 2) → Node{key=2, val=0} ✅ FOUND!

res[0] = i         = 1  (current index)
res[1] = node->val = 0  (stored index of 2)

return [1, 0]
```

### Why store `val = index`?

The problem asks for **indices**, not values. So `val` in each node is the **position** in `nums[]` where that number appeared — not the number itself.

---

### Dual role of `hash()`

Notice `hash()` is called **twice** per iteration:

```c
// 1. To LOOK UP the complement
int key = hash(target - nums[i], mapSize);
map *complement = getValue(hashMap, key, target - nums[i]);

// 2. To INSERT the current number
insertToHashMap(hashMap, hash(nums[i], mapSize), nums[i], i);
```

Both use the same function, ensuring that a number stored at `hash(x)` is always looked up at `hash(x)`.

---

## Memory Layout Visualization

After processing `i = 0` (stored `2` at index `0`):

```
hashMap[] (size 100000):

index  0: NULL
index  1: NULL
index  2: → [ key=2 | val=0 | next=NULL ]
index  3: NULL
...
index  7: NULL   ← complement of 9-7=2 is checked here at i=1
...
```

After a collision (e.g. two numbers that hash to the same bucket):

```
hashMap[5]: → [ key=5 | val=2 | next= ] → [ key=100005 | val=7 | next=NULL ]
                                             ↑ collision handled by chaining
```

---

## Complexity

| | Value |
|--|--|
| **Time** | O(n) average — one pass, O(1) hash + lookup per element |
| **Space** | O(n) — up to n nodes allocated on the heap |
| **Worst case** | O(n²) if all keys collide into one bucket (degenerate chaining) |

---
