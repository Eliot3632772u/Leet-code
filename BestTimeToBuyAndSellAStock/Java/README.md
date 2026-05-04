# Best Time to Buy and Sell Stock

**LeetCode #121** | Difficulty: Easy | Topic: Array, Greedy, Sliding Window

---

## Problem Statement

Given an array `prices` where `prices[i]` is the price of a stock on day `i`, find the **maximum profit** you can achieve by buying on one day and selling on a **later** day.

If no profit is possible, return `0`.

---

## Solution

```java
class Solution {
    public int maxProfit(int[] prices) {

        int min = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {

            if (price < min) min = price;

            int profit = price - min;

            maxProfit = Math.max(profit, maxProfit);
        }

        return maxProfit;
    }
}
```

---

## How It Works

The solution uses a **single pass** (O(n)) approach, tracking two things simultaneously:

| Variable    | Meaning                                      |
|-------------|----------------------------------------------|
| `min`       | The lowest price seen so far (best buy day)  |
| `maxProfit` | The best profit found so far                 |

### Step-by-step logic on each iteration:

1. **Update the minimum** — If the current price is lower than any previously seen price, update `min`. This represents finding a cheaper day to buy.
2. **Calculate current profit** — `profit = price - min`. This is how much we'd earn if we sold *today* after buying at the cheapest day seen so far.
3. **Update max profit** — If this profit beats the current best, update `maxProfit`.

---

## Why It Works

The key insight is:

> **To maximize profit on day `i`, you want to have bought at the lowest price among all days before `i`.**

By always tracking the running minimum, we guarantee that when we compute `price - min`, we're computing the **best possible profit ending on that day**. We never look ahead — only backward — which makes this approach both correct and efficient.

This is a classic **Greedy** pattern: at each step, make the locally optimal choice (buy at the lowest point seen so far), and this globally maximizes the result.

---

## Walkthrough Examples

### Example 1 — Normal case with profit

```
prices = [7, 1, 5, 3, 6, 4]
```

| Day | Price | min | profit (price - min) | maxProfit |
|-----|-------|-----|----------------------|-----------|
| 0   | 7     | 7   | 0                    | 0         |
| 1   | 1     | 1   | 0                    | 0         |
| 2   | 5     | 1   | 4                    | 4         |
| 3   | 3     | 1   | 2                    | 4         |
| 4   | 6     | 1   | 5                    | 5         |
| 5   | 4     | 1   | 3                    | 5         |

**Answer: `5`** — Buy at price `1` (day 1), sell at price `6` (day 4).

---

### Example 2 — Prices always decreasing (no profit)

```
prices = [7, 6, 4, 3, 1]
```

| Day | Price | min | profit (price - min) | maxProfit |
|-----|-------|-----|----------------------|-----------|
| 0   | 7     | 7   | 0                    | 0         |
| 1   | 6     | 6   | 0                    | 0         |
| 2   | 4     | 4   | 0                    | 0         |
| 3   | 3     | 3   | 0                    | 0         |
| 4   | 1     | 1   | 0                    | 0         |

**Answer: `0`** — The price only goes down, so we never sell. No profit possible.

> Notice that whenever `price < min`, the profit is always `0` (not negative), because `min` gets updated first. This is the guard that prevents us from ever "selling below our buy price."

---

### Example 3 — Best buy point is not at the start

```
prices = [3, 8, 1, 9]
```

| Day | Price | min | profit (price - min) | maxProfit |
|-----|-------|-----|----------------------|-----------|
| 0   | 3     | 3   | 0                    | 0         |
| 1   | 8     | 3   | 5                    | 5         |
| 2   | 1     | 1   | 0                    | 5         |
| 3   | 9     | 1   | 8                    | 8         |

**Answer: `8`** — Buy at price `1` (day 2), sell at price `9` (day 3).

> Even though we already found a profit of `5` earlier (buy at 3, sell at 8), the algorithm correctly identifies the even better opportunity later.

---

## Complexity Analysis

| | Complexity |
|---|---|
| **Time** | `O(n)` — single pass through the array |
| **Space** | `O(1)` — only two variables used |

---

## Key Takeaways

- You don't need to check every pair of days — one pass is enough.
- Always track the **running minimum** to know the best buy price up to this point.
- Compute profit at every step and track the **running maximum profit**.
- When price drops below `min`, the profit resets naturally to `0` — no explicit check needed.