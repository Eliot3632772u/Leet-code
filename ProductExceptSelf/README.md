# Product of Array Except Self — Prefix & Suffix Product Solution

## Problem

Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the product of all elements of `nums` **except** `nums[i]`.

The solution must run in **O(n)** time and must **not** use division.

**Example:**
```
Input:  [1, 2, 3, 4]
Output: [24, 12, 8, 6]
```

---

## Core Idea

For any index `i`, the answer is:

```
answer[i] = (product of everything to the LEFT of i)
           * (product of everything to the RIGHT of i)
```

So if we precompute those two values for every index, the final result is just a pair of multiplications per element — no division needed.

We do this with two arrays:

- `l[i]` — the product of all elements **strictly to the left** of index `i`
- `r[i]` — the product of all elements **strictly to the right** of index `i`

Both arrays are initialized to `1` everywhere, because the identity for multiplication is 1 — an index with nothing to its left (or right) contributes a factor of 1.

---

## The Loop

```java
for (int i = 1, j = s - 2; i < s; i++, j--) {
    l[i] = nums[i - 1] * l[i - 1];
    r[j] = nums[j + 1] * r[j + 1];
}
```

Both prefix and suffix arrays are built **simultaneously** in a single loop:

- `i` starts at `1` and moves right → builds `l` left to right
- `j` starts at `s - 2` and moves left → builds `r` right to left

`l[i]` says: "the product of everything before me is whatever was accumulated before `i-1`, times `nums[i-1]` itself."  
`r[j]` says: "the product of everything after me is whatever was accumulated after `j+1`, times `nums[j+1]` itself."

---

## Step-by-Step Example

Input: `nums = [1, 2, 3, 4]`, `s = 4`.

### Step 1 — Initialize

```
l = [1, 1, 1, 1]
r = [1, 1, 1, 1]
```

### Step 2 — Build `l` and `r` simultaneously

**Iteration 1** — `i = 1`, `j = 2`
```
l[1] = nums[0] * l[0] = 1 * 1 = 1
r[2] = nums[3] * r[3] = 4 * 1 = 4

l = [1, 1, 1, 1]
r = [1, 1, 4, 1]
```

**Iteration 2** — `i = 2`, `j = 1`
```
l[2] = nums[1] * l[1] = 2 * 1 = 2
r[1] = nums[2] * r[2] = 3 * 4 = 12

l = [1, 1, 2, 1]
r = [1, 12, 4, 1]
```

**Iteration 3** — `i = 3`, `j = 0`
```
l[3] = nums[2] * l[2] = 3 * 2 = 6
r[0] = nums[1] * r[1] = 2 * 12 = 24

l = [1,  1,  2,  6]
r = [24, 12, 4,  1]
```

**Final arrays:**
```
l = [1,  1,  2,  6]   ← products of everything to the LEFT of each index
r = [24, 12, 4,  1]   ← products of everything to the RIGHT of each index
```

Verify `l` manually:
- `l[0]` = nothing to the left → 1
- `l[1]` = `nums[0]` → 1
- `l[2]` = `nums[0] * nums[1]` → 1 × 2 = 2
- `l[3]` = `nums[0] * nums[1] * nums[2]` → 1 × 2 × 3 = 6 ✓

Verify `r` manually:
- `r[3]` = nothing to the right → 1
- `r[2]` = `nums[3]` → 4
- `r[1]` = `nums[3] * nums[2]` → 4 × 3 = 12
- `r[0]` = `nums[3] * nums[2] * nums[1]` → 4 × 3 × 2 = 24 ✓

### Step 3 — Multiply `l[i] * r[i]` for each index

```java
for (int i = 0; i < s; i++) {
    nums[i] = r[i] * l[i];
}
```

| i | l[i] | r[i] | answer[i] |
|---|------|------|-----------|
| 0 | 1    | 24   | **24**    |
| 1 | 1    | 12   | **12**    |
| 2 | 2    | 4    | **8**     |
| 3 | 6    | 1    | **6**     |

```
Output: [24, 12, 8, 6] ✓
```

---

## Why No Division?

The naive approach would be to compute the total product of the entire array and divide by `nums[i]` for each index. That breaks down in two ways: it requires division (forbidden by the problem), and it fails entirely when the array contains zeros. The prefix/suffix approach sidesteps both issues completely — it never divides anything.

---

## Complexity Analysis

| | Complexity |
|---|---|
| **Time** | O(n) — one combined pass to build `l` and `r`, one pass to produce output |
| **Space** | O(n) — two auxiliary arrays of size `n` |

The result is written back into `nums` itself, reusing the input array as the output rather than allocating a third array.