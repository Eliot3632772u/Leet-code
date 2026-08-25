class Solution {
    public int numTilings(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (n == 3) return 5;

        int[] dp = new int[n];
        dp[0] = 1;
        dp[1] = 2;
        dp[2] = 5;
        for(int i = 3; i < n; i++) {
            dp[i] = (int)((2l * dp[i - 1] + dp[i - 3]) % 1_000_000_007l);
        }

        return dp[n - 1];
    }
}