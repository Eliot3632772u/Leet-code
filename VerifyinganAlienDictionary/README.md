# 👽 Verifying an Alien Sorted Dictionary

## Problem Summary

You're given a list of `words` and a string `order` that represents the **alphabet of an alien language**. The alien alphabet has the same 26 letters as English, but they're arranged in a **different order**.

Your task: determine whether the given list of `words` is **sorted lexicographically** according to the alien alphabet.

---

## Intuition

In English, we know `'a' < 'b' < 'c' < ...`. When comparing two words, we go character by character and use this ordering to decide which word comes first.

In the alien language, the ordering is different — for example, `order = "bca..."` means `'b' < 'c' < 'a' < ...`.

The key insight is: **if we remap each alien character to its rank (position in `order`), we can compare words just like normal strings.**

---

## Solution Walkthrough

### Step 1 — Build the Character Map

```java
public char[] charMap = new char[26];

for (int i = 0; i < order.length(); i++) {
    charMap[(int)(order.charAt(i) - 'a')] = (char) i;
}
```

We create an array of size 26 (one slot per letter). For each character in `order`, we store its **rank** (its index in `order`) at that character's position in the array.

**Example:** `order = "bca"`

| Character | Index in `order` | Stored rank |
|-----------|-----------------|-------------|
| `b`       | 0               | `charMap['b'-'a'] = 0` |
| `c`       | 1               | `charMap['c'-'a'] = 1` |
| `a`       | 2               | `charMap['a'-'a'] = 2` |

So now `charMap` encodes the alien ordering: **b=0, c=1, a=2**.

---

### Step 2 — Check Each Adjacent Pair of Words

```java
for (int i = 1; i < words.length; i++) {
    if (compare(words[i - 1], words[i]) == false) return false;
}
return true;
```

For the list to be sorted, **every consecutive pair** of words must be in the correct order. We compare `words[0]` vs `words[1]`, then `words[1]` vs `words[2]`, and so on. If any adjacent pair is out of order, we immediately return `false`.

---

### Step 3 — The `compare` Function

```java
public boolean compare(String s1, String s2) {
    for (int i = 0; i < s1.length() && i < s2.length(); i++) {
        if (charMap[s1.charAt(i) - 'a'] == charMap[s2.charAt(i) - 'a']) continue;
        if (charMap[s1.charAt(i) - 'a'] >  charMap[s2.charAt(i) - 'a']) return false;
        if (charMap[s1.charAt(i) - 'a'] <  charMap[s2.charAt(i) - 'a']) return true;
    }
    if (s1.length() > s2.length()) return false;
    return true;
}
```

We compare the two words **character by character**, using their alien ranks:

| Condition | Meaning | Action |
|---|---|---|
| Ranks are **equal** | Same character, keep going | `continue` |
| `s1`'s rank **>** `s2`'s rank | `s1` comes after `s2` — wrong order | `return false` |
| `s1`'s rank **<** `s2`'s rank | `s1` correctly comes before `s2` | `return true` |
| Loop ends (prefix exhausted) | `s1` is a prefix of `s2` or equal | |
| `s1.length() > s2.length()` | `s1` is longer — wrong order (e.g. `"apple"` before `"app"`) | `return false` |
| Otherwise | `s1` is shorter or equal length — correct order | `return true` |

---

## Step-by-Step Example

### Input
```
words = ["hello", "leetcode"]
order = "hlabcdefgijkmnopqrstuvwxyz"
```

### Build `charMap`
`'h'` → rank 0, `'l'` → rank 1, `'a'` → rank 2, `'b'` → rank 3, ... `'e'` → rank 4 (wherever it falls in order)

For this example, the key mappings are:
- `h` → 0
- `l` → 1
- `e` → 4 (position in the given order string)

### Compare `"hello"` vs `"leetcode"`

| Position | `s1` char | `s1` rank | `s2` char | `s2` rank | Result |
|----------|-----------|-----------|-----------|-----------|--------|
| 0        | `h`       | **0**     | `l`       | **1**     | `0 < 1` → `return true ✅` |

Since `h` has a lower rank than `l` in the alien alphabet, `"hello"` correctly comes before `"leetcode"`. The list is sorted → `return true`.

---

### Another Example

```
words = ["word", "world", "row"]
order = "worldabcefghijkmnpqstuvxyz"
```

**Key ranks:** `w`=0, `o`=1, `r`=2, `l`=3, `d`=4

**Compare `"word"` vs `"world"`:**

| Position | `s1` | rank | `s2` | rank | Result |
|----------|------|------|------|------|--------|
| 0 | `w` | 0 | `w` | 0 | equal, continue |
| 1 | `o` | 1 | `o` | 1 | equal, continue |
| 2 | `r` | 2 | `r` | 2 | equal, continue |
| 3 | `d` | 4 | `l` | 3 | `4 > 3` → `return false ❌` |

`"word"` is NOT correctly sorted before `"world"` → final answer: **`false`**.

---

### Edge Case: Prefix Words

```
words = ["apple", "app"]
order = "abcdefghijklmnopqrstuvwxyz"
```

**Compare `"apple"` vs `"app"`:**

The loop runs for `min(5, 3) = 3` characters:

| Position | `s1` | `s2` | Result |
|----------|------|------|--------|
| 0 | `a` | `a` | equal |
| 1 | `p` | `p` | equal |
| 2 | `p` | `p` | equal |

Loop ends. Now check lengths: `s1.length() (5) > s2.length() (3)` → `return false ❌`

In any language, a longer word that starts with the shorter word must come **after** the shorter one (just like `"app"` before `"apple"`). Having `"apple"` before `"app"` violates this rule.

---

## Complexity Analysis

| | Complexity |
|---|---|
| **Time** | O(M) where M = total number of characters across all words |
| **Space** | O(1) — the `charMap` array is always size 26 |

---

## Key Takeaways

1. **Remapping is the trick.** By converting each character to its alien rank, you reduce the alien comparison problem to a standard integer comparison.
2. **Compare adjacent pairs only.** Sorted order is transitive — if every neighbor pair is in order, the whole list is in order.
3. **Don't forget the prefix edge case.** If one word is a prefix of another, the shorter word must come first.