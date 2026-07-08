# Place N Cameras Without Conflict on Blocked Grid

## Problem Statement

Given an `N x N` grid where each cell is either:
- `0` → empty (a camera can be placed here)
- `1` → blocked (a camera cannot be placed here)

determine whether it is possible to place exactly **N cameras**, one per row, such that no two cameras share:
- the same **row**
- the same **column**
- the same **diagonal** (either direction)

Return `true` if such a placement exists, otherwise `false`.

**Example**

```
N = 4
grid = [[0, 0, 0, 0],
        [0, 0, 0, 0],
        [0, 0, 0, 0],
        [0, 0, 0, 0]]

Output: True
```

This is the classic **N-Queens** problem, with an added twist: some cells are pre-blocked and cannot hold a camera.

---

## Conceptual Explanation

### It's N-Queens in disguise

A camera behaves exactly like a queen on a chessboard: it "attacks" along its row, column, and both diagonals. So "no two cameras conflict" is precisely the N-Queens placement rule. The only difference here is that certain cells are off-limits from the start (`1` in the grid).

### The core idea: place one camera per row, row by row

Since no two cameras can share a row, we know the final answer (if it exists) has **exactly one camera per row**. That means we can think of the problem as:

> For row 0, choose a valid column. Then for row 1, choose a valid column that doesn't conflict with row 0. Then for row 2, choose a column that doesn't conflict with rows 0 and 1. ... and so on until row N-1.

This is a perfect fit for **backtracking**: try a choice, recurse, and if it doesn't work out, undo the choice and try the next one.

### How do we track "conflicts" quickly?

Instead of scanning previously placed cameras every time (which would be slow), the solution keeps three `HashSet`s that record which columns/diagonals are already "occupied":

| Set | Meaning | Why this formula? |
|---|---|---|
| `colsSet` | columns already used | If a camera is at column `c`, no other camera can use column `c` again. |
| `pos` | "positive" diagonals in use | For any cell `(r, c)`, the value `r - c` is **constant** along a `↘` diagonal (top-left to bottom-right). Two cells share this diagonal if and only if they have the same `r - c`. |
| `neg` | "negative" diagonals in use | For any cell `(r, c)`, the value `r + c` is **constant** along a `↙` diagonal (top-right to bottom-left). Two cells share this diagonal if and only if they have the same `r + c`. |

This is the standard trick used in N-Queens solvers: instead of comparing every pair of cameras, you check membership in three sets in O(1) time.

### Why recurse row-by-row instead of checking the whole board at once?

Because it lets us **fail fast**. If row 2 has no valid column left given the choices made in rows 0 and 1, there's no point even considering rows 3, 4, 5... We immediately back up (backtrack) and try a different column in row 1.

---

## Annotated Code Walkthrough

```java
class Result {

    static HashSet<Integer> colsSet = new HashSet<>(); // columns already occupied
    static HashSet<Integer> pos = new HashSet<>();      // occupied r - c diagonals
    static HashSet<Integer> neg = new HashSet<>();      // occupied r + c diagonals
    static List<List<Integer>> grid;
    static int n;

    public static boolean canPlaceSecurityCameras(int N, List<List<Integer>> grids) {
        grid = grids;
        n = N;
        return dfs(0); // start trying to place a camera in row 0
    }

    public static boolean dfs(int r) {
        if (r == n) return true; // explained below — this is the success condition

        for (int c = 0; c < n; c++) {

            // Skip blocked cells — a camera physically cannot go here
            if (grid.get(r).get(c) == 1) continue;

            // Skip cells that conflict with an already-placed camera
            if (colsSet.contains(c) || pos.contains(r - c) || neg.contains(r + c)) continue;

            // --- Place the camera at (r, c) ---
            colsSet.add(c);
            pos.add(r - c);
            neg.add(r + c);

            // Recurse to the next row. If it eventually succeeds, propagate true all the way up.
            if (dfs(r + 1)) return true;

            // --- Undo the placement (backtrack) ---
            // This column/diagonal choice didn't lead to a full solution,
            // so free it up and let the loop try the next column c.
            colsSet.remove(c);
            pos.remove(r - c);
            neg.remove(r + c);
        }

        // No column in this row worked out — tell the caller (previous row) to try something else
        return false;
    }
}
```

