class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int max = 0;
        for(int p : piles) {
            max = Math.max(max, p);
        }

        int l = 1, r = max;
        while (l < r) {
            int mid = (l + r) / 2;
            if (validK(mid, h, piles)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }

        return l;
    }

    boolean validK(int k, int h, int[] piles) {
        int time = 0;
        for(int p : piles) {
            // time += Math.ceil((double) p / k);
            time += (p + k - 1)/k;
            if (time > h) return false;
        }
        return time <= h;
    }
}