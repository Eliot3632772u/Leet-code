# 🔍 Find First and Last Position of Element in Sorted Array

## Problem Summary

Given a sorted integer array `nums` and a `target` value, find the **starting and ending index** of `target` in the array.

- If `target` is found, return `[firstIndex, lastIndex]`.
- If `target` is **not** found, return `[-1, -1]`.

You must solve it in **O(log n)** time — meaning a plain linear scan is not acceptable.

### Example

```
nums   = [5, 7, 7, 8, 8, 8, 10]
target = 8

Answer = [3, 5]   (8 first appears at index 3, last at index 5)
```

---

## Intuition

The array is **sorted**, which is the classic signal to use **Binary Search** — a divide-and-conquer strategy that cuts the search space in half on every step, achieving O(log n) time.

The strategy has two phases:

1. **Phase 1 — Binary Search:** Find *any* occurrence of `target` in O(log n).
2. **Phase 2 — Expand Outward:** Once found, walk left and right from that index to find the exact boundaries of the target range.

> Why not two separate binary searches for the left and right boundary?
> Both approaches are valid and O(log n). This solution finds one hit first and then expands, which is simpler to reason about and works efficiently in practice.

---

## Solution

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {

        int l = 0;
        int r = nums.length - 1;
        int m;

        // Phase 1: Binary search for any occurrence of target
        while (l <= r) {
            m = (r + l) / 2;

            if (nums[m] == target) {
                // Phase 2: Expand left and right to find boundaries
                int i = m - 1;
                int j = m + 1;
                while (i >= 0 && nums[i] == target) i--;
                while (j <= nums.length - 1 && nums[j] == target) j++;
                return new int[]{i + 1, j - 1};
            }

            if (l == r) break;

            if (nums[m] > target) r = m;     // target is in the left half
            else l = m + 1;                   // target is in the right half
        }

        return new int[]{-1, -1}; // target not found
    }
}
```

---

## Deep Dive: How Each Part Works

### Binary Search Setup

```java
int l = 0;
int r = nums.length - 1;
```

`l` and `r` are the left and right boundaries of the current search window. We start with the entire array in scope.

---

### The Midpoint

```java
m = (r + l) / 2;
```

We calculate the middle index of the current window. On each iteration, we use `nums[m]` to decide whether to go left or right, halving the search space.

> **Why `(r + l) / 2` and not `(l + r) / 2`?**
> They're mathematically identical. Some codebases write it as `l + (r - l) / 2` to avoid integer overflow in languages without big integers, but in practice both work fine here.

---

### Hit: Target Found — Expand Outward

```java
if (nums[m] == target) {
    int i = m - 1;
    int j = m + 1;
    while (i >= 0 && nums[i] == target) i--;
    while (j <= nums.length - 1 && nums[j] == target) j++;
    return new int[]{i + 1, j - 1};
}
```

When binary search lands on the target:
- `i` walks **left** as long as it keeps seeing `target`.
- `j` walks **right** as long as it keeps seeing `target`.
- When both stop, `i` is one step **before** the first occurrence, and `j` is one step **after** the last occurrence.
- So the answer is `[i+1, j-1]` — correcting for those one-step overshoots.

---

### Miss: Narrow the Window

```java
if (l == r) break;           // window collapsed to 1 element and it wasn't the target
if (nums[m] > target) r = m; // target must be to the left
else l = m + 1;              // target must be to the right
```

- If `nums[m] > target`, everything from `m` rightward is too large — shrink right boundary to `m`.
- If `nums[m] < target`, everything from `m` leftward is too small — push left boundary past `m`.
- The `l == r` guard prevents an infinite loop when the window can't shrink further.

---

## Step-by-Step Walkthrough

### Input

```
nums   = [5, 7, 7, 8, 8, 8, 10]
           0  1  2  3  4  5   6
target = 8
```

---

### Phase 1: Binary Search

**Iteration 1**
```
l=0, r=6  →  m = (0+6)/2 = 3
nums[3] = 8 == target ✅  →  HIT! Jump to Phase 2
```

Binary search found `target` at index `3` in just **one step**.

---

### Phase 2: Expand Outward from m=3

```
Starting position: i = m-1 = 2,  j = m+1 = 4

Index:  0  1  2  3  4  5  6
nums: [ 5, 7, 7, 8, 8, 8, 10 ]
               i  ^  j
                  m
```

**Expand i to the left:**
```
i=2: nums[2] = 7 ≠ 8  →  STOP
```
`i` stops at 2 immediately (7 is not the target).

**Expand j to the right:**
```
j=4: nums[4] = 8 == 8  →  j becomes 5
j=5: nums[5] = 8 == 8  →  j becomes 6
j=6: nums[6] = 10 ≠ 8  →  STOP
```
`j` stops at 6.

**Final boundaries:**
```
i = 2  →  first occurrence = i + 1 = 3 ✅
j = 6  →  last occurrence  = j - 1 = 5 ✅
```

**Return `[3, 5]`** ✅

---

### Visualizing the Expansion

```
Index:   0    1    2    3    4    5    6
nums:  [ 5,   7,   7,   8,   8,   8,  10 ]
                   i←  [m]  →j

Expand left:   i hits 7  → stop at i=2
Expand right:  j passes 4, 5 → stops at j=6 (hits 10)

Answer: [ i+1, j-1 ] = [ 3, 5 ]
```

---

## Edge Cases

| Scenario | Input | Output |
|---|---|---|
| Target appears once | `[1,3,5,7,9]`, target=`7` | `[3, 3]` |
| Target fills entire array | `[4,4,4,4,4]`, target=`4` | `[0, 4]` |
| Target not in array | `[1,2,3,6,7]`, target=`5` | `[-1,-1]` |
| Empty array | `[]`, target=`0` | `[-1,-1]` |
| Target is first element | `[2,3,4,5]`, target=`2` | `[0, 0]` |
| Target is last element | `[1,2,3,4]`, target=`4` | `[3, 3]` |

---

## Complexity Analysis

| | Complexity | Reason |
|---|---|---|
| **Time** | O(log n + k) | O(log n) for binary search, O(k) for boundary expansion where k = count of target occurrences |
| **Space** | O(1) | No extra data structures used |

> In the worst case (e.g. all elements equal target), the expansion is O(n), making the overall complexity O(n). If a strict O(log n) is required in all cases, two separate binary searches for the left and right boundary would be needed. For most practical inputs this hybrid approach is fast and simple.

---

## Key Takeaways

1. **Binary search finds the target fast.** The sorted array lets us eliminate half the search space on every step — O(log n) to locate any occurrence.
2. **Expansion finds the exact range.** Once any hit is found, a simple linear walk in both directions pins down the first and last positions.
3. **The `i+1` / `j-1` correction is critical.** The expansion pointers overshoot by one in each direction before stopping — always correct for this on the return.
4. **The `l == r` guard prevents infinite loops.** Without it, the loop could spin forever when the window collapses to a single non-target element.
5. **Sorted input is the prerequisite.** Both binary search and the outward expansion rely entirely on the array being sorted. This solution would not work correctly on unsorted data.