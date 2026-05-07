# Maximum Product Subarray

Given an integer array `nums`, find the contiguous subarray with the **largest product** and return that product.

## Example

```
Input:  [2, 3, -2, 4]
Output: 6
Subarray: [2, 3]
```

---

## Why This Problem Is Harder Than Maximum Sum Subarray

In the maximum sum problem, negative numbers are almost always harmful and a positive running sum is always worth keeping. The logic is simple: drop the subarray the moment it goes negative.

Products are fundamentally different because of one rule:

> **Negative × Negative = Positive**

This means a deeply negative product — something that looks completely useless — can **instantly become the best product in the entire array** the moment it gets multiplied by another negative number.

### The example that breaks simple thinking

```
[-2, 3, -4]
```

If you only tracked the maximum product at each step:

| Step | Element | Naive max |
|------|---------|-----------|
| 0 | -2 | -2 |
| 1 |  3 | 3  ← dropped the -2 |
| 2 | -4 | -12 |

But the real answer is:

```
(-2) × 3 × (-4) = 24
```

The `-2` that was discarded at step 1 was exactly what was needed. A naive Kadane-style approach collapses here.

---

## The Core Insight

Because a negative can flip into a positive, **you must track two things at every index simultaneously**:

| Variable | Meaning |
|---|---|
| `maxProd` | Largest product of any subarray ending **exactly here** |
| `minProd` | Smallest (most negative) product of any subarray ending **exactly here** |

The minimum product is not a throwaway — it is secretly the most powerful value in the algorithm. When the next number is negative, `minProd` and `maxProd` swap roles:

```
hugely_negative × negative = hugely_positive   ← minProd becomes the new maxProd
hugely_positive × negative = hugely_negative   ← maxProd becomes the new minProd
```

Failing to track the minimum is why every simpler approach breaks on inputs like `[-2, 3, -4]`.

---

## The Dynamic Programming Recurrence

At each index `i`, the new maximum product ending here must be one of exactly three candidates:

1. **`nums[i]` alone** — start a brand-new subarray here
2. **`maxProd × nums[i]`** — extend the best positive chain
3. **`minProd × nums[i]`** — flip the worst negative chain into a positive

```
maxEndingHere = max(nums[i],  maxProd × nums[i],  minProd × nums[i])
minEndingHere = min(nums[i],  maxProd × nums[i],  minProd × nums[i])
```

These are the **only** possible origins for the best or worst subarray ending at `i`. Any subarray ending at `i` either:
- starts at `i` (candidate 1), or
- extends from `i-1`, in which case the best extension uses the previous max or min (candidates 2 and 3).

Nothing else is possible. This is what makes the recurrence exact and complete.

> ⚠️ **Implementation note:** You must save `maxProd` into a temporary variable before computing `minProd`, because the min calculation needs the **previous** max, not the one you just updated.

---

## Implementation

```java
int maxProduct(int[] nums) {
    int maxProd = nums[0];
    int minProd = nums[0];
    int answer  = nums[0];

    for (int i = 1; i < nums.length; i++) {
        int curr = nums[i];

        // Save before overwriting — minProd needs the old maxProd
        int tempMax = Math.max(curr, Math.max(maxProd * curr, minProd * curr));
        minProd     = Math.min(curr, Math.min(maxProd * curr, minProd * curr));
        maxProd     = tempMax;

        answer = Math.max(answer, maxProd);
    }

    return answer;
}
```

---

## Variable Reference

| Variable | What it tracks |
|---|---|
| `maxProd` | Maximum product of any subarray ending at the **current** index |
| `minProd` | Minimum product of any subarray ending at the **current** index |
| `answer` | Global maximum product seen across **all** positions so far |
| `tempMax` | Temporary holder for the new `maxProd` so `minProd` can safely use the old one |

---

## Deep Walkthrough — Example 1

**Array:** `[2, 3, -2, 4]`

**Initialization:**
```
maxProd = 2,  minProd = 2,  answer = 2
```

---

**Index 1 → value = 3**

| Candidate | Calculation | Value |
|---|---|---|
| Start fresh | 3 | 3 |
| Extend max | 2 × 3 | 6 |
| Extend min | 2 × 3 | 6 |

```
maxProd = max(3, 6, 6) = 6
minProd = min(3, 6, 6) = 3
answer  = max(2, 6)    = 6
```

---

**Index 2 → value = -2**

| Candidate | Calculation | Value |
|---|---|---|
| Start fresh | -2 | -2 |
| Extend max | 6 × -2 | -12 |
| Extend min | 3 × -2 | -6 |

```
maxProd = max(-2, -12, -6) = -2
minProd = min(-2, -12, -6) = -12   ← looks terrible, but may matter later
answer  = max(6, -2)       = 6
```

