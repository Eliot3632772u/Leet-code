# Max Number of K-Sum Pairs

## Problem Statement

Given an integer array `nums` and an integer `k`, in one operation you can pick two numbers from the array whose sum equals `k` and remove them from the array.

Return the **maximum number of operations** you can perform on the array.

**Example**

```
Input:  nums = [1,2,3,4], k = 5
Output: 2
Explanation: (1,4) and (2,3) sum to 5.

Input:  nums = [3,1,3,4,3], k = 6
Output: 1
Explanation: only (3,3) sums to 6; the third 3 has no partner left.
```

## Solution

```java
class Solution {
    public int maxOperations(int[] nums, int k) {

        int ops = 0;
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {

            Integer need = map.get(k - nums[i]);
            if (need != null && need > 0) {
                map.put(k - nums[i], need - 1);
                ops++;
            } else
                map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }

        return ops;
    }
}
```

## How It Works

The solution makes a **single left-to-right pass** over `nums`, using a `HashMap<Integer, Integer>` as a frequency counter of "unmatched" values seen so far.

For each number `nums[i]`, there are only two possibilities:

1. **Its complement is already waiting in the map.** The complement is `k - nums[i]` — the value that, added to `nums[i]`, gives `k`. If the map shows that complement has an available (unused) count, we've found a pair. We:
   - decrement the complement's count by 1 (one instance of it is now "used up"),
   - increment `ops`.

2. **No available complement exists.** Either the complement was never seen, or every prior occurrence of it has already been paired off. In that case, `nums[i]` itself becomes a candidate for a *future* number to pair with, so we add it to the map (`count + 1`).

Because every element is either immediately consumed as a match or stored for later, each element is touched **once**, giving an `O(n)` scan with `O(1)` map operations.

### Walking through `[3,1,3,4,3], k = 6`

| i | nums[i] | complement (6 - nums[i]) | in map w/ count > 0? | action | map state | ops |
|---|---------|--------------------------|-----------------------|--------|-----------|-----|
| 0 | 3 | 3 | no (empty map) | store 3 | `{3:1}` | 0 |
| 1 | 1 | 5 | no | store 1 | `{3:1, 1:1}` | 0 |
| 2 | 3 | 3 | **yes** (count 1) | match! decrement `map[3]` to 0 | `{3:0, 1:1}` | 1 |
| 3 | 4 | 2 | no | store 4 | `{3:0, 1:1, 4:1}` | 1 |
| 4 | 3 | 3 | no (count is 0) | store 3 | `{3:1, 1:1, 4:1}` | 1 |

Final answer: `ops = 1`, matching the expected output. Note how the third `3` at index 4 correctly finds *no* partner — the map's count for `3` had already dropped to `0` after the first pair was consumed, so it's treated as a fresh, unmatched value.

## Why It Works

The key invariant is: **at any point during the scan, `map` holds exactly the counts of numbers seen so far that have not yet been paired.**

This invariant is maintained by induction on each step:

- **Base case:** before processing any element, the map is empty — trivially true (no numbers seen, none paired).
- **Inductive step:** assume the invariant holds before processing `nums[i]`.
  - If `k - nums[i]` has a positive count, that means there's a genuine unmatched earlier element equal to `k - nums[i]`. Pairing it with `nums[i]` is always safe — greedily pairing here can never block a better solution, since any two equal values are interchangeable for matching purposes (the algorithm only cares about *values* and *counts*, not which specific index a value came from).
  - If no unmatched complement exists, `nums[i]` has nothing to pair with *yet*. Storing it preserves the invariant, since it's now an unmatched value available for a future match.

This greedy "match as soon as possible" strategy is optimal here because:
- The problem only cares about the **count of pairs**, not which specific elements form them.
- If two equal values `v` exist and both could theoretically match different future elements, it doesn't matter *which* occurrence of `v` gets used — they're indistinguishable. So matching the earliest available one greedily never costs us a pairing we could have made otherwise.
- Order of array traversal is irrelevant to the final count, since summing to `k` is a symmetric, position-independent relationship — only the multiset of values matters.

Hence a single greedy pass with a frequency map finds the true maximum number of pairs, not just a locally good one.

## Complexity

| | Complexity |
|---|---|
| Time | `O(n)` — one pass, `O(1)` amortized HashMap operations |
| Space | `O(n)` — worst case, all elements stored in the map (no pairs formed) |

## Edge Cases Handled

- **Duplicate values that sum to `k` with themselves** (e.g. `k = 6`, value `3`): handled correctly because the map tracks *counts*, not just presence — see the walkthrough above.
- **No valid pairs exist:** the map fills up with unmatched values, `ops` stays `0`.
- **Odd number of matching elements:** the leftover element simply remains in the map, unpaired, at the end — it's never revisited, which is correct since there's nothing left in the array for it to pair with.