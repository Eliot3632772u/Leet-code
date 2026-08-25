public class Main {
    public static void main(String[] args) {
        int[] cost = {10, 15, 20};
        Solution solution = new Solution();
        int minCost = solution.minCostClimbingStairs(cost);
        System.out.println("Minimum cost to reach the top: " + minCost);
    }
}