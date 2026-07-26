# Counting Duplicate Rows in a Grid

## Problem Context

Given a 2D integer grid, this snippet counts how many times each **distinct row** appears in the grid. It's the core building block for problems like:

- Counting duplicate rows in a matrix
- Finding groups of identical rows
- Detecting rows that repeat (to remove, merge, or report them)

The pattern generalizes to *"treat each row as a single unit and count its frequency"* — the same idea used in problems that require grouping equivalent sequences.

```java
for(int i = 0; i < grid.length; i++) {
    List<Integer> lst = new ArrayList<>();
    for(int j = 0; j < grid.length; j++) {
        lst.add(grid[i][j]);
    }
    map.put(lst, map.getOrDefault(lst, 0) + 1);
}
```

> **Note on the loop bound:** the inner loop uses `grid.length` (number of rows) instead of `grid[i].length` (number of columns in row `i`). This only works correctly if the grid is **square** (`rows == cols`). If the grid is rectangular, this should be `grid[i].length` — see [Edge Cases](#edge-cases) below.

---

## Approach

1. **Iterate over every row** of the grid using index `i`.
2. **Build a `List<Integer>`** (`lst`) by copying every element of that row into it, one by one, using the inner loop over `j`.
3. **Use the row (as a `List<Integer>`) as a HashMap key.** For each row, look up its current count with `map.getOrDefault(lst, 0)` (defaulting to `0` if it's never been seen), add `1`, and store it back with `map.put(lst, ...)`.

After the loops finish, `map` contains every **unique row** in the grid mapped to **how many times it appears**.

---

## Why It Works

### 1. `List<Integer>` as a HashMap key relies on structural equality

This is the crux of the whole technique. A `HashMap<K, V>` decides whether two keys are "the same" using `key.equals()` and `key.hashCode()`.

- **Arrays (`int[]`)** in Java do **not** override `equals()`/`hashCode()` — they use identity comparison (memory address). Two arrays with identical contents are considered *different* keys.
- **`ArrayList<Integer>`** *does* override both:
  - `equals()` compares elements **pairwise, in order** — `[1,2,3].equals([1,2,3])` is `true` even if they're two separate objects.
  - `hashCode()` is computed from the elements' hash codes combined in order (per the `List` interface contract), so two equal lists always produce the same hash bucket.

This is precisely why the code wraps each row in an `ArrayList<Integer>` instead of just using `int[]`. If you tried to use the raw row array as a key, every row would hash to a different bucket regardless of content, and the counting logic would silently break — every row would appear "unique."

### 2. `getOrDefault` + `put` implements a frequency counter

```java
map.put(lst, map.getOrDefault(lst, 0) + 1);
```

This is the standard **"increment or initialize"** idiom:
- If `lst` has never been seen, `getOrDefault` returns `0`, so the entry becomes `1` (first occurrence).
- If `lst` was seen before, it returns the current count, and we store `count + 1`.

This avoids a separate `containsKey` check — one map lookup instead of two (`getOrDefault` does the "check + fallback" in a single call).

### 3. Autoboxing makes `Integer` keys work with `int` grid values

`grid[i][j]` is a primitive `int`, but `lst.add(...)` expects an `Integer` (an object). Java automatically **autoboxes** the `int` into an `Integer` when it's added to the list. This is what makes `.equals()` comparisons meaningful — `Integer` objects compare by value (not reference) when using `.equals()`, which is what `ArrayList.equals()` uses internally.

---

## Complexity Analysis

Let `n = grid.length` (number of rows, and — since the loop assumes a square grid — also the number of columns).

| Step | Cost |
|---|---|
| Outer loop | Runs `n` times |
| Inner loop (build row list) | Runs `n` times per row → `O(n)` per row |
| `hashCode()` of a `List<Integer>` of size `n` | `O(n)` — must hash every element |
| `equals()` in worst case (hash collision) | `O(n)` — must compare every element |
| `map.put` / `map.getOrDefault` | `O(n)` amortized (due to hashing/equality cost above) |

**Total Time Complexity:** `O(n²)`
— `n` rows, each costing `O(n)` to build the list and `O(n)` to hash/compare it into the map.

**Total Space Complexity:** `O(n²)`
— In the worst case (all rows distinct), the map stores `n` lists of `n` integers each.

---

## Step-by-Step Trace

Consider a `3x3` grid:

```
grid = [
  [1, 2, 3],
  [4, 5, 6],
  [1, 2, 3]
]
```

| i | Row built (`lst`) | Map lookup | Action | Map state after |
|---|---|---|---|---|
| 0 | `[1, 2, 3]` | not found → default `0` | put `[1,2,3] → 1` | `{[1,2,3]: 1}` |
| 1 | `[4, 5, 6]` | not found → default `0` | put `[4,5,6] → 1` | `{[1,2,3]: 1, [4,5,6]: 1}` |
| 2 | `[1, 2, 3]` | found → `1` | put `[1,2,3] → 2` | `{[1,2,3]: 2, [4,5,6]: 1}` |

**Result:** `[1,2,3]` appears twice, `[4,5,6]` appears once — correctly identifying the duplicate row.

---

## Edge Cases

- **Rectangular grids:** As noted above, the inner loop bound `grid.length` assumes a square grid. For a rectangular grid (`m` rows, `n` columns), it must be changed to `grid[i].length` (or an explicit `cols` variable), otherwise you'll get an `ArrayIndexOutOfBoundsException` (if `cols < rows`) or silently drop columns (if `cols > rows`).
- **Empty grid:** If `grid.length == 0`, the outer loop never executes and `map` stays empty — handled gracefully.
- **Single row/column:** Works correctly; `lst` just has one element, still hashes/compares fine.
- **All rows identical:** Produces a single map entry with count `= grid.length`.
- **All rows distinct:** Produces `grid.length` map entries, each with count `1` — this is also the worst case for space.
- **Negative numbers / zero:** No issue — `Integer.hashCode()` and `.equals()` handle all `int` values uniformly.

---

## Key Takeaway

> When you need to treat a **sequence of values** as a single hashable "unit" (to dedupe, count, or group them), wrap it in a `List` (not an array) so Java's built-in structural `equals()`/`hashCode()` do the comparison work for you. This is a reusable pattern anywhere you'd otherwise write a manual array-comparison helper.