import java.util.*;

class Solution {
    public int orangesRotting(int[][] grid) {
        LinkedList<int[]> rotten = new LinkedList<>();
        int rows = grid.length, cols = grid[0].length;
        int minutes = 0;
        int fresh = 0;

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) rotten.add(new int[]{i, j});
                if (grid[i][j] == 1) fresh++;
            }
        }

        if (fresh == 0) return 0;

        while (!rotten.isEmpty() && fresh > 0) {
            int size = rotten.size();
            minutes++;
            for(int i = 0; i < size; i++) {
                int[] cords = rotten.pop();
                int r = cords[0], c = cords[1];
                if (r - 1 >= 0 && grid[r - 1][c] == 1) {
                    grid[r - 1][c] = 2;
                    fresh--;
                    rotten.add(new int[]{r-1, c});
                }
                if (r + 1 < rows && grid[r + 1][c] == 1) {
                    grid[r + 1][c] = 2;
                    fresh--;
                    rotten.add(new int[]{r+1, c});
                }
                if (c - 1 >= 0 && grid[r][c - 1] == 1) {
                    grid[r][c - 1] = 2;
                    fresh--;
                    rotten.add(new int[]{r, c-1});
                }
                if (c + 1 < cols && grid[r][c + 1] == 1) {
                    grid[r][c + 1] = 2;
                    fresh--;
                    rotten.add(new int[]{r, c+1});
                }

                if (fresh <= 0) return minutes;
            }
        }

        return fresh <= 0 ? minutes : -1;
    }
}