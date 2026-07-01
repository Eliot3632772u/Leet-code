# Number of Islands — Solution Explained

## 1. The Problem

You're given a 2D grid made of characters `'1'` (land) and `'0'` (water). An **island**
is a group of `'1'`s connected **horizontally or vertically** (not diagonally). Land
cells connected together, directly or through a chain of neighbors, form a single
island. Your job: count how many separate islands exist in the grid.

Example:

```
11000
11000
00100
00011
```

This grid contains **3 islands**:
- The 2×2 block of `1`s in the top-left corner
- The single `1` in the middle
- The 2-cell `1` block in the bottom-right corner

## 2. The Core Idea

This is fundamentally a **connected components** problem on a grid, and the grid can
be thought of as an implicit graph:

- Every cell is a node.
- Every cell has an edge to its up/down/left/right neighbor **if both cells are land**.

Counting islands is the same as counting the number of connected components made up of
`'1'` nodes in that graph.

The classic way to count connected components is:

1. Scan every node.
2. Whenever you find an unvisited node that's part of the structure you care about
   (here, land), that means you've discovered a **new** component.
3. Fully explore that component (mark every cell in it as visited) so you never count
   it again.
4. Increment your component counter once per discovery.

This solution implements exactly that pattern using **DFS (Depth-First Search)** for
step 3, and the DFS here is done through a technique called **flood fill**.

## 3. Walking Through the Code

```java
public int numIslands(char[][] grid) {
    if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

    int count = 0;

    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
            if (grid[i][j] == '1') {
                floodFill(grid, i, j);
                count++;
            }
        }
    }

    return count;
}
```

### 3.1 The guard clause

```java
if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
```

This protects against:
- `grid` being `null` (no grid at all).
- `grid.length == 0` (zero rows).
- `grid[0].length == 0` (rows exist, but each row is empty — zero columns).

If any of these are true, there's physically no land possible, so the answer is
trivially `0`. This also prevents `NullPointerException` or
`ArrayIndexOutOfBoundsException` later when the code accesses `grid[0].length`.

### 3.2 The scanning loop

```java
for (int i = 0; i < grid.length; i++) {
    for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == '1') {
            floodFill(grid, i, j);
            count++;
        }
    }
}
```

This visits **every single cell** in the grid exactly once, row by row, left to right.

- `i` = row index
- `j` = column index

Whenever it lands on a `'1'` it hasn't already dealt with, that `'1'` is guaranteed to
be the **first cell discovered** belonging to some island (because we scan in a fixed
order and never revisit cells that were already turned into `'0'`). So:

- `floodFill(grid, i, j)` is called to "sink" the entire island — turn every connected
  `'1'` reachable from `(i, j)` into `'0'`.
- `count++` records that we just found one new island.

Because `floodFill` eliminates the *entire* island in one call, by the time the outer
loop reaches any other cell that used to belong to this island, it will already be
`'0'` — so it's skipped, and the island is never double-counted.

### 3.3 The flood fill (DFS)

```java
public void floodFill(char[][] grid, int i, int j) {

    if (grid[i][j] == '0') return;

    grid[i][j] = '0';

    if (j + 1 < grid[0].length)
        floodFill(grid, i, j + 1);
    if (j - 1 >= 0)
        floodFill(grid, i, j - 1);
    if (i - 1 >= 0)
        floodFill(grid, i - 1, j);
    if (i + 1 < grid.length)
        floodFill(grid, i + 1, j);
}
```

This is a classic recursive DFS. Let's break down each piece.

**Base case:**

```java
if (grid[i][j] == '0') return;
```

If the current cell is water, there's nothing to do — stop recursing down this path.
This single line actually serves **two purposes** simultaneously:
1. It's the natural DFS base case (no land here, don't explore further).
2. It's the **visited check**. Because the very next line turns land into water, any
   cell that has *already been visited* during this or a previous flood fill will also
   read as `'0'` here — so the function returns immediately instead of re-exploring it.

**Marking visited ("sinking" the land):**

```java
grid[i][j] = '0';
```

