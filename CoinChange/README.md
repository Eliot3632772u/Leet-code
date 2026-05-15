# 🪙 Coin Change — LeetCode #322

## Problem Statement

Given an array of coin denominations `coins` and a target `amount`, return the **fewest number of coins** needed to make up that amount. If it cannot be done, return `-1`.

**Example:**
```
Input:  coins = [1, 3, 4, 5],  amount = 7
Output: 2

Explanation: 3 + 4 = 7  →  2 coins
```

---

## Solution — Dynamic Programming (Bottom-Up)

```java
class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];

        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {

                if (coins[j] == i) dp[i] = 1;
                else if (coins[j] < i) {
                    int remainder = i - coins[j];
                    int coinsToMakeRemainder = dp[remainder];
                    if (coinsToMakeRemainder > 0) {
                        if (dp[i] == 0) dp[i] = coinsToMakeRemainder + 1;
                        else dp[i] = Math.min(coinsToMakeRemainder + 1, dp[i]);
                    }
                } else continue;
            }
            if (dp[i] == 0) dp[i] = -1;
        }

        return dp[amount];
    }
}
```

---

## Why It Works

### Core Insight

> To make amount `i` using the fewest coins, try subtracting every coin denomination from `i`. The best answer is whichever remainder was cheapest to make, plus 1 coin.

Formally:

```
dp[i] = min( dp[i - coin] + 1 )   for every coin <= i where dp[i - coin] is solvable
```

This is **optimal substructure**: the best solution to `i` is built directly from the best solutions to smaller amounts. Since we compute `dp` left-to-right, every sub-result we need is already available — no redundant recomputation.

### Why Not Greedy?

- **Greedy fails** — taking the largest coin first doesn't always yield the minimum count.
  - Example: `coins=[1,3,4]`, `amount=6`
  - Greedy: `4+1+1 = 3 coins` ❌
  - Optimal: `3+3 = 2 coins` ✅
- **DP solves it optimally** — every sub-amount is solved exactly once and stored; larger amounts build on those guaranteed-optimal sub-results.

---

## How It Works — Step by Step

### Initialization

```
dp[0] = 0   (0 coins needed to make amount 0)
dp[1..amount] = 0  (sentinel: "not yet computed")
```

> **Note:** `0` is used as a sentinel for "unsolvable or unvisited". At the end of each outer iteration, if `dp[i]` is still `0`, it gets set to `-1` (unreachable).

### The Two Cases Inside the Loop

| Condition | Action | Meaning |
|-----------|--------|---------|
| `coins[j] == i` | `dp[i] = 1` | Exact match — one coin solves it |
| `coins[j] < i && dp[remainder] > 0` | `dp[i] = min(dp[i], dp[remainder] + 1)` | Use this coin + best solution to remainder |
| `coins[j] < i && dp[remainder] <= 0` | skip | Remainder is unsolvable, this coin can't help |
| `coins[j] > i` | `continue` | Coin is too large, skip |

### Trace — coins = [1, 3, 4, 5], amount = 7

| `i` | Coins tried | Best found | `dp[i]` |
|-----|------------|------------|---------|
| 1   | coin=1 → exact match | — | **1** |
| 2   | coin=1 → dp[1]+1=2 | 2 | **2** |
| 3   | coin=1 → dp[2]+1=3; coin=3 → exact | min(3,1)=1 | **1** |
| 4   | coin=1 → dp[3]+1=2; coin=3 → dp[1]+1=2; coin=4 → exact | min(2,2,1)=1 | **1** |
| 5   | coin=1 → dp[4]+1=2; coin=3 → dp[2]+1=3; coin=4 → dp[1]+1=2; coin=5 → exact | 1 | **1** |
| 6   | coin=1 → dp[5]+1=2; coin=3 → dp[3]+1=2; coin=4 → dp[2]+1=3; coin=5 → dp[1]+1=2 | 2 | **2** |
| 7   | coin=1 → dp[6]+1=3; coin=3 → dp[4]+1=2; coin=4 → dp[3]+1=2; coin=5 → dp[2]+1=3 | **2** | **2** |

**Final dp array:**

```
Index:  0   1   2   3   4   5   6   7
dp:    [0] [1] [2] [1] [1] [1] [2] [2]
```

**Answer:** `dp[7] = 2`  →  `3 + 4 = 7`

---

## Complexity Analysis

| | Complexity | Reason |
|---|---|---|
| **Time** | `O(amount × coins.length)` | Two nested loops |
| **Space** | `O(amount)` | dp array of size amount+1 |

---

## Edge Cases

| Scenario | Result |
|----------|--------|
| `amount = 0` | `0` (no coins needed) |
| No combination possible | `-1` |
| Single coin equals amount | `1` |

---

## Key Takeaway

> This is a classic **unbounded knapsack** variant: each coin can be used unlimited times, and we minimize count rather than maximizing value. The recurrence `dp[i] = min(dp[i - coin] + 1)` over all valid coins is the universal pattern for this family of problems.