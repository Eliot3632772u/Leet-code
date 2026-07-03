# Diameter of Binary Tree

## Problem Statement

Given the `root` of a binary tree, return the **length of the diameter** of the tree.

The **diameter** of a binary tree is the length of the **longest path** between any two nodes in the tree. This path **may or may not pass through the root**.

The **length** of a path between two nodes is represented by the **number of edges** between them (not the number of nodes).

### Example

```
Input:
        1
       / \
      2   3
     / \
    4   5

Output: 3
Explanation: The longest path is [4,2,1,3] or [5,2,1,3], with 3 edges.
```

---

## The Core Insight

The naive way to think about "diameter" is: *"find the two nodes that are farthest apart and count the edges between them."* That framing is correct but not directly computable — you'd have to check paths between every pair of nodes, which is expensive and awkward to reason about recursively.

The key reframing that makes this problem tractable is:

> **For every single node in the tree, the longest path that passes *through* that node (using it as the "peak") is exactly `height(left subtree) + height(right subtree)`.**

If you compute this "through-node" path length for *every* node in the tree and take the maximum over all of them, you are guaranteed to find the overall diameter — because the true longest path in the tree must have *some* node that sits at its highest point (its "peak" or turning point), and at that peak node, the path is made up of a run down the left subtree plus a run down the right subtree.

This is the classic **"compute something globally by combining a per-node local answer with a bottom-up recursive helper"** pattern, and it's worth internalizing because it recurs constantly in tree problems (see "Related Problems" at the end).

---

## Why "height" is the Right Building Block

**Height of a node** = the number of edges on the longest downward path from that node to a leaf. By convention:
- `height(null) = 0` (an empty subtree contributes no edges)
- `height(leaf) = 0` if we define height in terms of edges from the leaf itself, but in this solution's convention (see below), a leaf node returns `1` because the *node itself* counts as "one step down" from its parent's perspective.

Why do we need height at all? Because the longest path *through* a node `X` is built from two pieces:

1. The longest path going down into `X`'s **left** subtree (this length is `height(left subtree)`).
2. The longest path going down into `X`'s **right** subtree (this length is `height(right subtree)`).

Glue those two pieces together at `X`, and you get a path that enters from the deepest point on the left, passes through `X`, and exits to the deepest point on the right. Its edge count is `height(left) + height(right)`.

There is no way to know `height(left)` or `height(right)` for a given node without first fully exploring that subtree — which is exactly why this has to be a **post-order (bottom-up) traversal**: children must be fully processed *before* their parent can compute anything meaningful.

---

## Walking Through the Code

```java
class Solution {

    int res;

    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return res;
    }

    int height(TreeNode root) {
        if (root == null) return 0;

        int l = height(root.left);
        int r = height(root.right);

        res = Math.max(res, l + r);

        return Math.max(l + 1, r + 1);
    }
}
```

### Piece by piece

**1. The instance field `res`**

```java
int res;
```

This is the running answer — the best (largest) diameter found *so far*, across every node visited. It's declared outside of `height()` so that it persists and accumulates across all the recursive calls, rather than being reset each time. Java default-initializes `int` fields to `0`, which is exactly the right starting value (an empty tree, or a single-node tree, has diameter `0`).

This is a common technique: instead of threading a "best so far" value up and down through return values (which would force `height()` to return a pair of numbers — height *and* best diameter), we let a side-channel field absorb the answer while the return value of `height()` is reserved for a single, focused purpose: reporting height to the caller.

**2. The public entry point**

```java
public int diameterOfBinaryTree(TreeNode root) {
    height(root);
    return res;
}
```

This kicks off the recursion by calling `height(root)`. We deliberately **discard** the return value of that call — we don't care what the height of the whole tree is. We only call `height()` for its *side effect*: as it recurses, it will update `res` at every single node. By the time `height(root)` returns, every node in the tree has been visited and `res` holds the true maximum.

**3. The base case**

```java
if (root == null) return 0;
```