### Answering the comment in the code: "why return true here, why not false?"

```java
if (r == n) return true;
```

Think about what `r` represents: it's the row we are **currently trying to fill**. The recursion always moves forward with `dfs(r + 1)` only *after* successfully placing a camera in row `r`.

So by the time `r` reaches `n`, it means:
- row `0` got a camera ✅
- row `1` got a camera ✅
- ...
- row `n - 1` got a camera ✅

There is no row `n` — it's one past the last valid row index (rows go from `0` to `n-1`). Reaching `r == n` is simply the recursion's way of saying **"I ran out of rows to fill, because I already filled all of them successfully."**

That's a full, conflict-free placement of N cameras — exactly what the problem asks for. Hence `true`.

If instead a row could **not** find any valid column (the `for` loop finishes without ever calling `return true`), the function falls through to `return false` at the bottom — meaning "this row is stuck, go back and try a different choice in the previous row."

So:
- `return true` at `r == n` → **base case for success** (all rows filled)
- `return false` at the end of the loop → **failure case** (this row has no valid options left, backtrack)

It might feel backwards at first because `r == n` looks like an "edge" you'd expect to guard against, but here it's actually the **finish line**, not an error condition.

---

## Step-by-Step Example (Dry Run)

Let's trace a small case, `N = 4`, with an all-empty grid, to see the mechanics (a full N=4 solution exists, e.g., columns `[1, 3, 0, 2]`).

| Step | Row `r` | Try col `c` | Blocked? | Conflict? (col/diag) | Action |
|---|---|---|---|---|---|
| 1 | 0 | 0 | No | No (sets empty) | Place at (0,0). `colsSet={0}`, `pos={0}`, `neg={0}` → recurse row 1 |
| 2 | 1 | 0 | No | Yes (`colsSet` has 0) | Skip |
| 3 | 1 | 1 | No | Yes (`neg` has 1? no — check: `r+c=2`, not in `neg`; `r-c=0`, in `pos`!) | Skip |
| 4 | 1 | 2 | No | No | Place at (1,2). `colsSet={0,2}`, `pos={0,-1}`, `neg={0,3}` → recurse row 2 |
| 5 | 2 | 0,1,2 | No | Yes (various col/diag hits) | Skip all |
| 6 | 2 | ... | — | All conflict | Loop ends, `return false` |
| 7 | — | — | — | Backtrack to row 1 | Remove col 2, `pos`, `neg` entries for (1,2). Try next `c` in row 1 |
| 8 | 1 | 3 | No | No | Place at (1,3). Recurse row 2 |
| 9 | 2 | 0 | No | Yes | Skip |
| 10 | 2 | 1 | No | No | Place at (2,1). Recurse row 3 |
| 11 | 3 | ... | — | eventually col 3? check conflicts... suppose col 3 conflicts | Skip |
| ... | ... | ... | ... | eventually finds col that works, e.g., c not matching | Place at (3, c) |
| N | 4 (== n) | — | — | — | **`return true`** — success! |

The key behavior to notice:
1. Every time a placement fails deep in the recursion, the function **unwinds one row at a time**, removing exactly the column/diagonal it added, then tries the *next* column in that row.
2. This guarantees every possible arrangement is eventually tried (exhaustive search), but conflicting branches are pruned immediately instead of being explored to completion.
3. As soon as one full valid arrangement is found (`r == n`), the `true` result is propagated all the way back up through every recursive call via `if (dfs(r + 1)) return true;` — no further backtracking or removal happens once a solution is found, so the sets are left representing that winning configuration.

---

## Complexity Analysis

**Time Complexity:** `O(N!)` in the worst case.
- At row 0 there are up to `N` column choices, at row 1 up to `N-1` remaining valid choices (after pruning), and so on — similar to the classic N-Queens backtracking bound. Blocked cells (`1`s) only ever reduce the search space, never increase it.

**Space Complexity:** `O(N)`.
- `colsSet`, `pos`, and `neg` each hold at most `N` entries at any point in the recursion.
- The recursion (call) stack depth is at most `N` (one frame per row).

---