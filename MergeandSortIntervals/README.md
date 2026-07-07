# Merge Intervals — `mergeHighDefinitionIntervals`

## 1. Problem Statement

You are given a list of intervals, where each interval is represented as a
two-element list `[start, end]`. Two intervals **overlap** if they share at
least one point in common (including touching at the endpoints, since this
implementation uses `>=`).

**Goal:** Merge all overlapping intervals and return the smallest possible
list of non-overlapping intervals that covers all the input intervals.

**Example**

```
Input:  [[1,3], [2,6], [8,10], [15,18]]
Output: [[1,6], [8,10], [15,18]]
```

`[1,3]` and `[2,6]` overlap (they share the range `[2,3]`), so they merge
into `[1,6]`. The rest don't touch anything, so they stay as-is.

---

## 2. Core Idea

The trick that makes this problem tractable is a single observation:

> **If you sort the intervals by their start value, then any interval that
> overlaps with your "current merged interval" must appear immediately next
> to it in the sorted order.**

Once intervals are sorted by start, you never need to look back — you only
ever compare the interval you're currently "building" (`tmp`) against the
*next* interval in line. This turns an otherwise O(n²) pairwise comparison
problem into a single linear pass after sorting.

### Why sorting works

Before sorting, an interval like `[8,10]` could overlap with something way
later in the list, or something way earlier — you'd have no guarantee about
where to look. After sorting by start value, overlaps can only "chain"
forward: if interval A doesn't overlap interval B (the next one in sorted
order), it's *impossible* for A to overlap any interval after B either,
because every later interval starts at or after B's start.

---

## 3. Step-by-Step Walkthrough of the Code

```java
public static List<List<Integer>> mergeHighDefinitionIntervals(List<List<Integer>> intervals) {

    if (intervals == null || intervals.size() < 2) return intervals;
```
**Step 1 — Guard clause.**
If there's nothing to merge (`null`, empty, or a single interval), there's
nothing to do. Return immediately. This also avoids a crash later when the
code calls `intervals.get(0)` — that line would throw
`IndexOutOfBoundsException` on an empty list if this guard weren't here.

```java
    List<List<Integer>> result = new ArrayList<>();

    intervals.sort((a, b) -> a.get(0) - b.get(0));
```
**Step 2 — Sort by start value.**
This is the enabling step discussed above. The comparator
`(a, b) -> a.get(0) - b.get(0)` sorts in ascending order of each interval's
first element (its start). Note: this **mutates the input list in place**
(`List.sort` sorts the underlying list, it doesn't return a new one) — more
on this in the Pitfalls section.

```java
    List<Integer> tmp = intervals.get(0);
```
**Step 3 — Seed the "current merge window."**
`tmp` represents the interval currently being extended. It starts as the
very first interval in the now-sorted list.

```java
    for (var lst : intervals) {

        if (tmp.get(1) >= lst.get(0)) {
            if (tmp.get(1) < lst.get(1)) tmp.set(1, lst.get(1));
        } else {
            result.add(tmp);
            tmp = lst;
        }
    }
```
**Step 4 — Single linear scan, decide "merge or close out."**

For every interval `lst` (including, redundantly, the very first one — see
Pitfalls):

- **Overlap check:** `tmp.get(1) >= lst.get(0)` asks "does `tmp`'s end reach
  far enough to touch or pass `lst`'s start?" If yes, they overlap (or
  touch).
  - If they overlap, extend `tmp`'s end to whichever is bigger:
    `tmp.get(1) < lst.get(1)` guards against shrinking the interval if
    `lst` is actually a *sub-interval* already contained inside `tmp`.
- **No overlap:** `tmp` is finished — nothing later can ever reach back and
  merge with it (thanks to the sort). Push `tmp` into `result`, then start a
  new merge window seeded at `lst`.

```java
    result.add(tmp);

    return result;
}
```
**Step 5 — Flush the last window.**
The loop only adds a finished `tmp` to `result` when it finds a *disjoint*
interval to replace it with. The very last merge window never triggers that
`else` branch (there's no interval after it to disagree with), so it has to
be added manually after the loop ends.

---

## 4. Full Dry Run

Input:
```
[[1,3], [2,6], [8,10], [15,18]]
```

### After sorting by start value
Already sorted in this case:
```
[[1,3], [2,6], [8,10], [15,18]]
```

### Trace table

| Iteration | `lst`     | `tmp` (before) | `tmp.get(1) >= lst.get(0)` ? | Action                          | `tmp` (after) | `result`                  |
|-----------|-----------|-----------------|------------------------------|----------------------------------|---------------|----------------------------|
| 1         | `[1,3]`   | `[1,3]`         | `3 >= 1` → true               | `3 < 3`? false → no change        | `[1,3]`       | `[]`                       |
| 2         | `[2,6]`   | `[1,3]`         | `3 >= 2` → true               | `3 < 6`? true → extend end to 6   | `[1,6]`       | `[]`                       |
| 3         | `[8,10]`  | `[1,6]`         | `6 >= 8` → false              | close out `tmp`, start new window | `[8,10]`      | `[[1,6]]`                  |
| 4         | `[15,18]` | `[8,10]`        | `10 >= 15` → false            | close out `tmp`, start new window | `[15,18]`     | `[[1,6],[8,10]]`           |

### After the loop

`result.add(tmp)` flushes the final window:

```
result = [[1,6], [8,10], [15,18]]
```

**Final Output:**
```
[[1,6], [8,10], [15,18]]
```

which matches the expected result. ✅

### A second, trickier example (containment case)

Input: `[[1,10], [2,3], [4,5]]`

| Iteration | `lst`    | `tmp` (before) | overlap check     | Action                                      | `tmp` (after) |
|-----------|----------|-----------------|-------------------|----------------------------------------------|---------------|
| 1         | `[1,10]` | `[1,10]`        | `10 >= 1` true    | `10 < 10`? false → no change                  | `[1,10]`      |
| 2         | `[2,3]`  | `[1,10]`        | `10 >= 2` true    | `10 < 3`? false → no change (don't shrink!)   | `[1,10]`      |
| 3         | `[4,5]`  | `[1,10]`        | `10 >= 4` true    | `10 < 5`? false → no change                   | `[1,10]`      |

Final: `[[1,10]]`

This demonstrates why the `tmp.get(1) < lst.get(1)` guard matters — without
it, blindly setting `tmp.set(1, lst.get(1))` on iteration 2 would shrink the
interval from `[1,10]` down to `[1,3]`, silently losing data.

---

## 5. Complexity Analysis

| Aspect            | Complexity   | Why                                                                 |
|--------------------|--------------|----------------------------------------------------------------------|
| **Time**           | `O(n log n)` | Dominated by the sort. The merge pass itself is a single `O(n)` scan. |
| **Space (extra)**  | `O(n)`       | The `result` list holds up to `n` intervals in the worst case (no merges happen). Sorting is typically in-place for `ArrayList` (Java's `List.sort` uses TimSort, which needs `O(n)` auxiliary space internally, but doesn't duplicate the interval objects). |
| **Space (output)** | `O(n)`       | The returned list, which is required regardless of algorithm choice. |

You cannot beat `O(n log n)` overall unless the input is guaranteed to
already be sorted by start — the sort is the bottleneck.

---