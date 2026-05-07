# Maximum Sum Subarray — Kadane's Algorithm

Given an array of integers (positive, negative, or zero), find the contiguous subarray with the **largest possible sum**.

## Example

```
Input:  [-2, 1, -3, 4, -1, 2, 1, -5, 4]
Output: 6
Subarray: [4, -1, 2, 1]
```

---

## Approaches

### Brute Force — O(n²) / O(n³)

Try every possible subarray and compute each sum, keeping track of the maximum. With `n` elements there are ~n² subarrays, making this impractical for large inputs.

### Kadane's Algorithm — O(n) time · O(1) space

The optimal solution. Solves the problem in a single pass.

---

## Core Insight

> **If your running sum turns negative, throw it away and start fresh.**

A negative prefix only drags future sums down. Starting over from the current element is always at least as good.

```
Current sum = -5,  next number = 10

Keep:   -5 + 10 = 5
Restart:       10 = 10   ← better
```

---

## Algorithm

At each position `i`, ask: should I **extend** the current subarray, or **start a new one here**?

```
currentSum = max(nums[i], currentSum + nums[i])
```

Track the best answer seen across all positions:

```
maxSum = max(maxSum, currentSum)
```

### Implementation (Java)

```java
int maxSubArray(int[] nums) {
    int currentSum = nums[0];
    int maxSum     = nums[0];

    for (int i = 1; i < nums.length; i++) {
        currentSum = Math.max(nums[i], currentSum + nums[i]);
        maxSum     = Math.max(maxSum, currentSum);
    }

    return maxSum;
}
```

### Variables

| Variable | Meaning |
|---|---|
| `currentSum` | Best subarray sum ending **exactly** at the current index |
| `maxSum` | Best subarray sum seen **anywhere** so far |

---

## Step-by-Step Walkthrough

Array: `[-2, 1, -3, 4, -1, 2, 1, -5, 4]`

| i | num | Extend | Restart | currentSum | maxSum |
|---|-----|--------|---------|------------|--------|
| 0 | -2  | —      | —       | **-2**     | -2     |
| 1 |  1  | -2+1=-1 | 1      | **1**      | 1      |
| 2 | -3  | 1-3=-2 | -3      | **-2**     | 1      |
| 3 |  4  | -2+4=2 | 4       | **4** ← restart | 4 |
| 4 | -1  | 4-1=3  | -1      | **3**      | 4      |
| 5 |  2  | 3+2=5  | 2       | **5**      | 5      |
| 6 |  1  | 5+1=6  | 1       | **6**      | **6**  |
| 7 | -5  | 6-5=1  | -5      | **1**      | 6      |
| 8 |  4  | 1+4=5  | 4       | **5**      | 6      |

**Result:** `6`, from subarray `[4, -1, 2, 1]`

---

## Why It Works — Dynamic Programming

For every index `i`, the best subarray ending there is one of only two things:

1. Start fresh at `i` → `nums[i]`
2. Extend the best subarray from `i-1` → `bestEndingHere(i-1) + nums[i]`

```
bestEndingHere(i) = max(nums[i], bestEndingHere(i-1) + nums[i])
```

Kadane computes this recurrence iteratively — that is exactly what makes it dynamic programming.

---

## Edge Cases

### Mixed positives and negatives

Even if a number is negative, the subarray extends through it as long as `currentSum` stays positive.

```
[5, -2, 3, 4]  →  sum = 10  (entire array)
```

### All negatives

Kadane handles this correctly — it returns the least-negative element.

```
[-8, -3, -6, -2, -5]  →  -2
```

---

## Visual Intuition

Think of `currentSum` as a backpack you carry through the array:

- **Positive sum** → your backpack helps; keep carrying it.
- **Negative sum** → your backpack hurts; drop it and start fresh.

---

## Complexity

| | Complexity |
|---|---|
| Time | O(n) |
| Space | O(1) |