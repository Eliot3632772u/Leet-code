public class Main {
    public static void main(String[] args) {
        
        char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };

        Solution solution = new Solution();
        int result = solution.numIslands(grid);
        System.out.println("Number of islands: " + result);
    }
}