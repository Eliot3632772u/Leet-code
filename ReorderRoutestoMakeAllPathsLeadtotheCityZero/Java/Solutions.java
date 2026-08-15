import java.util.ArrayList;
import java.util.List;

class Solution {

    public class Pair {
        int neighbor;
        int cost;

        Pair(int n, int cost) {
            this.neighbor = n;
            this.cost = cost;
        }
    }

    boolean[] visited;
    List<List<Pair>> adjList;
    int count = 0;

    public int minReorder(int n, int[][] connections) {
        
        adjList = new ArrayList<>();
        visited = new boolean[n];

        for(int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for(int[] con : connections) {
            int a = con[0], b = con[1];

            adjList.get(a).add(new Pair(b, 1));
            adjList.get(b).add(new Pair(a, 0));
        }

        dfs(0);

        return count;
    }

    void dfs(int city) {
        visited[city] = true;

        for(Pair n : adjList.get(city)) {
            if (!visited[n.neighbor]) {
                count += n.cost;
                dfs(n.neighbor);
            }
        }
    }
}