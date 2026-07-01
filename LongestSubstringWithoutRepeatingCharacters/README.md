# Longest Substring Without Repeating Characters

## Problem Statement

Given a string `s`, find the length of the **longest substring** that does not contain any repeating characters.

**Example 1:**
```
Input:  s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with length 3.
```

**Example 2:**
```
Input:  s = "bbbbb"
Output: 1
Explanation: The answer is "b", with length 1.
```

**Example 3:**
```
Input:  s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with length 3.
```

---

## Solution

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();

        int maxLength = 0;
        int l = 0;

        for (int r = 0; r < s.length(); r++) {
            while (set.contains(s.charAt(r))) {
                set.remove(s.charAt(l));
                l++;
            }

            maxLength = Math.max(maxLength, r - l + 1);
            set.add(s.charAt(r));
        }

        return maxLength;
    }
}
```

---

## Approach: Sliding Window

This is the classic **variable-size sliding window** pattern, using two pointers (`l` for left, `r` for right) that both move only *forward* through the string, plus a `HashSet` to track which characters currently live inside the window.

### Why a sliding window works here

A brute-force solution would check every possible substring (O(n²) or O(n³)), testing each one for duplicate characters. But notice something important: if the substring `s[l..r]` has no repeating characters, and we then find that `s[r+1]` is a duplicate, we don't need to restart the search from `l+1`. We only need to shrink the window from the **left** until the duplicate is gone — every intermediate window we'd otherwise re-check has already been accounted for. This is what lets us do the whole thing in **one pass**, O(n) time.

### The invariant

At every point in the loop, the window `[l, r)` (before adding `s.charAt(r)`) — and after processing, `[l, r]` — represents a substring **with no repeated characters**. The `set` always mirrors exactly the characters currently between `l` and `r` (inclusive). This invariant is maintained by the `while` loop before we ever add a new character.

---

## Step-by-Step Walkthrough

### Variables

| Variable | Meaning |
|---|---|
| `set` | The set of characters currently inside the window `[l, r]` |
| `l` | Left boundary of the current window (inclusive) |
| `r` | Right boundary of the current window (inclusive), driven by the `for` loop |
| `maxLength` | The best (longest) window length seen so far |

### The loop, piece by piece

```java
for (int r = 0; r < s.length(); r++) {
```
We expand the window one character at a time by moving `r` from left to right across the string. Each iteration considers adding `s.charAt(r)` to the window.

```java
    while (set.contains(s.charAt(r))) {
        set.remove(s.charAt(l));
        l++;
    }
```
Before adding the new character, we check: **is it already in the window?**
- If yes, the window currently contains a duplicate of `s.charAt(r)`, so it's no longer a valid "no-repeat" window if we add it as-is.
- We shrink the window from the left, removing `s.charAt(l)` from the set and incrementing `l`, **repeatedly**, until the duplicate of `s.charAt(r)` has been evicted from the set.
- This `while` (not `if`) matters because the character at `l` isn't necessarily the duplicate — we may need to slide `l` past several characters before we reach and remove the actual clashing character.

```java
    maxLength = Math.max(maxLength, r - l + 1);
```
At this point, we know `s.charAt(r)` can safely join the window without creating a duplicate (the loop above guaranteed that). The current valid window size is `r - l + 1` (number of characters from index `l` to `r`, inclusive). We update `maxLength` if this window beats our previous best.

```java
    set.add(s.charAt(r));
}
```
Now we actually add `s.charAt(r)` into the set, officially extending the window to include index `r`.

```java
return maxLength;
```
After scanning the whole string, `maxLength` holds the length of the longest substring without repeating characters.

---

## Dry Run Example: `s = "pwwkew"`

| r | char | Shrink step | l after shrink | window `s[l..r]` | maxLength |
|---|------|-------------|-----------------|-------------------|-----------|
| 0 | p | none needed | 0 | `"p"` | 1 |
| 1 | w | none needed | 0 | `"pw"` | 2 |
| 2 | w | `'w'` is in set → remove `s[0]='p'`, l=1 → `'w'` still in set → remove `s[1]='w'`, l=2 | 2 | `"w"` | 2 |
| 3 | k | none needed | 2 | `"wk"` | 3 |
| 4 | e | none needed | 2 | `"wke"` | 3 |
| 5 | w | `'w'` is in set → remove `s[2]='w'`, l=3 | 3 | `"kew"` | 3 |

**Result: `maxLength = 3`** → the longest substrings without repeats are `"wke"` or `"kew"`, both length 3.

---

## Why This Is Correct

1. **No valid window is ever skipped.** Every time `r` advances, we first ensure the window `[l, r]` has no duplicates by shrinking from the left as needed. So `maxLength` is always updated on a genuinely valid (duplicate-free) window.
2. **`l` only moves forward, never backward.** Once we've decided a character must leave the window, there's no scenario where re-including it (without re-including the newer duplicate too) would produce a longer valid window. This monotonic movement of both pointers is what guarantees linear time.
3. **The `HashSet` gives O(1) average-time duplicate checks**, so each character is added to and removed from the set at most once across the entire run.

---

## Complexity Analysis

### Time Complexity: **O(n)**
Although there's a nested `while` loop inside the `for` loop, each character is visited by `r` exactly once (added once) and by `l` at most once (removed once). So the total number of operations across the whole run is bounded by `2n`, giving **O(n)** overall — not O(n²) — because `l` and `r` each traverse the string at most once, never resetting backward.

### Space Complexity: **O(min(n, m))**
Where `m` is the size of the character set (e.g., 128 for ASCII, 256 for extended ASCII). The `HashSet` holds at most as many characters as are in the current window, which can't exceed the length of the string or the size of the alphabet, whichever is smaller.

---

## Key Takeaways

- This problem is a textbook example of the **two-pointer / sliding window** technique for substring/subarray problems with a "no duplicates" or "at most K distinct" type constraint.
- The core trick: **when you hit a violation (duplicate), shrink from the left just enough to fix it — don't restart the whole search.**
- Using a `while` loop (not `if`) to shrink the window is essential for correctness whenever the duplicate could be several positions away from `l`.
- This same pattern generalizes to many similar problems, e.g., "longest substring with at most K distinct characters," "minimum window substring," and "longest subarray with sum at most K."