This is the key trick that makes the whole algorithm work without needing a separate
`visited[][]` boolean array. By overwriting the land cell with water in-place:
- The grid itself doubles as the visited-tracking structure.
- Memory usage drops — no extra `O(rows × cols)` visited array needed.
- It automatically guarantees the outer loop in `numIslands` will never re-process this
  cell as a "new" island later.

**Exploring all 4 neighbors:**

```java
if (j + 1 < grid[0].length) floodFill(grid, i, j + 1);   // right
if (j - 1 >= 0)             floodFill(grid, i, j - 1);   // left
if (i - 1 >= 0)             floodFill(grid, i - 1, j);   // up
if (i + 1 < grid.length)    floodFill(grid, i + 1, j);   // down
```

From the current land cell, the function recursively calls itself on the cell to the
right, left, above, and below — but only if that neighbor actually exists inside the
grid's bounds. Each bounds check prevents `ArrayIndexOutOfBoundsException`:
- `j + 1 < grid[0].length` — don't step past the last column.
- `j - 1 >= 0` — don't step before the first column.
- `i - 1 >= 0` — don't step above the first row.
- `i + 1 < grid.length` — don't step past the last row.

Each recursive call repeats the exact same logic: check if it's land, sink it, then
try all 4 directions from there. The recursion naturally stops on its own once it hits
water or the grid edge in every direction — there's no explicit "stop" signal needed
beyond the base case.

## 4. Why This Works

Think of an island as a **blob of connected land**. When `floodFill` is called on any
land cell belonging to that blob, it behaves like water flooding outward in all four
directions, spreading through every connected land cell until it hits water or the
grid boundary on every side. Because it explores **up, down, left, and right from every
cell it touches**, it's guaranteed to reach every land cell that is connected — directly
or indirectly — to the starting cell. That's exactly the definition of "island" given
in the problem.

Once `floodFill` returns, **the entire island has been converted to water**. This
guarantees two things simultaneously:

1. **Correctness** — the outer loop's `count++` only fires once per island, since by
   the time the scan reaches any other cell of that island, it's already `'0'`.
2. **No missed islands** — because the outer loop checks *every* cell in the grid, no
   island can be skipped. Any `'1'` still standing when the scan reaches it must belong
   to an island that hasn't been counted yet.

Put simply: **the outer double loop finds island "seeds," and `floodFill` claims the
entire island for that seed so nothing gets double-counted.** Together they form a
standard connected-components counting algorithm.

## 5. Tracing an Example

```
Grid:
11000
11000
00100
00011
```

- `(0,0)` is `'1'` → new island found, `count = 1`. `floodFill` sinks `(0,0)`, `(0,1)`,
  `(1,0)`, `(1,1)` — the whole top-left block becomes `'0'`.
- Scan continues... `(0,2)` through `(1,4)` are already `'0'`, skipped.
- `(2,2)` is `'1'` → new island found, `count = 2`. `floodFill` sinks just `(2,2)` (its
  neighbors are all water).
- `(3,3)` is `'1'` → new island found, `count = 3`. `floodFill` sinks `(3,3)` and
  `(3,4)`.
- Scan finishes. Final answer: **3**.

## 6. Complexity Analysis

Let `R` = number of rows, `C` = number of columns.

- **Time Complexity: `O(R × C)`**
  Every cell is visited by the outer loop exactly once. Every land cell is visited by
  `floodFill` exactly once too (because it's immediately turned into water and can
  never be re-entered). So the total work across the entire run — outer loop plus all
  recursive flood fill calls combined — is proportional to the total number of cells.

- **Space Complexity: `O(R × C)` in the worst case**
  This solution uses no extra visited array, so at first glance it looks like `O(1)`
  extra space. However, `floodFill` is **recursive**, and recursion uses the **call
  stack**. In the worst case — a grid that is entirely land — the recursion can go
  `R × C` calls deep before unwinding, so the call stack itself can grow to
  `O(R × C)`. (An iterative version using an explicit `Stack` or a `Queue`/BFS would
  have the same worst-case space complexity, just without risking a
  `StackOverflowError` on very large grids.)