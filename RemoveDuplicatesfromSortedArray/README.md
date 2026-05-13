# Remove Duplicates from Sorted Array

## Problem Summary

Given an integer array `nums` sorted in **non-decreasing order**, remove the duplicates **in-place** so that each unique element appears only once. The **relative order** of the elements must remain the same.

Since you can't use extra space for another array, you must do this by **modifying the input array in-place** and return the number of unique elements `k`.

> **Note:** The elements beyond index `k-1` in the array don't matter — only the first `k` elements are checked.

## Intuition

The array is already **sorted**, which is the key insight. Because duplicates are always adjacent in a sorted array, we never need to search far — if `nums[i] == nums[i-1]`, it's a duplicate. If not, it's a new unique value.

We use a **two-pointer technique**:
- **`i`** — the fast pointer, scanning every element
- **`j`** — the slow pointer, tracking where the next unique element should be written

Think of `j` as the "write head" and `i` as the "read head". The read head races ahead; the write head only moves when it finds something worth keeping.

---

## Solution

```java
class Solution {
    public int removeDuplicates(int[] nums) {

        int j = 1; // slow pointer — next position to write a unique value
        for (int i = 1; i < nums.length; i++) { // fast pointer — scans every element
            if (nums[i] != nums[i - 1]) { // found a new unique element
                nums[j] = nums[i];         // write it to the next available slot
                j++;                       // advance the write head
            }
        }
        return j; // number of unique elements
    }
}
```

---

## How It Works — Step by Step

### Why start both pointers at index 1?

- `nums[0]` is always unique (nothing before it to be a duplicate of), so it's already in the right place.
- `j = 1` means the next write slot is index 1.
- `i = 1` means we start comparing from the second element onward.

### The core logic

```
if (nums[i] != nums[i - 1])
```

Since the array is sorted, any duplicate of `nums[i]` would be at `nums[i-1]`. So this single comparison is all we need to detect a new unique value.

When a new unique value is found:
1. Write it to `nums[j]` (the next available slot in the "clean" prefix)
2. Increment `j` to advance the write head

When it IS a duplicate, we do nothing — `i` advances but `j` stays put, effectively skipping the duplicate.

---

## Worked Example

### Input: `nums = [1, 1, 2, 3, 3, 4]`

**Initial state:**
```
Index:  0  1  2  3  4  5
nums: [ 1, 1, 2, 3, 3, 4 ]
j = 1, i = 1
```

---

**i = 1:** `nums[1]=1` vs `nums[0]=1` → **equal (duplicate), skip**
```
Index:  0  1  2  3  4  5
nums: [ 1, 1, 2, 3, 3, 4 ]
        ↑  
        j=1 (unchanged)
```

---

**i = 2:** `nums[2]=2` vs `nums[1]=1` → **different! Write 2 to nums[j=1]**
```
Index:  0  1  2  3  4  5
nums: [ 1, 2, 2, 3, 3, 4 ]
           ↑
           j → becomes 2
```

---

**i = 3:** `nums[3]=3` vs `nums[2]=2` → **different! Write 3 to nums[j=2]**
```
Index:  0  1  2  3  4  5
nums: [ 1, 2, 3, 3, 3, 4 ]
              ↑
              j → becomes 3
```

---

**i = 4:** `nums[4]=3` vs `nums[3]=3` → **equal (duplicate), skip**
```
Index:  0  1  2  3  4  5
nums: [ 1, 2, 3, 3, 3, 4 ]
              ↑
              j=3 (unchanged)
```

---

**i = 5:** `nums[5]=4` vs `nums[4]=3` → **different! Write 4 to nums[j=3]**
```
Index:  0  1  2  3  4  5
nums: [ 1, 2, 3, 4, 3, 4 ]
                 ↑
                 j → becomes 4
```

---

**Loop ends. Return `j = 4`.**

The first `4` elements of `nums` are the answer:
```
[ 1, 2, 3, 4, _, _ ]   ← only the first k=4 elements matter
```

✅ Correct!

---

## Another Example

### Input: `nums = [0, 0, 0, 0, 1]`

| i | nums[i] | nums[i-1] | Duplicate? | Action | j after |
|---|---------|-----------|------------|--------|---------|
| 1 | 0       | 0         | ✅ Yes     | Skip   | 1       |
| 2 | 0       | 0         | ✅ Yes     | Skip   | 1       |
| 3 | 0       | 0         | ✅ Yes     | Skip   | 1       |
| 4 | 1       | 0         | ❌ No      | Write  | 2       |

**Result:** `k = 2`, array becomes `[0, 1, ...]`

---

## Edge Cases

| Case | Example Input | Expected Output |
|------|--------------|-----------------|
| All duplicates | `[3, 3, 3, 3]` | `k=1`, array=`[3,...]` |
| No duplicates | `[1, 2, 3, 4]` | `k=4`, array=`[1,2,3,4]` |
| Single element | `[7]` | `k=1`, array=`[7]` |
| Two elements, same | `[5, 5]` | `k=1`, array=`[5,...]` |
| Two elements, different | `[5, 6]` | `k=2`, array=`[5,6]` |

---

## Complexity Analysis

| | Complexity | Reason |
|---|---|---|
| **Time** | O(n) | Single pass through the array with pointer `i` |
| **Space** | O(1) | No extra data structures — modified in-place |

---

## Key Takeaways

1. **Sorted = duplicates are adjacent.** This is what makes a single `nums[i] != nums[i-1]` check sufficient. This solution would NOT work on an unsorted array.
2. **Two pointers = read/write separation.** `i` reads everything; `j` only advances when there's something worth writing. This is a classic in-place filtering pattern.
3. **`j` is both a pointer AND the count.** When the loop ends, `j` equals exactly the number of unique elements written — so returning it directly gives us `k`.
4. **Don't worry about the tail.** Elements after index `j-1` are leftover garbage values, but the problem explicitly says they don't matter.