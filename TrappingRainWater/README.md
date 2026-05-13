# 🌧️ Trapping Rain Water

## Problem Summary

Given an array `height` where each element represents the height of a vertical bar, compute how much **rainwater can be trapped** between the bars after it rains.

Water can only be trapped where there are taller bars on **both sides** to contain it. Any water at the edges spills off immediately.

---

## Intuition

The key question for every bar at index `i` is:

> **How much water sits on top of this bar?**

Water above a bar is determined by the **shorter of the two tallest walls** surrounding it — one to its left, one to its right. The water level can only rise as high as the shorter wall allows. After that, it spills over.

Then, the actual water above the bar is that water level minus the bar's own height (if the bar is taller than the water level, no water sits on it).

```
water[i] = max(0,  min(tallest wall on left,  tallest wall on right)  −  height[i])
```

The challenge is computing those two "tallest wall" values **efficiently for every index**. That's exactly what this solution pre-computes.

---

## Strategy: Pre-compute Left Max and Right Max Arrays

Instead of scanning left and right from every bar (which would be O(n²)), we make **two single passes** to build two helper arrays:

| Array | `hlm[i]` | `hrm[i]` |
|-------|----------|----------|
| Meaning | Tallest bar **strictly to the left** of index `i` | Tallest bar **strictly to the right** of index `i` |
| Built by | Left-to-right pass | Right-to-left pass |

With these arrays ready, the final answer is computed in one more pass — **three O(n) passes total**, O(n) space.

---

## Solution

```java
class Solution {
    public static int trap(int[] height) {

        int[] hlm = new int[height.length]; // left max for each index
        int[] hrm = new int[height.length]; // right max for each index

        // Pass 1: fill hlm — tallest bar to the LEFT of each index
        int maxl = 0;
        for (int i = 0; i < height.length; i++) {
            hlm[i] = maxl;
            maxl = Math.max(maxl, height[i]);
        }

        // Pass 2: fill hrm — tallest bar to the RIGHT of each index
        int maxr = 0;
        for (int j = height.length - 1; j >= 0; j--) {
            hrm[j] = maxr;
            maxr = Math.max(maxr, height[j]);
        }

        // Pass 3: compute trapped water at each index
        int sum = 0;
        for (int i = 0; i < height.length; i++) {
            int currSum = Math.min(hlm[i], hrm[i]) - height[i];
            sum += currSum > 0 ? currSum : 0;
        }

        return sum;
    }
}
```

---

## Deep Dive: How Each Pass Works

### Pass 1 — Build `hlm` (Left Max Array)

```java
int maxl = 0;
for (int i = 0; i < height.length; i++) {
    hlm[i] = maxl;           // store the max seen SO FAR (before reaching i)
    maxl = Math.max(maxl, height[i]); // then update with current bar
}
```

We store `maxl` **before** updating it with `height[i]`. This ensures `hlm[i]` contains the tallest bar strictly to the **left** of `i`, not including `i` itself.

- `hlm[0] = 0` — nothing to the left of the first bar
- `hlm[1] = height[0]` — only bar 0 is to the left
- `hlm[i] = max(height[0], height[1], ..., height[i-1])`

---

### Pass 2 — Build `hrm` (Right Max Array)

```java
int maxr = 0;
for (int j = height.length - 1; j >= 0; j--) {
    hrm[j] = maxr;
    maxr = Math.max(maxr, height[j]);
}
```

Same logic, but mirrored — we scan **right to left**, storing `maxr` before updating it.

- `hrm[last] = 0` — nothing to the right of the last bar
- `hrm[i] = max(height[i+1], height[i+2], ..., height[last])`

---

### Pass 3 — Compute Trapped Water

```java
int currSum = Math.min(hlm[i], hrm[i]) - height[i];
sum += currSum > 0 ? currSum : 0;
```

For each bar:
1. The water level above it is `min(hlm[i], hrm[i])` — capped by the shorter surrounding wall.
2. Subtract `height[i]` — the bar itself displaces that much water.
3. If the result is negative (bar is taller than its surrounding walls), no water sits here — clamp to 0.

---

## Step-by-Step Walkthrough

### Input

```
height = [3, 0, 2, 0, 4]
index:    0  1  2  3  4
```

---

### Pass 1: Build `hlm` (left to right)

We track `maxl` — the running maximum of everything seen so far — and store it **before** consuming the current bar.