At this point `-12` looks like a liability. It isn't — it's insurance.

---

**Index 3 → value = 4**

| Candidate | Calculation | Value |
|---|---|---|
| Start fresh | 4 | 4 |
| Extend max | -2 × 4 | -8 |
| Extend min | -12 × 4 | -48 |

```
maxProd = max(4, -8, -48) = 4
minProd = min(4, -8, -48) = -48
answer  = max(6, 4)       = 6
```

**Final answer: `6`** — subarray `[2, 3]`

---

## Deep Walkthrough — Example 2 (The Negative Flip)

**Array:** `[-2, 3, -4]`

This example is the most important one. It shows exactly why tracking the minimum product is not optional.

**Initialization:**
```
maxProd = -2,  minProd = -2,  answer = -2
```

---

**Index 1 → value = 3**

| Candidate | Calculation | Value |
|---|---|---|
| Start fresh | 3 | 3 |
| Extend max | -2 × 3 | -6 |
| Extend min | -2 × 3 | -6 |

```
maxProd = max(3, -6, -6) = 3
minProd = min(3, -6, -6) = -6   ← preserved even though it looks worthless
answer  = max(-2, 3)     = 3
```

A naive algorithm would discard `-6` here. This is the fatal mistake.

---

**Index 2 → value = -4**

| Candidate | Calculation | Value |
|---|---|---|
| Start fresh | -4 | -4 |
| Extend max | 3 × -4 | -12 |
| Extend min | **-6 × -4** | **24** ← the payoff |

```
maxProd = max(-4, -12, 24) = 24
minProd = min(-4, -12, 24) = -12
answer  = max(3, 24)       = 24
```

**Final answer: `24`** — subarray `[-2, 3, -4]`

The `-6` saved at index 1 was the product of `(-2) × 3`. When multiplied by `-4`, the two negatives cancelled and produced the correct global maximum. If that `-6` had been discarded, the answer would have been `3` — wrong by a factor of 8.

---

## Why Tracking Only the Maximum Fails

Here is what a maximum-only approach does on `[-2, 3, -4]`:

| i | num | maxOnly | Notes |
|---|-----|---------|-------|
| 0 | -2 | -2 | |
| 1 |  3 | **3** | discards -6 — fatal |
| 2 | -4 | **-4** | no negative min to flip |

Output: `3`. The real answer is `24`.

The information needed to produce `24` was the intermediate product `(-2) × 3 = -6`. The maximum-only approach threw it away because it was negative. The minimum tracker keeps it alive through every step, ready to flip into a large positive.

---

## Handling Zeros

A zero wipes out any product chain entirely. When the algorithm encounters a zero:

- All three candidates (`0`, `maxProd × 0`, `minProd × 0`) evaluate to `0`
- Both `maxProd` and `minProd` reset to `0`
- The next element effectively starts a fresh subarray

This reset is automatic — no special-case code is needed. The `start fresh` candidate handles it naturally, just as it does in Kadane's algorithm for sums.

---

## Full State Table — Example 2

| i | num | prev max | prev min | candidates | maxProd | minProd | answer |
|---|-----|----------|----------|------------|---------|---------|--------|
| 0 | -2  | —        | —        | init       | -2      | -2      | -2     |
| 1 |  3  | -2       | -2       | 3, -6, -6  | 3       | -6      | 3      |
| 2 | -4  | 3        | -6       | -4, -12, **24** | 24 | -12    | **24** |

---

## Comparison with Maximum Sum Subarray

| Property | Max Sum (Kadane's) | Max Product |
|---|---|---|
| Negatives are... | Always harmful | Dangerous but potentially valuable |
| What you track | One running value | Two running values (max and min) |
| Sign flip possible? | No | Yes — negative × negative = positive |
| Zero handling | Resets naturally | Resets naturally |
| Time complexity | O(n) | O(n) |
| Space complexity | O(1) | O(1) |

The structural difference is this: in the sum problem, information about negative prefixes is useless and can safely be discarded. In the product problem, the most negative prefix is precious because it holds the seed of the largest possible future positive. Discarding it produces wrong answers.

---

## Complexity

| | Complexity |
|---|---|
| **Time** | O(n) — single pass through the array |
| **Space** | O(1) — only three integer variables at any point |

---

## Mental Model

Think of `maxProd` and `minProd` as two runners moving through the array side by side:

- When the next number is **positive**: the max runner stays in the lead, the min runner stays behind.
- When the next number is **negative**: the two runners **swap positions** instantly — the former worst becomes the new best.
- When the next number is **zero**: both runners reset to the starting line.

The algorithm's job is simply to keep both runners on the track at all times. Even when one looks hopelessly far behind, the next negative in the array could instantly put them in first place.