public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] grid = {
            {3, 2, 1},
            {1, 7, 6},
            {2, 7, 7}
        };
        int result = solution.equalPairs(grid);
        System.out.println("Number of equal row and column pairs: " + result);
    }
}
