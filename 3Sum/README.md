# 3Sum — Two Pointer Solution

## Problem Statement

Given an integer array `nums`, return all **unique triplets** `[nums[i], nums[j], nums[k]]` such that:

- `i`, `j`, and `k` are distinct indices
- `nums[i] + nums[j] + nums[k] == 0`

The solution set must not contain duplicate triplets.

---

## Approach: Sorting + Two Pointers + HashSet

### Key Ideas

1. **Sort** the array so that duplicates are adjacent and the two-pointer technique becomes applicable.
2. **Fix one element** (`nums[i]`) and use two pointers (`l` and `r`) to find pairs that sum to `-nums[i]`.
3. Use a **HashSet** of lists to automatically discard duplicate triplets.

---

## How It Works

### Step 1 — Sort the Array

```
nums = [-1, 0, 1, 2, -1, -4]
sorted → [-4, -1, -1, 0, 1, 2]
```

Sorting enables the two-pointer scan and makes equal triplets produce identical lists, so the HashSet can deduplicate them.

---

### Step 2 — Outer Loop (Fix `i`)

Iterate `i` from index `0` to `nums.length - 3` (inclusive).  
For each `i`, we look for two numbers in the **rest of the array** that sum to `-nums[i]`.

---

### Step 3 — Two Pointer Scan (Fix `l` and `r`)

Set `l = i + 1` (just right of `i`) and `r = nums.length - 1` (far end).

| Condition | Action |
|-----------|--------|
| `sum == 0` | Record the triplet, then continue scanning |
| `sum < 0` | Move `l` right → need a larger value |
| `sum > 0` | Move `r` left → need a smaller value |

Because the array is sorted, moving the pointers inward is guaranteed to either increase or decrease the sum.

---

### Step 4 — Deduplication via HashSet

Every found triplet is added to a `HashSet<List<Integer>>`.  
Since the array is sorted and the triplet list is also sorted, two equal triplets always produce the **exact same list object**, which the HashSet silently rejects as a duplicate.

---

## Step-by-Step Example

**Input:** `nums = [-1, 0, 1, 2, -1, -4]`

### After Sorting

```
Index:  0    1    2   3   4   5
Value: -4   -1   -1   0   1   2
```

---

### Iteration i = 0 → nums[i] = -4, target pair sum = 4

```
[-4, -1, -1, 0, 1, 2]
  i   l              r
```

| l | r | nums[l] | nums[r] | sum | Action |
|---|---|---------|---------|-----|--------|
| 1 | 5 | -1 | 2 | -4+(-1)+2 = -3 | sum < 0 → l++ |
| 2 | 5 | -1 | 2 | -4+(-1)+2 = -3 | sum < 0 → l++ |
| 3 | 5 |  0 | 2 | -4+0+2 = -2    | sum < 0 → l++ |
| 4 | 5 |  1 | 2 | -4+1+2 = -1    | sum < 0 → l++ |
| 5 | 5 | — | — | l == r → stop  | — |

No triplets found.

---

### Iteration i = 1 → nums[i] = -1, target pair sum = 1

```
[-4, -1, -1, 0, 1, 2]
      i   l          r
```

| l | r | nums[l] | nums[r] | sum | Action |
|---|---|---------|---------|-----|--------|
| 2 | 5 | -1 | 2 | -1+(-1)+2 = 0 | ✅ Found [-1,-1,2] → set |
| 2 | 5 | — | — | sum == 0 → else branch: r-- |
| 2 | 4 | -1 | 1 | -1+(-1)+1 = -1 | sum < 0 → l++ |
| 3 | 4 |  0 | 1 | -1+0+1 = 0    | ✅ Found [-1,0,1] → set |
| 3 | 4 | — | — | sum == 0 → else branch: r-- |
| 3 | 3 | — | — | l == r → stop |

Triplets found: `[-1, -1, 2]`, `[-1, 0, 1]`

---

### Iteration i = 2 → nums[i] = -1, target pair sum = 1

```
[-4, -1, -1, 0, 1, 2]
           i  l      r
```

| l | r | nums[l] | nums[r] | sum | Action |
|---|---|---------|---------|-----|--------|
| 3 | 5 | 0 | 2 | -1+0+2 = 1 | sum > 0 → r-- |
| 3 | 4 | 0 | 1 | -1+0+1 = 0 | ✅ Found [-1,0,1] → HashSet rejects (duplicate) |
| 3 | 4 | — | — | sum == 0 → else branch: r-- |
| 3 | 3 | — | — | l == r → stop |

---

### Iteration i = 3 → nums[i] = 0, target pair sum = 0

```
[-4, -1, -1, 0, 1, 2]
               i  l  r
```

| l | r | nums[l] | nums[r] | sum | Action |
|---|---|---------|---------|-----|--------|
| 4 | 5 | 1 | 2 | 0+1+2 = 3 | sum > 0 → r-- |
| 4 | 4 | — | — | l == r → stop |

No new triplets.

---

### Final Result

```
[[-1, -1, 2], [-1, 0, 1]]
```

---

## Complexity Analysis

| | Complexity |
|---|---|
| **Time** | O(n²) — O(n log n) to sort + O(n²) for the nested loop/two-pointer scan |
| **Space** | O(k) — where k is the number of unique triplets stored in the HashSet |

---

## Why It Works

- **Sorting** makes it possible to use two pointers, because moving `l` right always increases the sum and moving `r` left always decreases it — giving us a deterministic search strategy.
- **Two pointers** reduce the inner search from O(n²) to O(n), bringing the overall complexity down from O(n³) (brute force) to O(n²).
- **HashSet deduplication** is correct here because the sorted array guarantees that any two identical triplets will always be stored in the same order, producing equal `List<Integer>` objects that Java's HashSet correctly identifies as duplicates.

---

## Code

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        HashSet<List<Integer>> set = new HashSet();
        Arrays.sort(nums);

        int l, r;
        for (int i = 0; i < nums.length - 2; i++) {
            l = i + 1;
            r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum == 0) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[l]);
                    list.add(nums[r]);
                    list.sort(null);
                    set.add(list);
                }
                if (sum < 0) l++;
                else r--;
            }
        }
        return new ArrayList<List<Integer>>(set);
    }
}
```

---