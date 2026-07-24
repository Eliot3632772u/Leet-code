# Max Consecutive Ones III — Sliding Window Solution

## Problem

Given a binary array `nums` and an integer `k`, find the length of the longest subarray containing only `1`s, if you are allowed to flip at most `k` `0`s to `1`s.

**Example**
```
nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
Output: 6
```

## The Core Idea

Flipping at most `k` zeros to ones is equivalent to asking: **what is the longest window in the array that contains at most `k` zeros?**

Once you reframe the problem this way, it becomes a classic **variable-size sliding window** problem. Instead of physically flipping bits, we just track how many zeros are currently inside our window and make sure that count never exceeds `k`.

## The Solution

```java
class Solution {
    public int longestOnes(int[] nums, int k) {
        int l = 0, r = 0;
        int max = 0;

        while (r < nums.length) {
            if (nums[r] == 1 || (nums[r] == 0 && k > 0)) {
                if (nums[r] == 0) k--;
                max = Math.max(max, r - l + 1);
                r++;
            } else {
                if (nums[l] == 0) k++;
                l++;
            }
        }

        return max;
    }
}
```

## How It Works

The algorithm keeps two pointers, `l` (left) and `r` (right), that define the current window `[l, r]`. `k` doubles as **both** a parameter and a **live budget counter** — it starts as "how many flips I'm allowed" and decreases every time a zero enters the window, then increases again when a zero leaves. This reuse of `k` is the cleverest part of the implementation, so it's worth tracing carefully.

### Step by step

1. **Try to grow the window.**
   At each iteration we look at `nums[r]`. We're allowed to include it if it's a `1` (free), or if it's a `0` and we still have budget (`k > 0`).
   - If we include a `0`, we spend one unit of budget: `k--`.
   - We update `max` with the new window size `r - l + 1`.
   - We advance `r` to look at the next element.

2. **Shrink the window when the budget is exhausted.**
   If `nums[r]` is a `0` and `k == 0`, we cannot include it without exceeding the allowed number of flips. Instead of resetting the window or moving `r` past it, we shrink from the **left**:
   - If the element leaving the window (`nums[l]`) was a `0`, we refund the budget: `k++`.
   - We advance `l`.
   - Note `r` does **not** move this iteration — we retry the same `r` against the new, smaller window on the next loop pass.

3. **Repeat until `r` reaches the end of the array.**
   `max` always holds the largest valid window size seen so far.

### Why the window never needs to shrink below the best size found

A subtle but important property of this pattern: once the window becomes invalid (more than `k` zeros), we shrink it by exactly one from the left — just enough to make it valid again — rather than collapsing it back to size zero. This means the window size is **monotonically non-decreasing** in a global sense: it never shrinks below the largest size it has already achieved. That's why `max` can simply be updated every time we successfully extend `r`, without needing to separately check "is the current window bigger than before" after every shrink.

## Why It Works

This is a proof sketch for why the greedy "expand right, shrink left only when necessary" strategy is correct — not just fast.

**Claim:** At every point in the loop, `[l, r]` is the largest window ending at position `r` (before advancing `r`) that contains at most `k'` zeros, where `k'` is the *original* `k` passed into the function.

- **Invariant:** the window `[l, r]` always contains at most the original `k` zeros. This holds because we only ever extend `r` into the window when doing so keeps the zero-count within budget, and whenever the budget hits zero and we'd need another flip, we shrink from the left until a slot frees up.
- **Left pointer only moves forward:** because array indices only increase, `l` never revisits an earlier position. This guarantees each element is added to the window and removed from the window **at most once**, which is what gives the algorithm its linear time complexity — there's no backtracking or re-scanning.
- **No missed opportunities:** suppose a longer valid window existed that this algorithm didn't find. Its right boundary would be some index `r`. But the algorithm, upon reaching that same `r`, always maintains the *smallest possible* `l` for which `[l, r]` is valid (it only shrinks `l` when forced to, and only by the minimum amount needed). So the window the algorithm has at `r` is already the maximal valid window ending at `r` — nothing longer was possible.

Because the algorithm considers every possible right endpoint and, for each one, finds the best matching left endpoint in amortized O(1) work, taking the max over all these windows is guaranteed to find the true global optimum.

## Complexity

| Metric | Complexity | Reasoning |
|---|---|---|
| Time | `O(n)` | Each pointer (`l` and `r`) traverses the array at most once — no element is visited more than a constant number of times. |
| Space | `O(1)` | Only a fixed number of scalar variables (`l`, `r`, `max`, `k`) are used, regardless of input size. |

## Dry Run

`nums = [1,1,1,0,0,0,1,1,1,1,0]`, `k = 2`

| r | nums[r] | action | window | k | max |
|---|---|---|---|---|---|
| 0 | 1 | expand | [0,0] | 2 | 1 |
| 1 | 1 | expand | [0,1] | 2 | 2 |
| 2 | 1 | expand | [0,2] | 2 | 3 |
| 3 | 0 | expand (spend) | [0,3] | 1 | 4 |
| 4 | 0 | expand (spend) | [0,4] | 0 | 5 |
| 5 | 0 | budget = 0 → shrink (nums[0]=1, no refund) | l=1 | 0 | 5 |
| 5 | 0 | still no budget → shrink (nums[1]=1, no refund) | l=2 | 0 | 5 |
| 5 | 0 | still no budget → shrink (nums[2]=1, no refund) | l=3 | 0 | 5 |
| 5 | 0 | still no budget → shrink (nums[3]=0, refund) | l=4, k=1 | 1 | 5 |
| 5 | 0 | expand (spend) | [4,5] | 0 | 5 |
| 6 | 1 | expand | [4,6] | 0 | 5 |
| 7 | 1 | expand | [4,7] | 0 | 5 |
| 8 | 1 | expand | [4,8] | 0 | 5 |
| 9 | 1 | expand | [4,9] | 0 | 6 |
| 10 | 0 | budget = 0 → shrink (nums[4]=0, refund) | l=5, k=1 | ... | 6 |
| 10 | 0 | expand (spend) | [5,10] | 0 | 6 |

Final answer: **6**, achieved at `r=9` with window `[4,9]` = `nums[4..9] = [0,0,1,1,1,1]`, which contains exactly 2 zeros — right at the flip budget.

## Key Takeaways

- Reframe "at most k flips" problems as "at most k of something forbidden in the window" — this turns a simulation problem into a counting problem.
- Reusing a single variable as both a budget and a live counter (rather than tracking a separate zero-count) keeps the code compact, but it's important to trace through carefully to see *why* it stays correct.
- The window never shrinks past its best-known size, which is what makes it safe to take the max only on expansion steps.