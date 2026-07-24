# Pivot Index

## Problem

Given an array of integers `nums`, find the **pivot index** — the index where the sum of all elements to its **left** equals the sum of all elements to its **right**. If no such index exists, return `-1`. If there are multiple pivot indices, return the leftmost one.

```
Input:  nums = [2, 1, -1]
Output: 0
```

## Solution

```java
class Solution {
    public int pivotIndex(int[] nums) {
        int[] l = new int[nums.length];
        int[] r = new int[nums.length];
        l[0] = nums[0];
        r[nums.length - 1] = nums[nums.length - 1];
        for (int i = 1, j = nums.length - 2; i < nums.length && j >= 0; i++, j--) {
            l[i] = l[i - 1] + nums[i];
            r[j] = r[j + 1] + nums[j];
        }

        for (int i = 0; i < nums.length; i++) {
            if (l[i] == r[i]) return i;
        }

        return -1;
    }
}
```

## How it works

The solution builds two arrays:

- `l[i]` — the **inclusive** prefix sum: `nums[0] + nums[1] + ... + nums[i]`
- `r[i]` — the **inclusive** suffix sum: `nums[i] + nums[i+1] + ... + nums[n-1]`

Both arrays are filled in a single pass, walking `i` from the front and `j` from the back at the same time:

```
l[i] = l[i - 1] + nums[i]   // add nums[i] to running total from the left
r[j] = r[j + 1] + nums[j]   // add nums[j] to running total from the right
```

Once both arrays are built, the code scans left to right looking for the first index where `l[i] == r[i]`, and returns it. If none is found, it returns `-1`.

## Why it works

The actual definition of "pivot" compares the sum **strictly to the left** of `i` and the sum **strictly to the right** of `i` — `nums[i]` itself is excluded from both sides:

```
leftSum(i)  = nums[0] + ... + nums[i-1]
rightSum(i) = nums[i+1] + ... + nums[n-1]
```

But `l[i]` and `r[i]` in the code are **inclusive** of `nums[i]`:

```
l[i] = leftSum(i)  + nums[i]
r[i] = rightSum(i) + nums[i]
```

So when the code checks `l[i] == r[i]`, that's really:

```
leftSum(i) + nums[i] == rightSum(i) + nums[i]
```

The `nums[i]` term appears identically on both sides, so it cancels out algebraically, leaving exactly the condition that defines a pivot:

```
leftSum(i) == rightSum(i)
```

That's the trick: by including `nums[i]` on both sides consistently, the code never needs to subtract it back out — the comparison `l[i] == r[i]` is mathematically equivalent to comparing the true left and right sums.

## Walking through `nums = [2, 1, -1]`

**Building `l` (inclusive prefix sums):**

| i | nums[i] | l[i] |
|---|---------|------|
| 0 | 2       | 2    |
| 1 | 1       | 2 + 1 = 3 |
| 2 | -1      | 3 + (-1) = 2 |

**Building `r` (inclusive suffix sums), filled from the back:**

| j | nums[j] | r[j] |
|---|---------|------|
| 2 | -1      | -1   |
| 1 | 1       | 1 + (-1) = 0 |
| 0 | 2       | 2 + 0 = 2 |

So `l = [2, 3, 2]` and `r = [2, 0, -1]`.

**Scanning for a match:**

At `i = 0`: `l[0] = 2` and `r[0] = 2` — they're equal, so `0` is returned immediately.

**Why index 0 works out correctly even though there's "nothing" to its left:**

- `l[0] = 2` — this is just `nums[0]` itself, since there's nothing before it to add.
- `r[0] = 2` — this is the sum of the *entire array* (`2 + 1 + (-1) = 2`), since the suffix starting at index 0 is the whole thing.

Subtracting `nums[0] = 2` from both sides to get the *true* left/right sums:

```
leftSum(0)  = l[0] - nums[0] = 2 - 2 = 0   → matches "no elements to the left"
rightSum(0) = r[0] - nums[0] = 2 - 2 = 0   → matches nums[1] + nums[2] = 1 + (-1) = 0
```

Both true sums are `0`, confirming index `0` is a valid pivot — exactly matching the expected explanation:

```
Left sum = 0 (no elements to the left of index 0)
Right sum = nums[1] + nums[2] = 1 + -1 = 0
```

The inclusive-sum trick handles this edge case (and the symmetric edge case at the last index) automatically, without needing any special-case `if` checks for the boundaries.

## Complexity

- **Time:** `O(n)` — one pass to build `l` and `r`, one pass to scan for the answer.
- **Space:** `O(n)` — two auxiliary arrays of size `n`.

This can be optimized to `O(1)` extra space by first computing the total sum of the array, then tracking only a running left sum while iterating once and deriving the right sum as `total - leftSum - nums[i]` on the fly — but the two-array version above is easier to read and reason about.