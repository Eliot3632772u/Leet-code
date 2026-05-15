# 🧗 Climb Stairs — LeetCode #70

## Problem Statement

You are climbing a staircase with **n** steps. Each time you can either climb **1** or **2** steps. In how many distinct ways can you climb to the top?

**Example:**
```
Input: n = 4
Output: 5

Explanation:
1+1+1+1
1+1+2
1+2+1
2+1+1
2+2
```

---

## Solution — Dynamic Programming (Bottom-Up)

```java
class Solution {
    public int climbStairs(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
```

---

## Why It Works

The key insight is that **the number of ways to reach step `i` depends only on the two steps before it**.

> To land on step `i`, you must have come from either step `i-1` (took 1 step) or step `i-2` (took 2 steps).

So the total number of ways to reach step `i` is:

```
ways(i) = ways(i-1) + ways(i-2)
```

This is exactly the **Fibonacci recurrence**. The problem reduces to finding the (n+1)-th Fibonacci number.

### Base Cases

| Step | Ways | Reasoning |
|------|------|-----------|
| `dp[0] = 1` | 1 | One way to be at the start (do nothing) |
| `dp[1] = 1` | 1 | Only one way to reach step 1 (take one 1-step) |

---

## How It Works — Step by Step

Let's trace through `n = 5`:

| Step `i` | `dp[i-2]` | `dp[i-1]` | `dp[i]` |
|----------|-----------|-----------|---------|
| 0        | —         | —         | 1       |
| 1        | —         | —         | 1       |
| 2        | 1         | 1         | **2**   |
| 3        | 1         | 2         | **3**   |
| 4        | 2         | 3         | **5**   |
| 5        | 3         | 5         | **8**   |

**Result:** `dp[5] = 8` — there are 8 distinct ways to climb 5 stairs.

### Visualization for n = 4

```
Start ──► Step 1 ──► Step 2 ──► Step 3 ──► Step 4 ✓
           (1)        (2)        (3)         (5)
           ways       ways       ways        ways
```

Each cell accumulates the total paths from all valid previous positions.

---

## Complexity Analysis

| | Complexity | Reason |
|---|---|---|
| **Time** | `O(n)` | Single loop from 2 to n |
| **Space** | `O(n)` | dp array of size n+1 |

### Space Optimization (Optional)

Since we only ever look back **two steps**, we can reduce space to `O(1)`:

```java
class Solution {
    public int climbStairs(int n) {
        if (n == 1) return 1;
        int prev2 = 1, prev1 = 1;
        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

---

## Key Takeaway

> This problem is a classic introduction to **Dynamic Programming**: break a problem into overlapping subproblems, store their results, and build up the final answer — avoiding redundant recomputation.

The pattern `dp[i] = dp[i-1] + dp[i-2]` appears in many DP problems. Recognizing it here is the first step toward mastering the technique.