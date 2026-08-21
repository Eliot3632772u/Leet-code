import java.util.Arrays;

class Solution {
    public int[] successfulPairs(int[] spells, int[] potions, long success) {
        int n = spells.length, m = potions.length;
        int[] res = new int[n];
        Arrays.sort(potions);

        for(int i = 0; i < n; i++) {
            int l = 0, r = m - 1;
            int ind = -1;

            while (l < r) {
                int mid = (l + r) / 2;
                long prod = (long) spells[i] * potions[mid];
                if (prod >= success) {
                    ind = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            if ((long) spells[i] * potions[l] >= success) 
                ind = l;

            if (ind != -1) res[i] = m - ind;
            else res[i] = 0;
        }

        return res;
    }
}