An empty subtree (i.e., we've walked off the bottom of the tree past a leaf) has height `0`. This is what allows a leaf node to correctly compute `height(leaf.left) = 0` and `height(leaf.right) = 0`.

**4. Recurse into both children — post-order**

```java
int l = height(root.left);
int r = height(root.right);
```

Before we can say anything about the current node, we need to know how "tall" its left and right subtrees are. This is a **post-order** traversal: children are fully resolved before the parent does its own work. This ordering is not optional — it's forced by the fact that `l` and `r` are needed to compute both `res` *and* the return value at this node.

**5. Update the global answer — the "aha" line**

```java
res = Math.max(res, l + r);
```

This is where the actual diameter logic lives. At this exact moment in the recursion, `root` is the "peak" of a candidate path: `l` edges going down into the left subtree, plus `r` edges going down into the right subtree, glued together at `root`. That candidate path has `l + r` edges total.

We compare this candidate against the best one seen so far (`res`) and keep the larger one. Because this line executes **once per node**, and every possible "peak" in the tree gets a turn at being `root` during the recursion, we are guaranteed to consider the true longest path — whichever node it peaks at.

**6. Return height to the parent**

```java
return Math.max(l + 1, r + 1);
```

This is the part of the return value that has nothing to do with the diameter directly — it's bookkeeping for the *parent* call. The height of the current node, as seen from above, is `1` (for the edge connecting root to whichever child) plus the taller of its two children's heights.

Equivalently this is often written as `1 + Math.max(l, r)` — the two forms are mathematically identical, just associativity moved around; `Math.max(l+1, r+1)` simply distributes the `+1` before taking the max instead of after.

> **Important distinction:** `l + r` (used for `res`) adds the two children's heights *together*, because a path *through* the node uses both sides at once. `Math.max(l, r) + 1` (used for the return value) takes only the *taller* side, because when reporting height upward, only one path continues past this node into its parent — you can't walk down the left subtree and the right subtree simultaneously on a single path to a grandparent.

---

## Tracing Through the Example

```
        1
       / \
      2   3
     / \
    4   5
```

Post-order recursion visits nodes in this order: `4, 5, 2, 3, 1`.

| Call | `l` (height of left child) | `r` (height of right child) | `l + r` (candidate diameter) | `res` after this call | Return value (height of this node) |
|---|---|---|---|---|---|
| `height(4)` | `height(null)=0` | `height(null)=0` | `0` | `0` | `max(0+1,0+1) = 1` |
| `height(5)` | `height(null)=0` | `height(null)=0` | `0` | `0` | `1` |
| `height(2)` | `height(4)=1` | `height(5)=1` | `1+1=2` | `2` | `max(1+1,1+1)=2` |
| `height(3)` | `height(null)=0` | `height(null)=0` | `0` | `2` (unchanged, `0<2`) | `1` |
| `height(1)` | `height(2)=2` | `height(3)=1` | `2+1=3` | `3` | `max(2+1,1+1)=3` |

Final `res = 3`, which matches the expected output.

Notice the winning path: at node `2`, `l=1, r=1` gives a candidate of `2` (the path `4-2-5`). But at the root, `l=2, r=1` gives `3` (the path `4-2-1-3`, or equally `5-2-1-3`), which is larger and overwrites `res`. The algorithm never explicitly reconstructs this path — it just tracks the *length*, and the max-tracking across all nodes guarantees the true peak node's candidate gets captured.

---

## Why This Greedy, Bottom-Up Approach is Correct

The correctness argument boils down to one claim:

> **Claim:** Every path in a tree has a unique highest node (an ancestor of every other node on the path, or a node on the path itself with no path-neighbor above it). At that node, the path decomposes into a "left descent" and a "right descent" (one of which may be empty if the path doesn't turn — e.g., a straight path down one side).

Given that claim:
- The overall diameter (the single longest path in the whole tree) has *some* peak node `P`.
- At `P`, the algorithm computes exactly `height(P.left) + height(P.right)` when `height(P)` is called — and this is by construction the length of the longest possible path peaking at `P`, because `height()` itself returns the longest downward run from a node.
- Since the algorithm evaluates this "peak candidate" formula for **every** node in the tree (not just some), it necessarily evaluates it for `P` too, at which point `res` gets updated to the true diameter (or something already at least as large, if a different node's candidate was larger for some reason — but by definition, `P`'s candidate *is* the largest since it's defined as the peak of the actual longest path).
- Therefore `res` can never end up smaller than the true diameter, and it can never end up larger either, since every candidate `l + r` computed is a genuinely realizable path length in the tree.

This is why the algorithm doesn't need to explicitly compare "path through root" vs. "path entirely within left subtree" vs. "path entirely within right subtree" the way a more naive divide-and-conquer formulation might: those two latter cases are automatically covered because the recursion visits every node in the left and right subtrees individually, and each of those nodes gets its own turn at line `res = Math.max(res, l + r)`. The root's own candidate only needs to handle the case where the path *passes through the root itself* — every other case is already handled by some deeper call.

---

## Complexity Analysis

**Time Complexity: O(n)**

Every node is visited exactly once by `height()`. At each visit we do O(1) work (two comparisons, an addition, a max). No node is revisited, and there's no repeated work across calls — unlike a naive approach that might recompute height from scratch at every node (which would be O(n²) in the worst case, i.e., a skewed/linear tree).

**Space Complexity: O(h)**, where `h` is the height of the tree

This space is entirely due to the recursion call stack — each recursive call to `height()` adds one frame, and the maximum depth of nested calls equals the height of the tree.

- **Balanced tree:** `h = O(log n)`, so space is `O(log n)`.
- **Completely skewed tree** (essentially a linked list, e.g., every node has only a left child): `h = O(n)`, so space degrades to `O(n)`.

The `res` field itself is O(1) extra space — it's a single integer, not something that grows with input size.

---