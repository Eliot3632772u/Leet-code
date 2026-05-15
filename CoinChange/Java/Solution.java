class Solution {
    public int coinChange(int[] coins, int amount) {
        
        int[] dp = new int[amount + 1];

        for(int i = 1; i <= amount; i++) {
            for(int j = 0; j < coins.length; j++) {

                if (coins[j] == i) dp[i] = 1;
                else if (coins[j] < i) {
                    int remainder = i - coins[j];
                    int coinsToMakeRemainder = dp[remainder];
                    if (coinsToMakeRemainder > 0) {
                      if (dp[i] == 0) dp[i] = coinsToMakeRemainder + 1;
                      else dp[i] = Math.min(coinsToMakeRemainder + 1, dp[i]);
                    }
                } 
                else continue;
            }
            if (dp[i] == 0) dp[i] = -1;
        }

        return dp[amount];
    }
}