| i | height[i] | hlm[i] = maxl before | maxl after |
|---|-----------|----------------------|------------|
| 0 | 3         | 0                    | 3          |
| 1 | 0         | 3                    | 3          |
| 2 | 2         | 3                    | 3          |
| 3 | 0         | 3                    | 3          |
| 4 | 4         | 3                    | 4          |

```
hlm = [0, 3, 3, 3, 3]
```

---

### Pass 2: Build `hrm` (right to left)

Same idea, scanning right to left.

| j | height[j] | hrm[j] = maxr before | maxr after |
|---|-----------|----------------------|------------|
| 4 | 4         | 0                    | 4          |
| 3 | 0         | 4                    | 4          |
| 2 | 2         | 4                    | 4          |
| 1 | 0         | 4                    | 4          |
| 0 | 3         | 4                    | 4          |

```
hrm = [4, 4, 4, 4, 0]
```

---

### Pass 3: Compute Water at Each Bar

```
formula: water[i] = max(0,  min(hlm[i], hrm[i])  −  height[i])
```

| i | hlm[i] | hrm[i] | min | height[i] | water | Reasoning |
|---|--------|--------|-----|-----------|-------|-----------|
| 0 | 0      | 4      | 0   | 3         | 0     | Left wall is 0 — water spills left |
| 1 | 3      | 4      | 3   | 0         | **3** | Walls of height 3 and 4 → water level = 3, bar = 0 |
| 2 | 3      | 4      | 3   | 2         | **1** | Water level = 3, bar = 2, net = 1 |
| 3 | 3      | 4      | 3   | 0         | **3** | Same walls, bar = 0 |
| 4 | 3      | 0      | 0   | 4         | 0     | Right wall is 0 — water spills right |

```
Total = 0 + 3 + 1 + 3 + 0 = 7 units
```

---

---

## Classic Example: LeetCode Test Case

### Input

```
height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
index:    0  1  2  3  4  5  6  7  8  9 10 11
```

**After all three passes:**

```
hlm = [0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3]
hrm = [3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 1, 0]
```

| i | hlm | hrm | min | h[i] | water |
|---|-----|-----|-----|------|-------|
| 0 | 0   | 3   | 0   | 0    | 0     |
| 1 | 0   | 3   | 0   | 1    | 0     |
| 2 | 1   | 3   | 1   | 0    | **1** |
| 3 | 1   | 3   | 1   | 2    | 0     |
| 4 | 2   | 3   | 2   | 1    | **1** |
| 5 | 2   | 3   | 2   | 0    | **2** |
| 6 | 2   | 3   | 2   | 1    | **1** |
| 7 | 2   | 2   | 2   | 3    | 0     |
| 8 | 3   | 2   | 2   | 2    | 0     |
| 9 | 3   | 2   | 2   | 1    | **1** |
|10 | 3   | 1   | 1   | 2    | 0     |
|11 | 3   | 0   | 0   | 1    | 0     |

```
Total = 1 + 1 + 2 + 1 + 1 = 6 units ✅
```

---

## Edge Cases

| Scenario | Input | Output |
|---|---|---|
| Empty array | `[]` | `0` |
| Single bar | `[5]` | `0` |
| Monotonically increasing | `[1, 2, 3, 4]` | `0` — water spills right at every bar |
| Monotonically decreasing | `[4, 3, 2, 1]` | `0` — water spills left at every bar |
| Flat array | `[3, 3, 3]` | `0` — no height difference to trap anything |
| Valley | `[3, 0, 3]` | `3` — perfect bowl |

---

## Complexity Analysis

| | Complexity | Reason |
|---|---|---|
| **Time** | O(n) | Three separate single-pass loops over the array |
| **Space** | O(n) | Two auxiliary arrays `hlm` and `hrm` of size n |

---

## Key Takeaways

1. **Water at any bar depends on its two surrounding walls.** Specifically, `min(leftMax, rightMax) - height[i]`. The shorter wall is always the bottleneck.
2. **Pre-computing left and right maxes turns O(n²) into O(n).** Without the helper arrays, you'd scan left and right from every bar — wasteful. One pass each direction builds everything you need.
3. **Store max BEFORE updating, not after.** `hlm[i] = maxl` must happen before `maxl = Math.max(maxl, height[i])`. Getting this order wrong is the most common bug in this solution — it would include the current bar in its own wall calculation.
4. **Clamp to zero for bars taller than their walls.** When `min(hlm[i], hrm[i]) < height[i]`, the result is negative — no water sits on tall bars, so we add 0 instead.
5. **Both edge bars always trap 0 water.** `hlm[0]` and `hrm[last]` are always 0 by definition — there is no wall on the outer side, so water at the edges always spills off.