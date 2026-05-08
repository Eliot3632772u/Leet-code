# 🔍 Find Minimum in Rotated Sorted Array

> **LeetCode #153** — Binary Search on a Rotated Array

---

## 📌 Problem Statement

You are given a **sorted array** that has been **rotated** at some unknown pivot. Your task is to find the **minimum element** in `O(log n)` time.

```
Input:  [3, 4, 5, 1, 2]
Output: 1

Input:  [4, 5, 6, 7, 0, 1, 2]
Output: 0

Input:  [11, 13, 15, 17]
Output: 11  ← not rotated at all
```

---

## 💡 Key Insight — Why Binary Search Works

A rotated sorted array has a very special property:

```
Original sorted:  [1, 2, 3, 4, 5, 6, 7]
Rotated at idx 4: [5, 6, 7, 1, 2, 3, 4]
                           ↑
                      pivot (minimum)
```

The array is split into **two sorted halves** by the pivot (minimum). Everything to the left of the pivot is **larger** than everything to the right. This asymmetry is exactly what we exploit.

> **Core Rule:** If `nums[mid] > nums[right]`, the minimum must be in the **right half**. Otherwise, it's in the **left half** (including `mid` itself).

---

## 🧠 The Algorithm

```java
class Solution {
    public int findMin(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        int m = (r + l) / 2;

        while (l < r) {
            if (nums[m] > nums[r]) l = m + 1;
            else r = m;
            m = (r + l) / 2;
        }

        return nums[l];
    }
}
```

### Variable Roles

| Variable | Role |
|----------|------|
| `l` | Left boundary of the search window |
| `r` | Right boundary of the search window |
| `m` | Midpoint — the element we're currently inspecting |

---

## 🔬 Why `nums[mid] > nums[right]`?

This is the heart of the algorithm. Let's think about what it means:

### Case 1 — `nums[mid] > nums[right]` → Minimum is on the RIGHT

```
Example: [3, 4, 5, 1, 2]
                ↑        ↑
               mid      right

nums[mid] = 5 > nums[right] = 2

This means the array "drops" somewhere between mid and right.
The minimum MUST be to the right of mid.
→ Move l = mid + 1
```

### Case 2 — `nums[mid] <= nums[right]` → Minimum is on the LEFT (or IS mid)

```
Example: [5, 1, 2, 3, 4]
            ↑        ↑
           mid      right

nums[mid] = 1 <= nums[right] = 4

The right half is cleanly sorted, so the minimum is at mid or to its left.
→ Move r = mid  (not mid - 1, because mid itself could be the answer!)
```

---

## 📋 Step-by-Step Examples

---

### Example 1 — `[3, 4, 5, 1, 2]`

```
Array indices:   0   1   2   3   4
Values:          3   4   5   1   2
```

**Initial State:**
```
l = 0,  r = 4,  m = 2
         [3, 4, 5, 1, 2]
          ↑      ↑     ↑
          l      m     r
```

---

**Iteration 1:**
```
nums[m] = 5,  nums[r] = 2
5 > 2  →  TRUE  →  minimum is to the RIGHT

l = m + 1 = 3
m = (3 + 4) / 2 = 3

         [3, 4, 5, 1, 2]
                   ↑  ↑
                  l,m  r
```

---

**Iteration 2:**
```
nums[m] = 1,  nums[r] = 2
1 > 2  →  FALSE  →  minimum is HERE or to the LEFT

r = m = 3
m = (3 + 3) / 2 = 3

         [3, 4, 5, 1, 2]
                   ↑
                 l,m,r
```

---

**Loop ends:** `l == r == 3`

```
✅ Answer: nums[3] = 1
```

---

### Example 2 — `[4, 5, 6, 7, 0, 1, 2]`

```
Array indices:   0   1   2   3   4   5   6
Values:          4   5   6   7   0   1   2
```

**Initial State:**
```
l = 0,  r = 6,  m = 3

[4, 5, 6, 7, 0, 1, 2]
 ↑         ↑         ↑
 l         m         r
```

---

