# Container With Most Water — Two Pointer Solution

## Problem Statement

Given an integer array `height` of length `n`, where each element represents a vertical line drawn at position `i` with height `height[i]`, find two lines that together with the x-axis form a container that holds the **most water**.

Return the **maximum amount of water** a container can store.

> The container cannot be slanted — water level is capped by the **shorter** of the two walls.

---

## The Formula

For any two walls at indices `l` and `r`:

```
area = min(height[l], height[r]) × (r - l)
         ↑ shorter wall limits height    ↑ distance between walls
```

---

## Approaches

### ❌ Brute Force — O(n²)

Check every possible pair `(i, j)` and track the maximum area.  
Works correctly but is too slow for large inputs.

```java
int max = Integer.MIN_VALUE;
for (int i = 0; i < height.length; i++) {
    for (int j = i + 1; j < height.length; j++) {
        int container;
        if (height[i] < height[j]) container = height[i] * (j - i);
        else container = height[j] * (j - i);
        if (max < container) max = container;
    }
}
return max;
```

### ✅ Two Pointers — O(n)

Start with the widest possible container and shrink inward — but only ever move the **shorter wall**.

```java
int max = Integer.MIN_VALUE;
int l = 0;
int r = height.length - 1;

while (l < r) {
    int container = Math.min(height[l], height[r]) * (r - l);
    if (max < container) max = container;

    if (height[l] < height[r]) l++;
    else r--;
}

return max;
```

---

## Why The Two Pointer Strategy Works

This is the core insight — **why is it safe to discard the shorter wall?**

Suppose `height[l] < height[r]`. The current area is:

```
area = height[l] × (r - l)
```

Now consider every other pairing for `l` while keeping it fixed:

- Any pointer we could try for the right side is **closer** to `l` (smaller `r - l`)
- The height is still capped by `height[l]` at best (since `height[l]` is the bottleneck)
- So every other pairing with the same `l` gives `area ≤ height[l] × (r - l)`

This means **no future pair involving the current `l` can beat the area we already computed for it**.  
We can safely discard `l` and move it inward. The same logic applies symmetrically when `height[r] < height[l]`.

> **Key principle:** Moving the taller wall inward can only make things worse (shorter distance, same or smaller height cap). So we always move the shorter wall — it's the only side that has any chance of improving the area.

---

## Step-by-Step Example

**Input:** `height = [1, 8, 6, 2, 5, 4, 8, 3, 7]`

```
Index:  0   1   2   3   4   5   6   7   8
Value:  1   8   6   2   5   4   8   3   7
```

---

**Initial state:** `l = 0`, `r = 8`, `max = MIN`

---

**Step 1**
```
l=0 (h=1),  r=8 (h=7)
area = min(1, 7) × (8 - 0) = 1 × 8 = 8
max = 8
height[l] < height[r] → move l right
```

---

**Step 2**
```
l=1 (h=8),  r=8 (h=7)
area = min(8, 7) × (8 - 1) = 7 × 7 = 49
max = 49
height[l] > height[r] → move r left
```

---

**Step 3**
```
l=1 (h=8),  r=7 (h=3)
area = min(8, 3) × (7 - 1) = 3 × 6 = 18
max = 49
height[l] > height[r] → move r left
```

---

**Step 4**
```
l=1 (h=8),  r=6 (h=8)
area = min(8, 8) × (6 - 1) = 8 × 5 = 40
max = 49
height[l] == height[r] → move r left (else branch)
```

---

**Step 5**
```
l=1 (h=8),  r=5 (h=4)
area = min(8, 4) × (5 - 1) = 4 × 4 = 16
max = 49
height[l] > height[r] → move r left
```

---

**Step 6**
```
l=1 (h=8),  r=4 (h=5)
area = min(8, 5) × (4 - 1) = 5 × 3 = 15
max = 49
height[l] > height[r] → move r left
```

---

**Step 7**
```
l=1 (h=8),  r=3 (h=2)
area = min(8, 2) × (3 - 1) = 2 × 2 = 4
max = 49
height[l] > height[r] → move r left
```

---

**Step 8**
```
l=1 (h=8),  r=2 (h=6)
area = min(8, 6) × (2 - 1) = 6 × 1 = 6
max = 49
height[l] > height[r] → move r left
```

---

**Step 9**
```
l=1, r=1 → l == r → loop ends
```

---

**Output:** `49` ✅

The maximum container is formed by walls at index `1` (height 8) and index `8` (height 7).

```
        |           |
        |           |
        |           |
        |           |
        |           |
        |           |
        |           |
        |___________|
  idx:  1           8
 dist:      7 wide
height:  min(8,7) = 7
 area:      7 × 7 = 49
```

---

## Complexity Analysis

| | Brute Force | Two Pointers |
|---|---|---|
| **Time** | O(n²) | O(n) |
| **Space** | O(1) | O(1) |

The two pointer approach makes a **single left-to-right pass**, shrinking the window by one step at a time — guaranteed to terminate in at most `n - 1` iterations.

---

## Summary

| Concept | Detail |
|---|---|
| Start wide | Begin with `l=0, r=n-1` — maximum possible width |
| Shrink inward | Move one pointer per step |
| Move which pointer? | Always move the **shorter wall** |
| Why? | The shorter wall is the bottleneck — keeping it can never improve the area |
| Termination | Stop when `l` and `r` meet |