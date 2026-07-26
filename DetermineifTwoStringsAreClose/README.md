# 1657. Close Strings

## Problem Statement

Two strings `word1` and `word2` are considered **close** if you can attain one from the other using the following operations, any number of times, in any order:

- **Operation 1**: Swap any two existing characters. (e.g. `abcde -> aecdb`)
- **Operation 2**: Transform every occurrence of one existing character into another existing character, and do the same with the other character. (e.g. `aacabb -> bbcbaa`, all `a`'s become `b`'s and all `b`'s become `a`'s)

Given two strings, `word1` and `word2`, return `true` if `word1` and `word2` are close, and `false` otherwise.

---

## Core Insight

Before writing a single line of code, it helps to translate the two operations into plain language, because that translation *is* the algorithm:

- **Operation 1 (swap characters within a string)** means **position doesn't matter**. You can rearrange letters however you like inside one string. So `"abc"` and `"cab"` are trivially "close" with respect to this operation alone.
- **Operation 2 (swap two characters' identities globally)** means **which letter is called what doesn't matter**, only **how many letters have each frequency**. If `word1` has 3 `a`'s and 2 `b`'s, and `word2` has 3 `x`'s and 2 `y`'s, Operation 2 lets you relabel `a -> x` and `b -> y` to make them match.

Putting these together, two strings are close **if and only if**:

1. They use **exactly the same set of distinct characters**, and
2. The **multiset of character frequencies** is identical (i.e., if you sort the frequency counts, the sorted lists match).

Notice what does **not** matter: the actual character-to-count mapping (which letter has which count), and the order of characters in the string. Both are erased by the two operations.

### Why condition 1 (same character set) is necessary

Operation 2 can only shuffle frequencies *among characters that already exist in the string*. It cannot invent a brand new character or delete one entirely — it only relabels. So if `word1` contains a `'z'` and `word2` never contains a `'z'` anywhere, no sequence of swaps or relabels can make them equal. The set of distinct characters used is an invariant that must match exactly.

### Why condition 2 (same sorted frequency multiset) is necessary and sufficient

Once the character sets match, Operation 2 lets us permute *which* character gets *which* frequency, completely freely, as long as we do it consistently (a true relabeling/bijection between the alphabets). Operation 1 makes the actual arrangement of characters within the string irrelevant. Therefore, the only structural property of a string that survives after all possible operations is: **"for this set of characters, what are the counts, disregarding which character maps to which count?"** That's exactly what the sorted array of frequencies captures.

If the sorted frequency arrays match, you can always construct an explicit relabeling (map the character with the i-th largest count in `word1` to the character with the i-th largest count in `word2`) and then freely rearrange with swaps. So the condition is also sufficient.

---

## Algorithm Walkthrough

```java
class Solution {
    public boolean closeStrings(String word1, String word2) {
        if (word1.length() != word2.length()) return false;
        if (word1.equals(word2)) return true;

        int[] freq1 = new int[27];
        int[] freq2 = new int[27];

        for (int i = 0; i < word1.length(); i++) {
            freq1[word1.charAt(i) - 'a']++;
        }

        for (int i = 0; i < word2.length(); i++) {
            freq2[word2.charAt(i) - 'a']++;
        }

        for (int i = 0; i < 27; i++) {
            if ((freq1[i] == 0 && freq2[i] != 0) || (freq1[i] != 0 && freq2[i] == 0)) return false;
        }

        Arrays.sort(freq1);
        Arrays.sort(freq2);

        return Arrays.equals(freq1, freq2);
    }
}
```

### Step-by-step

**1. Length check — early exit**
```java
if (word1.length() != word2.length()) return false;
```
Every operation (swap or relabel) preserves the total length of the string. If the lengths differ, the strings cannot possibly be close, so we bail out in O(1) instead of doing any counting work.

**2. Identity check — early exit (optimization only)**
```java
if (word1.equals(word2)) return true;
```
Not required for correctness (the general logic below would also return `true` in this case), but it's a cheap short-circuit that skips the frequency-counting work entirely for the common case of two identical strings.

**3. Build frequency arrays**
```java
int[] freq1 = new int[27];
int[] freq2 = new int[27];
```
Both arrays are sized 27, one slot larger than the 26 needed for lowercase English letters. This is a minor stylistic slack (a size of 26 would suffice since the problem guarantees lowercase English letters only); it doesn't affect correctness, it just reserves one unused trailing slot that stays `0` in both arrays throughout.

```java
for (int i = 0; i < word1.length(); i++) {
    freq1[word1.charAt(i) - 'a']++;
}
```
For each character, `charAt(i) - 'a'` maps `'a'` to index `0`, `'b'` to index `1`, ..., `'z'` to index `25`. Incrementing that index tallies how many times each letter appears. This is a standard O(n) counting pass. The same is done for `word2` into `freq2`.

At this point, `freq1[k]` is the number of times the k-th letter of the alphabet appears in `word1`, and likewise for `freq2[k]` and `word2`. Both arrays are indexed by *identity* of the character (position `0` always means `'a'`), not yet compared for the "same shape, different labels" relationship we actually care about.

**4. Verify condition 1 — same set of characters used**
```java
for (int i = 0; i < 27; i++) {
    if ((freq1[i] == 0 && freq2[i] != 0) || (freq1[i] != 0 && freq2[i] == 0)) return false;
}
```
This loop checks, letter by letter, whether the two strings "agree" on which characters are present at all. For each index `i` (i.e., each letter), exactly one of the following must be true for the strings to remain viable:
- Both `freq1[i]` and `freq2[i]` are zero (neither string uses that letter), or
- Both are nonzero (both strings use that letter, possibly a different number of times — that's fine, that's handled in step 5).

The condition being checked is the *bad* case: one string uses the letter and the other doesn't. If that ever happens for any letter, the strings can never be made equal (as argued above, Operation 2 can only relabel among characters that exist in both strings — it cannot create or destroy a character), so we return `false` immediately.

**5. Verify condition 2 — same multiset of frequencies**
```java
Arrays.sort(freq1);
Arrays.sort(freq2);
return Arrays.equals(freq1, freq2);
```
Sorting both frequency arrays discards *which* character had *which* count and keeps only the multiset of counts themselves. This is precisely what "relabeling is allowed" means algorithmically: after sorting, index alignment no longer represents "the same letter" — it represents "the k-th smallest frequency," which is exactly the invariant that Operation 2 leaves untouched.

If the sorted arrays are equal, then there's a valid one-to-one correspondence between the letters of `word1` and the letters of `word2` that matches up their frequencies exactly, which means the strings can be transformed into one another via relabeling plus rearrangement. `Arrays.equals` performs this final comparison.

---

## Why the Two-Step Check is Necessary (Not Just Sorting)

A natural question: why not skip step 4 and just sort-and-compare the frequency arrays directly?

Because sorting alone can't distinguish "same characters, same counts" from "different characters that happen to produce the same sorted count list, but not a 1-to-1 correspondence with zero counts aligned properly." Consider:

- `word1 = "aabbb"` → letter counts: `a:2, b:3` → other letters: `0`
- `word2 = "aabbc"` → letter counts: `a:2, b:2, c:1` → other letters: `0`

Sorted non-zero frequencies for `word1`: `[2, 3]`. Sorted non-zero frequencies for `word2`: `[1, 2, 2]`. These already differ, so this particular example is caught by sorting alone. But the real risk is when the *set of characters used* differs while the *shape of nonzero counts* coincides — step 4 exists specifically to catch a mismatched alphabet (e.g., one string using a character the other never uses at all), which sorting the full 27-length array (including all the zero-count slots) actually already implicitly encodes, since a missing letter contributes a `0` to the sorted array on one side that must be matched by a `0` on the other side, in the same position, for `Arrays.equals` to succeed at the end.

In other words: because this implementation sorts the **entire 27-slot array** (not just the nonzero counts), the final `Arrays.equals` check would, on its own, already catch differing alphabets too — a string using fewer distinct letters ends up with more `0`s in its sorted array, which would not line up against a string using more distinct letters. Step 4 is technically redundant with step 5 given this specific implementation (arrays of fixed length 27, sorted in full), but it is kept here as an **explicit, cheap, easy-to-read guard** that documents the "same alphabet" requirement directly, and lets the function short-circuit before paying for two `O(27 log 27)` sorts. Removing step 4 would not break correctness in this version of the code, but including it makes the two independent conditions ("same characters" and "same frequency multiset") visible and separately testable, which is good practice given the problem explicitly describes two separate operations with two separate invariants.

---

## Complexity Analysis

Let `n` be the length of `word1` (equal to the length of `word2` past the first check).

| Step | Time | Space |
|---|---|---|
| Length/equality check | O(n) worst case for `.equals`, O(1) amortized otherwise | O(1) |
| Building `freq1`, `freq2` | O(n) | O(1) — fixed size 27 arrays |
| Alphabet-match loop | O(1) — fixed 27 iterations | O(1) |
| Sorting `freq1`, `freq2` | O(1) — sorting a fixed-size-27 array is constant time | O(1) |
| Final `Arrays.equals` | O(1) — fixed 27 elements | O(1) |

**Overall: Time O(n), Space O(1).**

The only part of the algorithm whose cost scales with the input is the single pass to build the frequency arrays. Everything downstream operates on fixed-size, 27-element arrays, so it's constant time regardless of how long the input strings are — a direct consequence of the alphabet being bounded to 26 lowercase letters.

---

## Correctness Summary

| Check | What it verifies | What breaks it |
|---|---|---|
| `word1.length() != word2.length()` | Total character count must match | Any length mismatch — operations preserve length |
| Alphabet-match loop | Same set of distinct characters used | A character present in one string but totally absent in the other |
| Sorted frequency comparison | Same multiset of per-character counts, independent of which character owns which count | Frequencies that can't be paired up via any consistent relabeling |

Together these three checks exactly characterize the equivalence classes induced by the two allowed operations: **swap (order-invariance)** and **relabel (identity-invariance)**. Two strings are close precisely when they are indistinguishable once you strip away both position and label — which is exactly what "same characters present" + "same sorted frequency list" captures.