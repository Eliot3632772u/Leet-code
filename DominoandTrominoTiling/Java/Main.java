public class Main {
    public static void main(String[] args) {
        int n = 5; // Example input
        Solution solution = new Solution();
        int ways = solution.numTilings(n);
        System.out.println("Number of ways to tile a 2 x " + n + " board: " + ways);
    }
}
