# Smallest Infinite Set

A solution to **LeetCode 2336 — Smallest Infinite Set**, implemented in Java using a `PriorityQueue` + `HashSet`.

## Problem

Design a data structure that initially contains all positive integers `[1, 2, 3, ...]` in sorted order, and supports:

- `popSmallest()` — removes and returns the smallest integer currently in the set.
- `addBack(num)` — adds `num` back into the set, if it isn't already present.

## Core Idea

The set has two logical regions:

1. **The infinite tail** — all integers from `current` upward that have never been popped. These are represented implicitly by a single counter, `current`, rather than being stored individually (storing infinitely many integers is impossible).
2. **The "returned" pool** — a finite set of numbers smaller than `current` that were popped earlier and then added back via `addBack`. These are stored explicitly, since there are only ever a bounded number of them at once.

`popSmallest()` always prefers a number from the returned pool if one exists, because any number waiting there is guaranteed to be smaller than `current` (the smallest number in the untouched tail).

## Data Structures

| Field | Purpose |
|---|---|
| `PriorityQueue<Integer> queue` | Returns the *smallest* previously-popped number in `O(log n)`, used to answer `popSmallest()` correctly when the pool is non-empty. |
| `HashSet<Integer> set` | Mirrors the queue's contents so membership can be checked in `O(1)` — this is what prevents `addBack` from inserting a duplicate. |
| `int current` | The next never-yet-popped integer. Everything `>= current` is implicitly "in the set" without being stored anywhere. |

The queue and set are kept in sync: anything added to one is added to the other, and anything removed from one is removed from the other.

## Why Two Structures Instead of One?

A `PriorityQueue` alone can't efficiently check "is `num` already in here?" — that's an `O(n)` scan. A `HashSet` alone can't efficiently answer "what's the smallest element?" — that requires a scan or a sort. Combining them gives `O(log n)` smallest-extraction *and* `O(1)` duplicate checking, which is exactly what the two operations need.

## Method Walkthrough

### `popSmallest()`

```java
if (!queue.isEmpty()) {
    Integer n = queue.poll();
    set.remove(n);
    return n;
}
return current++;
```

- If the returned pool has anything in it, the smallest entry there is smaller than `current` by construction, so it's popped from both the queue and the set, and returned.
- Otherwise, nothing has been returned yet, so the smallest available number is `current` itself. It's returned, then `current` is advanced — permanently retiring it from the implicit tail.

### `addBack(num)`

```java
if (num < current && !set.contains(num)) {
    set.add(num);
    queue.offer(num);
}
```

Two guards keep the structure valid:

- `num < current` — if `num` is still `>= current`, it was never popped in the first place (it's still part of the untouched infinite tail), so re-adding it would be meaningless or incorrect.
- `!set.contains(num)` — the problem allows redundant `addBack` calls on the same number; the `HashSet` check makes this a no-op instead of creating a duplicate in the queue.

## Why It Works (Correctness Argument)

The invariant maintained at all times is:

> *Every integer in `[1, current)` is either in the returned pool (`queue`/`set`) or has never been popped... except that's impossible, since `current` only advances when a number is popped.*

More precisely: **every integer in `[1, current)` has been popped at least once**, and among those, exactly the ones sitting in `queue`/`set` are currently "back in the set." Every integer `>= current` has never been popped and is implicitly present.

This means the true smallest element of the set is always one of:
- the minimum of the returned pool (if non-empty), or
- `current` (if the pool is empty).

Since a `PriorityQueue` gives the pool's minimum directly, comparing "pool empty or not" and picking accordingly is sufficient — there's no need to ever compare pool-minimum against `current` explicitly, because anything in the pool is guaranteed `< current`.

## Complexity

| Operation | Time | Space |
|---|---|---|
| `popSmallest()` | `O(log n)` (heap poll) | — |
| `addBack(num)` | `O(log n)` (heap insert) | — |
| Overall | — | `O(k)`, where `k` = numbers currently in the returned pool |

`n` here refers to the number of elements currently sitting in the priority queue, which is bounded by however many distinct numbers have been popped and added back — not by the infinite range of positive integers.

## Example Trace

```
new SmallestInfiniteSet()      // current = 1, pool = {}
popSmallest()   -> 1           // current = 2, pool = {}
popSmallest()   -> 2           // current = 3, pool = {}
addBack(1)                     // pool = {1}   (1 < 3, not already in pool)
popSmallest()   -> 1           // pool = {}    (pulled from pool, current untouched)
popSmallest()   -> 3           // current = 4  (pool empty, fall back to current)
addBack(2)                     // pool = {2}   (2 < 4)
addBack(2)                     // no-op, already in pool
popSmallest()   -> 2           // pool = {}
```