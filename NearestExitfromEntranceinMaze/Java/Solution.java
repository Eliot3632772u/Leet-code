import java.util.*;

class Solution {

    public int nearestExit(char[][] maze, int[] e) {

        LinkedList<int[]> queue = new LinkedList<>();
        int rows = maze.length, cols = maze[0].length;

        queue.add(new int[]{e[0], e[1], 0});

        while (!queue.isEmpty()) {
            int[] cords = queue.pop();
            int r = cords[0], c = cords[1], steps = cords[2];

            if (r < rows && r >= 0 && c < cols && c >= 0 && maze[r][c] == '.') {
                if ((r == 0 || r == rows - 1 || c == 0 || c == cols - 1) && !(r == e[0] && c == e[1])) return steps;
                
                maze[r][c] = '+';
                queue.add(new int[]{r - 1, c, steps + 1});
                queue.add(new int[]{r + 1, c, steps + 1});
                queue.add(new int[]{r, c - 1, steps + 1});
                queue.add(new int[]{r, c + 1, steps + 1});
            }
        }

        return -1;
    }

}