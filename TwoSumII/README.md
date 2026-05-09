# 167. Two Sum II — Input Array Is Sorted

## Problem Summary

Given a **1-indexed**, **sorted (non-decreasing)** array of integers and a `target`, return the indices `[index1, index2]` of the two numbers that add up to `target`. You may not use the same element twice, and there is exactly one solution. The solution must use **O(1) extra space**.

---

## Solution: Two-Pointer Technique

```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int l = 0;
        int r = numbers.length - 1;

        while (l < r) {
            if (numbers[l] + numbers[r] == target) return new int[]{l + 1, r + 1};
            if (numbers[l] + numbers[r] > target) r--;
            if (numbers[l] + numbers[r] < target) l++;
        }

        return numbers;
    }
}
```

---

## Key Insight: Why Two Pointers Work Here

The array is **already sorted**. This is the critical property that makes the two-pointer approach valid.

Place one pointer at the **leftmost** element (smallest) and another at the **rightmost** element (largest). Their sum gives you three possible situations:

| Condition | Meaning | Action |
|---|---|---|
| `sum == target` | Found the answer | Return both indices |
| `sum > target` | Sum is too large | Move `r` left (decrease sum) |
| `sum < target` | Sum is too small | Move `l` right (increase sum) |

Because the array is sorted, moving `l` right always **increases** the sum, and moving `r` left always **decreases** it. This guarantees we never miss the solution and always converge toward it.

---

## Step-by-Step Example

### Example 1 — Standard Case

**Input:** `numbers = [2, 7, 11, 15]`, `target = 9`

```
Index:    0    1    2    3
Array:  [ 2,   7,  11,  15 ]
          ^                ^
          l                r
```

**Iteration 1:**
- `numbers[l] + numbers[r]` = `2 + 15` = `17`
- `17 > 9` → move `r` left

```
Index:    0    1    2    3
Array:  [ 2,   7,  11,  15 ]
          ^         ^
          l         r
```

**Iteration 2:**
- `numbers[l] + numbers[r]` = `2 + 11` = `13`
- `13 > 9` → move `r` left

```
Index:    0    1    2    3
Array:  [ 2,   7,  11,  15 ]
          ^    ^
          l    r
```

**Iteration 3:**
- `numbers[l] + numbers[r]` = `2 + 7` = `9`
- `9 == 9` ✅ → return `[l+1, r+1]` = `[1, 2]`

**Output:** `[1, 2]`

---

### Example 2 — Left Pointer Needs to Move

**Input:** `numbers = [1, 3, 4, 5, 7, 11]`, `target = 9`

```
Index:    0    1    2    3    4    5
Array:  [ 1,   3,   4,   5,   7,  11 ]
          ^                        ^
          l                        r
```

**Iteration 1:**
- `1 + 11` = `12`
- `12 > 9` → move `r` left

```
Array:  [ 1,   3,   4,   5,   7,  11 ]
          ^                   ^
          l                   r
```

**Iteration 2:**
- `1 + 7` = `8`
- `8 < 9` → move `l` right

```
Array:  [ 1,   3,   4,   5,   7,  11 ]
               ^              ^
               l              r
```

**Iteration 3:**
- `3 + 7` = `10`
- `10 > 9` → move `r` left

```
Array:  [ 1,   3,   4,   5,   7,  11 ]
               ^         ^
               l         r
```

**Iteration 4:**
- `3 + 5` = `8`
- `8 < 9` → move `l` right

```
Array:  [ 1,   3,   4,   5,   7,  11 ]
                    ^    ^
                    l    r
```

**Iteration 5:**
- `4 + 5` = `9`
- `9 == 9` ✅ → return `[l+1, r+1]` = `[3, 4]`

**Output:** `[3, 4]`

---

## Why This Is Correct (Proof of No Missed Cases)

At every step, the algorithm eliminates a candidate without skipping the answer:

- When `sum > target`: no element to the right of `r` can fix it (they're all ≥ `numbers[r]`), so `r` is useless with `l`. It's safe to discard `r`.
- When `sum < target`: no element to the left of `l` can fix it (they're all ≤ `numbers[l]`), so `l` is useless with `r`. It's safe to discard `l`.

This means every discarded index is provably not part of the solution. The loop will always reach the answer before `l >= r`, because the problem guarantees exactly one solution exists.

---

## Complexity Analysis

| | Complexity | Reason |
|---|---|---|
| **Time** | O(n) | Each pointer moves at most `n` steps total |
| **Space** | O(1) | Only two integer variables used, no extra data structures |

This is optimal — you must look at elements to find the answer, so O(n) time is the lower bound. The O(1) space meets the problem's explicit constraint.

---

## Why Not a HashMap?

A HashMap approach (common for the unsorted Two Sum problem) works in O(n) time but uses O(n) **space**. Since this array is sorted, two pointers achieve the same time complexity with O(1) space — strictly better and exactly what this problem requires.