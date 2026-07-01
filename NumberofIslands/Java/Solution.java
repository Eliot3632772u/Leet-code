public class Solution {
    public int numIslands(char[][] grid) {
        
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

        int count = 0;

        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    floodFill(grid, i, j);
                    count++;
                }
            }
        }

        return count;
    }

    public void floodFill(char[][] grid, int i, int j) {

        if (grid[i][j] == '0') return;

        grid[i][j] = '0';

        if (j + 1 < grid[0].length)
            floodFill(grid, i, j + 1);
        if (j - 1 >= 0)
            floodFill(grid, i, j - 1);
        if (i - 1 >= 0)
            floodFill(grid, i - 1, j);
        if (i + 1 < grid.length)
            floodFill(grid, i + 1, j);

    }
}