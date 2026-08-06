# Path Sum III — Prefix Sum + HashMap Solution

## Problem Recap

Given the `root` of a binary tree and an integer `t` (target sum), count the number of **paths** where:
- The path goes strictly **downward** (parent → child → grandchild...), no going back up.
- The path does **not** need to start at the root or end at a leaf — it can start and end at *any* node, as long as it moves downward.
- We want the total count of such paths whose node values sum to `t`.

A brute-force approach (trying every node as a starting point and walking down) works but costs **O(n²)** in the worst case. The solution below does it in **O(n)** using the same trick used for "subarray sum equals K" — prefix sums.

---

## Core Idea: Prefix Sums on a Tree

Imagine flattening the path from the root down to the current node into a straight line of numbers, like an array. If we call the running total from the root to the current node the **prefix sum**, then the sum of any path segment `A → B` (where B is a descendant of A) is:

```
sum(A → B) = prefixSum(B) - prefixSum(A's parent)
```

So a path from some ancestor down to the current node equals `t` exactly when:

```
prefixSum(current) - prefixSum(ancestor) = t
```

Rearranged:

```
prefixSum(ancestor) = prefixSum(current) - t
```

This means: **at every node, we just need to know how many ancestors (anywhere above us on the current root-to-node path) had a prefix sum equal to `curr - t`.** If we've been keeping a running count of prefix sums seen so far *on the current path*, we can look this up in O(1) using a HashMap.

This is exactly the same idea as "number of subarrays with sum K", just applied to a tree instead of a flat array, using DFS to walk the "array" that is the current root-to-node path.

---

## Why a HashMap (and not just any tally)?

The map stores: **prefix sum value → how many times that value has occurred among the ancestors of the node we're currently visiting** (including the node itself, and including the empty prefix of 0 at the very top).

The critical detail is that this map must reflect **only the current path from the root to where the DFS currently is** — not the whole tree. Since a path can branch in the left subtree or the right subtree independently, values recorded in the left subtree must NOT leak into the right subtree's search. This is why, after fully exploring a node's subtree, we **remove (decrement) its contribution from the map** before returning to the parent (this is classic backtracking).

---

## Line-by-Line Walkthrough

```java
class Solution {
    HashMap<Long, Integer> map = new HashMap<>();
    int count = 0;
```
- `map`: stores `prefixSum -> frequency`, counting how many ancestors on the *current* path have that prefix sum.
- We use `Long` instead of `Integer`/`int` because prefix sums can overflow a 32-bit int if node values are large and the path is long. Using `Long` keeps the sum safe.
- `count`: the running total of valid paths found so far. This is the final answer.

```java
    public int pathSum(TreeNode root, int t) {
        map.put(0l, 1);
        dfs(root, t, 0l);
        return count;
    }
```
- `map.put(0L, 1)`: this is the most important setup line. It represents an **empty path with sum 0**, placed there *before* we've added any real node. Without this, we would never be able to count a path that starts exactly at the root (or at any node) and sums to `t` on its own — because there'd be no "ancestor" entry to match against. Think of it as priming the map so that "prefixSum(current) - t == 0" cases (i.e. the path itself equals `t`) are still found.
- We then kick off the recursion with a running sum of `0L` (nothing added yet).
- Finally we return `count`, which was accumulated by every call to `dfs`.

```java
    public void dfs(TreeNode root, int t, Long curr) {
        if (root == null) return;
```
- Standard recursion base case: an empty subtree contributes nothing, so we stop.

```java
        curr += root.val;
```
- Update the running prefix sum to include the current node's value. Now `curr` = sum of all values from the root down to this node.

```java
        Integer freq = map.getOrDefault(curr - t, 0);
        count += freq;
```
- This is the heart of the algorithm. We ask: "how many ancestors (on this path) had a prefix sum of `curr - t`?"
- Why `curr - t`? Because if some ancestor `A` had `prefixSum(A) = curr - t`, then the segment from just-below-`A` down to the current node sums to `curr - prefixSum(A) = t` — exactly the target we want.
- Every such ancestor represents one valid downward path ending at the current node, so we add its frequency directly to `count`. (There can be more than one ancestor with the same prefix sum if some values are zero or negative, hence tracking frequency rather than just existence.)

```java
        map.put(curr, map.getOrDefault(curr, 0) + 1);
```
- Now that we've used the map to *look back*, we register the current node's own prefix sum into the map, so that **descendants** of this node can use it as a potential ancestor match later.

```java
        dfs(root.left, t, curr);
        dfs(root.right, t, curr);
```
- Recurse into both children, passing along the updated `curr`. Each child will see the full ancestor history collected in `map` so far (all nodes from the root down to here).

```java
        map.put(curr, map.get(curr) - 1);
        return;
    }
}
```
- **Backtracking step.** Once we're done exploring everything below the current node (both left and right subtrees have fully returned), we remove this node's contribution from the map by decrementing its count.
- This is essential: it ensures that when the DFS later moves to a *different branch* (e.g., after finishing the left subtree and moving to the right subtree of some ancestor), the map only reflects prefix sums from nodes that are genuinely still "above" the node currently being visited — not sums left over from an unrelated sibling branch.

---

## Why This Is Correct

Every downward path in the tree can be described as "from node A to node B, where B is a descendant of A (or A itself)". As the DFS visits B, the map contains the prefix sum of every ancestor of B on the current path (plus the sentinel `0` for "start from the root itself"). So checking `map[curr - t]` at node B enumerates **exactly** the set of valid starting points A such that `sum(A → B) = t`, no more and no less — thanks to backtracking keeping the map scoped to the current path only.

## Complexity

| Aspect | Complexity | Reason |
|---|---|---|
| Time | O(n) | Each node is visited once; map operations are O(1) average. |
| Space | O(h) | The map holds at most one entry per ancestor on the current path, so it grows with the tree height `h` (plus O(h) recursion stack). |

Compare this to the brute-force "try every node as a start" approach, which is O(n²) in the worst case (skewed tree) because it re-walks overlapping paths repeatedly.

---

## Quick Example

```
        10
       /  \
      5   -3
     / \    \
    3   2    11
   / \   \
  3  -2   1
```
Target `t = 8`. Valid paths (values sum to 8):
- `5 → 3`
- `5 → 2 → 1`
- `10 → 5 → -3` *(not valid here, just illustrating the pattern)*
- `-3 → 11` *(not a match, just showing traversal)*

As the DFS walks down, e.g., reaching the node `3` (bottom-left), `curr = 10 + 5 + 3 = 18`. It checks `map[18 - 8] = map[10]`, which was recorded when we visited the root — meaning the path `10 → 5 → 3` is *not* what matched (that sums to 18, not 8); instead it's `map[curr - t]` matching an ancestor whose prefix sum was exactly 10 less, e.g. path `5 → 3` matches because `prefixSum(3) - prefixSum(root) = 18 - 10 = 8`. This illustrates how any matching ancestor is found in O(1) via the map instead of manually summing every possible sub-path.