**Iteration 1:**
```
nums[m] = 7,  nums[r] = 2
7 > 2  →  TRUE  →  go RIGHT

l = 4,  m = (4 + 6) / 2 = 5

[4, 5, 6, 7, 0, 1, 2]
             ↑  ↑  ↑
             l  m  r
```

---

**Iteration 2:**
```
nums[m] = 1,  nums[r] = 2
1 > 2  →  FALSE  →  go LEFT (keep mid)

r = 5,  m = (4 + 5) / 2 = 4

[4, 5, 6, 7, 0, 1, 2]
             ↑  ↑
            l,m  r
```

---

**Iteration 3:**
```
nums[m] = 0,  nums[r] = 1
0 > 1  →  FALSE  →  go LEFT (keep mid)

r = 4,  m = (4 + 4) / 2 = 4

[4, 5, 6, 7, 0, 1, 2]
             ↑
           l,m,r
```

---

**Loop ends:** `l == r == 4`

```
✅ Answer: nums[4] = 0
```

---

### Example 3 — No Rotation: `[1, 2, 3, 4, 5]`

```
Array indices:   0   1   2   3   4
Values:          1   2   3   4   5
```

**Initial State:**
```
l = 0,  r = 4,  m = 2
```

---

**Iteration 1:**
```
nums[m] = 3,  nums[r] = 5
3 > 5  →  FALSE  →  go LEFT

r = 2,  m = 1
```

---

**Iteration 2:**
```
nums[m] = 2,  nums[r] = 3
2 > 3  →  FALSE  →  go LEFT

r = 1,  m = 0
```

---

**Iteration 3:**
```
nums[m] = 1,  nums[r] = 2
1 > 2  →  FALSE  →  go LEFT

r = 0,  m = 0
```

---

**Loop ends:** `l == r == 0`

```
✅ Answer: nums[0] = 1  ← correctly found at the start!
```

---

## ⚠️ Why `r = mid` and NOT `r = mid - 1`?

This is a subtle but critical point. When `nums[mid] <= nums[right]`, mid itself could be the minimum. If we did `r = mid - 1`, we'd skip it.

```
Example: [2, 1]
          ↑  ↑
          l  r
          m

nums[m] = 2,  nums[r] = 1
2 > 1  →  TRUE  →  l = m + 1 = 1

Now l == r == 1
Answer: nums[1] = 1  ✅

If we had done r = mid - 1 when nums[mid] <= nums[right]:
Example: [1, 2]
          ↑
         l,m,r=1

nums[m] = 1,  nums[r] = 2
1 > 2  →  FALSE
r = mid - 1 = -1  ← 💥 WRONG! We'd skip index 0.
```

Setting `r = mid` **preserves `mid` as a candidate** while still shrinking the window.

---

## 📊 Complexity Analysis

| Metric | Value | Reason |
|--------|-------|--------|
| **Time Complexity** | `O(log n)` | Window halves on each iteration |
| **Space Complexity** | `O(1)` | Only 3 integer variables used |

---

## 🔄 Comparison to Linear Search

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Linear scan | `O(n)` | `O(1)` | Simple but slow |
| **This solution** | **`O(log n)`** | **`O(1)`** | **Optimal** |
| Sorting first | `O(n log n)` | `O(1)` | Wasteful |

---

## ✅ Edge Cases Handled

| Case | Array | Behavior |
|------|-------|----------|
| No rotation | `[1, 2, 3, 4, 5]` | Converges to index 0 |
| Rotated by 1 | `[2, 3, 4, 5, 1]` | Correctly finds last element |
| Two elements | `[2, 1]` | Handles without infinite loop |
| Single element | `[42]` | `l == r` from start, returns immediately |
| Full rotation | `[1, 2, 3, 4, 5]` | Same as no rotation |

---

## 🎯 Summary

The algorithm works by repeatedly asking one question:

> **"Does the array drop somewhere between `mid` and `right`?"**

- **YES** (`nums[mid] > nums[right]`) → the minimum is in `[mid+1 ... right]`
- **NO** (`nums[mid] <= nums[right]`) → the minimum is in `[left ... mid]`

Each iteration cuts the search space in half, guaranteeing `O(log n)` performance. The loop terminates when `l == r`, at which point both pointers have converged on the minimum element.