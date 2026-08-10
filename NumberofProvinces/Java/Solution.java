import java.util.*;

class Solution {
    HashSet<Integer> visited = new HashSet<>();
    public int findCircleNum(int[][] isConnected) {
        int province = 0;
        for(int i = 0; i < isConnected.length; i++) {
            if (!visited.contains(i)) {
                province++;
                dfs(i, isConnected);
            }
        }

        return province;
    }

    void dfs(int city, int[][] isConnected) {
        if (visited.contains(city)) return;
        visited.add(city);

        int[] connections = isConnected[city];
        for(int i = 0; i < connections.length; i++) {
            if (connections[i] == 1 && !visited.contains(i))
                dfs(i, isConnected);
        }
    }
}