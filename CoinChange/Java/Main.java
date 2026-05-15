public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] coins = {1, 2, 5}; // Example coin denominations
        int amount = 11; // Example amount
        int result = solution.coinChange(coins, amount);
        System.out.println("Minimum number of coins needed to make amount " + amount + ": " + result);
    }
}
